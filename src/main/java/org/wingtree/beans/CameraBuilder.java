package org.wingtree.beans;

import com.google.common.collect.Sets;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class CameraBuilder
{
    private Coords coords;
    private double radius;

    private CameraBuilder()
    {
    }

    public static CameraBuilder builder()
    {
        return new CameraBuilder();
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

    public Camera build()
    {
        checkNotNull(coords);
        checkState(radius > 0);
        return new Camera(coords, radius);
    }
}
