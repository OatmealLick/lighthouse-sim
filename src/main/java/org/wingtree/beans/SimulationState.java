package org.wingtree.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wingtree.repositories.SimulationStateRepository;

import java.util.Set;

/**
 * With each simulation step the contents of this class are updated. It holds actors ({@see InternalActor}) and all
 * static entities of
 * simulation ({@see Junction}, {@see TrackingDevice})
 */
@Component
public class SimulationState
{
    private long intervalInMillis;
    private Route route;
    private Set<InternalActor> actors;

    @Autowired
    public SimulationState(final SimulationStateRepository simulationStateRepository)
    {
        this.intervalInMillis = 500L; // TODO CONFIGURATION
        this.route = simulationStateRepository.getRoute();
        this.actors = simulationStateRepository.getActors();
    }

    SimulationState(long intervalInMillis,
                    Route route,
                    Set<InternalActor> actors)
    {
        this.intervalInMillis = intervalInMillis;
        this.route = route;
        this.actors = actors;
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
}
