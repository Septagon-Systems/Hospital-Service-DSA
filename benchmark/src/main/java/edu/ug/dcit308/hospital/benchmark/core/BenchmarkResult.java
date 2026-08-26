package edu.ug.dcit308.hospital.benchmark.core;

import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable measurement result for a single execution run of an algorithm.
 */
public class BenchmarkResult {

    private final String runId;
    private final String runTimestamp;
    private final String algorithmName;
    private final int inputSize;
    private final int runNumber;
    private final long executionTimeNs;
    private final double memoryUsedKb;

    /**
     * Explicit constructor.
     */
    public BenchmarkResult(
            String runId,
            String runTimestamp,
            String algorithmName,
            int inputSize,
            int runNumber,
            long executionTimeNs,
            double memoryUsedKb) {

        this.runId = Objects.requireNonNull(runId, "runId must not be null");
        this.runTimestamp = Objects.requireNonNull(runTimestamp, "runTimestamp must not be null");
        this.algorithmName = Objects.requireNonNull(algorithmName, "algorithmName must not be null");
        this.inputSize = inputSize;
        this.runNumber = runNumber;
        this.executionTimeNs = executionTimeNs;
        this.memoryUsedKb = memoryUsedKb;
    }

    /**
     * Auto-generating constructor for new runs.
     */
    public BenchmarkResult(
            String algorithmName,
            int inputSize,
            int runNumber,
            long executionTimeNs,
            double memoryUsedKb) {

        this(UUID.randomUUID().toString(), Instant.now().toString(), algorithmName, inputSize, runNumber, executionTimeNs, memoryUsedKb);
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
        return String.format(Locale.US, "%s,%s,%s,%d,%d,%d,%.2f",
                runId, runTimestamp, algorithmName, inputSize, runNumber, executionTimeNs, memoryUsedKb);
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "BenchmarkResult[runId=%s, timestamp=%s, algorithm=%s, size=%d, run=%d, time=%.3f ms, mem=%.2f KB]",
                runId, runTimestamp, algorithmName, inputSize, runNumber, getExecutionTimeMs(), memoryUsedKb);
    }
}
