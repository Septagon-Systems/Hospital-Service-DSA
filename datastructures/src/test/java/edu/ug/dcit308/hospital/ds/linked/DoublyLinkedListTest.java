package edu.ug.dcit308.hospital.ds.linked;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class DoublyLinkedListTest {

    private DoublyLinkedList list;

    @BeforeEach
    void setUp() {
        list = new DoublyLinkedList();
    }

    @Test
    @DisplayName("New doubly linked list should be empty")
    void testInitialState() {
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    @DisplayName("addFirst and addLast should maintain front and back ordering")
    void testAddFirstAndLast() {
        list.addLast("ICU Unit");
        list.addFirst("Emergency Ward");
        list.addLast("Pediatrics");

        assertEquals(3, list.size());
        assertEquals("Emergency Ward", list.get(0));
        assertEquals("ICU Unit", list.get(1));
        assertEquals("Pediatrics", list.get(2));
    }

    @Test
    @DisplayName("removeFirst and removeLast should manage head and tail pointers correctly")
    void testRemovals() {
        list.addLast("Patient A");
        list.addLast("Patient B");
        list.addLast("Patient C");

        assertEquals("Patient A", list.removeFirst());
        assertEquals("Patient C", list.removeLast());
        assertEquals(1, list.size());
        assertEquals("Patient B", list.get(0));
    }

    @Test
    @DisplayName("Bi-directional index optimization works for elements in both halves")
    void testGetTraversalOptimization() {
        for (int i = 0; i < 10; i++) {
            list.addLast("Record " + i);
        }

        // Test accessing early element (starts from head)
        assertEquals("Record 1", list.get(1));
        // Test accessing late element (starts from tail)
        assertEquals("Record 8", list.get(8));
    }

    @Test
    @DisplayName("Single element removal resets both head and tail pointers to null")
    void testSingleElementRemoval() {
        list.addLast("Single Node");
        assertEquals("Single Node", list.removeFirst());
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());

        list.addFirst("Another Node");
        assertEquals("Another Node", list.removeLast());
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("Exceptions should be thrown on empty removal or invalid indices")
    void testExceptions() {
        assertThrows(NoSuchElementException.class, () -> list.removeFirst());
        assertThrows(NoSuchElementException.class, () -> list.removeLast());
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    }
}