package org.wingtree.beans;

import org.wingtree.util.Algebra;

import java.util.Objects;
import java.util.Set;

/**
 * One of tracking devices. In the event of someone entering closer than radius, movement sensor sets its
 * sensingMovement value to true.
 */
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
}
