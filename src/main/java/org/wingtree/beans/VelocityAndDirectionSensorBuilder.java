package org.wingtree.beans;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class VelocityAndDirectionSensorBuilder
{
    private Coords coords;
    private double radius;
    private double angle;
    private long timeStep;

    private VelocityAndDirectionSensorBuilder(final long timeStep)
    {
        this.timeStep = timeStep;
    }

    public static VelocityAndDirectionSensorBuilder builder(final long timeStep)
    {
        return new VelocityAndDirectionSensorBuilder(timeStep);
    }

    public VelocityAndDirectionSensorBuilder withCoords(final Coords coords)
    {
        this.coords = coords;
        return this;
    }

    public VelocityAndDirectionSensorBuilder withRadius(final double radius)
    {
        this.radius = radius;
        return this;
    }

    public VelocityAndDirectionSensorBuilder withAngle(final double angle)
    {
        this.angle = angle;
        return this;
    }

    public VelocityAndDirectionSensor build()
    {
        checkNotNull(coords);
        checkState(radius > 0);
        checkState(angle >= 0);
        return new VelocityAndDirectionSensor(coords, radius, angle, timeStep);
    }

}
