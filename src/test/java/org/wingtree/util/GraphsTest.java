package org.wingtree.util;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import org.junit.Test;
import org.wingtree.beans.ImmutableCoords;
import org.wingtree.beans.ImmutableJunction;
import org.wingtree.beans.Junction;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class GraphsTest
{
    @Test
    void transposeGraph()
    {
        // given
        Graph<Junction> graph = createSampleGraph();
        Graph<Junction> transposedModel = createTransposedSampleGraph();

        // when
        Graph<Junction> transposed = Graphs.transpose(graph);

        // then
        assertThat(transposed).isEqualTo(transposedModel);
    }

    @Test
    void allNodesAreReachableForSampleGraph()
    {
        // given
        Graph<Junction> graph = createSampleGraph();
        Junction initialNode = (Junction) graph.nodes().toArray()[0];

        // when
        boolean isAllNodesReachable = Graphs.isAllNodesReachable(initialNode, graph);

        // then
        assertThat(isAllNodesReachable).isTrue();
    }

    @Test
    void notAllNodesAreReachableForUnacceptableRoadGraph()
    {
        // given
        Graph<Junction> graph = createUnacceptableRoadGraph();
        Junction initialNode = (Junction) graph.nodes().toArray()[1];

        // when
        boolean isAllNodesReachable = Graphs.isAllNodesReachable(initialNode, graph);

        // then
        assertThat(isAllNodesReachable).isFalse();
    }

    @Test
    void sampleGraphIsStronglyConnected()
    {
        // given
        Graph<Junction> graph = createSampleGraph();

        // when
        boolean isStronglyConnected = Graphs.isStronglyConnected(graph);

        // then
        assertThat(isStronglyConnected).isTrue();
    }

    @Test
    void unacceptableRoadGraphIsNotStronglyConnected()
    {
        // given
        Graph<Junction> graph = createUnacceptableRoadGraph();

        // when
        boolean isStronglyConnected = Graphs.isStronglyConnected(graph);

        // then
        assertThat(isStronglyConnected).isFalse();
    }

    private Graph<Junction> createUnacceptableRoadGraph()
    {
        MutableGraph<Junction> graph = GraphBuilder.directed().allowsSelfLoops(true).build();
        List<Junction> nodes = createNodesForSampleGraph();

        graph.putEdge(nodes.get(0), nodes.get(1));
        graph.putEdge(nodes.get(1), nodes.get(2));
        graph.putEdge(nodes.get(2), nodes.get(3));

        return graph;
    }

    private Graph<Junction> createSampleGraph()
    {
        MutableGraph<Junction> graph = GraphBuilder.directed().allowsSelfLoops(true).build();
        List<Junction> nodes = createNodesForSampleGraph();

        graph.putEdge(nodes.get(0), nodes.get(1));
        graph.putEdge(nodes.get(1), nodes.get(2));
        graph.putEdge(nodes.get(1), nodes.get(3));
        graph.putEdge(nodes.get(2), nodes.get(3));
        graph.putEdge(nodes.get(3), nodes.get(0));

        return graph;
    }

    private Graph<Junction> createTransposedSampleGraph()
    {
        MutableGraph<Junction> graph = GraphBuilder.directed().allowsSelfLoops(true).build();
        List<Junction> nodes = createNodesForSampleGraph();

        graph.putEdge(nodes.get(1), nodes.get(0));
        graph.putEdge(nodes.get(2), nodes.get(1));
        graph.putEdge(nodes.get(3), nodes.get(1));
        graph.putEdge(nodes.get(3), nodes.get(2));
        graph.putEdge(nodes.get(0), nodes.get(3));

        return graph;
    }

    private List<Junction> createNodesForSampleGraph()
    {
        List<Junction> nodes = new ArrayList<>();
        nodes.add(ImmutableJunction.builder().withCoords(ImmutableCoords.of(0, 0)).withId("0").build());
        nodes.add(ImmutableJunction.builder().withCoords(ImmutableCoords.of(4, 2)).withId("1").build());
        nodes.add(ImmutableJunction.builder().withCoords(ImmutableCoords.of(6, 6)).withId("2").build());
        nodes.add(ImmutableJunction.builder().withCoords(ImmutableCoords.of(2, 5)).withId("3").build());
        return nodes;
    }
}