package edu.ug.dcit308.hospital.graph.core;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class HospitalNetworkGraphTest {

    @Test
    void testUndirectedEdgeIsAddedBothWays() {
        HospitalNetworkGraph graph = new HospitalNetworkGraph(3);
        graph.addEdge(0, 1, 12.5);

        assertTrue(graph.hasEdge(0, 1));
        assertTrue(graph.hasEdge(1, 0));
        assertEquals(12.5, graph.getWeight(0, 1));
        assertEquals(12.5, graph.getWeight(1, 0));
        assertEquals(1, graph.degree(0));
        assertEquals(1, graph.degree(1));
    }

    @Test
    void testAddEdgeUpdatesWeightInPlaceInsteadOfDuplicating() {
        HospitalNetworkGraph graph = new HospitalNetworkGraph(2);
        graph.addEdge(0, 1, 10.0);
        graph.addEdge(0, 1, 8.5);

        assertEquals(1, graph.degree(0));
        assertEquals(1, graph.degree(1));
        assertEquals(8.5, graph.getWeight(0, 1));
        assertEquals(8.5, graph.getWeight(1, 0));
        assertEquals(1, graph.getNeighbors(0).length);
    }

    @Test
    void testDirectedEdgeIsOneWayOnly() {
        HospitalNetworkGraph graph = new HospitalNetworkGraph(3, true);
        graph.addEdge(0, 1, 12.5);

        assertTrue(graph.hasEdge(0, 1));
        assertFalse(graph.hasEdge(1, 0));
        assertTrue(graph.isDirected());
    }

    @Test
    void testGetNeighborsReturnsTrimmedArray() {
        HospitalNetworkGraph graph = new HospitalNetworkGraph(4);
        graph.addEdge(0, 1, 5.0);
        graph.addEdge(0, 2, 8.0);

        Edge[] neighbors = graph.getNeighbors(0);

        assertEquals(2, neighbors.length);
        assertEquals(1, neighbors[0].getDestination());
        assertEquals(2, neighbors[1].getDestination());
    }

    @Test
    void testResizesBeyondDefaultAdjacencyCapacity() {
        HospitalNetworkGraph graph = new HospitalNetworkGraph(10);
        for (int i = 1; i < 10; i++) {
            graph.addEdge(0, i, i);
        }

        assertEquals(9, graph.degree(0));
        assertEquals(9, graph.getNeighbors(0).length);
    }

    @Test
    void testSelfLoopAddedOnce() {
        HospitalNetworkGraph graph = new HospitalNetworkGraph(2);
        graph.addEdge(0, 0, 3.0);

        assertEquals(1, graph.degree(0));
    }

    @Test
    void testRemoveEdgeUndirectedRemovesBothDirections() {
        HospitalNetworkGraph graph = new HospitalNetworkGraph(3);
        graph.addEdge(0, 1, 12.5);
        graph.addEdge(0, 2, 4.0);

        graph.removeEdge(0, 1);

        assertFalse(graph.hasEdge(0, 1));
        assertFalse(graph.hasEdge(1, 0));
        assertTrue(graph.hasEdge(0, 2));
        assertEquals(1, graph.degree(0));
        assertEquals(0, graph.degree(1));
    }

    @Test
    void testRemoveEdgeDirectedRemovesOnlyOneDirection() {
        HospitalNetworkGraph graph = new HospitalNetworkGraph(2, true);
        graph.addEdge(0, 1, 5.0);
        graph.addEdge(1, 0, 7.0);

        graph.removeEdge(0, 1);

        assertFalse(graph.hasEdge(0, 1));
        assertTrue(graph.hasEdge(1, 0));
    }

    @Test
    void testRemoveMissingEdgeThrowsException() {
        HospitalNetworkGraph graph = new HospitalNetworkGraph(2);
        assertThrows(NoSuchElementException.class, () -> graph.removeEdge(0, 1));
    }

    @Test
    void testRemoveEdgeVertexOutOfBoundsThrowsException() {
        HospitalNetworkGraph graph = new HospitalNetworkGraph(2);
        assertThrows(IndexOutOfBoundsException.class, () -> graph.removeEdge(0, 5));
    }

    @Test
    void testGetWeightMissingEdgeThrowsException() {
        HospitalNetworkGraph graph = new HospitalNetworkGraph(2);
        assertThrows(NoSuchElementException.class, () -> graph.getWeight(0, 1));
    }

    @Test
    void testNegativeOrInvalidWeightThrowsException() {
        HospitalNetworkGraph graph = new HospitalNetworkGraph(2);
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(0, 1, -1.0));
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(0, 1, Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(0, 1, Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(0, 1, Double.NEGATIVE_INFINITY));
    }

    @Test
    void testVertexOutOfBoundsThrowsException() {
        HospitalNetworkGraph graph = new HospitalNetworkGraph(2);
        assertThrows(IndexOutOfBoundsException.class, () -> graph.addEdge(0, 5, 1.0));
        assertThrows(IndexOutOfBoundsException.class, () -> graph.getNeighbors(-1));
    }

    @Test
    void testConstructorRejectsInvalidVertexCount() {
        assertThrows(IllegalArgumentException.class, () -> new HospitalNetworkGraph(0));
        assertThrows(IllegalArgumentException.class, () -> new HospitalNetworkGraph(-3));
    }
}
