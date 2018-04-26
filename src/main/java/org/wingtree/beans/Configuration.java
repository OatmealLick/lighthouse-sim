package org.wingtree.beans;

import org.immutables.value.Value;
import org.wingtree.immutables.Immutable;

@Value.Immutable
@Immutable
public interface Configuration
{
    int getSimulationDurationTime();

    long getSimulationTimeStep();

    double getMeasurementToleranceAngle();
}
