package org.wingtree.beans;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;
import org.wingtree.immutables.Immutable;

@Immutable
@Value.Immutable
@JsonDeserialize(as = ImmutableRouteSegment.class)
public interface RouteSegment {

    Junction getFrom();

    Junction getTo();
}
