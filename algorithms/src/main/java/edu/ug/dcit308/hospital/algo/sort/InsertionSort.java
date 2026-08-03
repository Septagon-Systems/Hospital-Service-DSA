package edu.ug.dcit308.hospital.algo.sort;

/**
 * In-place, stable Insertion Sort implementation.
 *
 * <p><b>Complexity summary</b>:
 * <ul>
 *   <li>Best case   : O(n)   – already sorted array (zero shifts)</li>
 *   <li>Average case: O(n²)  – random input</li>
 *   <li>Worst case  : O(n²)  – reverse-sorted array</li>
 *   <li>Space       : O(1)   – in-place, only a single key variable</li>
 * </ul>
 *
 * <p>Insertion Sort is efficient on small or nearly-sorted datasets and is
 * therefore used internally by hybrid algorithms (e.g. Timsort) for short
 * runs. It is the natural choice when new records arrive one-by-one and must
 * be kept in sorted order at all times, matching hospital triage admission
 * patterns where patient urgency scores are inserted into an ordered list as
 * patients check in.
 *
 * <p><b>No {@code java.util} collection classes are used</b> – plain arrays
 * only, as required by the project brief.
 */
public final class InsertionSort {

    /** Utility class – no instances. */
    private InsertionSort() {}

    // -----------------------------------------------------------------------
    // Generic overload – works with any Comparable type (String, Integer, …)
    // -----------------------------------------------------------------------

    /**
     * Sorts {@code array} in ascending order using Insertion Sort.
     *
     * <p>The sort is <em>stable</em>: equal elements retain their original
     * relative order.
     *
     * @param <T>   element type; must implement {@link Comparable}
     * @param array the array to be sorted in-place; must not be {@code null}
     * @throws IllegalArgumentException if {@code array} is {@code null}
     */
    public static <T extends Comparable<T>> void sort(T[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null");
        }
        // Outer loop: grow the sorted prefix one position at a time.
        // Invariant: array[0..i-1] is sorted at the start of each iteration.
        for (int i = 1; i < array.length; i++) {
            T key = array[i];          // element to be inserted
            int j = i - 1;

            // Shift elements of array[0..i-1] that are greater than key
            // one position to the right.
            while (j >= 0 && array[j].compareTo(key) > 0) {
                array[j + 1] = array[j];
                j--;
            }

            // Place key immediately after the last element that is ≤ key
            // (or at index 0 if all preceding elements were shifted).
            array[j + 1] = key;
        }
    }

    // -----------------------------------------------------------------------
    // int[] primitive overload – avoids boxing overhead for int arrays
    // -----------------------------------------------------------------------

    /**
     * Sorts a primitive {@code int[]} in ascending order using Insertion Sort.
     *
     * @param array the array to be sorted in-place; must not be {@code null}
     * @throws IllegalArgumentException if {@code array} is {@code null}
     */
    public static void sort(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null");
        }
        for (int i = 1; i < array.length; i++) {
            int key = array[i];
            int j = i - 1;

            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
            }

            array[j + 1] = key;
        }
    }

    // -----------------------------------------------------------------------
    // insertSorted – insert a single new element into an already-sorted
    // prefix of length n (indices 0..n-1) so that sortedness is maintained.
    // -----------------------------------------------------------------------

    /**
     * Inserts the element currently at {@code array[n]} into the sorted prefix
     * {@code array[0..n-1]}, extending the sorted region to {@code array[0..n]}.
     *
     * <p>This is the inner "one-step" routine of Insertion Sort and is also
     * useful standalone when a record arrives and must be placed into a
     * partially sorted buffer without re-sorting the whole buffer.
     *
     * <pre>
     * Example:
     *   array = [10, 20, 45, 15, …]   n = 3  (sorted prefix length)
     *   key   = array[3] = 15
     *   After call → [10, 15, 20, 45, …]
     * </pre>
     *
     * @param <T>   element type; must implement {@link Comparable}
     * @param array backing array that holds the sorted prefix in [0, n-1]
     *              and the new element at index {@code n}
     * @param n     length of the currently sorted prefix; the element to
     *              insert is at {@code array[n]}
     * @throws IllegalArgumentException  if {@code array} is {@code null}
     * @throws IndexOutOfBoundsException if {@code n < 0} or {@code n >= array.length}
     */
    public static <T extends Comparable<T>> void insertSorted(T[] array, int n) {
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null");
        }
        if (n < 0 || n >= array.length) {
            throw new IndexOutOfBoundsException(
                    "n must satisfy 0 <= n < array.length, got n=" + n);
        }
        T key = array[n];
        int j = n - 1;

        while (j >= 0 && array[j].compareTo(key) > 0) {
            array[j + 1] = array[j];
            j--;
        }

        array[j + 1] = key;
    }
}
