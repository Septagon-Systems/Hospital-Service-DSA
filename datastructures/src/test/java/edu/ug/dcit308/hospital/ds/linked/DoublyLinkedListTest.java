package edu.ug.dcit308.hospital.ds.linked;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class DoublyLinkedListTest {

    private DoublyLinkedList<String> list;

    @BeforeEach
    void setUp() {
        list = new DoublyLinkedList<>();
    }

    @Test
    void testAddFirstAndAddLast() {
        list.addFirst("Center");
        list.addFirst("Front");
        list.addLast("Back");

        assertEquals(3, list.size());
        assertEquals("Front", list.get(0));
        assertEquals("Center", list.get(1));
        assertEquals("Back", list.get(2));
    }

    @Test
    void testRemoveFirstAndRemoveLast() {
        list.addLast("One");
        list.addLast("Two");
        list.addLast("Three");

        assertEquals("One", list.removeFirst());
        assertEquals("Three", list.removeLast());
        assertEquals(1, list.size());
        assertEquals("Two", list.get(0));
    }

    @Test
    void testGetOptimization() {
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        list.addLast("D");

        assertEquals("A", list.get(0));
        assertEquals("D", list.get(3));
    }

    @Test
    void testRemoveFromEmptyThrowsException() {
        assertThrows(NoSuchElementException.class, () -> list.removeFirst());
        assertThrows(NoSuchElementException.class, () -> list.removeLast());
    }

    @Test
    void testGetOutOfBoundsThrowsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
    }

    @Test
    void testClear() {
        list.addLast("Data");
        list.clear();

        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }
}