package org.wingtree.beans;

import org.immutables.value.Value;
import org.wingtree.immutables.Bean;

import java.util.Set;

@Value.Immutable
@Bean
public interface Camera
{
    String getLanternId();

    Set<InternalActor> getInternalActors();

    float getRadius();
}
