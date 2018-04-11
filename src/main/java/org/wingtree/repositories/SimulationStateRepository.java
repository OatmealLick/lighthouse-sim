package org.wingtree.repositories;

import org.wingtree.beans.*;

import java.util.Set;

public interface SimulationStateRepository
{
    Route getRoute();

    Set<InternalActor> getActors();
}
