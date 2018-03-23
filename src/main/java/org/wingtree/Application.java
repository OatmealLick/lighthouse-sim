package org.wingtree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.wingtree.beans.StartupParameters;
import org.wingtree.http.HttpServer;
import org.wingtree.simulation.SimulationManager;

import java.util.Arrays;

@SpringBootApplication
public class Application
{
    public static void main(String[] args)
    {
        ConfigurableApplicationContext run = SpringApplication.run(HttpServer.class, args);
        System.out.println(Arrays.toString(run.getBeanDefinitionNames()));
        System.out.println(run.containsBean("simulation.controller"));
        System.out.println(run.containsLocalBean("simulation.controller"));
        try {
            new SimulationManager(StartupParameters.dummy(), 500).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
