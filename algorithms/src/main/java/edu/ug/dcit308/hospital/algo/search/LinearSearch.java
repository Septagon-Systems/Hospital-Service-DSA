package edu.ug.dcit308.hospital.algo.search;

import java.util.function.Predicate;

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

    /**
     * Searches any custom Iterable-backed data structure (linked lists, custom
     * queues, etc.) so pod-built structures don't need array conversion first.
     */
    public static <T> int search(Iterable<T> list, T target) {
        if (list == null) {
            throw new IllegalArgumentException("List must not be null");
        }
        int index = 0;
        for (T item : list) {
            if (item == null ? target == null : item.equals(target)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * Searches for the first element matching an arbitrary criterion, rather
     * than equality against a fixed target.
     */
    public static <T> int searchBy(T[] array, Predicate<T> criterion) {
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null");
        }
        if (criterion == null) {
            throw new IllegalArgumentException("Criterion must not be null");
        }
        for (int i = 0; i < array.length; i++) {
            if (criterion.test(array[i])) {
                return i;
            }
        }
        return -1;
    }
}
