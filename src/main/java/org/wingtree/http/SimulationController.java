package org.wingtree.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wingtree.beans.Junction;
import org.wingtree.beans.SimulationState;

@RestController("simulation.controller")
public class SimulationController
{
    private ApplicationContext applicationContext;

    @Autowired
    public SimulationController(final ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }

    @RequestMapping("/lantern/{lanternId}")
    public Junction getLantern(@PathVariable final String lanternId)
    {
        final SimulationState simulationState = (SimulationState) applicationContext.getAutowireCapableBeanFactory()
                .getBean("simulation-state");
        return simulationState.getRoute().getJunctionOfId(lanternId);
    }
}
