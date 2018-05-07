package org.wingtree.repositories;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.types.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.wingtree.beans.*;
import org.wingtree.database.StandaloneNeo4jDriverProvider;

import java.util.Set;

@Component
@Profile({"production"})
public class StandaloneNeo4jRepository implements SimulationStateRepository
{
    private static final String ID = "nodeId";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String RADIUS = "radius";
    private static final String ANGLE = "angle";
    private static final String DURATION_TIME = "simulationDurationTime";
    private static final String TIME_STEP = "simulationTimeStep";
    private static final String VEHICLE_ID = "id";
    private static final String START_NODE_ID = "startNodeId";
    private static final String TARGET_NODE_ID = "targetNodeId";
    private static final String VELOCITY = "velocity";
    private static final String HAS = ":HAS";
    private static final String CONNECTED_TO = ":CONNECTED_TO";

    private final Driver driver;
    private final Configuration configuration;
    private final Route route;

    private static int pedestrianId = 1;

    @Autowired
    public StandaloneNeo4jRepository(final StandaloneNeo4jDriverProvider provider)
    {
        this.driver = provider.get();
        this.configuration = loadConfiguration();
        this.route = loadRoute();
    }

    private Configuration loadConfiguration()
    {
        try (final Session session = driver.session()) {
            final Node configuration = extractSingleNode(session.run("MATCH (c:configuration) RETURN c").next());
            return ImmutableConfiguration.builder()
                                         .withSimulationDurationTime(configuration.get(DURATION_TIME).asInt())
                                         .withSimulationTimeStep(configuration.get(TIME_STEP).asLong())
                                         .build();
        }
    }

    private Route loadRoute()
    {
        final Set<RouteSegment> routeSegments = Sets.newHashSet();
        try (final Session session = driver.session()) {
            final StatementResult statementResult = session.run("MATCH (n:lantern) RETURN n");
            while (statementResult.hasNext()) {
                final Node lantern = extractSingleNode(statementResult.next());
                final Junction from = toJunction(session, lantern);
                final StatementResult connectedToResults = session.run(String.format(
                        "MATCH (:lantern{%s:\"%s\"})-[%s]->(x) RETURN x",
                        ID,
                        lantern.get(ID).asString(),
                        CONNECTED_TO));
                while (connectedToResults.hasNext()) {
                    final Node connectedLantern = extractSingleNode(connectedToResults.next());
                    final Junction to = toJunction(session, connectedLantern);
                    routeSegments.add(ImmutableRouteSegment.builder()
                            .withFrom(from)
                            .withTo(to)
                            .build());
                }
            }
        }
        return RouteBuilder.builder()
                .addRouteSegments(routeSegments)
                .build();
    }

    private Set<TrackingDevice> getTrackingDevices(final String lanternId,
                                                   final Coords lanternCoords,
                                                   final Session session)
    {
        final ImmutableSet.Builder<TrackingDevice> trackingDevicesBuilder = new ImmutableSet.Builder<>();

        final StatementResult cameraResults =
                session.run(String.format("MATCH (:lantern{%s:\"%s\"})-[%s]->(cam:camera) RETURN cam;",
                        ID, lanternId, HAS));
        while (cameraResults.hasNext()) {
            trackingDevicesBuilder.add(toCamera(extractSingleNode(cameraResults.next()), lanternCoords));
        }

        final StatementResult msResults = session.run(String.format(
                "MATCH (:lantern{%s:\"%s\"})-[%s]->(ms:movement_sensor) RETURN ms;", ID, lanternId, HAS));
        while (msResults.hasNext()) {
            trackingDevicesBuilder.add(toMovementSensor(extractSingleNode(msResults.next()), lanternCoords));
        }

        final StatementResult vdsResults = session.run(String.format(
                "MATCH (:lantern{%s:\"%s\"})-[%s]->(vds:velocity_and_direction_sensor) RETURN vds;",
                ID, lanternId, HAS));
        while (vdsResults.hasNext()) {
            trackingDevicesBuilder.add(toVelocityAndDirectionSensor(extractSingleNode(vdsResults.next()), lanternCoords));
        }
        return trackingDevicesBuilder.build();
    }

    private Camera toCamera(final Node node, final Coords lanternCoords)
    {
        return CameraBuilder.builder(configuration.getSimulationTimeStep())
                            .withCoords(lanternCoords)
                            .withRadius(node.get(RADIUS).asDouble())
                            .withAngle(node.get(ANGLE).asDouble())
                            .build();
    }

    private MovementSensor toMovementSensor(final Node node, final Coords lanternCoords)
    {
        return MovementSensorBuilder.builder()
                                    .withCoords(lanternCoords)
                                    .withRadius(node.get(RADIUS).asDouble())
                                    .build();
    }

    private VelocityAndDirectionSensor toVelocityAndDirectionSensor(final Node node, final Coords lanternCoords)
    {
        return VelocityAndDirectionSensorBuilder.builder(configuration.getSimulationTimeStep())
                                                .withCoords(lanternCoords)
                                                .withRadius(node.get(RADIUS).asDouble())
                                                .build();
    }

    private Junction toJunction(final Session session, final Node lantern)
    {
        Coords coords = ImmutableCoords.of(lantern.get(X).asDouble(), lantern.get(Y).asDouble());
        return ImmutableJunction.builder()
                .withId(lantern.get(ID).asString())
                .withCoords(coords)
                .withTrackingDevices(getTrackingDevices(lantern.get(ID).asString(), coords, session))
                .build();
    }

    private Node extractSingleNode(final Record record)
    {
        return record.values().get(0).asNode();
    }

    @Override
    public Configuration getConfiguration()
    {
        return configuration;
    }

    @Override
    public Route getRoute()
    {
        return route;
    }

    @Override
    public Set<InternalActor> getActors()
    {
        final Set<InternalActor> actors = Sets.newHashSet();
        try (final Session session = driver.session()) {
            StatementResult vehicleResults = session.run("MATCH (v:vehicle) RETURN v");
            while (vehicleResults.hasNext()) {
                Node vehicle = extractSingleNode(vehicleResults.next());
                actors.add(toInternalActor(session, vehicle, ActorType.VEHICLE));
            }
            StatementResult pedestrianResults = session.run("MATCH (p:pedestrian) RETURN p");
            while (pedestrianResults.hasNext()) {
                Node pedestrian = extractSingleNode(pedestrianResults.next());
                actors.add(toInternalActor(session, pedestrian, ActorType.PEDESTRIAN));
            }

            return actors;
        }
    }

    private InternalActor toInternalActor(Session session, Node vehicle, ActorType type)
    {
        String startNodeId = vehicle.get(START_NODE_ID).asString();
        String targetNodeId = vehicle.get(TARGET_NODE_ID).asString();
        Coords startCoords = getNodeCoords(session, startNodeId);
        Junction target = route.getJunctionOfId(targetNodeId);
        return InternalActorBuilder.builder()
                .withId(type == ActorType.VEHICLE ?
                                vehicle.get(VEHICLE_ID).asString() :
                                String.format("%s", pedestrianId++))
                .withType(type)
                .withPreviousCoords(startCoords)
                .withCurrentCoords(startCoords)
                .withTarget(target)
                .withVelocity(vehicle.get(VELOCITY).asDouble())
                .build();
    }

    private Coords getNodeCoords(Session session, String startNodeId)
    {
        String query = String.format("MATCH (x:lantern) WHERE x.nodeId = '%s' RETURN x", startNodeId);
        StatementResult results = session.run(query);
        if (results.hasNext()) {
            Node startLantern = extractSingleNode(results.next());
            return ImmutableCoords.of(startLantern.get(X).asDouble(), startLantern.get(Y).asDouble());
        }
        else throw new IllegalStateException("Node id references nonexistent node");
    }
}
