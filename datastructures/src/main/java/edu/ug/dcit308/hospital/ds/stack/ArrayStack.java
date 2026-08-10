package edu.ug.dcit308.hospital.ds.stack;

public class ArrayStack<T> {

    private T[] elements;
    private int top;

    private static final int DEFAULT_CAPACITY = 10;


    @SuppressWarnings("unchecked")
    public ArrayStack() {

        elements = (T[]) new Object[DEFAULT_CAPACITY];

        top = -1;
    }


    // Add item to stack
    public void push(T item) {

        if (top == elements.length - 1) {
            resize();
        }


        elements[++top] = item;
    }



    // Remove and return top item
    public T pop() {

        if (isEmpty()) {
            throw new IllegalStateException("Stack underflow");
        }


        T item = elements[top];

        elements[top] = null;

        top--;


        return item;
    }



    // View top item without removing
    public T peek() {

        if (isEmpty()) {
            throw new IllegalStateException("Stack underflow");
        }


        return elements[top];
    }



    // Check if stack is empty
    public boolean isEmpty() {

        return top == -1;
    }



    // Number of elements
    public int size() {

        return top + 1;
    }



    // Increase capacity
    @SuppressWarnings("unchecked")
    private void resize() {

        T[] newElements = (T[]) new Object[elements.length * 2];


        for (int i = 0; i <= top; i++) {

            newElements[i] = elements[i];

        }


        elements = newElements;
    }
}