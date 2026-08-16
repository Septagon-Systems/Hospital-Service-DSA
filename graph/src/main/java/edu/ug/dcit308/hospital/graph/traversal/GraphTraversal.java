package edu.ug.dcit308.hospital.graph.traversal;

import edu.ug.dcit308.hospital.graph.core.Edge;
import edu.ug.dcit308.hospital.graph.core.HospitalNetworkGraph;

/**
 * Provides Breadth-First Search (BFS) and Depth-First Search (DFS)
 * traversal for the hospital network graph.
 */
public final class GraphTraversal {

    private GraphTraversal() {
    }

    /**
     * Traverses the graph using Breadth-First Search.
     *
     * @param graph the hospital network graph
     * @param startNode the vertex where traversal begins
     * @return vertices in BFS visitation order
     */
    public static int[] traverseBFS(
            HospitalNetworkGraph graph,
            int startNode) {

        validate(graph, startNode);

        int vertexCount = graph.vertexCount();
        boolean[] visited = new boolean[vertexCount];

        int[] queue = new int[vertexCount];
        int front = 0;
        int rear = 0;

        int[] traversal = new int[vertexCount];
        int count = 0;

        visited[startNode] = true;
        queue[rear++] = startNode;

        while (front < rear) {
            int current = queue[front++];
            traversal[count++] = current;

            Edge[] neighbors = graph.getNeighbors(current);

            for (Edge edge : neighbors) {
                int neighbor = edge.getDestination();

                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue[rear++] = neighbor;
                }
            }
        }

        return trim(traversal, count);
    }

    /**
     * Traverses the graph using Depth-First Search.
     *
     * @param graph the hospital network graph
     * @param startNode the vertex where traversal begins
     * @return vertices in DFS visitation order
     */
    public static int[] traverseDFS(
            HospitalNetworkGraph graph,
            int startNode) {

        validate(graph, startNode);

        boolean[] visited = new boolean[graph.vertexCount()];
        int[] traversal = new int[graph.vertexCount()];
        int[] count = {0};

        dfs(graph, startNode, visited, traversal, count);

        return trim(traversal, count[0]);
    }

    private static void dfs(
            HospitalNetworkGraph graph,
            int vertex,
            boolean[] visited,
            int[] traversal,
            int[] count) {

        visited[vertex] = true;
        traversal[count[0]++] = vertex;

        Edge[] neighbors = graph.getNeighbors(vertex);

        for (Edge edge : neighbors) {
            int neighbor = edge.getDestination();

            if (!visited[neighbor]) {
                dfs(graph, neighbor, visited, traversal, count);
            }
        }
    }

    private static void validate(
            HospitalNetworkGraph graph,
            int startNode) {

        if (graph == null) {
            throw new IllegalArgumentException("Graph must not be null");
        }

        if (startNode < 0 || startNode >= graph.vertexCount()) {
            throw new IndexOutOfBoundsException(
                    "Start node out of bounds: " + startNode);
        }
    }

    private static int[] trim(int[] array, int size) {
        int[] result = new int[size];

        for (int i = 0; i < size; i++) {
            result[i] = array[i];
        }

        return result;
    }
}