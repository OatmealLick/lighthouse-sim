package org.wingtree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.wingtree.beans.SimulationState;
import org.wingtree.simulation.Simulation;

import java.util.Timer;

@SpringBootApplication
public class Application
{
    public static void main(String[] args) throws InterruptedException
    {
        final ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        final SimulationState simulationState = SimulationState.dummy();
        final long intervalInMillis = 500L;

        context.getBeanFactory().registerSingleton("simulation-state", simulationState);
        context.getBeanFactory().registerSingleton("interval-in-millis", intervalInMillis);

        final Timer timer = new Timer(true);
        timer.scheduleAtFixedRate((Simulation)context.getBean("simulation"), 0, intervalInMillis);
        Thread.sleep(60000);
    }
}
