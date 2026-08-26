package edu.ug.dcit308.hospital.db.model;

import java.util.Objects;

/**
 * Entity model representing an empirical algorithm benchmark execution run.
 */
public class AlgorithmRun {
    private String runId;
    private String algorithmName;
    private int inputSize;
    private long executionTimeNs;
    private double memoryUsedKb;
    private int iterationNumber;
    private String runTimestamp;

    public AlgorithmRun() {}

    public AlgorithmRun(String runId, String algorithmName, int inputSize,
                        long executionTimeNs, double memoryUsedKb,
                        int iterationNumber, String runTimestamp) {
        this.runId = runId;
        this.algorithmName = algorithmName;
        this.inputSize = inputSize;
        this.executionTimeNs = executionTimeNs;
        this.memoryUsedKb = memoryUsedKb;
        this.iterationNumber = iterationNumber;
        this.runTimestamp = runTimestamp;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public int getInputSize() {
        return inputSize;
    }

    public void setInputSize(int inputSize) {
        this.inputSize = inputSize;
    }

    public long getExecutionTimeNs() {
        return executionTimeNs;
    }

    public void setExecutionTimeNs(long executionTimeNs) {
        this.executionTimeNs = executionTimeNs;
    }

    public double getMemoryUsedKb() {
        return memoryUsedKb;
    }

    public void setMemoryUsedKb(double memoryUsedKb) {
        this.memoryUsedKb = memoryUsedKb;
    }

    public int getIterationNumber() {
        return iterationNumber;
    }

    public void setIterationNumber(int iterationNumber) {
        this.iterationNumber = iterationNumber;
    }

    public String getRunTimestamp() {
        return runTimestamp;
    }

    public void setRunTimestamp(String runTimestamp) {
        this.runTimestamp = runTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlgorithmRun that = (AlgorithmRun) o;
        return inputSize == that.inputSize &&
                iterationNumber == that.iterationNumber &&
                Objects.equals(runId, that.runId) &&
                Objects.equals(algorithmName, that.algorithmName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(runId, algorithmName, inputSize, iterationNumber);
    }

    @Override
    public String toString() {
        return "AlgorithmRun{" +
                "runId='" + runId + '\'' +
                ", algorithmName='" + algorithmName + '\'' +
                ", inputSize=" + inputSize +
                ", executionTimeNs=" + executionTimeNs +
                ", memoryUsedKb=" + memoryUsedKb +
                ", iterationNumber=" + iterationNumber +
                ", runTimestamp='" + runTimestamp + '\'' +
                '}';
    }
}
