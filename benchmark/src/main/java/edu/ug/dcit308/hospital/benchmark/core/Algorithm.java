package edu.ug.dcit308.hospital.benchmark.core;

/**
 * Functional interface representing an algorithm that can be benchmarked
 * against integer array datasets of varying sizes.
 */
@FunctionalInterface
public interface Algorithm {

    /**
     * Executes the algorithm on the provided dataset.
     *
     * @param data input array for the benchmark run
     */
    void run(int[] data);

    /**
     * Optional display name of the algorithm.
     *
     * @return name of the algorithm
     */
    default String getName() {
        return getClass().getSimpleName();
    }
}
