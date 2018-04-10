package org.wingtree.repositories;

import org.wingtree.beans.*;

import java.util.Set;

public interface SimulationStateRepository
{
    Route getRoute();
    Set<InternalActor> getActors();
    Set<Camera> getCameras();
    Set<MovementSensor> getMovementSensors();
    Set<VelocityAndDirectionSensor> getMovementAndDirectionSensors();
}
