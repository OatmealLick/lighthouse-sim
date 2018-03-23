package org.wingtree.beans;

import com.google.common.collect.ImmutableSet;
import org.immutables.value.Value;
import org.wingtree.immutables.Immutable;

import java.util.Optional;
import java.util.Set;

@Value.Immutable
@Immutable
//todo think of better name - JobParameters?
public interface StartupParameters //TODO add neo4j -> startup parameters conversion and logic
{
    //todo this will be actually a graph
    Set<Lantern> getLanterns();

    Set<InternalActor> getInternalActors();

    Set<Camera> getCameras();

    Set<MovementSensor> getMovementSensors();

    Set<MovementAndDirectionSensor> getMovementAndDirectionSensors();

    static StartupParameters dummy()
    {
        final Camera camera = CameraBuilder.builder()
                .withLanternId("lantern_001")
                .withRadius(0.7f)
                .build();
        final MovementSensor movementSensor = MovementSensorBuilder.builder()
                .withLanternId("lantern_002")
                .withRadius(2.5f)
                .withSensingMovement(false)
                .build();
        return ImmutableStartupParameters.builder()
                .withInternalActors(ImmutableSet.of(
                        InternalActorBuilder.builder()
                                .withId(Optional.of("KR01112"))
                                .withCurrentCoords(ImmutableCoords.of(0, 0))
                                .withTargetCoords(ImmutableCoords.of(5, 5))
                                .withType(ActorType.VEHICLE)
                                .withVelocity(1)
                                .build()))
                .withLanterns(ImmutableSet.of(
                        ImmutableLantern.builder()
                                .withCoords(ImmutableCoords.of(5, 5))
                                .withId("lantern_001")
                                .withTrackingDevices(ImmutableSet.of(camera))
                                .build(),
                        ImmutableLantern.builder()
                                .withCoords(ImmutableCoords.of(3, 4))
                                .withId("lantern_002")
                                .withTrackingDevices(ImmutableSet.of(movementSensor))
                                .build()))
                .withCameras(ImmutableSet.of(camera))
                .withMovementSensors(ImmutableSet.of(movementSensor))
                .withMovementAndDirectionSensors(ImmutableSet.of())
                .build();
    }
}
