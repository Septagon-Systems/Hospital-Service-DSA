package edu.ug.dcit308.hospital.graph.traversal;

import edu.ug.dcit308.hospital.graph.core.HospitalNetworkGraph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class GraphTraversalTest {

    @Test
    void testBfsTraversal() {
        HospitalNetworkGraph graph = new HospitalNetworkGraph(5);

        graph.addEdge(0, 1, 1.0);
        graph.addEdge(0, 2, 1.0);
        graph.addEdge(1, 3, 1.0);
        graph.addEdge(2, 4, 1.0);

        int[] result = GraphTraversal.traverseBFS(graph, 0);

        assertArrayEquals(new int[]{0, 1, 2, 3, 4}, result);
    }

    @Test
    void testDfsTraversal() {
        HospitalNetworkGraph graph = new HospitalNetworkGraph(5);

        graph.addEdge(0, 1, 1.0);
        graph.addEdge(0, 2, 1.0);
        graph.addEdge(1, 3, 1.0);
        graph.addEdge(2, 4, 1.0);

        int[] result = GraphTraversal.traverseDFS(graph, 0);

        assertArrayEquals(new int[]{0, 1, 3, 2, 4}, result);
    }
}
