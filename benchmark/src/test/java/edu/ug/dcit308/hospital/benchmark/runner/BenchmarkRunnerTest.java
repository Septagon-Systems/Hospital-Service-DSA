package edu.ug.dcit308.hospital.benchmark.runner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ug.dcit308.hospital.benchmark.core.BenchmarkResult;

class BenchmarkRunnerTest {

    private BenchmarkRunner runner;

    @BeforeEach
    void setUp() {
        runner = new BenchmarkRunner();
    }

    @Test
    void testDefaultAlgorithmsRegistered() {
        List<String> registered = runner.getRegisteredAlgorithmNames();

        assertTrue(registered.contains("InsertionSort"));
        assertTrue(registered.contains("SelectionSort"));
        assertTrue(registered.contains("MergeSort"));
        assertTrue(registered.contains("QuickSort"));
        assertTrue(registered.contains("LinearSearch"));
        assertTrue(registered.contains("BinarySearch"));
    }

    @Test
    void testRegisterCustomAlgorithm() {
        runner.registerAlgorithm("NoOp", data -> {});

        assertTrue(runner.getRegisteredAlgorithmNames().contains("NoOp"));
        List<BenchmarkResult> results = runner.runExperiment("NoOp", 100, 2);

        assertEquals(2, results.size());
        assertEquals("NoOp", results.get(0).getAlgorithmName());
        assertEquals(100, results.get(0).getInputSize());
        assertEquals(1, results.get(0).getRunNumber());
        assertEquals(2, results.get(1).getRunNumber());
    }

    @Test
    void testRunExperimentSortingAlgorithm() {
        List<BenchmarkResult> results = runner.runExperiment("MergeSort", 200, 3);

        assertEquals(3, results.size());
        for (int i = 0; i < results.size(); i++) {
            BenchmarkResult r = results.get(i);
            assertEquals("MergeSort", r.getAlgorithmName());
            assertEquals(200, r.getInputSize());
            assertEquals(i + 1, r.getRunNumber());
            assertTrue(r.getExecutionTimeNs() > 0);
            assertTrue(r.getMemoryUsedKb() >= 0);
        }
    }

    @Test
    void testRunAllCustomSizes() {
        runner.registerAlgorithm("Dummy", data -> {});
        List<BenchmarkResult> results = runner.runExperiment("Dummy", 50, 1);

        assertEquals(1, results.size());
        assertEquals(50, results.get(0).getInputSize());
    }

    @Test
    void testRejectsUnregisteredAlgorithm() {
        assertThrows(IllegalArgumentException.class,
                () -> runner.runExperiment("NonExistentAlgo", 100, 1));
    }

    @Test
    void testRejectsInvalidInputSize() {
        assertThrows(IllegalArgumentException.class,
                () -> runner.runExperiment("QuickSort", 0, 1));
        assertThrows(IllegalArgumentException.class,
                () -> runner.runExperiment("QuickSort", -10, 1));
    }

    @Test
    void testRejectsInvalidIterations() {
        assertThrows(IllegalArgumentException.class,
                () -> runner.runExperiment("QuickSort", 100, 0));
        assertThrows(IllegalArgumentException.class,
                () -> runner.runExperiment("QuickSort", 100, -1));
    }

    @Test
    void testRegisterAlgorithmRejectsNullArguments() {
        assertThrows(IllegalArgumentException.class,
                () -> runner.registerAlgorithm(null, data -> {}));
        assertThrows(IllegalArgumentException.class,
                () -> runner.registerAlgorithm("Algo", null));
    }
}
