package org.wingtree.http;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.wingtree.beans.Junction;
import org.wingtree.beans.SimulationState;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

class SimulationControllerTest
{
    @Test
    void getExistingJunctionBasedOnId()
    {
        // given
        ApplicationContext applicationContextMock = createApplicationContextMockReturningDummySimulationState();

        // when
        Junction junction = new SimulationController(applicationContextMock).getLantern("1");

        // then
        assertThat(junction).isNotNull();
    }

    @Test
    void getNonexistentJunctionBasedOnId()
    {
        // given
        ApplicationContext applicationContextMock = createApplicationContextMockReturningDummySimulationState();

        // when
        Junction junction = new SimulationController(applicationContextMock).getLantern("0");

        // then
        assertThat(junction).extracting(Junction::getId).contains("not found");
    }

    private ApplicationContext createApplicationContextMockReturningDummySimulationState()
    {
        ApplicationContext applicationContextMock = mock(ApplicationContext.class, withSettings().stubOnly());
        AutowireCapableBeanFactory beanFactoryMock = mock(AutowireCapableBeanFactory.class, withSettings().stubOnly());
        given(applicationContextMock.getAutowireCapableBeanFactory()).willReturn(beanFactoryMock);
        given(beanFactoryMock.getBean("simulation-state")).willReturn(SimulationState.dummy());
        return applicationContextMock;
    }
}