package org.wingtree.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Camera is one of TrackingDevice. It holds actors which are currently in view. It also stores information whether
 * they are approaching or leaving, as well as their speed.
 */

public class Camera extends VelocityAndDirectionSensor
{
    Camera(final Coords coords,
           final double radius,
           final double angle,
           final long timeStep)
    {
        super(coords, radius, angle, timeStep);
    }

    @Override
    protected Reading createReadingFor(InternalActor actor)
    {
        return new CameraReading(actor.getId(),
                                actor.getType(),
                                getMovementDirection(actor),
                                getRelativeMovementSpeed(actor));
    }
}
