package org.wingtree.simulation;

import org.springframework.stereotype.Service;
import org.wingtree.beans.SimulationState;

@Service
public class SimulationStateProvider
{
    private SimulationState simulationState;

    public synchronized SimulationState get()
    {
        return simulationState;
    }

    public synchronized void set(final SimulationState simulationState)
    {
        this.simulationState = simulationState;
    }
}
