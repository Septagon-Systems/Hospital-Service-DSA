package edu.ug.dcit308.hospital.algo.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class BinarySearchTest {

    @Test
    void findsElementInSortedArray() {
        Integer[] values = {2, 4, 6, 8, 10};
        BinarySearch binarySearch = new BinarySearch();

        int result = binarySearch.search(values, 8);

        assertEquals(3, result);
    }

    @Test
    void returnsMinusOneWhenElementIsNotFound() {
        Integer[] values = {2, 4, 6, 8, 10};
        BinarySearch binarySearch = new BinarySearch();

        int result = binarySearch.search(values, 7);

        assertEquals(-1, result);
    }

    @Test
    void rejectsUnsortedArray() {
        Integer[] values = {6, 2, 8, 4, 10};
        BinarySearch binarySearch = new BinarySearch();

        assertThrows(
                IllegalArgumentException.class,
                () -> binarySearch.search(values, 8)
        );
    }
}