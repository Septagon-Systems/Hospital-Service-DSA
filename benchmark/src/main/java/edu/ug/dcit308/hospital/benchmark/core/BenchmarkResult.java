package edu.ug.dcit308.hospital.benchmark.core;

import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable measurement result for a single execution run of an algorithm.
 */
public class BenchmarkResult {

    private final UUID runId;
    private final Instant runTimestamp;
    private final String algorithmName;
    private final int inputSize;
    private final int runNumber;
    private final long executionTimeNs;
    private final double memoryUsedKb;

    /**
     * Primary constructor accepting UUID and Instant.
     */
    public BenchmarkResult(
            UUID runId,
            Instant runTimestamp,
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
     * Overload accepting String representations for runId and runTimestamp.
     */
    public BenchmarkResult(
            String runId,
            String runTimestamp,
            String algorithmName,
            int inputSize,
            int runNumber,
            long executionTimeNs,
            double memoryUsedKb) {

        this(
            UUID.fromString(Objects.requireNonNull(runId, "runId must not be null")),
            Instant.parse(Objects.requireNonNull(runTimestamp, "runTimestamp must not be null")),
            algorithmName,
            inputSize,
            runNumber,
            executionTimeNs,
            memoryUsedKb
        );
    }

    /**
     * Backward-compatible constructor that auto-generates runId (UUID) and runTimestamp (Instant).
     */
    public BenchmarkResult(
            String algorithmName,
            int inputSize,
            int runNumber,
            long executionTimeNs,
            double memoryUsedKb) {

        this(UUID.randomUUID(), Instant.now(), algorithmName, inputSize, runNumber, executionTimeNs, memoryUsedKb);
    }

    public UUID getRunId() {
        return runId;
    }

    public String getRunIdAsString() {
        return runId.toString();
    }

    public Instant getRunTimestamp() {
        return runTimestamp;
    }

    public String getRunTimestampAsString() {
        return runTimestamp.toString();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BenchmarkResult that = (BenchmarkResult) o;
        return inputSize == that.inputSize &&
                runNumber == that.runNumber &&
                executionTimeNs == that.executionTimeNs &&
                Double.compare(that.memoryUsedKb, memoryUsedKb) == 0 &&
                Objects.equals(runId, that.runId) &&
                Objects.equals(runTimestamp, that.runTimestamp) &&
                Objects.equals(algorithmName, that.algorithmName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(runId, runTimestamp, algorithmName, inputSize, runNumber, executionTimeNs, memoryUsedKb);
    }
}
