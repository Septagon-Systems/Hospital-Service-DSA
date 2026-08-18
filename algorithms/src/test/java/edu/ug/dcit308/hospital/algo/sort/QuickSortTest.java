package edu.ug.dcit308.hospital.algo.sort;

import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link QuickSort}.
 */
class QuickSortTest {

    @Test
    void testSortNormalIntegerArray() {
        Integer[] arr = {38, 12, 27, 5};
        QuickSort.sort(arr);
        assertArrayEquals(new Integer[]{5, 12, 27, 38}, arr);
    }

    @Test
    void testSortSingleElement() {
        Integer[] arr = {42};
        QuickSort.sort(arr);
        assertArrayEquals(new Integer[]{42}, arr);
    }

    @Test
    void testSortRandomArray() {
        Integer[] arr = {38, 27, 43, 3, 9, 82, 10};
        QuickSort.sort(arr);
        assertArrayEquals(new Integer[]{3, 9, 10, 27, 38, 43, 82}, arr);
    }

    @Test
    void testSortDuplicateKeys() {
        Integer[] arr = {5, 3, 5, 2, 5, 8, 3};
        QuickSort.sort(arr);
        assertArrayEquals(new Integer[]{2, 3, 3, 5, 5, 5, 8}, arr);
    }

    @Test
    void testSortAlreadySortedArray() {
        Integer[] arr = {1, 2, 3, 4, 5, 6, 7};
        QuickSort.sort(arr);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6, 7}, arr);
    }

    @Test
    void testSortReverseSortedArray() {
        Integer[] arr = {7, 6, 5, 4, 3, 2, 1};
        QuickSort.sort(arr);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6, 7}, arr);
    }

    @Test
    void testSortEmptyArray() {
        Integer[] arr = {};
        QuickSort.sort(arr);
        assertArrayEquals(new Integer[]{}, arr);
    }

    @Test
    void testSortNullArrayThrows() {
        assertThrows(IllegalArgumentException.class, () -> QuickSort.sort((Integer[]) null));
        assertThrows(IllegalArgumentException.class, () -> QuickSort.sort((int[]) null));
    }

    @Test
    void testSortNullComparatorThrows() {
        Integer[] arr = {1, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> QuickSort.sort(arr, null));
    }

    @Test
    void testSortStringArrayWithComparator() {
        String[] arr = {"Korle Bu", "37 Military", "Komfo Anokye", "Ridge"};
        QuickSort.sort(arr, Comparator.reverseOrder());
        assertArrayEquals(new String[]{"Ridge", "Korle Bu", "Komfo Anokye", "37 Military"}, arr);
    }

    @Test
    void testSortPrimitiveIntArray() {
        int[] arr = {38, 12, 27, 5};
        QuickSort.sort(arr);
        assertArrayEquals(new int[]{5, 12, 27, 38}, arr);
    }

    @Test
    void testSortPrimitiveIntEmptyArray() {
        int[] arr = {};
        QuickSort.sort(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    void testSortPrimitiveIntSingleElement() {
        int[] arr = {42};
        QuickSort.sort(arr);
        assertArrayEquals(new int[]{42}, arr);
    }

    @Test
    void testSortPrimitiveDuplicates() {
        int[] arr = {5, 3, 5, 2, 5, 8, 3};
        QuickSort.sort(arr);
        assertArrayEquals(new int[]{2, 3, 3, 5, 5, 5, 8}, arr);
    }
}