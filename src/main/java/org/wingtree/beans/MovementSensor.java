package org.wingtree.beans;

import org.wingtree.util.Algebra;

import java.util.Objects;
import java.util.Set;

public class MovementSensor implements TrackingDevice
{
    private String lanternId;
    private boolean sensingMovement;
    private double radius;

    MovementSensor(final String lanternId, final boolean sensingMovement, final double radius)
    {
        this.lanternId = lanternId;
        this.sensingMovement = sensingMovement;
        this.radius = radius;
    }

    public String getLanternId()
    {
        return lanternId;
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
                Double.compare(that.radius, radius) == 0 &&
                Objects.equals(lanternId, that.lanternId);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(lanternId, sensingMovement, radius);
    }

    @Override
    public String toString()
    {
        return "MovementSensor{" +
                "lanternId='" + lanternId + '\'' +
                ", sensingMovement=" + sensingMovement +
                ", radius=" + radius +
                '}';
    }
}
