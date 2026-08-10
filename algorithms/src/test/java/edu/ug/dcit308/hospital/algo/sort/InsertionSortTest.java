package edu.ug.dcit308.hospital.algo.sort;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link InsertionSort}.
 *
 * <p>Coverage targets (per issue #5 spec):
 * <ul>
 *   <li>Normal / general arrays</li>
 *   <li>Single-element array</li>
 *   <li>Arrays with duplicate keys</li>
 *   <li>Already-sorted and reverse-sorted arrays</li>
 *   <li>Empty array edge-case</li>
 *   <li>Null-guard preconditions</li>
 *   <li>{@link InsertionSort#insertSorted} standalone helper</li>
 *   <li>Primitive {@code int[]} overload</li>
 *   <li>Stability: equal elements must preserve original relative order</li>
 * </ul>
 */
class InsertionSortTest {

    // -----------------------------------------------------------------------
    // sort(T[] array) – generic Comparable overload
    // -----------------------------------------------------------------------

    @Test
    void testSortNormalIntegerArray() {
        Integer[] arr = {45, 15, 20, 10};
        InsertionSort.sort(arr);
        assertArrayEquals(new Integer[]{10, 15, 20, 45}, arr);
    }

    @Test
    void testSortSingleElement() {
        Integer[] arr = {42};
        InsertionSort.sort(arr);
        assertArrayEquals(new Integer[]{42}, arr);
    }

    @Test
    void testSortDuplicateKeys() {
        Integer[] arr = {5, 3, 5, 1, 3};
        InsertionSort.sort(arr);
        assertArrayEquals(new Integer[]{1, 3, 3, 5, 5}, arr);
    }

    @Test
    void testSortAlreadySorted() {
        Integer[] arr = {1, 2, 3, 4, 5};
        InsertionSort.sort(arr);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    void testSortReverseSorted() {
        Integer[] arr = {5, 4, 3, 2, 1};
        InsertionSort.sort(arr);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    void testSortEmptyArray() {
        Integer[] arr = {};
        InsertionSort.sort(arr);
        assertArrayEquals(new Integer[]{}, arr);
    }

    @Test
    void testSortStringArray() {
        String[] arr = {"Tema", "Accra", "Kumasi", "Accra"};
        InsertionSort.sort(arr);
        assertArrayEquals(new String[]{"Accra", "Accra", "Kumasi", "Tema"}, arr);
    }

    @Test
    void testSortNullArrayThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> InsertionSort.sort((Integer[]) null));
    }

    /**
     * Stability test: equal elements must appear in their original relative order
     * after sorting. We use wrapper objects with an identity tag so we can track
     * which "copy" of a duplicate came first.
     */
    @Test
    void testSortIsStable() {
        // Three patients with urgency score 3; tagged A, B, C in arrival order.
        // After sort by urgency, A must still precede B and B must precede C.
        Patient p1 = new Patient("A", 3);
        Patient p2 = new Patient("B", 1);
        Patient p3 = new Patient("C", 3);
        Patient p4 = new Patient("D", 2);
        Patient p5 = new Patient("E", 3);

        Patient[] arr = {p1, p2, p3, p4, p5};
        InsertionSort.sort(arr);

        // Expected order by urgency: p2(1), p4(2), p1(3), p3(3), p5(3)
        assertEquals("B", arr[0].tag);
        assertEquals("D", arr[1].tag);
        assertEquals("A", arr[2].tag);  // first arrival with score 3
        assertEquals("C", arr[3].tag);  // second arrival with score 3
        assertEquals("E", arr[4].tag);  // third arrival with score 3
    }

    // -----------------------------------------------------------------------
    // sort(int[] array) – primitive overload
    // -----------------------------------------------------------------------

    @Test
    void testSortPrimitiveIntArray() {
        int[] arr = {64, 25, 12, 22, 11};
        InsertionSort.sort(arr);
        assertArrayEquals(new int[]{11, 12, 22, 25, 64}, arr);
    }

    @Test
    void testSortPrimitiveSingleElement() {
        int[] arr = {7};
        InsertionSort.sort(arr);
        assertArrayEquals(new int[]{7}, arr);
    }

    @Test
    void testSortPrimitiveDuplicates() {
        int[] arr = {3, 1, 3, 2, 1};
        InsertionSort.sort(arr);
        assertArrayEquals(new int[]{1, 1, 2, 3, 3}, arr);
    }

    @Test
    void testSortPrimitiveNullThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> InsertionSort.sort((int[]) null));
    }

    @Test
    void testSortPrimitiveEmptyArray() {
        int[] arr = {};
        InsertionSort.sort(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    // -----------------------------------------------------------------------
    // insertSorted(T[] array, int n)
    // -----------------------------------------------------------------------

    @Test
    void testInsertSortedBasic() {
        // Sorted prefix: [10, 20, 45], new element at index 3: 15
        Integer[] arr = {10, 20, 45, 15};
        InsertionSort.insertSorted(arr, 3);
        assertArrayEquals(new Integer[]{10, 15, 20, 45}, arr);
    }

    @Test
    void testInsertSortedNewSmallest() {
        Integer[] arr = {20, 30, 40, 5};
        InsertionSort.insertSorted(arr, 3);
        assertArrayEquals(new Integer[]{5, 20, 30, 40}, arr);
    }

    @Test
    void testInsertSortedNewLargest() {
        Integer[] arr = {10, 20, 30, 99};
        InsertionSort.insertSorted(arr, 3);
        assertArrayEquals(new Integer[]{10, 20, 30, 99}, arr);
    }

    @Test
    void testInsertSortedSingleElementPrefix() {
        // Sorted prefix length = 1, element to insert at index 1
        Integer[] arr = {50, 10};
        InsertionSort.insertSorted(arr, 1);
        assertArrayEquals(new Integer[]{10, 50}, arr);
    }

    @Test
    void testInsertSortedNullArrayThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> InsertionSort.insertSorted(null, 0));
    }

    @Test
    void testInsertSortedNegativeNThrows() {
        Integer[] arr = {10, 20, 5};
        assertThrows(IndexOutOfBoundsException.class,
                () -> InsertionSort.insertSorted(arr, -1));
    }

    @Test
    void testInsertSortedNOutOfBoundsThrows() {
        Integer[] arr = {10, 20, 5};
        assertThrows(IndexOutOfBoundsException.class,
                () -> InsertionSort.insertSorted(arr, arr.length));
    }

    // -----------------------------------------------------------------------
    // Private helper – minimal Comparable wrapper for stability test
    // -----------------------------------------------------------------------

    private static class Patient implements Comparable<Patient> {
        final String tag;
        final int urgency;

        Patient(String tag, int urgency) {
            this.tag = tag;
            this.urgency = urgency;
        }

        @Override
        public int compareTo(Patient other) {
            return Integer.compare(this.urgency, other.urgency);
        }
    }
}
