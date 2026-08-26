package edu.ug.dcit308.hospital.app;

import edu.ug.dcit308.hospital.algo.sort.InsertionSort;
import edu.ug.dcit308.hospital.algo.sort.MergeSort;
import edu.ug.dcit308.hospital.algo.sort.QuickSort;
import edu.ug.dcit308.hospital.algo.sort.SelectionSort;
import edu.ug.dcit308.hospital.ds.list.DynamicArrayList;

public class RunBenchmarkDemo {

    public static void execute() {
        DynamicArrayList<RunDataLoader.Hospital> defaultDataset = RunDataLoader.loadData();
        execute(defaultDataset);
    }

    public static void execute(DynamicArrayList<RunDataLoader.Hospital> dataset) {
        System.out.println();
        System.out.println("========== EMPIRICAL PERFORMANCE BENCHMARKS ==========");

        if (dataset == null || dataset.isEmpty()) {
            System.out.println("[!] No hospital dataset available for benchmarking.");
            System.out.println("=======================================================");
            return;
        }

        int count = dataset.size();
        System.out.println("Benchmarking sort execution time over " + count + " active hospital records...");

        // Extract bed capacity values from the loaded dataset
        Integer[] baseData = new Integer[count];
        for (int i = 0; i < count; i++) {
            baseData[i] = dataset.get(i).getCapacity();
        }

        System.out.println("Measuring execution time in milliseconds (ms)...\n");

        // 1. Insertion Sort
        Integer[] insertionData = copyArray(baseData);
        long startTime = System.nanoTime();
        InsertionSort.sort(insertionData);
        long endTime = System.nanoTime();
        double insertionTimeMs = (endTime - startTime) / 1_000_000.0;
        System.out.printf("  Insertion Sort Time : %.4f ms\n", insertionTimeMs);

        // 2. Selection Sort
        Integer[] selectionData = copyArray(baseData);
        SelectionSort<Integer> selectionSort = new SelectionSort<>();
        startTime = System.nanoTime();
        selectionSort.sort(selectionData);
        endTime = System.nanoTime();
        double selectionTimeMs = (endTime - startTime) / 1_000_000.0;
        System.out.printf("  Selection Sort Time : %.4f ms\n", selectionTimeMs);

        // 3. Merge Sort
        Integer[] mergeData = copyArray(baseData);
        startTime = System.nanoTime();
        MergeSort.sort(mergeData);
        endTime = System.nanoTime();
        double mergeTimeMs = (endTime - startTime) / 1_000_000.0;
        System.out.printf("  Merge Sort Time     : %.4f ms\n", mergeTimeMs);

        // 4. Quick Sort
        Integer[] quickData = copyArray(baseData);
        startTime = System.nanoTime();
        QuickSort.sort(quickData);
        endTime = System.nanoTime();
        double quickTimeMs = (endTime - startTime) / 1_000_000.0;
        System.out.printf("  Quick Sort Time     : %.4f ms\n", quickTimeMs);

        System.out.println("=======================================================");
    }

    private static Integer[] copyArray(Integer[] source) {
        Integer[] copy = new Integer[source.length];
        for (int i = 0; i < source.length; i++) {
            copy[i] = source[i];
        }
        return copy;
    }
}