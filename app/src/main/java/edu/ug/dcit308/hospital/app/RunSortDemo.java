package edu.ug.dcit308.hospital.app;

import edu.ug.dcit308.hospital.algo.sort.InsertionSort;
import edu.ug.dcit308.hospital.algo.sort.MergeSort;
import edu.ug.dcit308.hospital.algo.sort.SelectionSort;

import java.util.Arrays;

public class RunSortDemo {

    public static void execute() {

        System.out.println();
        System.out.println("========== SORT DEMONSTRATION ==========");

        Integer[] original = {64, 25, 12, 22, 11};

        System.out.println("Original array: " + Arrays.toString(original));

        Integer[] insertionArray = original.clone();
        InsertionSort.sort(insertionArray);
        System.out.println("Insertion Sort: " + Arrays.toString(insertionArray));

        Integer[] mergeArray = original.clone();
        MergeSort.sort(mergeArray);
        System.out.println("Merge Sort:     " + Arrays.toString(mergeArray));

        Integer[] selectionArray = original.clone();
        SelectionSort<Integer> selectionSort = new SelectionSort<>();
        selectionSort.sort(selectionArray);
        System.out.println("Selection Sort: " + Arrays.toString(selectionArray));

        System.out.println("=========================================");
    }
}