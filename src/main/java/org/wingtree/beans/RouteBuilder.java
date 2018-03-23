package org.wingtree.beans;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkState;

public class RouteBuilder
{
    private MutableGraph<Junction> routeGraph;

    private RouteBuilder()
    {
        routeGraph = GraphBuilder.directed().allowsSelfLoops(true).build();
    }

    public static RouteBuilder builder()
    {
        return new RouteBuilder();
    }

    public RouteBuilder withRoad(Junction from, Junction to)
    {
        routeGraph.putEdge(from, to);
        return this;
    }

    public Route build()
    {
        checkState(checkConnectivity(), "The route graph is not connected - some junctions are unreachable");
        return new Route(ImmutableGraph.copyOf(routeGraph));
    }

    private boolean checkConnectivity()
    {
        Set<Junction> nodes = routeGraph.nodes();
        Junction currentNode = new ArrayList<>(nodes).get(0);
        Set<Junction> visitedNodes = new HashSet<>();
        List<Junction> nodesToVisit = new ArrayList<>();

        while (nodesToVisit.size() > 0) {
            visitedNodes.add(currentNode);
            nodesToVisit.addAll(routeGraph.adjacentNodes(currentNode));
        }

        return visitedNodes.size() == nodes.size();
    }
}
