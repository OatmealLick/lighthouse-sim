package org.wingtree.repositories;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.wingtree.beans.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Component
@Profile("development")
public class InternalRepository implements SimulationStateRepository
{
    private static final long TIME_STEP = 500;
    final private Camera camera = CameraBuilder.builder(TIME_STEP)
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
                .withId("KR01112")
                .withCurrentCoords(ImmutableCoords.of(0, 0))
                .withPreviousCoords(ImmutableCoords.of(1,0))
                .withTarget(two)
                .withType(ActorType.VEHICLE)
                .withVelocity(1)
                .build());
    }

    @Override
    public Configuration getConfiguration()
    {
        return ImmutableConfiguration.builder()
                .withSimulationDurationTime(60)
                .withSimulationTimeStep(500)
                .build();
    }
}
