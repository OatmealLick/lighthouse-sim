package org.wingtree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.wingtree.beans.StartupParameters;
import org.wingtree.simulation.SimulationBeanUpdater;
import org.wingtree.simulation.SimulationManager;

@SpringBootApplication
public class Application
{
    public static void main(String[] args)
    {
        final ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        context.getBeanFactory().registerSingleton("startup-parameters", StartupParameters.dummy());
        context.getBeanFactory().registerSingleton("interval-in-millis", 500);

        context.getBeanFactory().addBeanPostProcessor(new SimulationBeanUpdater(StartupParameters.dummy(),500));
//        context.getBeanFactory().addBeanPostProcessor();
//        context.refresh();

        try {
            new SimulationManager().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
