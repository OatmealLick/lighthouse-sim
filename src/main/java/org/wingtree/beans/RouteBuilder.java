package org.wingtree.beans;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;
import org.wingtree.util.Graphs;

import java.util.Collection;
import java.util.stream.Stream;

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

    public RouteBuilder addRouteSegment(final Junction from, final Junction to)
    {
        return addRouteSegment(ImmutableRouteSegment.of(from, to));
    }

    public RouteBuilder addRouteSegment(final RouteSegment routeSegment)
    {
        routeGraph.putEdge(routeSegment.getFrom(), routeSegment.getTo());
        return this;
    }

    public RouteBuilder addRouteSegments(final Collection<RouteSegment> routeSegments)
    {
        routeSegments.forEach(routeSegment -> routeGraph.putEdge(routeSegment.getFrom(), routeSegment.getTo()));
        return this;
    }

    public Route build()
    {
        checkState(Graphs.isStronglyConnected(routeGraph), "The route graph is not strongly connected");
        return new Route(ImmutableGraph.copyOf(routeGraph));
    }
}
