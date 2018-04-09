package org.wingtree.beans;

import org.wingtree.util.Algebra;

import java.util.Objects;
import java.util.Set;

public class MovementSensor implements TrackingDevice
{
    private boolean sensingMovement;
    private double radius;

    MovementSensor(final boolean sensingMovement, final double radius)
    {
        this.sensingMovement = sensingMovement;
        this.radius = radius;
    }

    public boolean isSensingMovement()
    {
        return sensingMovement;
    }

    public double getRadius()
    {
        return radius;
    }

    public void setSensingMovement(final boolean sensingMovement)
    {
        this.sensingMovement = sensingMovement;
    }

    @Override
    public void updateState(final Coords trackingDeviceCoords, final Set<InternalActor> actors)
    {
        sensingMovement = actors.stream()
                .anyMatch(actor -> Algebra.isTargetInRadius(trackingDeviceCoords, radius, actor.getCurrentCoords()));
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MovementSensor that = (MovementSensor) o;
        return sensingMovement == that.sensingMovement &&
                Double.compare(that.radius, radius) == 0;
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(sensingMovement, radius);
    }

    @Override
    public String toString() {
        return "MovementSensor{" +
                "sensingMovement=" + sensingMovement +
                ", radius=" + radius +
                '}';
    }
}
