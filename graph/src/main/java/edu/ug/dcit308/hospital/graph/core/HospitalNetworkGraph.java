package edu.ug.dcit308.hospital.graph.core;

import java.util.NoSuchElementException;

/**
 * Weighted graph representing the hospital road network. Vertices are hospital
 * indices in {@code [0, vertexCount)}; edges carry a weight (e.g. distance_km
 * or avg_travel_time_mins from the routes table). Backed by a resizable
 * adjacency list per vertex, suited to the sparse road network expected here.
 * Undirected by default (roads are bidirectional); pass {@code directed = true}
 * to model one-way routes.
 */
public class HospitalNetworkGraph {

    private static final int DEFAULT_ADJACENCY_CAPACITY = 4;

    private final Edge[][] adjacency;
    private final int[] edgeCounts;
    private final int vertexCount;
    private final boolean directed;

    public HospitalNetworkGraph(int vertexCount) {
        this(vertexCount, false);
    }

    public HospitalNetworkGraph(int vertexCount, boolean directed) {
        if (vertexCount <= 0) {
            throw new IllegalArgumentException("Vertex count must be positive");
        }
        this.vertexCount = vertexCount;
        this.directed = directed;
        this.adjacency = new Edge[vertexCount][];
        this.edgeCounts = new int[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            adjacency[i] = new Edge[DEFAULT_ADJACENCY_CAPACITY];
        }
    }

    public void addEdge(int source, int destination, double weight) {
        checkVertex(source);
        checkVertex(destination);
        if (weight < 0) {
            throw new IllegalArgumentException("Edge weight must not be negative");
        }
        appendEdge(source, new Edge(destination, weight));
        if (!directed && source != destination) {
            appendEdge(destination, new Edge(source, weight));
        }
    }

    public void removeEdge(int source, int destination) {
        checkVertex(source);
        checkVertex(destination);
        boolean removed = removeFromAdjacency(source, destination);
        if (!removed) {
            throw new NoSuchElementException("No edge from " + source + " to " + destination);
        }
        if (!directed && source != destination) {
            removeFromAdjacency(destination, source);
        }
    }

    public Edge[] getNeighbors(int vertex) {
        checkVertex(vertex);
        Edge[] trimmed = new Edge[edgeCounts[vertex]];
        for (int i = 0; i < edgeCounts[vertex]; i++) {
            trimmed[i] = adjacency[vertex][i];
        }
        return trimmed;
    }

    public boolean hasEdge(int source, int destination) {
        checkVertex(source);
        checkVertex(destination);
        for (int i = 0; i < edgeCounts[source]; i++) {
            if (adjacency[source][i].getDestination() == destination) {
                return true;
            }
        }
        return false;
    }

    public double getWeight(int source, int destination) {
        checkVertex(source);
        checkVertex(destination);
        for (int i = 0; i < edgeCounts[source]; i++) {
            if (adjacency[source][i].getDestination() == destination) {
                return adjacency[source][i].getWeight();
            }
        }
        throw new NoSuchElementException("No edge from " + source + " to " + destination);
    }

    public int vertexCount() {
        return vertexCount;
    }

    public int degree(int vertex) {
        checkVertex(vertex);
        return edgeCounts[vertex];
    }

    public boolean isDirected() {
        return directed;
    }

    private void appendEdge(int vertex, Edge edge) {
        if (edgeCounts[vertex] == adjacency[vertex].length) {
            Edge[] resized = new Edge[adjacency[vertex].length * 2];
            for (int i = 0; i < edgeCounts[vertex]; i++) {
                resized[i] = adjacency[vertex][i];
            }
            adjacency[vertex] = resized;
        }
        adjacency[vertex][edgeCounts[vertex]++] = edge;
    }

    private boolean removeFromAdjacency(int vertex, int destination) {
        for (int i = 0; i < edgeCounts[vertex]; i++) {
            if (adjacency[vertex][i].getDestination() == destination) {
                for (int j = i; j < edgeCounts[vertex] - 1; j++) {
                    adjacency[vertex][j] = adjacency[vertex][j + 1];
                }
                adjacency[vertex][--edgeCounts[vertex]] = null;
                return true;
            }
        }
        return false;
    }

    private void checkVertex(int vertex) {
        if (vertex < 0 || vertex >= vertexCount) {
            throw new IndexOutOfBoundsException("Vertex out of bounds: " + vertex);
        }
    }
}
