package edu.ug.dcit308.hospital.algo.sort;

import java.util.Comparator;

/**
 * Recursive QuickSort implementation using Lomuto partitioning.
 * Average case: O(n log n) time, O(log n) auxiliary space (recursion stack).
 * Worst case: O(n^2) time when the array is already sorted or reverse sorted,
 * since the last element is always chosen as the pivot.
 */
public final class QuickSort {

    private QuickSort() {
    }

    // 1. Generic Comparable overload
    public static <T extends Comparable<? super T>> void sort(T[] array) {
        sort(array, Comparator.naturalOrder());
    }

    // 2. Generic Comparator overload
    public static <T> void sort(T[] array, Comparator<T> comparator) {
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator must not be null");
        }
        if (array.length <= 1) {
            return;
        }

        quickSort(array, 0, array.length - 1, comparator);
    }

    private static <T> void quickSort(T[] array, int low, int high, Comparator<T> comparator) {
        if (low < high) {
            int pivotIndex = partition(array, low, high, comparator);
            quickSort(array, low, pivotIndex - 1, comparator);
            quickSort(array, pivotIndex + 1, high, comparator);
        }
    }

    private static <T> int partition(T[] array, int low, int high, Comparator<T> comparator) {
        T pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparator.compare(array[j], pivot) <= 0) {
                i++;
                swap(array, i, j);
            }
        }

        swap(array, i + 1, high);
        return i + 1;
    }

    private static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // 3. Primitive int[] overload
    public static void sort(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null");
        }
        if (array.length <= 1) {
            return;
        }

        quickSort(array, 0, array.length - 1);
    }

    private static void quickSort(int[] array, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(array, low, high);
            quickSort(array, low, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, high);
        }
    }

    private static int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j);
            }
        }

        swap(array, i + 1, high);
        return i + 1;
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}