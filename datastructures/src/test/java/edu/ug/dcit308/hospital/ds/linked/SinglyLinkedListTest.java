package edu.ug.dcit308.hospital.ds.linked;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class SinglyLinkedListTest {

    private SinglyLinkedList<String> list;

    @BeforeEach
    void setUp() {
        list = new SinglyLinkedList<>();
    }

    @Test
    void testAddFirstAndGet() {
        list.addFirst("Alpha");
        list.addFirst("Beta");

        assertEquals(2, list.size());
        assertEquals("Beta", list.get(0));
        assertEquals("Alpha", list.get(1));
    }

    @Test
    void testAddLast() {
        list.addLast("Alpha");
        list.addLast("Beta");

        assertEquals(2, list.size());
        assertEquals("Alpha", list.get(0));
        assertEquals("Beta", list.get(1));
    }

    @Test
    void testRemoveFirst() {
        list.addLast("Alpha");
        list.addLast("Beta");

        assertEquals("Alpha", list.removeFirst());
        assertEquals(1, list.size());
        assertEquals("Beta", list.get(0));
    }

    @Test
    void testRemoveLast() {
        list.addLast("Alpha");
        list.addLast("Beta");

        assertEquals("Beta", list.removeLast());
        assertEquals(1, list.size());
        assertEquals("Alpha", list.get(0));
    }

    @Test
    void testRemoveFromEmptyThrowsException() {
        assertThrows(NoSuchElementException.class, () -> list.removeFirst());
        assertThrows(NoSuchElementException.class, () -> list.removeLast());
    }

    @Test
    void testGetOutOfBoundsThrowsException() {
        list.addLast("Alpha");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    void testClear() {
        list.addLast("Alpha");
        list.addLast("Beta");
        list.clear();

        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }
}