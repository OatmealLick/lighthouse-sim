package org.wingtree.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wingtree.beans.Junction;
import org.wingtree.beans.SimulationState;

@RestController
public class SimulationController
{
    private ApplicationContext applicationContext;

    @Autowired
    public SimulationController(final ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }

    @RequestMapping("/lanterns/{lanternId}")
    public Junction getLantern(@PathVariable final String lanternId)
    {
        final SimulationState simulationState = applicationContext.getBean(SimulationState.class);
        return simulationState.getRoute().getJunctionOfId(lanternId);
    }
}
