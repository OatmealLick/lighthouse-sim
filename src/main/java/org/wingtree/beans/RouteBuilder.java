package org.wingtree.beans;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;
import org.wingtree.util.Graphs;

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
        checkState(Graphs.isStronglyConnected(routeGraph), "The route graph is not strongly connected");
        return new Route(ImmutableGraph.copyOf(routeGraph));
    }
}
