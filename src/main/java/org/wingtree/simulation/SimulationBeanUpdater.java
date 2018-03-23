package org.wingtree.simulation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.wingtree.beans.StartupParameters;

public class SimulationBeanUpdater implements BeanPostProcessor
{
    private StartupParameters startupParameters;
    private long intervalInMillis;

    public SimulationBeanUpdater(final StartupParameters startupParameters, final long intervalInMillis)
    {
        this.startupParameters = startupParameters;
        this.intervalInMillis = intervalInMillis;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException
    {
        switch (beanName) {

            case "simulation-trigger":
                return bean;
            default:
                return bean;
        }
    }
}
