package edu.ug.dcit308.hospital.benchmark.exporter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import edu.ug.dcit308.hospital.benchmark.core.BenchmarkResult;

class ResultWriterTest {

    @TempDir
    Path tempDir;

    @Test
    void testExportToCsvCreatesFileAndWritesHeaderAndRows() throws IOException {
        Path csvPath = tempDir.resolve("results.csv");

        List<BenchmarkResult> results = new ArrayList<>();
        results.add(new BenchmarkResult("QuickSort", 100, 1, 50000L, 10.5));
        results.add(new BenchmarkResult("QuickSort", 100, 2, 48000L, 10.2));

        ResultWriter.exportToCsv(results, csvPath);

        assertTrue(Files.exists(csvPath));
        List<String> lines = Files.readAllLines(csvPath);

        assertEquals(3, lines.size());
        assertEquals(ResultWriter.CSV_HEADER, lines.get(0));
        assertTrue(lines.get(1).startsWith("QuickSort,100,1,50000,"));
        assertTrue(lines.get(2).startsWith("QuickSort,100,2,48000,"));
    }

    @Test
    void testExportToCsvCreatesParentDirectories() throws IOException {
        Path nestedPath = tempDir.resolve("nested/dir/sub/benchmarks.csv");
        List<BenchmarkResult> results = List.of(new BenchmarkResult("BinarySearch", 500, 1, 1200L, 2.0));

        ResultWriter.exportToCsv(results, nestedPath);

        assertTrue(Files.exists(nestedPath));
    }

    @Test
    void testExportRejectsNullArguments() {
        assertThrows(IllegalArgumentException.class,
                () -> ResultWriter.exportToCsv(null, "out.csv"));
        assertThrows(IllegalArgumentException.class,
                () -> ResultWriter.exportToCsv(List.of(), (String) null));
        assertThrows(IllegalArgumentException.class,
                () -> ResultWriter.exportToCsv(List.of(), "   "));
        assertThrows(IllegalArgumentException.class,
                () -> ResultWriter.exportToCsv(List.of(), (Path) null));
    }
}
