package org.wingtree.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Camera extends VelocityAndDirectionSensor
{
    private List<VelocityAndDirectionSensorReading> actorsInView;

    Camera(final Coords coords, final double radius)
    {
        super(coords, radius);
        actorsInView = new ArrayList<>();
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

    public List<VelocityAndDirectionSensorReading> getActorsInView()
    {
        return actorsInView;
    }

    @Override
    public void updateState(final Set<InternalActor> actors)
    {
        actorsInView.clear();
        actors.stream()
                .filter(this::isMeasurementAcceptable)
                .map(this::createReadingFor)
                .forEach(actorsInView::add);

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
