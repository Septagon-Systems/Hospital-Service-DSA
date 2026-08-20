package edu.ug.dcit308.hospital.graph.traversal;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import edu.ug.dcit308.hospital.graph.core.HospitalNetworkGraph;

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
    @Test
void testBfsDisconnectedGraphReturnsOnlyReachableVertices() {
    HospitalNetworkGraph graph = new HospitalNetworkGraph(5);

    graph.addEdge(0, 1, 1.0);
    graph.addEdge(1, 2, 1.0);

    // Vertices 3 and 4 are disconnected.
    int[] result = GraphTraversal.traverseBFS(graph, 0);

    assertArrayEquals(new int[]{0, 1, 2}, result);
}

@Test
void testDfsDisconnectedGraphReturnsOnlyReachableVertices() {
    HospitalNetworkGraph graph = new HospitalNetworkGraph(5);

    graph.addEdge(0, 1, 1.0);
    graph.addEdge(1, 2, 1.0);

    int[] result = GraphTraversal.traverseDFS(graph, 0);

    assertArrayEquals(new int[]{0, 1, 2}, result);
}

@Test
void testBfsHandlesCycleWithoutRepeatingVertices() {
    HospitalNetworkGraph graph = new HospitalNetworkGraph(3);

    graph.addEdge(0, 1, 1.0);
    graph.addEdge(1, 2, 1.0);
    graph.addEdge(2, 0, 1.0);

    int[] result = GraphTraversal.traverseBFS(graph, 0);

    assertArrayEquals(new int[]{0, 1, 2}, result);
}

@Test
void testDfsHandlesCycleWithoutRepeatingVertices() {
    HospitalNetworkGraph graph = new HospitalNetworkGraph(3);

    graph.addEdge(0, 1, 1.0);
    graph.addEdge(1, 2, 1.0);
    graph.addEdge(2, 0, 1.0);

    int[] result = GraphTraversal.traverseDFS(graph, 0);

    assertArrayEquals(new int[]{0, 1, 2}, result);
}

@Test
void testBfsSingleVertexGraph() {
    HospitalNetworkGraph graph = new HospitalNetworkGraph(1);

    assertArrayEquals(
            new int[]{0},
            GraphTraversal.traverseBFS(graph, 0));
}

@Test
void testDfsSingleVertexGraph() {
    HospitalNetworkGraph graph = new HospitalNetworkGraph(1);

    assertArrayEquals(
            new int[]{0},
            GraphTraversal.traverseDFS(graph, 0));
}

@Test
void testBfsRejectsNullGraph() {
    assertThrows(
            IllegalArgumentException.class,
            () -> GraphTraversal.traverseBFS(null, 0));
}

@Test
void testDfsRejectsNullGraph() {
    assertThrows(
            IllegalArgumentException.class,
            () -> GraphTraversal.traverseDFS(null, 0));
}

@Test
void testBfsRejectsNegativeStartNode() {
    HospitalNetworkGraph graph = new HospitalNetworkGraph(3);

    assertThrows(
            IndexOutOfBoundsException.class,
            () -> GraphTraversal.traverseBFS(graph, -1));
}

@Test
void testDfsRejectsStartNodeEqualToVertexCount() {
    HospitalNetworkGraph graph = new HospitalNetworkGraph(3);

    assertThrows(
            IndexOutOfBoundsException.class,
            () -> GraphTraversal.traverseDFS(graph, 3));
}

@Test
void testGhanaHospitalEmergencyReachability() {
    // 0 = Korle Bu Teaching Hospital
    // 1 = 37 Military Hospital
    // 2 = Ridge Hospital
    HospitalNetworkGraph graph = new HospitalNetworkGraph(3);

    graph.addEdge(0, 1, 1.0);
    graph.addEdge(1, 2, 1.0);

    int[] result = GraphTraversal.traverseBFS(graph, 0);

    assertArrayEquals(new int[]{0, 1, 2}, result);
}
}
