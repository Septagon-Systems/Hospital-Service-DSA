package edu.ug.dcit308.hospital.ds.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class DynamicArrayListTest {

    private DynamicArrayList<String> list;

    @BeforeEach
    void setUp() {
        list = new DynamicArrayList<>();
    }

    @Test
    void testAddAndGet() {
        list.add("Alpha");
        list.add("Beta");

        assertEquals(2, list.size());
        assertEquals("Alpha", list.get(0));
        assertEquals("Beta", list.get(1));
    }

    @Test
    void testAddAtIndexShiftsElements() {
        list.add("Alpha");
        list.add("Gamma");
        list.add(1, "Beta");

        assertEquals(3, list.size());
        assertEquals("Alpha", list.get(0));
        assertEquals("Beta", list.get(1));
        assertEquals("Gamma", list.get(2));
    }

    @Test
    void testSetReplacesAndReturnsOldValue() {
        list.add("Alpha");
        String old = list.set(0, "Beta");

        assertEquals("Alpha", old);
        assertEquals("Beta", list.get(0));
        assertEquals(1, list.size());
    }

    @Test
    void testRemoveShiftsElements() {
        list.add("Alpha");
        list.add("Beta");
        list.add("Gamma");

        String removed = list.remove(1);

        assertEquals("Beta", removed);
        assertEquals(2, list.size());
        assertEquals("Alpha", list.get(0));
        assertEquals("Gamma", list.get(1));
    }

    @Test
    void testResizesBeyondDefaultCapacity() {
        for (int i = 0; i < 25; i++) {
            list.add("Item" + i);
        }

        assertEquals(25, list.size());
        assertEquals("Item0", list.get(0));
        assertEquals("Item24", list.get(24));
    }

    @Test
    void testGetOutOfBoundsThrowsException() {
        list.add("Alpha");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> new DynamicArrayList<String>().get(0));
    }

    @Test
    void testAddAtInvalidIndexThrowsException() {
        list.add("Alpha");
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, "X"));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(2, "X"));
    }

    @Test
    void testRemoveFromEmptyThrowsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
    }

    @Test
    void testConstructorRejectsInvalidCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new DynamicArrayList<String>(0));
        assertThrows(IllegalArgumentException.class, () -> new DynamicArrayList<String>(-5));
    }

    @Test
    void testClear() {
        list.add("Alpha");
        list.add("Beta");
        list.clear();

        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void testIteratorVisitsElementsInOrder() {
        list.add("Alpha");
        list.add("Beta");
        list.add("Gamma");

        StringBuilder visited = new StringBuilder();
        for (String element : list) {
            visited.append(element).append(",");
        }

        assertEquals("Alpha,Beta,Gamma,", visited.toString());
    }

    @Test
    void testIteratorOnEmptyListHasNoElements() {
        Iterator<String> iterator = list.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    void testIteratorNextPastEndThrowsException() {
        list.add("Alpha");
        Iterator<String> iterator = list.iterator();
        iterator.next();

        assertThrows(NoSuchElementException.class, iterator::next);
    }
}
