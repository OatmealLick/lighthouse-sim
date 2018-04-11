package org.wingtree.beans;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;
import org.wingtree.immutables.Immutable;

import java.util.Set;

@Value.Immutable
@Immutable
@JsonDeserialize(as = ImmutableMovementAndDirectionSensor.class)
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
