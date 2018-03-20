package org.wingtree.beans;

import org.immutables.value.Value;
import org.wingtree.immutables.Bean;

import java.util.Set;

@Value.Immutable
@Bean
//TODO update movement and direction sensors
public interface MovementAndDirectionSensor extends TrackingDevice
{
    String getLanternId();

    boolean isSensingMovement();

    double getRadius();

    Direction getDirection();

    @Override
    default void updateState(Coords trackingDeviceCoords, Set<InternalActor> actors) {}
}
