package edu.ug.dcit308.hospital.graph.mst;

import edu.ug.dcit308.hospital.graph.core.HospitalNetworkGraph;
import edu.ug.dcit308.hospital.graph.mst.PrimMST.MSTEdge;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimMSTTest {

    private HospitalNetworkGraph ghanaNetwork() {
        HospitalNetworkGraph g = new HospitalNetworkGraph(5);
        g.addEdge(0, 1, 5.2);
        g.addEdge(1, 2, 4.8);
        g.addEdge(0, 2, 9.1);
        g.addEdge(2, 3, 14.0);
        g.addEdge(3, 4, 3.1);
        g.addEdge(1, 3, 7.5);
        g.addEdge(0, 3, 12.0);
        return g;
    }

    @Test
    void testMSTHasVMinus1Edges() {
        MSTEdge[] mst = PrimMST.compute(ghanaNetwork(), 0);
        assertEquals(4, mst.length);
    }

    @Test
    void testMSTTotalWeightMatchesKruskal() {
        HospitalNetworkGraph g = ghanaNetwork();
        assertEquals(KruskalMST.totalWeight(g), PrimMST.totalWeight(g, 0), 1e-9);
    }

    @Test
    void testDifferentStartVerticesSameTotalWeight() {
        HospitalNetworkGraph g = ghanaNetwork();
        assertEquals(PrimMST.totalWeight(g, 0), PrimMST.totalWeight(g, 3), 1e-9);
    }

    @Test
    void testMSTFormsSpanningTree() {
        HospitalNetworkGraph g = ghanaNetwork();
        MSTEdge[] mst = PrimMST.compute(g, 0);
        boolean[] seen = new boolean[g.vertexCount()];
        seen[0] = true;
        for (MSTEdge e : mst) {
            seen[e.source]      = true;
            seen[e.destination] = true;
        }
        for (int i = 0; i < g.vertexCount(); i++) {
            assertTrue(seen[i], "Vertex " + i + " not covered by MST");
        }
    }

    @Test
    void testSimpleTriangle() {
        HospitalNetworkGraph g = new HospitalNetworkGraph(3);
        g.addEdge(0, 1, 1.0);
        g.addEdge(1, 2, 2.0);
        g.addEdge(0, 2, 10.0);
        assertEquals(2, PrimMST.compute(g, 0).length);
        assertEquals(3.0, PrimMST.totalWeight(g, 0), 1e-9);
    }

    @Test
    void testTwoVertexGraph() {
        HospitalNetworkGraph g = new HospitalNetworkGraph(2);
        g.addEdge(0, 1, 7.0);
        MSTEdge[] mst = PrimMST.compute(g, 0);
        assertEquals(1, mst.length);
        assertEquals(7.0, mst[0].weight, 1e-9);
    }

    @Test
    void testDisconnectedGraphReturnsReachableComponent() {
        HospitalNetworkGraph g = new HospitalNetworkGraph(4);
        g.addEdge(0, 1, 1.0);
        g.addEdge(2, 3, 2.0);
        MSTEdge[] mst = PrimMST.compute(g, 0);
        assertEquals(1, mst.length);
        assertEquals(1.0, mst[0].weight, 1e-9);
    }

    @Test
    void testNullGraphThrows() {
        assertThrows(IllegalArgumentException.class, () -> PrimMST.compute(null, 0));
    }

    @Test
    void testDirectedGraphThrows() {
        HospitalNetworkGraph directed = new HospitalNetworkGraph(3, true);
        directed.addEdge(0, 1, 1.0);
        assertThrows(IllegalArgumentException.class, () -> PrimMST.compute(directed, 0));
    }

    @Test
    void testOutOfRangeStartVertexThrows() {
        HospitalNetworkGraph g = new HospitalNetworkGraph(3);
        assertThrows(IndexOutOfBoundsException.class, () -> PrimMST.compute(g, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> PrimMST.compute(g, 3));
    }
}
