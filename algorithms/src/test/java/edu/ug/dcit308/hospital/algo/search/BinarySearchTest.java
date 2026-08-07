package edu.ug.dcit308.hospital.algo.search;

import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void searchesGenericStringArray() {
        String[] values = {
                "Accra",
                "Kumasi",
                "Tamale",
                "Tema"
        };

        BinarySearch binarySearch = new BinarySearch();

        int result = binarySearch.search(values, "Tamale");

        assertEquals(2, result);
    }

    @Test
    void searchesUsingComparator() {
        String[] values = {
                "Korle Bu",
                "Ridge",
                "Tema General"
        };

        BinarySearch binarySearch = new BinarySearch();

        int result = binarySearch.search(
                values,
                "Ridge",
                Comparator.naturalOrder()
        );

        assertEquals(1, result);
    }

    @Test
    void rejectsNullArray() {
        BinarySearch binarySearch = new BinarySearch();

        assertThrows(
                IllegalArgumentException.class,
                () -> binarySearch.search(
                        (Integer[]) null,
                        8
                )
        );
    }

    @Test
    void rejectsNullKey() {
        Integer[] values = {2, 4, 6, 8, 10};
        BinarySearch binarySearch = new BinarySearch();

        assertThrows(
                IllegalArgumentException.class,
                () -> binarySearch.search(values, null)
        );
    }

    @Test
    void rejectsNullComparator() {
        Integer[] values = {2, 4, 6, 8, 10};
        BinarySearch binarySearch = new BinarySearch();

        assertThrows(
                IllegalArgumentException.class,
                () -> binarySearch.search(values, 8, null)
        );
    }

    @Test
    void rejectsNullElementWhenEncountered() {
        Integer[] values = {2, 4, null, 8, 10};
        BinarySearch binarySearch = new BinarySearch();

        assertThrows(
                IllegalArgumentException.class,
                () -> binarySearch.search(values, 8)
        );
    }

    @Test
    void returnsMinusOneForEmptyArray() {
        Integer[] values = {};
        BinarySearch binarySearch = new BinarySearch();

        assertEquals(-1, binarySearch.search(values, 8));
    }
}