package edu.ug.dcit308.hospital.graph.mst;

import edu.ug.dcit308.hospital.algo.sort.MergeSort;
import edu.ug.dcit308.hospital.ds.unionfind.DisjointSet;
import edu.ug.dcit308.hospital.graph.core.Edge;
import edu.ug.dcit308.hospital.graph.core.HospitalNetworkGraph;

public class KruskalMST {

    private KruskalMST() {}

    public static final class WeightedEdge {
        public final int source;
        public final int destination;
        public final double weight;

        WeightedEdge(int source, int destination, double weight) {
            this.source      = source;
            this.destination = destination;
            this.weight      = weight;
        }
    }

    public static WeightedEdge[] compute(HospitalNetworkGraph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph must not be null");
        }
        if (graph.isDirected()) {
            throw new IllegalArgumentException("Kruskal's MST is only defined for undirected graphs");
        }

        int V = graph.vertexCount();

        // Step 1: collect all edges
        WeightedEdge[] allEdges = collectEdges(graph);
        int edgeCount = allEdges.length;

        // Step 2: sort by weight ascending using MergeSort (O(n log n))
        MergeSort.sort(allEdges, (a, b) -> Double.compare(a.weight, b.weight));

        // Step 3: greedily pick edges using Union-Find
        DisjointSet ds  = new DisjointSet(V);
        WeightedEdge[] mst = new WeightedEdge[V - 1];
        int mstSize = 0;

        for (int i = 0; i < edgeCount && mstSize < V - 1; i++) {
            WeightedEdge e = allEdges[i];
            if (ds.union(e.source, e.destination)) {
                mst[mstSize++] = e;
            }
        }

        // trim if graph is disconnected
        if (mstSize < V - 1) {
            WeightedEdge[] trimmed = new WeightedEdge[mstSize];
            for (int i = 0; i < mstSize; i++) {
                trimmed[i] = mst[i];
            }
            return trimmed;
        }

        return mst;
    }

    public static double totalWeight(HospitalNetworkGraph graph) {
        WeightedEdge[] mst = compute(graph);
        double total = 0.0;
        for (WeightedEdge e : mst) {
            total += e.weight;
        }
        return total;
    }

    // collect each undirected edge once (u < v)
    private static WeightedEdge[] collectEdges(HospitalNetworkGraph graph) {
        int V = graph.vertexCount();
        int count = 0;
        for (int u = 0; u < V; u++) {
            for (Edge e : graph.getNeighbors(u)) {
                if (u < e.getDestination()) {
                    count++;
                }
            }
        }

        WeightedEdge[] edges = new WeightedEdge[count];
        int idx = 0;
        for (int u = 0; u < V; u++) {
            for (Edge e : graph.getNeighbors(u)) {
                if (u < e.getDestination()) {
                    edges[idx++] = new WeightedEdge(u, e.getDestination(), e.getWeight());
                }
            }
        }
        return edges;
    }

}
