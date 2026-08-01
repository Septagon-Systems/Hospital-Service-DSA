package edu.ug.dcit308.hospital.ds.deque;

public class ArrayDeque<T> {

    private T[] elements;
    private int front;
    private int rear;
    private int size;

    private static final int DEFAULT_CAPACITY = 10;


    @SuppressWarnings("unchecked")
    public ArrayDeque() {
        elements = (T[]) new Object[DEFAULT_CAPACITY];
        front = 0;
        rear = 0;
        size = 0;
    }


    // Add item to the front
    public void addFirst(T item) {

        resizeIfNeeded();

        front = (front - 1 + elements.length) % elements.length;

        elements[front] = item;

        size++;
    }


    // Add item to the back
    public void addLast(T item) {

        resizeIfNeeded();

        elements[rear] = item;

        rear = (rear + 1) % elements.length;

        size++;
    }


    // Remove item from front
    public T removeFirst() {

        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty");
        }


        T item = elements[front];

        elements[front] = null;

        front = (front + 1) % elements.length;

        size--;

        return item;
    }


    // Remove item from back
    public T removeLast() {

        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty");
        }


        rear = (rear - 1 + elements.length) % elements.length;

        T item = elements[rear];

        elements[rear] = null;

        size--;

        return item;
    }


    // Get first element without removing
    public T getFirst() {

        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty");
        }

        return elements[front];
    }


    // Get last element without removing
    public T getLast() {

        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty");
        }

        int lastIndex = (rear - 1 + elements.length) % elements.length;

        return elements[lastIndex];
    }


    // Check if empty
    public boolean isEmpty() {
        return size == 0;
    }


    // Return size
    public int size() {
        return size;
    }


    // Double capacity when full
    @SuppressWarnings("unchecked")
    private void resizeIfNeeded() {

        if (size == elements.length) {

            T[] newElements = (T[]) new Object[elements.length * 2];


            for (int i = 0; i < size; i++) {

                newElements[i] = elements[(front + i) % elements.length];

            }


            elements = newElements;

            front = 0;

            rear = size;
        }
    }
}