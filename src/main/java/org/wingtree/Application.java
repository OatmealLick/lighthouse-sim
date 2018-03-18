package org.wingtree;

import org.quartz.SchedulerException;
import org.wingtree.beans.StartupParameters;
import org.wingtree.http.HttpServer;
import org.wingtree.simulation.SimulationManager;

public class Application
{
    public void run(final String[] args)
    {
        new Thread(() -> new HttpServer().run(args)).start();

        try {
            new SimulationManager(StartupParameters.dummy(), 500).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        new Application().run(args);
    }
}
