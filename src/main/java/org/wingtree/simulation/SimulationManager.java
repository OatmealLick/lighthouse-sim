package org.wingtree.simulation;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimulationManager
{
    public static final String TIME_INTERVAL = "time.interval";
    public static final String STARTUP_PARAMETERS = "startup.parameters";

    @Autowired
    private SimulationStateProvider simulationStateProvider;


//    @Autowired
//    private StartupParameters startupParameters;
//    @Autowired
//    private long intervalInMillis;

    //TODO consider merging interval to startup params
//    public SimulationManager(final StartupParameters startupParameters, final long intervalInMillis)
//    {
//        this.startupParameters = startupParameters;
//        this.intervalInMillis = intervalInMillis;
//    }

    public void run() throws SchedulerException, InterruptedException
    {
        final Scheduler scheduler = getScheduler();
        final JobDetail jobDetail = JobBuilder.newJob(Simulation.class)
                .withIdentity("simulation.step")
                .build();
        final JobDataMap dataMap = jobDetail.getJobDataMap();
//        final StartupParameters startupParameters = quartzJobFactory.getStartupParameters();
//        dataMap.put(TIME_INTERVAL, intervalInMillis);
//        dataMap.put(STARTUP_PARAMETERS, startupParameters);
        final Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
//                        .withIntervalInMilliseconds(intervalInMillis)
                        .repeatForever())
                .startNow()
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();

        //TODO so this library works like this, you schedule job, and then you need to WAIT/BLOCK thread and let it
        //do work asynchronously, so we should sleep kind of forever if no exception is thrown.
        Thread.sleep(60000);
        scheduler.shutdown(true);
    }

    private Scheduler getScheduler()
    {
        try {
            return StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
