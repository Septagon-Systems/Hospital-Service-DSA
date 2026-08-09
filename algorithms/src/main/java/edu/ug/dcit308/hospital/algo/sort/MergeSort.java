package edu.ug.dcit308.hospital.algo.sort;

import java.util.Comparator;

/**
 * Recursive Merge Sort implementation.
 * Runs in O(n log n) time and O(n) auxiliary space. 
 */ 
public final class MergeSort {

    private MergeSort() {
    }
    
    
    // 1. Generic Comparable overload 
    public static <T extends Comparable<T>> void sort(T[] array) {
        sort(array, Comparator.naturalOrder());
    }

    // 2. Custom Comparabtor overload 
    public static <T> void sort(T[] array, Comparator<T> comparator) {
        if (array == null)  {
            throw new IllegalArgumentException("Array must not be null"); 
    }
    if (comparator == null) {
        throw new IllegalArgumentException("Comparator must not be null");
    }

    if (array.length <= 1) {
        return;
    }

    int middle = array.length / 2;

    T[] left = java.util.Arrays.copyOfRange(array, 0, middle);
    T[] right = java.util.Arrays.copyOfRange(array, middle, array.length);

    sort(left, comparator);
    sort(right, comparator);

    int i = 0;
    int j = 0;
    int k = 0;

    while (i < left.length && j < right.length) {
        if (comparator.compare(left[i], right[j]) <= 0) {
            array[k++] = left[i++];
        } else {
            array[k++] = right[j++];
        }
    }

    while (i < left.length) {
        array[k++] = left[i++];
    }

    while (j < right.length) {
        array[k++] = right[j++];
    }
}
}
   