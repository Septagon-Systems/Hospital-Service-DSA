package edu.ug.dcit308.hospital.ds.queue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MinHeapPriorityQueueTest {

    @Test
    void testEnqueueAndPeek() {

        MinHeapPriorityQueue<Integer> queue = new MinHeapPriorityQueue<>();

        queue.enqueue(10);
        queue.enqueue(5);
        queue.enqueue(20);
        queue.enqueue(2);

        assertEquals(2, queue.peek());
    }

    @Test
    void testDequeueReturnsMinimumElement() {

        MinHeapPriorityQueue<Integer> queue = new MinHeapPriorityQueue<>();

        queue.enqueue(10);
        queue.enqueue(3);
        queue.enqueue(7);
        queue.enqueue(1);

        assertEquals(1, queue.dequeue());
        assertEquals(3, queue.dequeue());
        assertEquals(7, queue.dequeue());
        assertEquals(10, queue.dequeue());
    }

    @Test
    void testSize() {

        MinHeapPriorityQueue<Integer> queue = new MinHeapPriorityQueue<>();

        assertEquals(0, queue.size());

        queue.enqueue(5);
        queue.enqueue(2);

        assertEquals(2, queue.size());
    }

    @Test
    void testIsEmpty() {

        MinHeapPriorityQueue<Integer> queue = new MinHeapPriorityQueue<>();

        assertTrue(queue.isEmpty());

        queue.enqueue(15);

        assertFalse(queue.isEmpty());
    }

    @Test
    void testRemoveFromEmptyQueueThrowsException() {

        MinHeapPriorityQueue<Integer> queue = new MinHeapPriorityQueue<>();

        assertThrows(
                IllegalStateException.class,
                queue::dequeue
        );
    }

    @Test
    void testPriorityOrdering() {

        MinHeapPriorityQueue<Integer> queue = new MinHeapPriorityQueue<>();

        queue.enqueue(50);
        queue.enqueue(10);
        queue.enqueue(30);
        queue.enqueue(5);

        int previous = Integer.MIN_VALUE;

        while (!queue.isEmpty()) {

            int current = queue.dequeue();

            assertTrue(current >= previous);

            previous = current;
        }
    }

    @Test
    void testEnqueueNullThrowsException() {

        MinHeapPriorityQueue<Integer> queue = new MinHeapPriorityQueue<>();

        assertThrows(
                IllegalArgumentException.class,
                () -> queue.enqueue(null)
        );
    }

    @Test
void testPeekOnEmptyQueueThrowsException() {

    MinHeapPriorityQueue<Integer> queue = new MinHeapPriorityQueue<>();

    assertThrows(
            IllegalStateException.class,
            queue::peek
    );
}


@Test
void testDequeueLastElementMakesQueueEmpty() {

    MinHeapPriorityQueue<Integer> queue = new MinHeapPriorityQueue<>();

    queue.enqueue(100);

    assertEquals(100, queue.dequeue());

    assertEquals(0, queue.size());
    assertTrue(queue.isEmpty());
}
}