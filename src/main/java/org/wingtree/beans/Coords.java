package org.wingtree.beans;

import org.immutables.value.Value;
import org.wingtree.immutables.Immutable;

@Value.Immutable
@Immutable
public interface Coords
{
    double getX();

    double getY();
}
