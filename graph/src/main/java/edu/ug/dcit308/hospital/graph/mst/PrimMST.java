package edu.ug.dcit308.hospital.graph.mst;

import edu.ug.dcit308.hospital.graph.core.Edge;
import edu.ug.dcit308.hospital.graph.core.HospitalNetworkGraph;

public class PrimMST {

    private PrimMST() {}

    public static final class MSTEdge {
        public final int source;
        public final int destination;
        public final double weight;

        MSTEdge(int source, int destination, double weight) {
            this.source      = source;
            this.destination = destination;
            this.weight      = weight;
        }
    }

    public static MSTEdge[] compute(HospitalNetworkGraph graph, int startVertex) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph must not be null");
        }
        if (graph.isDirected()) {
            throw new IllegalArgumentException("Prim's MST is only defined for undirected graphs");
        }

        int V = graph.vertexCount();
        if (startVertex < 0 || startVertex >= V) {
            throw new IndexOutOfBoundsException(
                    "startVertex " + startVertex + " out of bounds for V=" + V);
        }

        double[]  key    = new double[V];
        int[]     parent = new int[V];
        boolean[] inTree = new boolean[V];

        for (int i = 0; i < V; i++) {
            key[i]    = Double.MAX_VALUE;
            parent[i] = -1;
        }
        key[startVertex] = 0.0;

        MSTEdge[] mst    = new MSTEdge[V - 1];
        int mstSize = 0;

        for (int iter = 0; iter < V; iter++) {

            // pick non-tree vertex with smallest key
            int u = -1;
            for (int v = 0; v < V; v++) {
                if (!inTree[v] && (u == -1 || key[v] < key[u])) {
                    u = v;
                }
            }

            if (u == -1 || key[u] == Double.MAX_VALUE) {
                break; // disconnected graph
            }

            inTree[u] = true;

            if (parent[u] != -1) {
                mst[mstSize++] = new MSTEdge(parent[u], u, key[u]);
            }

            // relax neighbours
            for (Edge e : graph.getNeighbors(u)) {
                int v = e.getDestination();
                if (!inTree[v] && e.getWeight() < key[v]) {
                    key[v]    = e.getWeight();
                    parent[v] = u;
                }
            }
        }

        // trim for disconnected graphs
        if (mstSize < mst.length) {
            MSTEdge[] trimmed = new MSTEdge[mstSize];
            for (int i = 0; i < mstSize; i++) {
                trimmed[i] = mst[i];
            }
            return trimmed;
        }

        return mst;
    }

    public static double totalWeight(HospitalNetworkGraph graph, int startVertex) {
        MSTEdge[] mst = compute(graph, startVertex);
        double total = 0.0;
        for (MSTEdge e : mst) {
            total += e.weight;
        }
        return total;
    }
}
