package edu.ug.dcit308.hospital.benchmark.exporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import edu.ug.dcit308.hospital.benchmark.core.BenchmarkResult;

/**
 * Exporter utility that writes {@link BenchmarkResult} lists to CSV files in append mode.
 */
public final class ResultWriter {

    public static final String DEFAULT_CSV_PATH = Paths.get("data", "results", "algorithm_runs.csv").toString();
    public static final String CSV_HEADER = "run_id,run_timestamp,algorithm_name,input_size,run_number,execution_time_ns,memory_used_kb";

    private ResultWriter() {}

    /**
     * Exports the given benchmark results to the default CSV location (data/results/algorithm_runs.csv) in append mode.
     *
     * @param results list of benchmark results to export
     * @throws IOException if writing fails
     */
    public static void exportToCsv(List<BenchmarkResult> results) throws IOException {
        exportToCsv(results, DEFAULT_CSV_PATH);
    }

    /**
     * Exports the given benchmark results to the specified CSV file path in append mode.
     *
     * @param results list of benchmark results to export
     * @param filePath target file destination
     * @throws IOException if writing fails
     */
    public static void exportToCsv(List<BenchmarkResult> results, String filePath) throws IOException {
        if (results == null) {
            throw new IllegalArgumentException("Results list must not be null");
        }
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path must not be null or blank");
        }

        File targetFile = new File(filePath);
        File parentDir = targetFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        boolean isNewFile = !targetFile.exists() || targetFile.length() == 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile, true))) {
            if (isNewFile) {
                writer.write(CSV_HEADER);
                writer.newLine();
            }

            for (BenchmarkResult result : results) {
                writer.write(result.toCsvRow());
                writer.newLine();
            }
        }
    }

    /**
     * Convenience overload accepting a {@link Path}.
     *
     * @param results list of benchmark results to export
     * @param path target file destination path
     * @throws IOException if writing fails
     */
    public static void exportToCsv(List<BenchmarkResult> results, Path path) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("Path must not be null");
        }
        exportToCsv(results, path.toString());
    }
}
