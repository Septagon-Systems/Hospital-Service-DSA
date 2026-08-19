package edu.ug.dcit308.hospital.graph.shortestpath;

import edu.ug.dcit308.hospital.ds.queue.MinHeapPriorityQueue;
import edu.ug.dcit308.hospital.graph.core.Edge;
import edu.ug.dcit308.hospital.graph.core.HospitalNetworkGraph;

/**
 * Dijkstra's single-source shortest-path algorithm for the hospital road
 * network. Uses {@link MinHeapPriorityQueue} as the frontier structure.
 * All edge weights must be non-negative (enforced by
 * {@link HospitalNetworkGraph#addEdge}).
 *
 * <p>Time complexity: O((V + E) log V) with a binary heap.</p>
 */
public final class DijkstraShortestPath {

    private DijkstraShortestPath() {}

    // ------------------------------------------------------------------
    // Internal priority-queue entry
    // ------------------------------------------------------------------

    private static final class NodeDistance implements Comparable<NodeDistance> {
        final int node;
        final double distance;

        NodeDistance(int node, double distance) {
            this.node     = node;
            this.distance = distance;
        }

        public int compareTo(NodeDistance other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    // ------------------------------------------------------------------
    // Result object
    // ------------------------------------------------------------------

    /**
     * Immutable result of a Dijkstra run.  Provides distance query and
     * path reconstruction from the stored predecessor array.
     */
    public static final class PathResult {
        private final double[] distances;
        private final int[]    predecessors;
        private final int      source;
        private final int      destination;

        PathResult(double[] distances, int[] predecessors, int source, int destination) {
            this.distances    = distances;
            this.predecessors = predecessors;
            this.source       = source;
            this.destination  = destination;
        }

        /** Shortest distance to destination; {@code Double.POSITIVE_INFINITY} if unreachable. */
        public double getDistance() {
            return distances[destination];
        }

        /**
         * Returns the shortest path from source to destination as an array of
         * vertex indices, or an empty array if unreachable.
         */
        public int[] reconstructPath() {
            if (distances[destination] == Double.POSITIVE_INFINITY) {
                return new int[0];
            }
            // Count hops
            int count = 0;
            for (int v = destination; v != -1; v = predecessors[v]) {
                count++;
            }
            int[] path = new int[count];
            int idx = count - 1;
            for (int v = destination; v != -1; v = predecessors[v]) {
                path[idx--] = v;
            }
            return path;
        }

        /** Shortest distance to an arbitrary node. */
        public double distanceTo(int node) {
            return distances[node];
        }
    }

    // ------------------------------------------------------------------
    // Algorithm
    // ------------------------------------------------------------------

    /**
     * Computes shortest paths from {@code source} to every reachable node.
     *
     * @param graph  the hospital road network
     * @param source starting vertex index
     * @return a {@link PathResult} containing distances and predecessors
     * @throws IllegalArgumentException if graph is null or source is out of bounds
     */
    public static PathResult findShortestPath(HospitalNetworkGraph graph, int source) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph must not be null");
        }
        int V = graph.vertexCount();
        if (source < 0 || source >= V) {
            throw new IndexOutOfBoundsException("Source vertex out of bounds: " + source);
        }

        double[] dist = new double[V];
        int[]    pred = new int[V];
        boolean[] visited = new boolean[V];

        for (int i = 0; i < V; i++) {
            dist[i] = Double.POSITIVE_INFINITY;
            pred[i] = -1;
        }
        dist[source] = 0.0;

        MinHeapPriorityQueue<NodeDistance> pq = new MinHeapPriorityQueue<>();
        pq.enqueue(new NodeDistance(source, 0.0));

        while (!pq.isEmpty()) {
            NodeDistance current = pq.dequeue();
            int u = current.node;

            if (visited[u]) continue;
            visited[u] = true;

            for (Edge e : graph.getNeighbors(u)) {
                int v = e.getDestination();
                double w = e.getWeight();
                if (!visited[v] && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pred[v] = u;
                    pq.enqueue(new NodeDistance(v, dist[v]));
                }
            }
        }

        return new PathResult(dist, pred, source, -1);
    }

    /**
     * Convenience overload: compute shortest path from source to a specific
     * destination and return a result pre-configured for that target.
     *
     * @param graph       the hospital road network
     * @param source      starting vertex index
     * @param destination target vertex index
     * @return a {@link PathResult} whose {@link PathResult#getDistance()} and
     *         {@link PathResult#reconstructPath()} refer to the destination
     */
    public static PathResult findShortestPath(
            HospitalNetworkGraph graph, int source, int destination) {
        if (destination < 0 || destination >= graph.vertexCount()) {
            throw new IndexOutOfBoundsException("Destination out of bounds: " + destination);
        }
        PathResult result = findShortestPath(graph, source);
        return new PathResult(result.distances, result.predecessors, source, destination);
    }
}
