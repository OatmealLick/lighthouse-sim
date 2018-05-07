package org.wingtree.beans;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.wingtree.util.Algebra;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class VelocityAndDirectionSensor implements TrackingDevice
{
    protected Coords coords;
    protected double radius;
    protected double angle;
    protected double timeStep;
    private List<VelocityAndDirectionSensorReading> readings;

    VelocityAndDirectionSensor(final Coords coords,
                               final double radius,
                               final double angle,
                               final long timeStep)
    {
        this.coords = coords;
        this.radius = radius;
        this.angle = angle;
        this.timeStep = timeStep / 1000;
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

        return Math.abs(previous.getNorm() - projectedVectorNorm) / timeStep;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VelocityAndDirectionSensor that = (VelocityAndDirectionSensor) o;
        return Double.compare(that.radius, radius) == 0 &&
                Double.compare(that.angle, angle) == 0 &&
                Double.compare(that.timeStep, timeStep) == 0 &&
                Objects.equals(coords, that.coords) &&
                Objects.equals(readings, that.readings);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(coords, radius, angle, timeStep, readings);
    }

    @Override
    public String toString()
    {
        return "VelocityAndDirectionSensor{" +
                "coords=" + coords +
                ", radius=" + radius +
                ", angle=" + angle +
                ", timeStep=" + timeStep +
                ", readings=" + readings +
                '}';
    }
}
