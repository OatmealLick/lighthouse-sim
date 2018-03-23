package org.wingtree.beans;

import org.immutables.value.Value;
import org.wingtree.immutables.Immutable;

import java.util.Set;

@Value.Immutable
@Immutable
public interface SimulationState
{
    //todo this will be actually a graph
    Set<Lantern> getLanterns();

    Set<InternalActor> getInternalActors();

    Set<Camera> getCameras();

    Set<MovementSensor> getMovementSensors();

    Set<MovementAndDirectionSensor> getMovementAndDirectionSensors();
}
