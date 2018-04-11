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
import org.wingtree.beans.ActorType;
import org.wingtree.beans.Camera;
import org.wingtree.beans.CameraBuilder;
import org.wingtree.beans.ImmutableCoords;
import org.wingtree.beans.ImmutableJunction;
import org.wingtree.beans.ImmutableRouteSegment;
import org.wingtree.beans.InternalActor;
import org.wingtree.beans.InternalActorBuilder;
import org.wingtree.beans.MovementSensor;
import org.wingtree.beans.MovementSensorBuilder;
import org.wingtree.beans.Route;
import org.wingtree.beans.RouteBuilder;
import org.wingtree.beans.RouteSegment;
import org.wingtree.beans.TrackingDevice;
import org.wingtree.database.StandaloneNeo4jDriverProvider;

import java.util.Optional;
import java.util.Set;

@Component
@Profile({"production"})
public class StandaloneNeo4jRepository implements SimulationStateRepository
{
    private static final String ID = "id";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String RADIUS = "radius";
    private static final String SENSING_MOVEMENT = "sensing_movement";
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
                final ImmutableJunction from = toJunction(session, lantern);
                final StatementResult connectedToResults = session.run(String.format(
                        "MATCH (:lantern{id:\"%s\"})-[%s]->(x) RETURN x",
                        lantern.get(ID).asString(),
                        CONNECTED_TO));
                while (connectedToResults.hasNext()) {
                    final Node connectedLantern = extractSingleNode(connectedToResults.next());
                    final ImmutableJunction to = toJunction(session, connectedLantern);
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

    private Set<TrackingDevice> getTrackingDevices(final String lanternId, final Session session)
    {
        final ImmutableSet.Builder<TrackingDevice> trackingDevicesBuilder = new ImmutableSet.Builder<>();
        final StatementResult cameraResults =
                session.run(String.format("MATCH (:lantern{id:\"%s\"})-[%s]->(cam:camera) RETURN cam;",
                        lanternId, HAS));
        while (cameraResults.hasNext()) {
            trackingDevicesBuilder.add(toCamera(extractSingleNode(cameraResults.next())));
        }
        final StatementResult msResults = session.run(String.format(
                "MATCH (:lantern{id:\"%s\"})-[%s]->(ms:movement_sensor) RETURN ms;", lanternId, HAS));
        while (msResults.hasNext()) {
            trackingDevicesBuilder.add(toMovementSensor(extractSingleNode(msResults.next())));
        }
        //todo add third type
        return trackingDevicesBuilder.build();
    }

    private Camera toCamera(final Node node)
    {
        return CameraBuilder.builder()
                .withRadius(node.get(RADIUS).asDouble())
                .withActorsInView(Sets.newHashSet())
                .build();
    }

    private MovementSensor toMovementSensor(final Node node)
    {
        return MovementSensorBuilder.builder()
                .withRadius(node.get(RADIUS).asDouble())
                .withSensingMovement(node.get(SENSING_MOVEMENT).asBoolean())
                .build();
    }

    private ImmutableJunction toJunction(final Session session, final Node lantern)
    {
        return ImmutableJunction.builder()
                .withId(lantern.get(ID).asString())
                .withCoords(ImmutableCoords.of(lantern.get(X).asDouble(), lantern.get(Y).asDouble()))
                .withTrackingDevices(getTrackingDevices(lantern.get(ID).asString(), session))
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
                    .withCurrentCoords(ImmutableCoords.of(0, 0))
                    .withTarget(toJunction(session, lantern))
                    .withType(ActorType.VEHICLE)
                    .withVelocity(1)
                    .build());
        }
    }
}
