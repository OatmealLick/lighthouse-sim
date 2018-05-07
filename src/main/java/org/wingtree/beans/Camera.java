package org.wingtree.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Camera is one of TrackingDevice. It holds actors which are currently in view. It also stores information whether
 * they are approaching or leaving, as well as their speed.
 */

public class Camera extends VelocityAndDirectionSensor
{
    private Map<InternalActor, VelocityAndDirectionSensorReading> actorsInView;

    Camera(final Coords coords,
           final double radius,
           final double angle,
           final long timeStep)
    {
        super(coords, radius, angle, timeStep);
        actorsInView = new HashMap<>();
    }

    public Map<InternalActor, VelocityAndDirectionSensorReading> getActorsInView()
    {
        return actorsInView;
    }

    @Override
    public void updateState(final Set<InternalActor> actors)
    {
        actorsInView.clear();
        actors.stream()
              .filter(this::isMeasurementAcceptable)
              .forEach(actor -> actorsInView.put(actor, createReadingFor(actor)));

    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Camera camera = (Camera) o;
        return Objects.equals(actorsInView, camera.actorsInView);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(actorsInView);
    }

    @Override
    public String toString()
    {
        return "Camera{" +
                "coords=" + coords +
                ", radius=" + radius +
                ", actorsInView=" + actorsInView +
                '}';
    }
}
