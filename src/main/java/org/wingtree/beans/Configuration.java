package org.wingtree.beans;

import org.immutables.value.Value;
import org.springframework.stereotype.Component;
import org.wingtree.immutables.Immutable;

@Value.Immutable
@Immutable
@Component
public interface Configuration
{
    int getSimulationDurationTime();

    long getSimulationTimeStep();
}
