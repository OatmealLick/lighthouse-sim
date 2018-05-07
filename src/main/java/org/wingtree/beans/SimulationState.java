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
    private Configuration configuration;
    private Route route;
    private Set<InternalActor> actors;

    @Autowired
    public SimulationState(final SimulationStateRepository simulationStateRepository)
    {
        this.configuration = simulationStateRepository.getConfiguration();
        this.route = simulationStateRepository.getRoute();
        this.actors = simulationStateRepository.getActors();
    }

    SimulationState(Configuration configuration,
                    Route route,
                    Set<InternalActor> actors)
    {
        this.configuration = configuration;
        this.route = route;
        this.actors = actors;
    }

    public Configuration getConfiguration()
    {
        return configuration;
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
