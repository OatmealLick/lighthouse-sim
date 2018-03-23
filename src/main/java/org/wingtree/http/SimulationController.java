package org.wingtree.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wingtree.beans.Lantern;
import org.wingtree.simulation.SimulationStateProvider;

@Controller("simulation.controller")
public class SimulationController
{
    public SimulationStateProvider simulationStateProvider;

    @Autowired
    public SimulationController(final SimulationStateProvider simulationStateProvider)
    {
        this.simulationStateProvider = simulationStateProvider;
    }

    @RequestMapping("/lantern/{lanternId}")
    public Lantern getLantern(@PathVariable final String lanternId)
    {
        return simulationStateProvider.get().getLanterns().stream().findAny().orElseThrow(RuntimeException::new);
    }
}
