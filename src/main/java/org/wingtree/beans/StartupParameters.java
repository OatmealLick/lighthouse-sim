package org.wingtree.beans;

import com.google.common.collect.ImmutableSet;
import org.immutables.value.Value;
import org.wingtree.immutables.Bean;

import java.util.Set;

@Value.Immutable
@Bean
public interface StartupParameters
{
    Set<Lantern> getLanterns();

    Set<InternalActor> getInternalActors();

    Set<Camera> getCameras();

    Set<MovementSensor> getMovementSensors();

    Set<MovementAndDirectionSensor> getMovementAndDirectionSensors();

    static StartupParameters dummy()
    {
        return StartupParametersBuilder.builder()
                .withInternalActors(ImmutableSet.of(
                        InternalActorBuilder.builder()
                                .withId("KR01112")
                                .withCurrentCoords(CoordsBuilder.of(0, 0))
                                .withTargetCoords(CoordsBuilder.of(5, 5))
                                .withType(ActorType.VEHICLE)
                                .withVelocity(1)
                                .build()))
                .withLanterns(ImmutableSet.of(LanternBuilder.builder()
                                .withCoords(CoordsBuilder.of(5, 5))
                                .withId("lantern_001")
                                .build(),
                        LanternBuilder.builder()
                                .withCoords(CoordsBuilder.of(3, 4))
                                .withId("lantern_002")
                                .build()))
                .withCameras(ImmutableSet.of(CameraBuilder.builder()
                        .withLanternId("lantern_001")
                        .withInternalActors(ImmutableSet.of())
                        .withRadius(0.7f)
                        .build()))
                .withMovementSensors(ImmutableSet.of(MovementSensorBuilder.builder()
                        .withLanternId("lantern_002")
                        .withRadius(0.5f)
                        .withSensingMovement(false)
                        .build()))
                .withMovementAndDirectionSensors(ImmutableSet.of())
                .build();
    }
}
