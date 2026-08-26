package com.benchmark;

/**
 * Immutable record of a single benchmark measurement (one algorithm, one
 * input size, one iteration), tagged with the id/timestamp of the run
 * session it belongs to so multiple runs can be told apart once appended
 * into the same results file over time.
 */
public final class BenchmarkResult {

    private final String runId;
    private final String runTimestamp; // ISO-8601, shared by every result in the same run session
    private final String algorithmName;
    private final int inputSize;
    private final int iteration;
    private final long executionTimeNs;
    private final double memoryUsedKb;

    public BenchmarkResult(String runId,
                            String runTimestamp,
                            String algorithmName,
                            int inputSize,
                            int iteration,
                            long executionTimeNs,
                            double memoryUsedKb) {
        this.runId = runId;
        this.runTimestamp = runTimestamp;
        this.algorithmName = algorithmName;
        this.inputSize = inputSize;
        this.iteration = iteration;
        this.executionTimeNs = executionTimeNs;
        this.memoryUsedKb = memoryUsedKb;
    }

    public String getRunId() {
        return runId;
    }

    public String getRunTimestamp() {
        return runTimestamp;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public int getInputSize() {
        return inputSize;
    }

    public int getIteration() {
        return iteration;
    }

    public long getExecutionTimeNs() {
        return executionTimeNs;
    }

    public double getMemoryUsedKb() {
        return memoryUsedKb;
    }

    @Override
    public String toString() {
        return String.format(
            "[%s] %s | size=%d | iter=%d | time=%d ns | mem=%.2f KB",
            runId.substring(0, 8), algorithmName, inputSize, iteration, executionTimeNs, memoryUsedKb);
    }
}
