package org.wingtree.beans;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class MovementSensorBuilder
{
    private boolean sensingMovement;
    private double radius;

    public static MovementSensorBuilder builder()
    {
        return new MovementSensorBuilder();
    }

    private MovementSensorBuilder()
    {
    }

    public MovementSensorBuilder withSensingMovement(final boolean sensingMovement)
    {
        this.sensingMovement = sensingMovement;
        return this;
    }

    public MovementSensorBuilder withRadius(final double radius)
    {
        this.radius = radius;
        return this;
    }

    public MovementSensor build()
    {
        checkState(radius > 0);
        return new MovementSensor(sensingMovement, radius);
    }
}
