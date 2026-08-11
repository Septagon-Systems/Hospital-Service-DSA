package edu.ug.dcit308.hospital.ds.deque;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArrayDequeTest {


    @Test
    void testAddFirst() {

        ArrayDeque<Integer> deque = new ArrayDeque<>();

        deque.addFirst(10);
        deque.addFirst(20);
        deque.addFirst(30);


        assertEquals(30, deque.getFirst());
        assertEquals(10, deque.getLast());
    }



    @Test
    void testAddLast() {

        ArrayDeque<Integer> deque = new ArrayDeque<>();

        deque.addLast(10);
        deque.addLast(20);
        deque.addLast(30);


        assertEquals(10, deque.getFirst());
        assertEquals(30, deque.getLast());
    }



    @Test
    void testRemoveFirst() {

        ArrayDeque<String> deque = new ArrayDeque<>();

        deque.addLast("A");
        deque.addLast("B");
        deque.addLast("C");


        assertEquals("A", deque.removeFirst());
        assertEquals("B", deque.getFirst());
    }



    @Test
    void testRemoveLast() {

        ArrayDeque<String> deque = new ArrayDeque<>();

        deque.addLast("A");
        deque.addLast("B");
        deque.addLast("C");


        assertEquals("C", deque.removeLast());
        assertEquals("B", deque.getLast());
    }



    @Test
    void testGetFirst() {

        ArrayDeque<Integer> deque = new ArrayDeque<>();

        deque.addLast(100);
        deque.addLast(200);


        assertEquals(100, deque.getFirst());
    }



    @Test
    void testGetLast() {

        ArrayDeque<Integer> deque = new ArrayDeque<>();

        deque.addLast(100);
        deque.addLast(200);


        assertEquals(200, deque.getLast());
    }



    @Test
    void testSize() {

        ArrayDeque<Integer> deque = new ArrayDeque<>();

        assertEquals(0, deque.size());

        deque.addFirst(1);
        deque.addLast(2);


        assertEquals(2, deque.size());

        deque.removeFirst();

        assertEquals(1, deque.size());
    }



    @Test
    void testIsEmpty() {

        ArrayDeque<Integer> deque = new ArrayDeque<>();

        assertTrue(deque.isEmpty());


        deque.addLast(5);


        assertFalse(deque.isEmpty());
    }



    @Test
    void testRemoveFromEmptyDequeThrowsException() {

        ArrayDeque<Integer> deque = new ArrayDeque<>();


        assertThrows(
                IllegalStateException.class,
                deque::removeFirst
        );


        assertThrows(
                IllegalStateException.class,
                deque::removeLast
        );
    }



    @Test
    void testCircularResize() {

        ArrayDeque<Integer> deque = new ArrayDeque<>();


        // Force resize
        for(int i = 0; i < 20; i++) {
            deque.addLast(i);
        }


        assertEquals(0, deque.getFirst());
        assertEquals(19, deque.getLast());
        assertEquals(20, deque.size());
    }
}