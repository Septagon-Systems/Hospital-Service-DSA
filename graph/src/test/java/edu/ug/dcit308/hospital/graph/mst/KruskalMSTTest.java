package edu.ug.dcit308.hospital.graph.mst;

import edu.ug.dcit308.hospital.graph.core.HospitalNetworkGraph;
import edu.ug.dcit308.hospital.graph.mst.KruskalMST.WeightedEdge;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KruskalMSTTest {

    // Ghana hospital nodes: 0=Korle Bu, 1=Ridge, 2=37 Military, 3=KATH, 4=Manhyia
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
        WeightedEdge[] mst = KruskalMST.compute(ghanaNetwork());
        assertEquals(4, mst.length);
    }

    @Test
    void testMSTTotalWeightGhanaNetwork() {
        // MST: (3,4)=3.1, (1,2)=4.8, (0,1)=5.2, (1,3)=7.5 → total=20.6
        assertEquals(20.6, KruskalMST.totalWeight(ghanaNetwork()), 1e-9);
    }

    @Test
    void testMSTEdgesAreSortedByWeight() {
        WeightedEdge[] mst = KruskalMST.compute(ghanaNetwork());
        for (int i = 1; i < mst.length; i++) {
            assertTrue(mst[i - 1].weight <= mst[i].weight);
        }
    }

    @Test
    void testMSTFormsSpanningTree() {
        HospitalNetworkGraph g = ghanaNetwork();
        WeightedEdge[] mst = KruskalMST.compute(g);
        boolean[] seen = new boolean[g.vertexCount()];
        for (WeightedEdge e : mst) {
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
        assertEquals(2, KruskalMST.compute(g).length);
        assertEquals(3.0, KruskalMST.totalWeight(g), 1e-9);
    }

    @Test
    void testTwoVertexGraph() {
        HospitalNetworkGraph g = new HospitalNetworkGraph(2);
        g.addEdge(0, 1, 7.0);
        WeightedEdge[] mst = KruskalMST.compute(g);
        assertEquals(1, mst.length);
        assertEquals(7.0, mst[0].weight, 1e-9);
    }

    @Test
    void testDisconnectedGraphReturnsForest() {
        HospitalNetworkGraph g = new HospitalNetworkGraph(4);
        g.addEdge(0, 1, 1.0);
        g.addEdge(2, 3, 2.0);
        WeightedEdge[] mst = KruskalMST.compute(g);
        assertEquals(2, mst.length);
        assertEquals(3.0, KruskalMST.totalWeight(g), 1e-9);
    }

    @Test
    void testDuplicateWeightsStillProducesValidMST() {
        HospitalNetworkGraph g = new HospitalNetworkGraph(4);
        g.addEdge(0, 1, 5.0);
        g.addEdge(1, 2, 5.0);
        g.addEdge(2, 3, 5.0);
        g.addEdge(0, 3, 5.0);
        WeightedEdge[] mst = KruskalMST.compute(g);
        assertEquals(3, mst.length);
        assertEquals(15.0, KruskalMST.totalWeight(g), 1e-9);
    }

    @Test
    void testNullGraphThrows() {
        assertThrows(IllegalArgumentException.class, () -> KruskalMST.compute(null));
    }

    @Test
    void testDirectedGraphThrows() {
        HospitalNetworkGraph directed = new HospitalNetworkGraph(3, true);
        directed.addEdge(0, 1, 1.0);
        assertThrows(IllegalArgumentException.class, () -> KruskalMST.compute(directed));
    }
}
