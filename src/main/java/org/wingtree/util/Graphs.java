package org.wingtree.util;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Graphs
{
    public static <T> boolean isStronglyConnected(Graph<T> graph)
    {
        Set<T> nodes = graph.nodes();

        if (nodes.isEmpty()) return true;
        else {
            T initialNode = new ArrayList<>(nodes).get(0);
            boolean allNodesReachableInBaseGraph = isAllNodesReachable(initialNode, graph);
            boolean allNodesReachableInTransposedGraph = isAllNodesReachable(initialNode, transpose(graph));

            return allNodesReachableInBaseGraph && allNodesReachableInTransposedGraph;
        }
    }

    public static <T> boolean isAllNodesReachable(T initialNode, Graph<T> graph)
    {
        Set<T> visitedNodes = new HashSet<>();
        List<T> nodesToVisit = new ArrayList<>();
        nodesToVisit.add(initialNode);

        while (!nodesToVisit.isEmpty()) {
            T currentNode = nodesToVisit.remove(0);
            visitedNodes.add(currentNode);
            nodesToVisit.addAll(graph.successors(currentNode).stream()
                    .filter(node -> !visitedNodes.contains(node))
                    .collect(Collectors.toList()));
        }

        return graph.nodes().size() == visitedNodes.size();
    }

    public static <T> Graph<T> transpose(Graph<T> graph)
    {
        MutableGraph<T> transposedGraph = GraphBuilder.directed().allowsSelfLoops(true).build();
        graph.nodes().forEach(
                node -> graph.successors(node).forEach(
                        successor -> transposedGraph.putEdge(successor, node)));
        return transposedGraph;
    }
}
