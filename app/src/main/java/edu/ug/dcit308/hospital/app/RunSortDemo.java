package edu.ug.dcit308.hospital.app;

import edu.ug.dcit308.hospital.algo.sort.InsertionSort;
import edu.ug.dcit308.hospital.algo.sort.MergeSort;
import edu.ug.dcit308.hospital.algo.sort.QuickSort;
import edu.ug.dcit308.hospital.algo.sort.SelectionSort;
import edu.ug.dcit308.hospital.ds.list.DynamicArrayList;

public class RunSortDemo {

    public static void execute() {
        DynamicArrayList<RunDataLoader.Hospital> defaultDataset = RunDataLoader.loadData();
        execute(defaultDataset);
    }

    public static void execute(DynamicArrayList<RunDataLoader.Hospital> dataset) {
        System.out.println();
        System.out.println("========== SORT DEMONSTRATION ==========");

        if (dataset == null || dataset.isEmpty()) {
            System.out.println("[!] No hospital dataset available for sorting.");
            System.out.println("=========================================");
            return;
        }

        System.out.println("Sorting " + dataset.size() + " active hospital records by Bed Capacity...");

        // Extract bed capacities into an array for sorting demonstrations
        Integer[] original = new Integer[dataset.size()];
        for (int i = 0; i < dataset.size(); i++) {
            original[i] = dataset.get(i).getCapacity();
        }

        printArray("Original Capacities", original);

        // 1. Insertion Sort
        Integer[] insertionArray = copyArray(original);
        InsertionSort.sort(insertionArray);
        printArray("Insertion Sort     ", insertionArray);

        // 2. Merge Sort
        Integer[] mergeArray = copyArray(original);
        MergeSort.sort(mergeArray);
        printArray("Merge Sort         ", mergeArray);

        // 3. Selection Sort
        Integer[] selectionArray = copyArray(original);
        SelectionSort<Integer> selectionSort = new SelectionSort<>();
        selectionSort.sort(selectionArray);
        printArray("Selection Sort     ", selectionArray);

        // 4. Quick Sort
        Integer[] quickArray = copyArray(original);
        QuickSort.sort(quickArray);
        printArray("Quick Sort         ", quickArray);

        System.out.println("=========================================");
    }

    private static Integer[] copyArray(Integer[] source) {
        Integer[] copy = new Integer[source.length];
        for (int i = 0; i < source.length; i++) {
            copy[i] = source[i];
        }
        return copy;
    }

    private static void printArray(String label, Integer[] array) {
        System.out.print(label + ": [");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + (i < array.length - 1 ? ", " : ""));
        }
        System.out.println("]");
    }
}