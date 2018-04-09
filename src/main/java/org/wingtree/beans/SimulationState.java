package org.wingtree.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wingtree.repositories.SimulationStateRepository;

import java.util.Set;

@Component
public class SimulationState
{
    private long intervalInMillis;
    private Route route;
    private Set<InternalActor> actors;
    private Set<Camera> cameras;
    private Set<MovementSensor> movementSensors;
    private Set<MovementAndDirectionSensor> movementAndDirectionSensors;

    @Autowired
    public SimulationState(final SimulationStateRepository simulationStateRepository)
    {
        this.intervalInMillis = 500L; // FIXME read from some configuration file?
        this.route = simulationStateRepository.getRoute();
        this.actors = simulationStateRepository.getActors();
//        this.cameras = simulationStateRepository.getCameras();
//        this.movementSensors = simulationStateRepository.getMovementSensors();
//        this.movementAndDirectionSensors = simulationStateRepository.getMovementAndDirectionSensors();
    }

    SimulationState(long intervalInMillis,
                    Route route,
                    Set<InternalActor> actors,
                    Set<Camera> cameras,
                    Set<MovementSensor> movementSensors,
                    Set<MovementAndDirectionSensor> movementAndDirectionSensors)
    {
        this.intervalInMillis = intervalInMillis;
        this.route = route;
        this.actors = actors;
        this.cameras = cameras;
        this.movementSensors = movementSensors;
        this.movementAndDirectionSensors = movementAndDirectionSensors;
    }

    public long getIntervalInMillis()
    {
        return intervalInMillis;
    }

    public Route getRoute()
    {
        return route;
    }

    public Set<InternalActor> getActors()
    {
        return actors;
    }

//    public Set<Camera> getCameras()
//    {
//        return cameras;
//    }
//
//    public Set<MovementSensor> getMovementSensors()
//    {
//        return movementSensors;
//    }
//
//    public Set<MovementAndDirectionSensor> getMovementAndDirectionSensors()
//    {
//        return movementAndDirectionSensors;
//    }
}
