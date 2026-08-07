package edu.ug.dcit308.hospital.graph.core;

/**
 * Immutable weighted edge pointing at a destination vertex index.
 * Weight typically represents distance (km) or travel time (mins) between hospitals.
 */
public final class Edge {

    private final int destination;
    private final double weight;

    public Edge(int destination, double weight) {
        this.destination = destination;
        this.weight = weight;
    }

    public int getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }
}
