package org.wingtree.simulation;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.wingtree.beans.*;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

public class SimulationTest
{
    @Test
    public void updatedActorApproachesTargetInVerticalLine()
    {
        // given
        ApplicationContext applicationCtxMock = mock(ApplicationContext.class, withSettings().stubOnly());
        Junction target = ImmutableJunction.of("2", ImmutableCoords.of(0, 10), ImmutableSet.of());
        InternalActor actor = InternalActorBuilder.builder()
                .withId("KR12345")
                .withType(ActorType.VEHICLE)
                .withVelocity(1)
                .withCurrentCoords(ImmutableCoords.of(0, 5))
                .withPreviousCoords(ImmutableCoords.of(0, 0))
                .withTarget(target)
                .build();
        SimulationState simulationState = customSimulationStateOne(ImmutableSet.of());

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

    @Test
    public void updatedActorApproachesTargetInSlantedLine()
    {
        // given
        ApplicationContext applicationCtxMock = mock(ApplicationContext.class, withSettings().stubOnly());
        Junction target = ImmutableJunction.of("3", ImmutableCoords.of(10, 0), ImmutableSet.of());
        InternalActor actor = InternalActorBuilder.builder()
                                                  .withId("KR12345")
                                                  .withType(ActorType.VEHICLE)
                                                  .withVelocity(1)
                                                  .withCurrentCoords(ImmutableCoords.of(5, 5))
                                                  .withPreviousCoords(ImmutableCoords.of(0, 10))
                                                  .withTarget(target)
                                                  .build();
        SimulationState simulationState = customSimulationStateOne(ImmutableSet.of());

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
    public void updatedActorApproachesTargetInNegativeSlantedLine()
    {
        // given
        ApplicationContext applicationCtxMock = mock(ApplicationContext.class, withSettings().stubOnly());
        Junction target = ImmutableJunction.of("1", ImmutableCoords.of(0, 0), ImmutableSet.of());
        InternalActor actor = InternalActorBuilder.builder()
                .withId("KR12345")
                .withType(ActorType.VEHICLE)
                .withVelocity(1)
                .withCurrentCoords(ImmutableCoords.of(5, 5))
                .withPreviousCoords(ImmutableCoords.of(0, 0))
                .withTarget(target)
                .build();
        SimulationState simulationState = customSimulationStateTwo(ImmutableSet.of());

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
        assertEquals(5 - 1.0 / Math.sqrt(2), updatedCoords.getX(), 10e-2);
        assertEquals(5 - 1.0 / Math.sqrt(2), updatedCoords.getY(), 10e-2);
    }

    @Test
    public void updatedActorExceedsTargetAndTravelsTowardsNewOne()
    {
        // given
        ApplicationContext applicationCtxMock = mock(ApplicationContext.class, withSettings().stubOnly());
        Junction target = ImmutableJunction.of("1", ImmutableCoords.of(0, 0), ImmutableSet.of());
        Junction newTarget = ImmutableJunction.of("2", ImmutableCoords.of(0, 10), ImmutableSet.of());
        InternalActor actor = InternalActorBuilder.builder()
                .withId("KR12345")
                .withType(ActorType.VEHICLE)
                .withVelocity(1)
                .withCurrentCoords(ImmutableCoords.of(0.5, 0))
                .withPreviousCoords(ImmutableCoords.of(0, 0))
                .withTarget(target)
                .build();
        SimulationState simulationState = customSimulationStateOne(ImmutableSet.of());

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

    @Test
    public void updateMultipleActors()
    {
        // given
        Junction target1 = ImmutableJunction.of("2", ImmutableCoords.of(0, 10), ImmutableSet.of());
        Junction target2 = ImmutableJunction.of("1", ImmutableCoords.of(0, 0), ImmutableSet.of());
        InternalActor vehicle = InternalActorBuilder.builder()
                                                    .withId("KR12345")
                                                    .withType(ActorType.VEHICLE)
                                                    .withVelocity(1)
                                                    .withCurrentCoords(ImmutableCoords.of(0, 5))
                                                    .withPreviousCoords(ImmutableCoords.of(0, 0))
                                                    .withTarget(target1)
                                                    .build();
        InternalActor pedestrian = InternalActorBuilder.builder()
                                                       .withId("1")
                                                       .withType(ActorType.PEDESTRIAN)
                                                       .withVelocity(1)
                                                       .withCurrentCoords(ImmutableCoords.of(5, 0))
                                                       .withPreviousCoords(ImmutableCoords.of(10, 0))
                                                       .withTarget(target2)
                                                       .build();
        ApplicationContext applicationCtxMock = mock(ApplicationContext.class, withSettings().stubOnly());
        given(applicationCtxMock.getBean(SimulationState.class)).willReturn(
                customSimulationStateOne(ImmutableSet.of(vehicle, pedestrian)));

        // when
        new Simulation(applicationCtxMock).run();

        // then
        assertThat(vehicle).extracting(
                InternalActor::getCurrentCoords,
                InternalActor::getTarget,
                InternalActor::getVelocityInMetersPerSec
        ).containsExactly(
                ImmutableCoords.of(0, 6),
                target1,
                1.0
        );
        assertThat(pedestrian).extracting(
                InternalActor::getCurrentCoords,
                InternalActor::getTarget,
                InternalActor::getVelocityInMetersPerSec
        ).containsExactly(
                ImmutableCoords.of(4, 0),
                target2,
                1.0
        );
    }

    private SimulationState customSimulationStateOne(Set<InternalActor> actors)
    {
        Junction one = ImmutableJunction.of("1", ImmutableCoords.of(0, 0), ImmutableSet.of());
        Junction two = ImmutableJunction.of("2", ImmutableCoords.of(0, 10), ImmutableSet.of());
        Junction three = ImmutableJunction.of("3", ImmutableCoords.of(10, 0), ImmutableSet.of());

        return SimulationStateBuilder.builder()
                .withConfiguration(ImmutableConfiguration.builder()
                                                         .withSimulationDurationTime(60)
                                                         .withSimulationTimeStep(1000)
                                                         .build())
                .withActors(actors)
                .withRoute(RouteBuilder.builder()
                        .addRouteSegment(one, two)
                        .addRouteSegment(two, three)
                        .addRouteSegment(three, one)
                        .build())
                .build();
    }

    private SimulationState customSimulationStateTwo(Set<InternalActor> actors)
    {
        Junction one = ImmutableJunction.of("1", ImmutableCoords.of(0, 0), ImmutableSet.of());
        Junction two = ImmutableJunction.of("2", ImmutableCoords.of(0, 10), ImmutableSet.of());
        Junction three = ImmutableJunction.of("3", ImmutableCoords.of(10, 10), ImmutableSet.of());

        return SimulationStateBuilder.builder()
                 .withConfiguration(ImmutableConfiguration.builder()
                                                          .withSimulationDurationTime(60)
                                                          .withSimulationTimeStep(1000)
                                                          .build())
                 .withActors(actors)
                 .withRoute(RouteBuilder.builder()
                                        .addRouteSegment(one, two)
                                        .addRouteSegment(two, three)
                                        .addRouteSegment(three, one)
                                        .build())
                 .build();
    }
}