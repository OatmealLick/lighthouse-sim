package org.wingtree.beans;

import com.google.common.collect.ImmutableSet;
import org.immutables.value.Value;
import org.wingtree.immutables.Immutable;

import java.util.Optional;
import java.util.Set;

@Value.Immutable
@Immutable
public interface SimulationState
{
    Route getRoute();

    Set<InternalActor> getInternalActors();

    Set<Camera> getCameras();

    Set<MovementSensor> getMovementSensors();

    Set<MovementAndDirectionSensor> getMovementAndDirectionSensors();

    static SimulationState dummy()
    {
        final Camera camera = CameraBuilder.builder()
                .withLanternId("1")
                .withRadius(0.7f)
                .build();
        final MovementSensor movementSensor = MovementSensorBuilder.builder()
                .withLanternId("2")
                .withRadius(2.5f)
                .withSensingMovement(false)
                .build();
        final Junction one = ImmutableJunction.builder()
                .withCoords(ImmutableCoords.of(0, 0))
                .withId("1")
                .withTrackingDevices(ImmutableSet.of(camera))
                .build();
        final Junction two = ImmutableJunction.builder()
                .withCoords(ImmutableCoords.of(0, 10))
                .withId("2")
                .withTrackingDevices(ImmutableSet.of(movementSensor))
                .build();
        final Junction three = ImmutableJunction.builder()
                .withCoords(ImmutableCoords.of(10, 0))
                .withId("3")
                .withTrackingDevices(ImmutableSet.of())
                .build();
        return ImmutableSimulationState.builder()
                .withInternalActors(ImmutableSet.of(
                        InternalActorBuilder.builder()
                                .withId(Optional.of("KR01112"))
                                .withCurrentCoords(ImmutableCoords.of(0, 0))
                                .withTarget(two)
                                .withType(ActorType.VEHICLE)
                                .withVelocity(1)
                                .build()))
                .withRoute(RouteBuilder.builder()
                                   .withRoad(one, two)
                                   .withRoad(two, three)
                                   .withRoad(three, one)
                                   .build())
                .withCameras(ImmutableSet.of(camera))
                .withMovementSensors(ImmutableSet.of(movementSensor))
                .withMovementAndDirectionSensors(ImmutableSet.of())
                .build();
    }
}
