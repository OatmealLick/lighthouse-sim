package org.wingtree.repositories;

import org.wingtree.beans.*;

import java.util.Set;

public interface SimulationStateRepository
{
    Route getRoute();
    Set<InternalActor> getActors();
    // todo as of April 9th discussion, removing this. Leaving temporary comment for now
//    Set<Camera> getCameras();
//    Set<MovementSensor> getMovementSensors();
//    Set<MovementAndDirectionSensor> getMovementAndDirectionSensors();
}
