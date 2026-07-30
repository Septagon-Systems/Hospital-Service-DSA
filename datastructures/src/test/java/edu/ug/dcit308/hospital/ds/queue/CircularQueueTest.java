package edu.ug.dcit308.hospital.ds.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class CircularQueueTest {

    private CircularQueue queue;

    @BeforeEach
    void setUp() {
        queue = new CircularQueue(3);
    }

    @Test
    @DisplayName("New queue should be empty with correct capacity")
    void testInitialState() {
        assertTrue(queue.isEmpty());
        assertFalse(queue.isFull());
        assertEquals(0, queue.size());
        assertEquals(3, queue.capacity());
    }

    @Test
    @DisplayName("Enqueue should fill queue and reject elements when full")
    void testEnqueueAndFull() {
        assertTrue(queue.enqueue("Patient 1"));
        assertTrue(queue.enqueue("Patient 2"));
        assertTrue(queue.enqueue("Patient 3"));

        // Queue is at capacity
        assertTrue(queue.isFull());
        assertEquals(3, queue.size());

        // Overflow attempt
        assertFalse(queue.enqueue("Patient 4"));
        assertEquals(3, queue.size());
    }

    @Test
    @DisplayName("Circular wrap-around logic reuses released array slots")
    void testCircularWrapAround() {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");

        // Dequeue one slot to free index 0
        assertEquals("A", queue.dequeue());
        assertFalse(queue.isFull());

        // Enqueue 'D' which should wrap around to index 0
        assertTrue(queue.enqueue("D"));
        assertTrue(queue.isFull());

        // Verify FIFO order maintained across wrap-around
        assertEquals("B", queue.dequeue());
        assertEquals("C", queue.dequeue());
        assertEquals("D", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    @DisplayName("Peek returns front element without removing it")
    void testPeek() {
        queue.enqueue("Emergency Task");
        assertEquals("Emergency Task", queue.peek());
        assertEquals(1, queue.size()); // Size remains unchanged
    }

    @Test
    @DisplayName("Clear should reset pointers and size")
    void testClear() {
        queue.enqueue("T1");
        queue.enqueue("T2");
        queue.clear();

        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
        assertFalse(queue.isFull());
    }

    @Test
    @DisplayName("Dequeue and peek on empty queue throw NoSuchElementException")
    void testEmptyExceptions() {
        assertThrows(NoSuchElementException.class, () -> queue.dequeue());
        assertThrows(NoSuchElementException.class, () -> queue.peek());
    }

    @Test
    @DisplayName("Constructor throws IllegalArgumentException for zero or negative capacity")
    void testInvalidCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new CircularQueue(0));
        assertThrows(IllegalArgumentException.class, () -> new CircularQueue(-5));
    }
}