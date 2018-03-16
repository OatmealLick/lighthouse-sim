package org.wingtree.beans;

import org.immutables.value.Value;
import org.wingtree.immutables.Bean;

@Value.Immutable
@Bean
public interface Lantern
{
    String getId();

    Coords getCoords();
}
