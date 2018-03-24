package org.wingtree.beans;

import com.google.common.collect.ImmutableSet;
import org.immutables.value.Value;
import org.wingtree.immutables.Bean;

import java.util.Optional;
import java.util.Set;

@Value.Immutable
@Bean
public interface StartupParameters //TODO add neo4j -> startup parameters conversion and logic
{
    Route getRoute();

    Set<InternalActor> getInternalActors();

    Set<Camera> getCameras();

    Set<MovementSensor> getMovementSensors();

    Set<MovementAndDirectionSensor> getMovementAndDirectionSensors();

    static StartupParameters dummy()
    {
        Junction start = JunctionBuilder.builder().withCoords(CoordsBuilder.of(5, 5)).withId("lantern_001").build();
        Junction end = JunctionBuilder.builder().withCoords(CoordsBuilder.of(3, 4)).withId("lantern_002").build();
        Route straightRoute = RouteBuilder.builder().withRoad(start, end).build();

        return StartupParametersBuilder.builder()
                .withInternalActors(ImmutableSet.of(
                        InternalActorBuilder.builder()
                                .withId(Optional.of("KR01112"))
                                .withCurrentCoords(start.getCoords())
                                .withTarget(end)
                                .withType(ActorType.VEHICLE)
                                .withVelocity(1)
                                .build()))
                .withRoute(straightRoute)
                .withCameras(ImmutableSet.of(
                        CameraBuilder.builder()
                                .withLanternId("lantern_001")
                                .withInternalActors(ImmutableSet.of())
                                .withRadius(0.7f)
                                .build()))
                .withMovementSensors(ImmutableSet.of(
                        MovementSensorBuilder.builder()
                                .withLanternId("lantern_002")
                                .withRadius(0.5f)
                                .withSensingMovement(false)
                                .build()))
                .withMovementAndDirectionSensors(ImmutableSet.of())
                .build();
    }
}
