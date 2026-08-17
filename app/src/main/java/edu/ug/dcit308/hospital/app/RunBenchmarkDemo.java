package edu.ug.dcit308.hospital.app;

import edu.ug.dcit308.hospital.algo.sort.InsertionSort;
import edu.ug.dcit308.hospital.algo.sort.MergeSort;

import java.util.Random;

public class RunBenchmarkDemo {

    public static void execute() {
        System.out.println();
        System.out.println("========== EMPIRICAL PERFORMANCE BENCHMARKS ==========");

        int sampleSize = 5000;
        Integer[] dataset = generateRandomArray(sampleSize);

        System.out.println("Generated random dataset of " + sampleSize + " elements.");
        System.out.println("Measuring execution time...\n");

        // Benchmark Insertion Sort
        Integer[] insertionData = dataset.clone();
        long startTime = System.nanoTime();
        InsertionSort.sort(insertionData);
        long endTime = System.nanoTime();
        double insertionTimeMs = (endTime - startTime) / 1_000_000.0;
        System.out.printf("  Insertion Sort Time : %.3f ms\n", insertionTimeMs);

        // Benchmark Merge Sort
        Integer[] mergeData = dataset.clone();
        startTime = System.nanoTime();
        MergeSort.sort(mergeData);
        endTime = System.nanoTime();
        double mergeTimeMs = (endTime - startTime) / 1_000_000.0;
        System.out.printf("  Merge Sort Time     : %.3f ms\n", mergeTimeMs);

        System.out.println("=======================================================");
    }

    private static Integer[] generateRandomArray(int size) {
        Random random = new Random(42); // Fixed seed for reproducible benchmarks
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(10000);
        }
        return arr;
    }
}