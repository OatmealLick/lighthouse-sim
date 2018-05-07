package org.wingtree.beans;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class SimulationStateBuilder
{
    private Configuration configuration;
    private Route route;
    private Set<InternalActor> actors;

    private SimulationStateBuilder()
    {
    }

    public static SimulationStateBuilder builder()
    {
        return new SimulationStateBuilder();
    }

    public SimulationStateBuilder withConfiguration(final Configuration configuration)
    {
        this.configuration = configuration;
        return this;
    }

    public SimulationStateBuilder withRoute(final Route route)
    {
        this.route = route;
        return this;
    }

    public SimulationStateBuilder withActors(final Set<InternalActor> actors)
    {
        this.actors = actors;
        return this;
    }

    public SimulationState build()
    {
        checkNotNull(configuration);
        checkState(configuration.getSimulationTimeStep() > 0);
        checkState(configuration.getSimulationDurationTime() > 0);
        checkNotNull(route);
        checkNotNull(actors);

        return new SimulationState(configuration, route, actors);
    }
}
