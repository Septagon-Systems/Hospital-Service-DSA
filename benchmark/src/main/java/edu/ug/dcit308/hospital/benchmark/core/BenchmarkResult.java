package edu.ug.dcit308.hospital.benchmark.core;

import java.util.Objects;

/**
 * Immutable measurement result for a single execution run of an algorithm.
 */
public class BenchmarkResult {

    private final String algorithmName;
    private final int inputSize;
    private final int runNumber;
    private final long executionTimeNs;
    private final double memoryUsedKb;

    public BenchmarkResult(
            String algorithmName,
            int inputSize,
            int runNumber,
            long executionTimeNs,
            double memoryUsedKb) {

        this.algorithmName = Objects.requireNonNull(algorithmName, "algorithmName must not be null");
        this.inputSize = inputSize;
        this.runNumber = runNumber;
        this.executionTimeNs = executionTimeNs;
        this.memoryUsedKb = memoryUsedKb;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public int getInputSize() {
        return inputSize;
    }

    public int getRunNumber() {
        return runNumber;
    }

    public long getExecutionTimeNs() {
        return executionTimeNs;
    }

    public double getMemoryUsedKb() {
        return memoryUsedKb;
    }

    public double getExecutionTimeMs() {
        return executionTimeNs / 1_000_000.0;
    }

    public String toCsvRow() {
        return String.format("%s,%d,%d,%d,%.2f",
                algorithmName, inputSize, runNumber, executionTimeNs, memoryUsedKb);
    }

    @Override
    public String toString() {
        return String.format(
                "BenchmarkResult[algorithm=%s, size=%d, run=%d, time=%.3f ms, mem=%.2f KB]",
                algorithmName, inputSize, runNumber, getExecutionTimeMs(), memoryUsedKb);
    }
}
