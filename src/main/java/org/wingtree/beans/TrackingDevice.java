package org.wingtree.beans;

import java.util.Set;

public interface TrackingDevice
{
    Coords getCoords();

    void updateState(Set<InternalActor> actors);
}
