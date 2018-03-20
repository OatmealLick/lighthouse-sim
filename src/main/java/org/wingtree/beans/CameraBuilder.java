package org.wingtree.beans;

import com.google.common.collect.Sets;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class CameraBuilder
{
    private String lanternId;
    private Set<InternalActor> actorsInView = Sets.newHashSet();
    private double radius;

    public static CameraBuilder builder()
    {
        return new CameraBuilder();
    }
    
    private CameraBuilder()
    {
    }

    public CameraBuilder withLanternId(final String lanternId)
    {
        this.lanternId = lanternId;
        return this;
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
        checkNotNull(lanternId);
        checkState(radius > 0);
        return new Camera(lanternId, actorsInView, radius);
    }
}
