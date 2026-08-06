package edu.ug.dcit308.hospital.algo.sort;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class SelectionSortTest {

    @Test
    void sortsNormalArray() {
        Integer[] values = {64, 25, 12, 22, 11};
        SelectionSort<Integer> sorter = new SelectionSort<>();

        sorter.sort(values);

        assertArrayEquals(
                new Integer[]{11, 12, 22, 25, 64},
                values
        );

        assertEquals(10, sorter.getComparisonCount());
        assertEquals(3, sorter.getSwapCount());
    }

    @Test
    void sortsAlreadySortedArray() {
        Integer[] values = {1, 2, 3, 4, 5};
        SelectionSort<Integer> sorter = new SelectionSort<>();

        sorter.sort(values);

        assertArrayEquals(
                new Integer[]{1, 2, 3, 4, 5},
                values
        );

        assertEquals(10, sorter.getComparisonCount());
        assertEquals(0, sorter.getSwapCount());
    }

    @Test
    void sortsReverseSortedArray() {
        Integer[] values = {5, 4, 3, 2, 1};
        SelectionSort<Integer> sorter = new SelectionSort<>();

        sorter.sort(values);

        assertArrayEquals(
                new Integer[]{1, 2, 3, 4, 5},
                values
        );

        assertEquals(10, sorter.getComparisonCount());
        assertEquals(2, sorter.getSwapCount());
    }
}