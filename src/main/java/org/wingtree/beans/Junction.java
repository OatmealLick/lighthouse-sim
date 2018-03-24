package org.wingtree.beans;

import org.immutables.value.Value;
import org.wingtree.immutables.Immutable;

import java.util.Set;

@Value.Immutable
@Immutable
public interface Junction
{
    String getId();

    Coords getCoords();

    Set<TrackingDevice> getTrackingDevices();
}
