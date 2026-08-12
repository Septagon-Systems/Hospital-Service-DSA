package edu.ug.dcit308.hospital.algo.sort;

import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link MergeSort}.
 */
class MergeSortTest {

    @Test
    void testSortNormalIntegerArray() {
        Integer[] arr = {38, 12, 27, 5};
        MergeSort.sort(arr);
        assertArrayEquals(new Integer[]{5, 12, 27, 38}, arr);
    }

    @Test
    void testSortSingleElement() {
        Integer[] arr = {42};
        MergeSort.sort(arr);
        assertArrayEquals(new Integer[]{42}, arr);
    }

    @Test
    void testSortEmptyArray() {
        Integer[] arr = {};
        MergeSort.sort(arr);
        assertArrayEquals(new Integer[]{}, arr);
    }

    @Test
    void testSortDuplicateKeys() {
        Integer[] arr = {5, 3, 5, 1, 3};
        MergeSort.sort(arr);
        assertArrayEquals(new Integer[]{1, 3, 3, 5, 5}, arr);
    }

    @Test
    void testSortAlreadySorted() {
        Integer[] arr = {1, 2, 3, 4, 5};
        MergeSort.sort(arr);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    void testSortReverseSorted() {
        Integer[] arr = {5, 4, 3, 2, 1};
        MergeSort.sort(arr);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    void testSortStringArrayWithComparator() {
        String[] arr = {"Tema", "Accra", "Kumasi"};
        MergeSort.sort(arr, Comparator.reverseOrder());
        assertArrayEquals(new String[]{"Tema", "Kumasi", "Accra"}, arr);
    }

    @Test
    void testSortPrimitiveIntArray() {
        int[] arr = {38, 12, 27, 5};
        MergeSort.sort(arr);
        assertArrayEquals(new int[]{5, 12, 27, 38}, arr);
    }

    @Test
    void testSortPrimitiveIntSingleElement() {
        int[] arr = {7};
        MergeSort.sort(arr);
        assertArrayEquals(new int[]{7}, arr);
    }

    @Test
    void testSortPrimitiveIntEmptyArray() {
        int[] arr = {};
        MergeSort.sort(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    void testSortNullArrayThrows() {
        assertThrows(IllegalArgumentException.class, () -> MergeSort.sort((Integer[]) null));
    }

    @Test
    void testSortNullPrimitiveArrayThrows() {
        assertThrows(IllegalArgumentException.class, () -> MergeSort.sort((int[]) null));
    }

    @Test
    void testSortNullComparatorThrows() {
        Integer[] arr = {1, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> MergeSort.sort(arr, null));
    }

    @Test
    void testSortIsStable() {
        Patient p1 = new Patient("A", 3);
        Patient p2 = new Patient("B", 1);
        Patient p3 = new Patient("C", 3);
        Patient p4 = new Patient("D", 2);

        Patient[] arr = {p1, p2, p3, p4};
        MergeSort.sort(arr);

        assertEquals("B", arr[0].tag);
        assertEquals("D", arr[1].tag);
        assertEquals("A", arr[2].tag);
        assertEquals("C", arr[3].tag);
    }

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
