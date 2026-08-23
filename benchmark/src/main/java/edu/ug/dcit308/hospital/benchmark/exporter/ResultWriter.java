package edu.ug.dcit308.hospital.benchmark.exporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import edu.ug.dcit308.hospital.benchmark.core.BenchmarkResult;

/**
 * Exporter utility that writes {@link BenchmarkResult} lists to CSV files.
 */
public final class ResultWriter {

    public static final String CSV_HEADER = "algorithm_name,input_size,run_number,execution_time_ns,memory_used_kb";

    private ResultWriter() {}

    /**
     * Exports the given benchmark results to the specified CSV file path.
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

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile))) {
            writer.write(CSV_HEADER);
            writer.newLine();

            for (BenchmarkResult result : results) {
                writer.write(result.toCsvRow());
                writer.newLine();
            }
        }
    }

    /**
     * Convenience overload accepting a {@link Path}.
     */
    public static void exportToCsv(List<BenchmarkResult> results, Path path) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("Path must not be null");
        }
        exportToCsv(results, path.toString());
    }
}
