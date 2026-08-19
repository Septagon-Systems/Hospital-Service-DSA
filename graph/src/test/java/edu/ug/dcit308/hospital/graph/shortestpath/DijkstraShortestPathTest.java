package edu.ug.dcit308.hospital.graph.shortestpath;

import edu.ug.dcit308.hospital.graph.core.HospitalNetworkGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DijkstraShortestPathTest {

    private HospitalNetworkGraph graph;

    // ------------------------------------------------------------------
    // 4-node graph used by most tests:
    //
    //   0 --5-- 1
    //   |       |
    //   2       3
    //   |       |
    //   4 --1-- 2
    //
    // Edges: 0-1(5), 0-4(2), 4-2(1), 1-3(3)
    // ------------------------------------------------------------------

    @BeforeEach
    void setUp() {
        graph = new HospitalNetworkGraph(5);
        graph.addEdge(0, 1, 5.0);
        graph.addEdge(0, 4, 2.0);
        graph.addEdge(4, 2, 1.0);
        graph.addEdge(1, 3, 3.0);
    }

    // ------------------------------------------------------------------
    // Direct route
    // ------------------------------------------------------------------

    @Test
    void testDirectRoute() {
        // 1 -> 3: single hop, weight 3
        DijkstraShortestPath.PathResult result =
                DijkstraShortestPath.findShortestPath(graph, 1, 3);

        assertEquals(3.0, result.getDistance(), 1e-9);
        int[] path = result.reconstructPath();
        assertArrayEquals(new int[]{1, 3}, path);
    }

    @Test
    void testDirectRouteSameNode() {
        DijkstraShortestPath.PathResult result =
                DijkstraShortestPath.findShortestPath(graph, 0, 0);

        assertEquals(0.0, result.getDistance(), 1e-9);
        assertArrayEquals(new int[]{0}, result.reconstructPath());
    }

    // ------------------------------------------------------------------
    // Multi-hop route
    // ------------------------------------------------------------------

    @Test
    void testMultiHopShorterThanDirect() {
        // 0 -> 2: direct path is 0-4-2 = 3.0, no direct edge exists anyway.
        DijkstraShortestPath.PathResult result =
                DijkstraShortestPath.findShortestPath(graph, 0, 2);

        assertEquals(3.0, result.getDistance(), 1e-9);
        int[] path = result.reconstructPath();
        assertArrayEquals(new int[]{0, 4, 2}, path);
    }

    @Test
    void testMultiHopLongestPath() {
        // 0 -> 3: 0-1-3 = 8.0, vs 0-4-2-? (2 is not connected to 3)
        DijkstraShortestPath.PathResult result =
                DijkstraShortestPath.findShortestPath(graph, 0, 3);

        assertEquals(8.0, result.getDistance(), 1e-9);
        assertArrayEquals(new int[]{0, 1, 3}, result.reconstructPath());
    }

    // ------------------------------------------------------------------
    // Unreachable destination
    // ------------------------------------------------------------------

    @Test
    void testUnreachableDestination() {
        HospitalNetworkGraph disconnected = new HospitalNetworkGraph(4);
        disconnected.addEdge(0, 1, 1.0);
        disconnected.addEdge(2, 3, 1.0);

        DijkstraShortestPath.PathResult result =
                DijkstraShortestPath.findShortestPath(disconnected, 0, 3);

        assertEquals(Double.POSITIVE_INFINITY, result.getDistance(), 1e-9);
        assertArrayEquals(new int[0], result.reconstructPath());
    }

    // ------------------------------------------------------------------
    // Source vertex has no outgoing edges
    // ------------------------------------------------------------------

    @Test
    void testSourceIsolated() {
        HospitalNetworkGraph g = new HospitalNetworkGraph(3);
        g.addEdge(1, 2, 4.0);

        DijkstraShortestPath.PathResult result =
                DijkstraShortestPath.findShortestPath(g, 0, 2);

        assertEquals(Double.POSITIVE_INFINITY, result.getDistance(), 1e-9);
    }

    // ------------------------------------------------------------------
    // Full distances from source
    // ------------------------------------------------------------------

    @Test
    void testDistancesFromSource() {
        DijkstraShortestPath.PathResult result =
                DijkstraShortestPath.findShortestPath(graph, 0);

        assertEquals(0.0, result.distanceTo(0), 1e-9);
        assertEquals(5.0, result.distanceTo(1), 1e-9);
        assertEquals(3.0, result.distanceTo(2), 1e-9);
        assertEquals(8.0, result.distanceTo(3), 1e-9);
        assertEquals(2.0, result.distanceTo(4), 1e-9);
    }

    // ------------------------------------------------------------------
    // Invalid inputs
    // ------------------------------------------------------------------

    @Test
    void testNullGraphThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> DijkstraShortestPath.findShortestPath(null, 0));
    }

    @Test
    void testSourceOutOfBoundsThrows() {
        assertThrows(IndexOutOfBoundsException.class,
                () -> DijkstraShortestPath.findShortestPath(graph, 5));
    }

    @Test
    void testDestinationOutOfBoundsThrows() {
        assertThrows(IndexOutOfBoundsException.class,
                () -> DijkstraShortestPath.findShortestPath(graph, 0, 10));
    }
}
