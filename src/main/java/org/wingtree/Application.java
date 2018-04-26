package org.wingtree;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.factory.GraphDatabaseFacade;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.wingtree.beans.Configuration;
import org.wingtree.beans.SimulationState;
import org.wingtree.simulation.Simulation;

import java.io.File;
import java.util.Timer;

@SpringBootApplication
public class Application
{
    public static void main(String[] args) throws InterruptedException
    {
        final ApplicationContext context = SpringApplication.run(Application.class, args);
        scheduleSimulation(context);
    }

    private static void scheduleSimulation(ApplicationContext context) throws InterruptedException
    {
        final Timer timer = new Timer(true);
        Simulation simulation = context.getBean(Simulation.class);
        SimulationState simulationState = context.getBean(SimulationState.class);
        Configuration configuration = simulationState.getConfiguration();
        long intervalInMillis = configuration.getSimulationTimeStep();
        long simulationDurationTimeInMillis = configuration.getSimulationDurationTime();

        timer.scheduleAtFixedRate(simulation, 0, intervalInMillis);
        Thread.sleep(simulationDurationTimeInMillis);
    }
}
