package org.wingtree.repositories;

import com.google.common.collect.ImmutableSet;
import org.jooq.lambda.Seq;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.wingtree.beans.*;
import org.wingtree.database.EmbeddedNeo4jDriverProvider;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@Profile({"embedded-neo4j"})
public class EmbeddedNeo4jRepository implements SimulationStateRepository {
    private static final String ID = "id";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String RADIUS = "radius";
    private static final String ANGLE = "angle";
    private static final String DURATION_TIME = "simulationDurationTime";
    private static final String TIME_STEP = "simulationTimeStep";
    private static final Label CONFIGURATION = Label.label("configuration");
    private static final RelationshipType CONNECTED_TO = RelationshipType.withName("connected_to");
    private static final RelationshipType HAS = RelationshipType.withName("has");

    private final GraphDatabaseService graphService;
    private final Configuration configuration;

    @Autowired
    public EmbeddedNeo4jRepository(final EmbeddedNeo4jDriverProvider databaseServiceProvider) {
        graphService = databaseServiceProvider.get();
        configuration = loadConfiguration();
    }

    private Configuration loadConfiguration()
    {
        try (final Transaction transaction = graphService.beginTx()) {
            Node configuration = graphService.findNodes(CONFIGURATION)
                                             .stream()
                                             .findFirst()
                                             .orElseThrow(IllegalStateException::new);
            transaction.success();

            return ImmutableConfiguration.builder()
                                         .withSimulationDurationTime((int) configuration.getProperty(DURATION_TIME))
                                         .withSimulationTimeStep((long) configuration.getProperty(TIME_STEP))
                                         .build();
        }
    }

    @Override
    public Route getRoute() {

        try(final Transaction transaction = graphService.beginTx())
        {
            final Node node = graphService.getAllNodes()
                    .stream()
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);
            transaction.success();
            final Set<RouteSegment> routeSegments = new HashSet<>();
            buildPathsFromNodeRecursively(node, routeSegments);
            return RouteBuilder.builder()
                    .addRouteSegments(routeSegments)
                    .build();
        }
    }

    private void buildPathsFromNodeRecursively(final Node node, final Set<RouteSegment> routeSegments) {
        for (final Path path : getLanternsTraverser(node)) {
            final RouteSegment routeSegment = ImmutableRouteSegment.builder()
                    .withFrom(toJunction(path.startNode()))
                    .withTo(toJunction(path.endNode()))
                    .build();
            if(!routeSegments.contains(routeSegment)) {
                routeSegments.add(routeSegment);
                buildPathsFromNodeRecursively(path.endNode(), routeSegments);
            }
        }
    }

    private Traverser getLanternsTraverser(final Node node) {
        final TraversalDescription description = graphService.traversalDescription()
                .breadthFirst()
                .evaluator(path -> {
                    if (path.length() < 1) {
                        return Evaluation.EXCLUDE_AND_CONTINUE;
                    } else if (path.length() > 1) {
                        return Evaluation.EXCLUDE_AND_PRUNE;
                    } else {
                        return Evaluation.INCLUDE_AND_CONTINUE;
                    }
                })
                .relationships(CONNECTED_TO, org.neo4j.graphdb.Direction.OUTGOING);
        return description.traverse(node);
    }

    private Junction toJunction(final Node node) {
        Coords coords = ImmutableCoords.of(
                (double) checkNotNull(node.getProperty(X)),
                (double) checkNotNull(node.getProperty(Y)));
        final Set<TrackingDevice> trackingDevices = Seq.seq(node.getRelationships(HAS))
                .map(relationship -> toTrackingDevice(relationship.getEndNode(), coords))
                .collect(Collectors.toSet());
        return ImmutableJunction.builder()
                .withId((String) checkNotNull(node.getProperty(ID)))
                .withCoords(coords)
                .withTrackingDevices(trackingDevices)
                .build();
    }

    private TrackingDevice toTrackingDevice(final Node node, final Coords lanternCoords) {
        final Label label = Seq.seq(node.getLabels()).findFirst().orElseThrow(IllegalStateException::new);
        switch (label.name()) {
            case "camera":
                return CameraBuilder.builder(configuration.getSimulationTimeStep())
                                    .withCoords(lanternCoords)
                                    .withRadius((double) checkNotNull(node.getProperty(RADIUS)))
                                    .withAngle((double) checkNotNull(node.getProperty(ANGLE)))
                                    .build();
            case "movement-sensor":
                return MovementSensorBuilder.builder()
                                            .withCoords(lanternCoords)
                                            .withRadius((double) checkNotNull(node.getProperty(RADIUS)))
                                            .build();
            case "velocity-and-direction-sensor":
                return VelocityAndDirectionSensorBuilder.builder(configuration.getSimulationTimeStep())
                                                        .withCoords(lanternCoords)
                                                        .withRadius((double) checkNotNull(node.getProperty(RADIUS)))
                                                        .withAngle((double) checkNotNull(node.getProperty(ANGLE)))
                                                        .build();
            case "lantern":
                throw new IllegalStateException("Data model error. Given node is a lantern, and it is supposed to be tracking device");
            default:
                throw new IllegalStateException("Unknown tracking device label");
        }
    }

    @Override
    public Set<InternalActor> getActors() {
        try(final Transaction transaction = graphService.beginTx())
        {
            final Junction junction = toJunction(graphService.getAllNodes()
                    .stream()
                    .filter(node -> node.getProperty(ID).equals("2"))
                    .findFirst()
                    .orElseThrow(IllegalStateException::new));
            transaction.success();
            return ImmutableSet.of(InternalActorBuilder.builder()
                    .withId("KR01112")
                    .withCurrentCoords(ImmutableCoords.of(0, 0))
                    .withPreviousCoords(ImmutableCoords.of(0, 0))
                    .withTarget(junction)
                    .withType(ActorType.VEHICLE)
                    .withVelocity(1)
                    .build());
        }
    }

    @Override
    public Configuration getConfiguration()
    {
        return configuration;
    }
}
