package org.wingtree.beans;

import org.immutables.value.Value;
import org.wingtree.immutables.Bean;

import java.util.Optional;

@Value.Immutable
@Bean
public interface InternalActor
{
    ActorType getType();

    float getVelocity();

    Optional<String> getId();

    Coords getCurrentCoords();

    Coords getTargetCoords();
}
