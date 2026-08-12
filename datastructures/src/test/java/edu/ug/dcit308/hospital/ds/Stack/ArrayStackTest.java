package edu.ug.dcit308.hospital.ds.stack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArrayStackTest {


    @Test
    void testPush() {

        ArrayStack<Integer> stack = new ArrayStack<>();


        stack.push(10);
        stack.push(20);


        assertEquals(2, stack.size());
        assertEquals(20, stack.peek());
    }



    @Test
    void testPop() {

        ArrayStack<String> stack = new ArrayStack<>();


        stack.push("A");
        stack.push("B");
        stack.push("C");


        assertEquals("C", stack.pop());
        assertEquals("B", stack.pop());
        assertEquals("A", stack.pop());
    }



    @Test
    void testPeekDoesNotRemoveItem() {

        ArrayStack<Integer> stack = new ArrayStack<>();


        stack.push(50);


        assertEquals(50, stack.peek());

        assertEquals(1, stack.size());
    }



    @Test
    void testIsEmpty() {

        ArrayStack<Integer> stack = new ArrayStack<>();


        assertTrue(stack.isEmpty());


        stack.push(1);


        assertFalse(stack.isEmpty());
    }



    @Test
    void testSize() {

        ArrayStack<Integer> stack = new ArrayStack<>();


        assertEquals(0, stack.size());


        stack.push(5);
        stack.push(10);


        assertEquals(2, stack.size());


        stack.pop();


        assertEquals(1, stack.size());
    }



    @Test
    void testStackUnderflow() {

        ArrayStack<Integer> stack = new ArrayStack<>();


        assertThrows(
                IllegalStateException.class,
                stack::pop
        );


        assertThrows(
                IllegalStateException.class,
                stack::peek
        );
    }



    @Test
    void testStackCapacityResize() {

        ArrayStack<Integer> stack = new ArrayStack<>();


        // Exceed default capacity of 10

        for(int i = 0; i < 25; i++) {

            stack.push(i);

        }


        assertEquals(25, stack.size());

        assertEquals(24, stack.peek());
    }
}