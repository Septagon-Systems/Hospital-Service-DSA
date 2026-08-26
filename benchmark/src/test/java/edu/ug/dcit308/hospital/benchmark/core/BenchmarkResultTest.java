package edu.ug.dcit308.hospital.benchmark.core;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class BenchmarkResultTest {

    @Test
    void testBenchmarkResultFieldsAndGetters() {
        BenchmarkResult result = new BenchmarkResult("run-1", "2026-08-26T10:00:00Z", "MergeSort", 1000, 1, 1500000L, 45.5);

        assertEquals("run-1", result.getRunId());
        assertEquals("2026-08-26T10:00:00Z", result.getRunTimestamp());
        assertEquals("MergeSort", result.getAlgorithmName());
        assertEquals(1000, result.getInputSize());
        assertEquals(1, result.getRunNumber());
        assertEquals(1500000L, result.getExecutionTimeNs());
        assertEquals(45.5, result.getMemoryUsedKb(), 1e-9);
        assertEquals(1.5, result.getExecutionTimeMs(), 1e-9);
    }

    @Test
    void testAutoGeneratingConstructor() {
        BenchmarkResult result = new BenchmarkResult("QuickSort", 500, 1, 100000L, 10.0);

        assertNotNull(result.getRunId());
        assertNotNull(result.getRunTimestamp());
        assertEquals("QuickSort", result.getAlgorithmName());
        assertEquals(500, result.getInputSize());
    }

    @Test
    void testToCsvRow() {
        BenchmarkResult result = new BenchmarkResult("run-abc", "2026-08-26T10:00:00Z", "QuickSort", 500, 2, 800000L, 12.0);
        String csv = result.toCsvRow();

        assertEquals("run-abc,2026-08-26T10:00:00Z,QuickSort,500,2,800000,12.00", csv);
    }

    @Test
    void testToStringContainsEssentialInfo() {
        BenchmarkResult result = new BenchmarkResult("InsertionSort", 100, 1, 500000L, 8.0);
        String str = result.toString();

        assertTrue(str.contains("InsertionSort"));
        assertTrue(str.contains("100"));
        assertTrue(str.contains("1"));
    }

    @Test
    void testRejectsNullAlgorithmName() {
        assertThrows(NullPointerException.class,
                () -> new BenchmarkResult(null, 100, 1, 1000L, 5.0));
    }
}
