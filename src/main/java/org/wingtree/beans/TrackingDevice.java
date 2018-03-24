package org.wingtree.beans;

import java.util.Set;

public interface TrackingDevice
{
    void updateState(Coords trackingDeviceCoords, Set<InternalActor> actors);
}
