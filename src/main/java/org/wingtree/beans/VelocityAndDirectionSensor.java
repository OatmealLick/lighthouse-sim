package org.wingtree.beans;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.wingtree.util.Algebra;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * One of tracking devices. Holds data in readings {@see VelocityAndDirectionSensorReading}
 */
public class VelocityAndDirectionSensor implements TrackingDevice
{
    protected Coords coords;
    protected double radius;
    private List<VelocityAndDirectionSensorReading> readings;

    public VelocityAndDirectionSensor(final Coords coords, final double radius)
    {
        this.coords = coords;
        this.radius = radius;
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

    public List<VelocityAndDirectionSensorReading> getReadings()
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
                Algebra.isTargetMovingInParallelDirection(coords, actor);
    }

    protected VelocityAndDirectionSensorReading createReadingFor(InternalActor actor)
    {
        return ImmutableVelocityAndDirectionSensorReading.of(getMovementDirection(actor),
                                                             getRelativeMovementSpeed(actor));
    }

    private Direction getMovementDirection(InternalActor actor)
    {
        double previousDistanceFromSensor = Algebra.getAbsoluteDistance(coords, actor.getPreviousCoords());
        double currentDistanceFromSensor = Algebra.getAbsoluteDistance(coords, actor.getCurrentCoords());

        if (previousDistanceFromSensor >= currentDistanceFromSensor) return Direction.APPROACHING;
        else return Direction.LEAVING;
    }

    private double getRelativeMovementSpeed(InternalActor actor)
    {
        Vector2D previous = new Vector2D(coords.getX() - actor.getPreviousCoords().getX(),
                                         coords.getY() - actor.getPreviousCoords().getY());
        Vector2D current = new Vector2D(coords.getX() - actor.getCurrentCoords().getX(),
                                        coords.getY() - actor.getCurrentCoords().getY());
        double angle = Algebra.getRelativeAngle(previous, current);
        double projectedVectorNorm = current.getNorm() * Math.cos(angle);

        // FIXME move fixed interval to ConfigurationParameters
        return Math.abs(previous.getNorm() - projectedVectorNorm) / 0.5;
    }
}
