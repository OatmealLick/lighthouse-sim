package org.wingtree.repositories;

import com.google.common.collect.ImmutableSet;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.wingtree.beans.*;

import java.util.Optional;
import java.util.Set;

@Component
@Profile("development")
public class InternalRepository implements SimulationStateRepository {
    final private Camera camera = CameraBuilder.builder()
            .withRadius(0.7f)
            .build();
    final private MovementSensor movementSensor = MovementSensorBuilder.builder()
            .withRadius(2.5f)
            .withSensingMovement(false)
            .build();
    final private Junction one = ImmutableJunction.builder()
            .withCoords(ImmutableCoords.of(0, 0))
            .withId("1")
            .withTrackingDevices(ImmutableSet.of(camera))
            .build();
    final private Junction two = ImmutableJunction.builder()
            .withCoords(ImmutableCoords.of(0, 10))
            .withId("2")
            .withTrackingDevices(ImmutableSet.of(movementSensor))
            .build();
    final private Junction three = ImmutableJunction.builder()
            .withCoords(ImmutableCoords.of(10, 0))
            .withId("3")
            .withTrackingDevices(ImmutableSet.of())
            .build();

    @Override
    public Route getRoute() {
        return RouteBuilder.builder()
                .withRoad(one, two)
                .withRoad(two, three)
                .withRoad(three, one)
                .build();
    }

    @Override
    public Set<InternalActor> getActors() {
        return ImmutableSet.of(InternalActorBuilder.builder()
                .withId(Optional.of("KR01112"))
                .withCurrentCoords(ImmutableCoords.of(0, 0))
                //todo this will always be the same target?
                .withTarget(two)
                .withType(ActorType.VEHICLE)
                .withVelocity(1)
                .build());
    }
}
