package org.wingtree.repositories;

import com.google.common.collect.ImmutableSet;
import org.jooq.lambda.Seq;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.wingtree.beans.*;
import org.wingtree.beans.Direction;
import org.wingtree.database.GraphDatabaseServiceProvider;

import java.io.File;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@Profile({"embedded-neo4j", "production"})
public class GraphDbRepository implements SimulationStateRepository {
    private static final String ID = "id";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String RADIUS = "radius";
    private static final String SENSING_MOVEMENT = "sensing_movement";
    private static final Label LANTERN_LABEL = Label.label("lantern");
    private static final Label CAMERA_LABEL = Label.label("camera");
    private static final Label MOVEMENT_SENSOR_LABEL = Label.label("movement-sensor");
    private static final RelationshipType CONNECTED_TO = RelationshipType.withName("connected_to");
    private static final RelationshipType HAS = RelationshipType.withName("has");

    private final GraphDatabaseService graphService;

    @Autowired
    public GraphDbRepository(final GraphDatabaseServiceProvider databaseServiceProvider) {
        graphService = databaseServiceProvider.get();
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
            final RouteBuilder builder = RouteBuilder.builder();
            for (final Path path : getLanternsTraverser(node)) {
                builder.withRoad(toJunction(path.startNode()), toJunction(path.endNode()));
            }
            return builder.build();
        }
    }

    private Traverser getLanternsTraverser(final Node node) {
        final TraversalDescription description = graphService.traversalDescription()
                .breadthFirst()
                .evaluator(path -> {
                    if (path.length() < 2) {
                        return Evaluation.EXCLUDE_AND_CONTINUE;
                    } else if (path.length() > 2) {
                        return Evaluation.EXCLUDE_AND_PRUNE;
                    } else {
                        return Evaluation.INCLUDE_AND_CONTINUE;
                    }
                })
                .relationships(CONNECTED_TO, org.neo4j.graphdb.Direction.OUTGOING);
        return description.traverse(node);
    }

    private Junction toJunction(final Node node) {
        final Set<TrackingDevice> trackingDevices = Seq.seq(node.getRelationships(HAS))
                .map(relationship -> toTrackingDevice(relationship.getEndNode()))
                .collect(Collectors.toSet());
        return ImmutableJunction.builder()
                .withId((String) checkNotNull(node.getProperty(ID)))
                .withCoords(ImmutableCoords.of(
                        (double) checkNotNull(node.getProperty(X)),
                        (double) checkNotNull(node.getProperty(Y))))
                .withTrackingDevices(trackingDevices)
                .build();
    }

    private TrackingDevice toTrackingDevice(final Node node) {
        final Label label = Seq.seq(node.getLabels()).findFirst().orElseThrow(IllegalStateException::new);
        switch (label.name()) {
            case "camera":
                return CameraBuilder.builder()
                        .withRadius((double) checkNotNull(node.getProperty(RADIUS)))
                        .withActorsInView(ImmutableSet.of())
                        .build();
            case "lantern":
                throw new IllegalStateException("Data model error. Given node is a lantern, and it is supposed to be tracking device");
            case "movement-sensor":
                return MovementSensorBuilder.builder()
                        .withRadius((double) checkNotNull(node.getProperty(RADIUS)))
                        .withSensingMovement((boolean) checkNotNull(node.getProperty(SENSING_MOVEMENT)))
                        .build();
            default:
                throw new IllegalStateException("Unknown tracking device label");
        }
    }

    @Override
    public Set<InternalActor> getActors() {
        //todo this might not always be the same junction :^)
        final Junction junction = toJunction(graphService.getAllNodes()
                .stream()
                .findFirst()
                .orElseThrow(IllegalStateException::new));
        return ImmutableSet.of(InternalActorBuilder.builder()
                .withId(Optional.of("KR01112"))
                .withCurrentCoords(ImmutableCoords.of(0, 0))
                .withTarget(junction)
                .withType(ActorType.VEHICLE)
                .withVelocity(1)
                .build());
    }
}
