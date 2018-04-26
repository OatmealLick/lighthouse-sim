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

import java.util.Optional;
import java.util.Set;

@Component
@Profile({"production"})
public class StandaloneNeo4jRepository implements SimulationStateRepository
{
    private static final String ID = "node_id";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String RADIUS = "radius";
    private static final String DURATION_TIME = "simulationDurationTime";
    private static final String TIME_STEP = "simulationTimeStep";
    private static final String ANGLE = "measurementToleranceAngle";
    private static final String HAS = ":HAS";
    private static final String CONNECTED_TO = ":CONNECTED_TO";

    private final Driver driver;

    @Autowired
    public StandaloneNeo4jRepository(final StandaloneNeo4jDriverProvider provider)
    {
        driver = provider.get();
    }

    @Override
    public Route getRoute()
    {
        final Set<RouteSegment> routeSegments = Sets.newHashSet();
        try (final Session session = driver.session()) {
            final StatementResult statementResult = session.run("MATCH (n:lantern) RETURN n");
            while (statementResult.hasNext()) {
                final Node lantern = extractSingleNode(statementResult.next());
                final Junction from = toJunction(session, lantern);
                final StatementResult connectedToResults = session.run(String.format(
                        "MATCH (:lantern{node_id:\"%s\"})-[%s]->(x) RETURN x",
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
                session.run(String.format("MATCH (:lantern{node_id:\"%s\"})-[%s]->(cam:camera) RETURN cam;",
                        lanternId, HAS));
        while (cameraResults.hasNext()) {
            trackingDevicesBuilder.add(toCamera(extractSingleNode(cameraResults.next()), lanternCoords));
        }

        final StatementResult msResults = session.run(String.format(
                "MATCH (:lantern{node_id:\"%s\"})-[%s]->(ms:movement_sensor) RETURN ms;", lanternId, HAS));
        while (msResults.hasNext()) {
            trackingDevicesBuilder.add(toMovementSensor(extractSingleNode(msResults.next()), lanternCoords));
        }

        final StatementResult vdsResults = session.run(String.format(
                "MATCH (:lantern{node_id:\"%s\"})-[%s]->(vds:velocity_and_direction_sensor) RETURN vds;", lanternId, HAS));
        while (vdsResults.hasNext()) {
            trackingDevicesBuilder.add(toVelocityAndDirectionSensor(extractSingleNode(vdsResults.next()), lanternCoords));
        }
        return trackingDevicesBuilder.build();
    }

    private Camera toCamera(final Node node, final Coords lanternCoords)
    {
        return CameraBuilder.builder()
                            .withCoords(lanternCoords)
                            .withRadius(node.get(RADIUS).asDouble())
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
        return VelocityAndDirectionSensorBuilder.builder()
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
    public Set<InternalActor> getActors()
    {
        try (final Session session = driver.session()) {
            final Node lantern = extractSingleNode(session.run("MATCH (n:lantern) RETURN n").next());
            return ImmutableSet.of(InternalActorBuilder.builder()
                    .withId(Optional.of("KR01112"))
                    .withPreviousCoords(ImmutableCoords.of(0, 0))
                    .withCurrentCoords(ImmutableCoords.of(0, 0)) // FIXME should be configurable
                    .withTarget(toJunction(session, lantern)) // FIXME based on current coords
                    .withType(ActorType.VEHICLE)
                    .withVelocity(1)
                    .build());
        }
    }

    @Override
    public Configuration getConfiguration()
    {
        try (final Session session = driver.session()) {
            final Node configuration = extractSingleNode(session.run("MATCH (c:configuration) RETURN c").next());
            return ImmutableConfiguration.builder()
                    .withSimulationDurationTime(configuration.get(DURATION_TIME).asInt())
                    .withSimulationTimeStep(configuration.get(TIME_STEP).asLong())
                    .withMeasurementToleranceAngle(configuration.get(ANGLE).asDouble())
                    .build();
        }
    }
}
