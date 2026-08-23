package edu.ug.dcit308.hospital.benchmark.runner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.ug.dcit308.hospital.algo.search.BinarySearch;
import edu.ug.dcit308.hospital.algo.search.LinearSearch;
import edu.ug.dcit308.hospital.algo.sort.InsertionSort;
import edu.ug.dcit308.hospital.algo.sort.MergeSort;
import edu.ug.dcit308.hospital.algo.sort.QuickSort;
import edu.ug.dcit308.hospital.algo.sort.SelectionSort;
import edu.ug.dcit308.hospital.benchmark.core.Algorithm;
import edu.ug.dcit308.hospital.benchmark.core.BenchmarkResult;

/**
 * Executes registered {@link Algorithm} implementations across a set of input
 * sizes, repeating each run {@code iterations} times, and captures
 * execution time (ns) and approximate heap memory delta (KB) per run.
 */
public class BenchmarkRunner {

    /** Standard input sizes specified by the project brief: 100, 500, 1000, 5000, 10000, 20000. */
    public static final int[] DEFAULT_SIZES = {100, 500, 1000, 5000, 10000, 20000};

    /** Number of repeated runs per (algorithm, size) pair. */
    public static final int DEFAULT_ITERATIONS = 3;

    private static final long RANDOM_SEED = 42L;

    private final Map<String, Algorithm> registry = new LinkedHashMap<>();

    public BenchmarkRunner() {
        registerDefaultAlgorithms();
    }

    private void registerDefaultAlgorithms() {
        registry.put("InsertionSort", InsertionSort::sort);
        registry.put("MergeSort", MergeSort::sort);
        registry.put("QuickSort", QuickSort::sort);
        registry.put("SelectionSort", data -> {
            Integer[] boxed = box(data);
            new SelectionSort<Integer>().sort(boxed);
        });
        registry.put("LinearSearch", data -> LinearSearch.search(data, -1));
        registry.put("BinarySearch", data -> {
            QuickSort.sort(data);
            Integer[] boxed = box(data);
            new BinarySearch().search(boxed, -1);
        });
    }

    private static Integer[] box(int[] data) {
        Integer[] boxed = new Integer[data.length];
        for (int i = 0; i < data.length; i++) {
            boxed[i] = data[i];
        }
        return boxed;
    }

    public void registerAlgorithm(String name, Algorithm algorithm) {
        if (name == null || algorithm == null) {
            throw new IllegalArgumentException("Name and Algorithm must not be null");
        }
        registry.put(name, algorithm);
    }

    public List<String> getRegisteredAlgorithmNames() {
        return Collections.unmodifiableList(new ArrayList<>(registry.keySet()));
    }

    /**
     * Runs a single (algorithm, inputSize) experiment for {@code iterations}
     * repetitions and returns one {@link BenchmarkResult} per repetition.
     */
    public List<BenchmarkResult> runExperiment(String algorithmName, int inputSize, int iterations) {
        if (algorithmName == null || !registry.containsKey(algorithmName)) {
            throw new IllegalArgumentException("No algorithm registered under name: " + algorithmName);
        }
        if (inputSize <= 0) {
            throw new IllegalArgumentException("Input size must be positive: " + inputSize);
        }
        if (iterations <= 0) {
            throw new IllegalArgumentException("Iterations must be positive: " + iterations);
        }

        Algorithm algorithm = registry.get(algorithmName);
        List<BenchmarkResult> results = new ArrayList<>(iterations);
        int[] baseline = generateData(inputSize);

        for (int iter = 1; iter <= iterations; iter++) {
            int[] data = baseline.clone(); // fresh copy so in-place modifications stay fair

            System.gc(); // Suggest GC before run for cleaner memory estimation
            long memBefore = usedMemoryBytes();
            long start = System.nanoTime();

            algorithm.run(data);

            long elapsedNs = System.nanoTime() - start;
            long memAfter = usedMemoryBytes();

            double memoryDeltaKb = Math.max(0, memAfter - memBefore) / 1024.0;
            results.add(new BenchmarkResult(algorithmName, inputSize, iter, elapsedNs, memoryDeltaKb));
        }

        return results;
    }

    /**
     * Convenience method: runs every registered algorithm across {@code sizes},
     * {@code iterations} times each, and returns the combined result set.
     */
    public List<BenchmarkResult> runAll(int[] sizes, int iterations) {
        if (sizes == null || sizes.length == 0) {
            sizes = DEFAULT_SIZES;
        }
        List<BenchmarkResult> all = new ArrayList<>();
        for (String name : registry.keySet()) {
            for (int size : sizes) {
                all.addAll(runExperiment(name, size, iterations));
            }
        }
        return all;
    }

    public List<BenchmarkResult> runAll() {
        return runAll(DEFAULT_SIZES, DEFAULT_ITERATIONS);
    }

    private int[] generateData(int size) {
        Random rnd = new Random(RANDOM_SEED + size);
        int[] data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = rnd.nextInt(1_000_000);
        }
        return data;
    }

    private long usedMemoryBytes() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
