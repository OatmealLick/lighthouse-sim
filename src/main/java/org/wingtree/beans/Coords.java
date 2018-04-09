package org.wingtree.beans;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;
import org.wingtree.immutables.Immutable;

@Value.Immutable
@Immutable
@JsonDeserialize(as = ImmutableCoords.class)
public interface Coords
{
    double getX();

    double getY();
}
