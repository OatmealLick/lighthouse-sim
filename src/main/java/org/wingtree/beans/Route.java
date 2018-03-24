package org.wingtree.beans;

import com.google.common.graph.ImmutableGraph;

import java.util.*;

public class Route
{
    final ImmutableGraph<Junction> routeGraph;

    Route(final ImmutableGraph<Junction> routeGraph)
    {
        this.routeGraph = routeGraph;
    }

    public Junction getNextRandomizedTarget(Junction currentJunction)
    {
        List<Junction> adjacentJunctions = new ArrayList<>(routeGraph.successors(currentJunction));
        Collections.shuffle(adjacentJunctions);
        return adjacentJunctions.get(0);
    }
}
