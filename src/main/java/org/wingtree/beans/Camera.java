package org.wingtree.beans;

import org.wingtree.util.Algebra;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Camera implements TrackingDevice
{
    private Set<InternalActor> actorsInView;
    private final double radius;

    Camera(final Set<InternalActor> actorsInView, final double radius)
    {
        this.actorsInView = actorsInView;
        this.radius = radius;
    }

    public Set<InternalActor> getActorsInView()
    {
        return actorsInView;
    }

    public void setActorsInView(final Set<InternalActor> actorsInView)
    {
        this.actorsInView = actorsInView;
    }

    public double getRadius()
    {
        return radius;
    }

    @Override
    public void updateState(final Coords trackingDeviceCoords, final Set<InternalActor> actors)
    {
        actorsInView = actors.stream()
                .filter(actor -> Algebra.isTargetInRadius(trackingDeviceCoords, radius, actor.getCurrentCoords()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Camera camera = (Camera) o;
        return Double.compare(camera.radius, radius) == 0 &&
                Objects.equals(actorsInView, camera.actorsInView);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(actorsInView, radius);
    }

    @Override
    public String toString() {
        return "Camera{" +
                "actorsInView=" + actorsInView +
                ", radius=" + radius +
                '}';
    }
}
