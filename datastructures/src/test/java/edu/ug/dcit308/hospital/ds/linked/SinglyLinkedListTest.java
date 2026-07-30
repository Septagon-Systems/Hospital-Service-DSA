package edu.ug.dcit308.hospital.ds.linked;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class SinglyLinkedListTest {

    private SinglyLinkedList list;

    @BeforeEach
    void setUp() {
        list = new SinglyLinkedList();
    }

    @Test
    @DisplayName("New list should be empty and have size 0")
    void testNewListIsEmpty() {
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    @DisplayName("addFirst should insert elements at the head")
    void testAddFirst() {
        list.addFirst("Korle Bu");
        list.addFirst("Ridge Hospital");

        assertEquals(2, list.size());
        assertEquals("Ridge Hospital", list.get(0));
        assertEquals("Korle Bu", list.get(1));
        assertFalse(list.isEmpty());
    }

    @Test
    @DisplayName("addLast should insert elements at the tail")
    void testAddLast() {
        list.addLast("KATH");
        list.addLast("Tema General");

        assertEquals(2, list.size());
        assertEquals("KATH", list.get(0));
        assertEquals("Tema General", list.get(1));
    }

    @Test
    @DisplayName("removeFirst should remove and return head element")
    void testRemoveFirst() {
        list.addLast("Hospital A");
        list.addLast("Hospital B");

        Object removed = list.removeFirst();
        assertEquals("Hospital A", removed);
        assertEquals(1, list.size());
        assertEquals("Hospital B", list.get(0));
    }

    @Test
    @DisplayName("removeLast should remove and return tail element")
    void testRemoveLast() {
        list.addLast("Hospital A");
        list.addLast("Hospital B");
        list.addLast("Hospital C");

        Object removed = list.removeLast();
        assertEquals("Hospital C", removed);
        assertEquals(2, list.size());
        assertEquals("Hospital B", list.get(1));
    }

    @Test
    @DisplayName("contains should correctly find elements")
    void testContains() {
        list.addLast("Ambulance Unit 1");
        list.addLast("Ambulance Unit 2");

        assertTrue(list.contains("Ambulance Unit 1"));
        assertFalse(list.contains("Ambulance Unit 99"));
    }

    @Test
    @DisplayName("clear should reset the list")
    void testClear() {
        list.addLast("Item 1");
        list.addLast("Item 2");
        list.clear();

        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    @DisplayName("Exceptions should throw on empty operations or invalid index")
    void testExceptions() {
        assertThrows(NoSuchElementException.class, () -> list.removeFirst());
        assertThrows(NoSuchElementException.class, () -> list.removeLast());
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    }
}