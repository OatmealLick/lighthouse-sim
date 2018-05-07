package org.wingtree.beans;

import java.util.Set;

/**
 * Can be attached to lantern and provide information about simulation.
 */
public interface TrackingDevice
{
    Coords getCoords();

    void updateState(Set<InternalActor> actors);
}
