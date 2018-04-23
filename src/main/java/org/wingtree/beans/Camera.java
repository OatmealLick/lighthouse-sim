package org.wingtree.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Camera extends VelocityAndDirectionSensor
{
    private Map<InternalActor, VelocityAndDirectionSensorReading> actorsInView;

    Camera(final Coords coords, final double radius)
    {
        super(coords, radius);
        actorsInView = new HashMap<>();
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
        return Double.compare(camera.radius, radius) == 0 &&
                Objects.equals(coords, camera.coords) &&
                Objects.equals(actorsInView, camera.actorsInView);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(coords, radius, actorsInView);
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
