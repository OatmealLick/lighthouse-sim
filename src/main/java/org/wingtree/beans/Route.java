package org.wingtree.beans;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.ImmutableGraph;

import java.util.*;

public class Route
{
    final ImmutableGraph<Junction> routeGraph;

    Route(final ImmutableGraph<Junction> routeGraph)
    {
        this.routeGraph = routeGraph;
    }

    public Set<Junction> getJunctions()
    {
        return routeGraph.nodes();
    }

    public Junction getJunctionOfId(String id)
    {
        return routeGraph.nodes().stream().filter(junction -> junction.getId().equals(id)).findFirst()
                .orElse(ImmutableJunction.of("not found", ImmutableCoords.of(0, 0), ImmutableSet.of()));
    }

    public Junction getNextRandomizedTarget(Junction currentJunction)
    {
        List<Junction> adjacentJunctions = new ArrayList<>(routeGraph.successors(currentJunction));
        Collections.shuffle(adjacentJunctions);
        return adjacentJunctions.get(0);
    }
}
