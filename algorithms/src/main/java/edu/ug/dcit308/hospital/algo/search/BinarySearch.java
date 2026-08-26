package edu.ug.dcit308.hospital.algo.search;

import java.util.Comparator;

public class BinarySearch {

    public static int search(int[] array, int key) {
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null.");
        }
        int low = 0;
        int high = array.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (array[mid] == key) {
                return mid;
            }
            if (array[mid] < key) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    public <T extends Comparable<T>> int search(T[] array, T key) {
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null.");
        }

        if (key == null) {
            throw new IllegalArgumentException("Key must not be null.");
        }

        int low = 0;
        int high = array.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (array[mid] == null) {
                throw new IllegalArgumentException("Array elements must not be null.");
            }

            int comparison = array[mid].compareTo(key);

            if (comparison == 0) {
                return mid;
            }

            if (comparison < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1;
    }

    public <T> int search(T[] array, T key, Comparator<T> comparator) {
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null.");
        }

        if (key == null) {
            throw new IllegalArgumentException("Key must not be null.");
        }

        if (comparator == null) {
            throw new IllegalArgumentException("Comparator must not be null.");
        }

        int low = 0;
        int high = array.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (array[mid] == null) {
                throw new IllegalArgumentException("Array elements must not be null.");
            }

            int comparison = comparator.compare(array[mid], key);

            if (comparison == 0) {
                return mid;
            }

            if (comparison < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1;
    }
}