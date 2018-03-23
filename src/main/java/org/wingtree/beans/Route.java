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
        List<Junction> adjacentJunctions = new ArrayList<>(routeGraph.adjacentNodes(currentJunction));
        Collections.shuffle(adjacentJunctions);
        // TODO can throw exception if graph is not a loop (if some node is a dead end) - forbid it when building?
        return adjacentJunctions.get(0);
    }
}
