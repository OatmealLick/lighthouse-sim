package org.wingtree.beans;

import com.google.common.collect.Sets;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class CameraBuilder
{
    private Set<InternalActor> actorsInView = Sets.newHashSet();
    private double radius;

    public static CameraBuilder builder()
    {
        return new CameraBuilder();
    }
    
    private CameraBuilder()
    {
    }

    public CameraBuilder withActorsInView(final Set<InternalActor> actorsInView)
    {
        this.actorsInView = actorsInView;
        return this;
    }

    public CameraBuilder withRadius(final double radius)
    {
        this.radius = radius;
        return this;
    }

    public Camera build()
    {
        checkState(radius > 0);
        return new Camera(actorsInView, radius);
    }
}
