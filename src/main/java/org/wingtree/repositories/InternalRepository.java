package org.wingtree.repositories;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.wingtree.beans.ActorType;
import org.wingtree.beans.Camera;
import org.wingtree.beans.CameraBuilder;
import org.wingtree.beans.ImmutableCoords;
import org.wingtree.beans.ImmutableJunction;
import org.wingtree.beans.InternalActor;
import org.wingtree.beans.InternalActorBuilder;
import org.wingtree.beans.Junction;
import org.wingtree.beans.MovementSensor;
import org.wingtree.beans.MovementSensorBuilder;
import org.wingtree.beans.Route;
import org.wingtree.beans.RouteBuilder;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Java hard coded implementation of {@see SimulationStateRepository}. Destined for development use to facilitate
 * test loop.
 */
@Component
@Profile("development")
public class InternalRepository implements SimulationStateRepository {
    final private Camera camera = CameraBuilder.builder()
            .withCoords(ImmutableCoords.of(0, 0))
            .withRadius(0.7f)
            .build();
    final private MovementSensor movementSensor = MovementSensorBuilder.builder()
            .withRadius(2.5f)
            .withCoords(ImmutableCoords.of(0, 10))
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
        final RouteBuilder builder = RouteBuilder.builder();
        Stream.of(Pair.of(one, two), Pair.of(two, three), Pair.of(three, one))
                .forEach(pair -> builder.addRouteSegment(pair.getLeft(), pair.getRight()));
        return builder.build();
    }

    @Override
    public Set<InternalActor> getActors() {
        return ImmutableSet.of(InternalActorBuilder.builder()
                .withId(Optional.of("KR01112"))
                .withCurrentCoords(ImmutableCoords.of(0, 0))
                .withPreviousCoords(ImmutableCoords.of(1,0))
                .withTarget(two)
                .withType(ActorType.VEHICLE)
                .withVelocity(1)
                .build());
    }
}
