package edu.ug.dcit308.hospital.algo.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinearSearchTest {

    @Test
    void testFindsElementInMiddle() {
        Integer[] array = {45, 15, 20, 10};
        assertEquals(1, LinearSearch.search(array, 15));
    }

    @Test
    void testFindsFirstAndLastElement() {
        Integer[] array = {45, 15, 20, 10};
        assertEquals(0, LinearSearch.search(array, 45));
        assertEquals(3, LinearSearch.search(array, 10));
    }

    @Test
    void testTargetNotPresentReturnsMinusOne() {
        Integer[] array = {45, 15, 20, 10};
        assertEquals(-1, LinearSearch.search(array, 99));
    }

    @Test
    void testEmptyArrayReturnsMinusOne() {
        Integer[] array = {};
        assertEquals(-1, LinearSearch.search(array, 5));
    }

    @Test
    void testNullArrayThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> LinearSearch.search((Integer[]) null, 5));
    }

    @Test
    void testPrimitiveIntOverload() {
        int[] array = {45, 15, 20, 10};
        assertEquals(2, LinearSearch.search(array, 20));
        assertEquals(-1, LinearSearch.search(array, 99));
    }

    @Test
    void testGenericTypeWithStrings() {
        String[] array = {"Korle Bu", "Ridge", "Komfo Anokye"};
        assertEquals(2, LinearSearch.search(array, "Komfo Anokye"));
    }
}
