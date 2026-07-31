package edu.ug.dcit308.hospital.algo.search;

/**
 * Sequential (linear) search over an unsorted collection.
 * Runs in O(n) time and O(1) auxiliary space.
 */
public final class LinearSearch {

    private LinearSearch() {
    }

    public static <T> int search(T[] array, T target) {
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null");
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null ? target == null : array[i].equals(target)) {
                return i;
            }
        }
        return -1;
    }

    public static int search(int[] array, int target) {
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null");
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
}
