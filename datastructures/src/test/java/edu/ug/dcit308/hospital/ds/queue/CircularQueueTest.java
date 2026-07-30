package edu.ug.dcit308.hospital.ds.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CircularQueueTest {

    private CircularQueue<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new CircularQueue<>(3);
    }

    @Test
    void testEnqueueAndDequeue() {
        assertTrue(queue.enqueue(10));
        assertTrue(queue.enqueue(20));

        assertEquals(10, queue.peek());
        assertEquals(10, queue.dequeue());
        assertEquals(20, queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    void testQueueFull() {
        assertTrue(queue.enqueue(1));
        assertTrue(queue.enqueue(2));
        assertTrue(queue.enqueue(3));
        assertFalse(queue.enqueue(4));
        assertTrue(queue.isFull());
    }

    @Test
    void testCircularWrapAround() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        assertEquals(1, queue.dequeue());
        assertTrue(queue.enqueue(4));

        assertEquals(2, queue.dequeue());
        assertEquals(3, queue.dequeue());
        assertEquals(4, queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    void testEmptyQueueExceptions() {
        assertThrows(NoSuchElementException.class, () -> queue.dequeue());
        assertThrows(NoSuchElementException.class, () -> queue.peek());
    }

    @Test
    void testInvalidCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new CircularQueue<Integer>(0));
        assertThrows(IllegalArgumentException.class, () -> new CircularQueue<Integer>(-5));
    }
}