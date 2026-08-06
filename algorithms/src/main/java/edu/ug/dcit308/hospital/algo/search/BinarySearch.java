package edu.ug.dcit308.hospital.algo.search;

import java.util.Comparator;

public class BinarySearch {

    public <T extends Comparable<T>> int search(T[] array, T key) {
        validateSorted(array);

        int low = 0;
        int high = array.length - 1;

        while (low <= high) {
            int middle = low + (high - low) / 2;
            int comparison = array[middle].compareTo(key);

            if (comparison == 0) {
                return middle;
            }

            if (comparison < 0) {
                low = middle + 1;
            } else {
                high = middle - 1;
            }
        }

        return -1;
    }

    public <T> int search(T[] array, T key, Comparator<T> comparator) {
        validateSorted(array, comparator);

        int low = 0;
        int high = array.length - 1;

        while (low <= high) {
            int middle = low + (high - low) / 2;
            int comparison = comparator.compare(array[middle], key);

            if (comparison == 0) {
                return middle;
            }

            if (comparison < 0) {
                low = middle + 1;
            } else {
                high = middle - 1;
            }
        }

        return -1;
    }

    private <T extends Comparable<T>> void validateSorted(T[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null.");
        }

        for (int i = 1; i < array.length; i++) {
            if (array[i - 1].compareTo(array[i]) > 0) {
                throw new IllegalArgumentException(
                        "Binary search requires a sorted array."
                );
            }
        }
    }

    private <T> void validateSorted(T[] array, Comparator<T> comparator) {
        if (array == null || comparator == null) {
            throw new IllegalArgumentException(
                    "Array and comparator must not be null."
            );
        }

        for (int i = 1; i < array.length; i++) {
            if (comparator.compare(array[i - 1], array[i]) > 0) {
                throw new IllegalArgumentException(
                        "Binary search requires a sorted array."
                );
            }
        }
    }
}