package org.wingtree.beans;

import org.wingtree.util.Algebra;

import java.util.Objects;
import java.util.Set;

public class MovementSensor implements TrackingDevice
{
    private Coords coords;
    private double radius;
    private boolean sensingMovement;

    MovementSensor(final Coords coords, final double radius)
    {
        this.coords = coords;
        this.radius = radius;
    }

    public boolean isSensingMovement()
    {
        return sensingMovement;
    }

    @Override
    public Coords getCoords()
    {
        return coords;
    }

    public double getRadius()
    {
        return radius;
    }

    @Override
    public void updateState(final Set<InternalActor> actors)
    {
        sensingMovement = actors.stream()
                .anyMatch(actor -> Algebra.isTargetInRadius(coords, radius, actor.getCurrentCoords()));
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovementSensor that = (MovementSensor) o;
        return Double.compare(that.radius, radius) == 0 &&
                sensingMovement == that.sensingMovement &&
                Objects.equals(coords, that.coords);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(coords, radius, sensingMovement);
    }

    @Override
    public String toString()
    {
        return "MovementSensor{" +
                "coords=" + coords +
                ", radius=" + radius +
                ", sensingMovement=" + sensingMovement +
                '}';
    }
}
