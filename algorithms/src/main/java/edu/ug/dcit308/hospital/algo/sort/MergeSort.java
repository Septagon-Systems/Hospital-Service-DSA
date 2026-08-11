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

    // 2. Generic Comparator overload 
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

    // Create ONE auxilliary array and reuse it
    T[] aux = array.clone();

    mergeSort(array, aux, 0, array.length - 1, comparator);
}
private static <T> void mergeSort(
            T[] array,
            T[] aux,
            int low,
            int high,
            Comparator<T> comparator) {

        if (low >= high) {
            return;
        }

        int mid = low + (high - low) / 2;

        mergeSort(array, aux, low, mid, comparator);
        mergeSort(array, aux, mid + 1, high, comparator);

        merge(array, aux, low, mid, high, comparator);
    }

    private static <T> void merge(
            T[] array,
            T[] aux,
            int low,
            int mid,
            int high,
            Comparator<T> comparator) {

        System.arraycopy(array, low, aux, low, high - low + 1);

        int i = low;
        int j = mid + 1;
        int k = low;

        while (i <= mid && j <= high) {
            if (comparator.compare(aux[i], aux[j]) <= 0) {
                array[k++] = aux[i++];
            } else {
                array[k++] = aux[j++];
            }
        }

        while (i <= mid) {
            array[k++] = aux[i++];
        }

        while (j <= high) {
            array[k++] = aux[j++];
        }
    }

    // 3. Primitive int[] overload
    public static void sort(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null");
        }

        if (array.length <= 1) {
            return;
        }

        int[] aux = new int[array.length];

        mergeSort(array, aux, 0, array.length - 1);
    }

    private static void mergeSort(
            int[] array,
            int[] aux,
            int low,
            int high) {

        if (low >= high) {
            return;
        }

        int mid = low + (high - low) / 2;

        mergeSort(array, aux, low, mid);
        mergeSort(array, aux, mid + 1, high);

        merge(array, aux, low, mid, high);
    }

    private static void merge(
            int[] array,
            int[] aux,
            int low,
            int mid,
            int high) {

        System.arraycopy(array, low, aux, low, high - low + 1);

        int i = low;
        int j = mid + 1;
        int k = low;

        while (i <= mid && j <= high) {
            if (aux[i] <= aux[j]) {
                array[k++] = aux[i++];
            } else {
                array[k++] = aux[j++];
            }
        }

        while (i <= mid) {
            array[k++] = aux[i++];
        }

        while (j <= high) {
            array[k++] = aux[j++];
        }
    }    
}
   