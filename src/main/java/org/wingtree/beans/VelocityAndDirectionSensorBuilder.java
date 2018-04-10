package org.wingtree.beans;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class VelocityAndDirectionSensorBuilder
{
    private Coords coords;
    private double radius;

    private VelocityAndDirectionSensorBuilder()
    {
    }

    public static VelocityAndDirectionSensorBuilder builder()
    {
        return new VelocityAndDirectionSensorBuilder();
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

    public VelocityAndDirectionSensor build()
    {
        checkNotNull(coords);
        checkState(radius > 0);
        return new VelocityAndDirectionSensor(coords, radius);
    }

}
