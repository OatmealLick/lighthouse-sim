package org.wingtree.simulation;

import org.quartz.SimpleTrigger;
import org.quartz.spi.JobFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@Configuration
public class QuartzConfig
{
    @Bean
    public JobFactory jobFactory(final ApplicationContext applicationContext)
    {
        final QuartzJobFactory quartzJobFactory = new QuartzJobFactory();
        quartzJobFactory.setApplicationContext(applicationContext);
        return quartzJobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(final ApplicationContext applicationContext)
    {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        factory.setJobFactory(jobFactory(applicationContext));
//        Properties quartzProperties = new Properties();
//        quartzProperties.setProperty("org.quartz.scheduler.instanceName", instanceName);
//        quartzProperties.setProperty("org.quartz.scheduler.instanceId", instanceId);
//        quartzProperties.setProperty("org.quartz.threadPool.threadCount", threadCount);
//        factory.setDataSource(dataSource);
//        factory.setQuartzProperties(quartzProperties);
        factory.setTriggers(simulationTrigger().getObject());
        return factory;
    }


    @Bean(name = "simulation-trigger")
    public SimpleTriggerFactoryBean simulationTrigger()
    {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(simulationJobDetails().getObject());
//        factoryBean.setStartDelay(startDelay);
        factoryBean.setRepeatInterval(500);
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);

        return factoryBean;
    }

    @Bean(name = "simulation-job-details")
    public JobDetailFactoryBean simulationJobDetails()
    {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(Simulation.class);
//        jobDetailFactoryBean.setDescription(description);
        jobDetailFactoryBean.setDurability(true);
//        jobDetailFactoryBean.setName(key);
        return jobDetailFactoryBean;
    }
}
