package edu.ug.dcit308.hospital.algo.search;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

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

    @Test
    void testReturnsFirstDuplicateAndFindsNullTarget() {
        assertEquals(0, LinearSearch.search(new Integer[]{15, 15}, 15));
        assertEquals(1, LinearSearch.search(new String[]{"Korle Bu", null}, null));
    }

    @Test
    void testPrimitiveNullArrayThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> LinearSearch.search((int[]) null, 5));
    }

    @Test
    void testFindsElementByCriterion() {
        String[] facilities = {"Korle Bu", "Komfo Anokye"};
        assertEquals(1, LinearSearch.searchBy(
                facilities, facility -> facility.startsWith("Komfo")));
    }

    @Test
    void testSearchByNullArgumentsThrowException() {
        String[] facilities = {"Korle Bu"};
        assertThrows(IllegalArgumentException.class,
                () -> LinearSearch.searchBy(null, facility -> true));
        assertThrows(IllegalArgumentException.class,
                () -> LinearSearch.searchBy(facilities, null));
    }

    @Test
    void testSearchOverIterableCustomStructure() {
        Iterable<String> facilities = new ArrayIterable<>("Korle Bu", "Ridge", "Komfo Anokye");

        assertEquals(2, LinearSearch.search(facilities, "Komfo Anokye"));
        assertEquals(-1, LinearSearch.search(facilities, "Tema General"));
    }

    @Test
    void testSearchOverIterableNullThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> LinearSearch.search((Iterable<String>) null, "Korle Bu"));
    }

    /**
     * Minimal Iterable stand-in for a pod-built structure (linked list, queue, etc.)
     * so this test doesn't depend on another pod's unmerged implementation.
     */
    private static final class ArrayIterable<T> implements Iterable<T> {
        private final T[] values;

        @SafeVarargs
        ArrayIterable(T... values) {
            this.values = values;
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                private int index = 0;

                @Override
                public boolean hasNext() {
                    return index < values.length;
                }

                @Override
                public T next() {
                    return values[index++];
                }
            };
        }
    }
}
