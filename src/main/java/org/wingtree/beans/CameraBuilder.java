package org.wingtree.beans;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class CameraBuilder
{
    private Coords coords;
    private double radius;
    private double angle;
    private long timeStep;

    private CameraBuilder(final long timeStep)
    {
        this.timeStep = timeStep;
    }

    public static CameraBuilder builder(final long timeStep)
    {
        checkState(timeStep > 0);
        return new CameraBuilder(timeStep);
    }

    public CameraBuilder withCoords(final Coords coords)
    {
        this.coords = coords;
        return this;
    }

    public CameraBuilder withRadius(final double radius)
    {
        this.radius = radius;
        return this;
    }

    public CameraBuilder withAngle(final double angle)
    {
        this.angle = angle;
        return this;
    }

    public Camera build()
    {
        checkNotNull(coords);
        checkState(radius > 0);
        checkState(angle >= 0);
        return new Camera(coords, radius, angle, timeStep);
    }
}
