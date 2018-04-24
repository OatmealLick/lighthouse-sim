package org.wingtree.repositories;

import org.wingtree.beans.*;

import java.util.Set;

/**
 * Abstract interface providing Route and Simulation Actors.
 */
public interface SimulationStateRepository
{
    Route getRoute();

    Set<InternalActor> getActors();
}
