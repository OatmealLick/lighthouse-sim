package org.wingtree.beans;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;
import org.wingtree.immutables.Immutable;

import java.util.Set;

/**
 * This is class representing lantern. It has an id, position(coordinates) and set of tracking devices.
 */
@Value.Immutable
@Immutable
@JsonDeserialize(as = ImmutableJunction.class)
public interface Junction
{
    String getId();

    Coords getCoords();

    @Value.Auxiliary
    Set<TrackingDevice> getTrackingDevices();
}
