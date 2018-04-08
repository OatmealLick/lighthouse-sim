package org.wingtree.simulation;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.wingtree.beans.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

class SimulationTest
{
    @Test
    void updatedActorApproachesTargetInVerticalLine()
    {
        // given
        ApplicationContext applicationCtxMock = mock(ApplicationContext.class, withSettings().stubOnly());
        Junction target = ImmutableJunction.of("2", ImmutableCoords.of(0, 10), ImmutableSet.of());
        InternalActor actor = InternalActorBuilder.builder()
                .withId(Optional.of("KR12345"))
                .withType(ActorType.VEHICLE)
                .withVelocity(1)
                .withCurrentCoords(ImmutableCoords.of(0, 5))
                .withTarget(target)
                .build();
        SimulationState simulationState = customSimulationState();

        // when
        new Simulation(applicationCtxMock).updateActor(actor, simulationState);

        // then
        assertThat(actor).extracting(
                InternalActor::getCurrentCoords,
                InternalActor::getTarget,
                InternalActor::getVelocityInMetersPerSec
        ).containsExactly(
                ImmutableCoords.of(0, 6),
                target,
                1.0
        );
    }

    // TODO add test for movement in "negative direction" (with decreasing x and y coords values)

    @Test
    void updatedActorApproachesTargetInSlantedLine()
    {
        // given
        ApplicationContext applicationCtxMock = mock(ApplicationContext.class, withSettings().stubOnly());
        Junction target = ImmutableJunction.of("3", ImmutableCoords.of(10, 0), ImmutableSet.of());
        InternalActor actor = InternalActorBuilder.builder()
                .withId(Optional.of("KR12345"))
                .withType(ActorType.VEHICLE)
                .withVelocity(1)
                .withCurrentCoords(ImmutableCoords.of(5, 5))
                .withTarget(target)
                .build();
        SimulationState simulationState = customSimulationState();

        // when
        new Simulation(applicationCtxMock).updateActor(actor, simulationState);

        // then
        assertThat(actor).extracting(
                InternalActor::getTarget,
                InternalActor::getVelocityInMetersPerSec
        ).containsExactly(
                target,
                1.0
        );
        Coords updatedCoords = actor.getCurrentCoords();
        assertEquals(5 + 1.0 / Math.sqrt(2), updatedCoords.getX(), 10e-2);
        assertEquals(5 - 1.0 / Math.sqrt(2), updatedCoords.getY(), 10e-2);
    }

    @Test
    void updatedActorExceedsTargetAndTravelsTowardsNewOne()
    {
        // given
        ApplicationContext applicationCtxMock = mock(ApplicationContext.class, withSettings().stubOnly());
        Junction target = ImmutableJunction.of("1", ImmutableCoords.of(0, 0), ImmutableSet.of());
        Junction newTarget = ImmutableJunction.of("2", ImmutableCoords.of(0, 10), ImmutableSet.of());
        InternalActor actor = InternalActorBuilder.builder()
                .withId(Optional.of("KR12345"))
                .withType(ActorType.VEHICLE)
                .withVelocity(1)
                .withCurrentCoords(ImmutableCoords.of(0.5, 0))
                .withTarget(target)
                .build();
        SimulationState simulationState = customSimulationState();

        // when
        new Simulation(applicationCtxMock).updateActor(actor, simulationState);

        // then
        assertThat(actor).extracting(
                InternalActor::getCurrentCoords,
                InternalActor::getTarget,
                InternalActor::getVelocityInMetersPerSec
        ).containsExactly(
                ImmutableCoords.of(0, 0.5),
                newTarget,
                1.0
        );

    }

    private SimulationState customSimulationState()
    {
        Junction one = ImmutableJunction.of("1", ImmutableCoords.of(0, 0), ImmutableSet.of());
        Junction two = ImmutableJunction.of("2", ImmutableCoords.of(0, 10), ImmutableSet.of());
        Junction three = ImmutableJunction.of("3", ImmutableCoords.of(10, 0), ImmutableSet.of());

        return SimulationStateBuilder.builder()
                                     .withIntervalInMillis(1000L)
                                     .withActors(ImmutableSet.of())
                                     .withRoute(RouteBuilder.builder()
                                                            .withRoad(one, two)
                                                            .withRoad(two, three)
                                                            .withRoad(three, one)
                                                            .build())
                                     .withCameras(ImmutableSet.of())
                                     .withMovementSensors(ImmutableSet.of())
                                     .withMovementAndDirectionSensors(ImmutableSet.of())
                                     .build();
    }
}