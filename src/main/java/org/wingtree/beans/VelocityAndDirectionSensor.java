package org.wingtree.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.wingtree.util.Algebra;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * One of tracking devices. Holds data in readings {@see Reading}
 */
public class VelocityAndDirectionSensor implements TrackingDevice
{
    protected Coords coords;
    protected double radius;
    protected double angle;
    @JsonIgnore
    protected double timeStep;
    private List<Reading> readings;

    VelocityAndDirectionSensor(final Coords coords,
                               final double radius,
                               final double angle,
                               final long timeStep)
    {
        this.coords = coords;
        this.radius = radius;
        this.angle = angle;
        this.timeStep = (double) timeStep / 1000;
        this.readings = new ArrayList<>();
    }

    public Coords getCoords()
    {
        return coords;
    }

    public double getRadius()
    {
        return radius;
    }

    public List<Reading> getReadings()
    {
        return readings;
    }

    @Override
    public void updateState(Set<InternalActor> actors)
    {
        readings.clear();
        actors.stream()
              .filter(this::isMeasurementAcceptable)
              .forEach(actor -> readings.add(createReadingFor(actor)));
    }

    protected boolean isMeasurementAcceptable(InternalActor actor)
    {
        return Algebra.isTargetInRadius(coords, radius, actor.getCurrentCoords()) &&
                Algebra.isTargetMovingInParallelDirection(coords, actor, angle);
    }

    protected Reading createReadingFor(InternalActor actor)
    {
        return new Reading(getMovementDirection(actor), getRelativeMovementSpeed(actor));
    }

    protected Direction getMovementDirection(InternalActor actor)
    {
        double previousDistanceFromSensor = Algebra.getAbsoluteDistance(coords, actor.getPreviousCoords());
        double currentDistanceFromSensor = Algebra.getAbsoluteDistance(coords, actor.getCurrentCoords());

        if (previousDistanceFromSensor >= currentDistanceFromSensor) return Direction.APPROACHING;
        else return Direction.LEAVING;
    }

    protected double getRelativeMovementSpeed(InternalActor actor)
    {
        Vector2D previous = new Vector2D(coords.getX() - actor.getPreviousCoords().getX(),
                                         coords.getY() - actor.getPreviousCoords().getY());
        Vector2D current = new Vector2D(coords.getX() - actor.getCurrentCoords().getX(),
                                        coords.getY() - actor.getCurrentCoords().getY());
        double angle = Algebra.getRelativeAngle(previous, current);
        double projectedVectorNorm = current.getNorm() * Math.cos(angle);

        return Math.abs(previous.getNorm() - projectedVectorNorm) / timeStep;
    }
}
