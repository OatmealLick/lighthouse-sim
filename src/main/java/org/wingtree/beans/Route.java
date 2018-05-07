package org.wingtree.beans;

import com.google.common.graph.ImmutableGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * This is the class holding graph of lanterns.
 */
public class Route
{
    final ImmutableGraph<Junction> routeGraph;

    Route(final ImmutableGraph<Junction> routeGraph)
    {
        this.routeGraph = routeGraph;
    }

    public Set<Junction> getJunctions()
    {
        return routeGraph.nodes();
    }

    public Junction getJunctionOfId(final String id)
    {
        return routeGraph.nodes()
                .stream()
                .filter(junction -> junction.getId().equals(id))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public TrackingDevice getCameraOfId(final String id)
    {
        return getJunctionOfId(id).getTrackingDevices()
                .stream()
                .filter(trackingDevice -> trackingDevice instanceof Camera)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public TrackingDevice getMovementSensorOfId(final String id)
    {
        return getJunctionOfId(id).getTrackingDevices()
                .stream()
                .filter(trackingDevice -> trackingDevice instanceof MovementSensor)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public TrackingDevice getVelocityAndDirectionSensorOfId(final String id)
    {
        return getJunctionOfId(id).getTrackingDevices()
                .stream()
                .filter(trackingDevice ->
                        trackingDevice instanceof VelocityAndDirectionSensor &&
                        !(trackingDevice instanceof Camera))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public Junction getNextRandomizedTarget(Junction currentJunction)
    {
        List<Junction> adjacentJunctions = new ArrayList<>(routeGraph.successors(currentJunction));
        Collections.shuffle(adjacentJunctions);
        return adjacentJunctions.get(0);
    }
}
