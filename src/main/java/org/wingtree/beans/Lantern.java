package org.wingtree.beans;

import org.immutables.value.Value;
import org.wingtree.immutables.Bean;

import java.util.Set;

@Value.Immutable
@Bean
public interface Lantern
{
    String getId();

    Coords getCoords();

    Set<TrackingDevice> getTrackingDevices();
}
