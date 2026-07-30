package edu.ug.dcit308.hospital.ds.queue;

import java.util.NoSuchElementException;

public class CircularQueue {

    private final Object[] data;
    private int front;
    private int rear;
    private int size;
    private final int capacity;

    /**
     * Constructs a Circular Queue with a fixed maximum capacity.
     *
     * @param capacity Maximum number of elements the queue can hold.
     */
    public CircularQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.capacity = capacity;
        this.data = new Object[capacity];
        this.front = 0;
        this.rear = 0;
        this.size = 0;
    }

    /**
     * Adds an element to the rear of the queue.
     *
     * @param element the element to add
     * @return {@code true} if the element was added, {@code false} if the queue is full
     */
    public boolean enqueue(Object element) {
        if (isFull()) {
            return false;
        }
        data[rear] = element;
        rear = (rear + 1) % capacity;
        size++;
        return true;
    }

    /**
     * Removes and returns the element at the front of the queue. O(1)
     *
     * @return The front element.
     * @throws NoSuchElementException if the queue is empty.
     */
    public Object dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        Object removed = data[front];
        data[front] = null; // Prevent memory leak / allow GC
        front = (front + 1) % capacity;
        size--;
        return removed;
    }

    /**
     * Inspects the element at the front of the queue without removing it.
     *
     * @return the front element
     * @throws NoSuchElementException if the queue is empty
     */
    public Object peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return data[front];
    }

    /**
     * Determines whether the queue contains no elements.
     *
     * @return {@code true} if the queue is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Determines whether the queue has reached its fixed capacity.
     *
     * @return {@code true} if the queue is full, {@code false} otherwise
     */
    public boolean isFull() {
        return size == capacity;
    }

    /**
     * Reports the number of elements currently stored in the queue.
     *
     * @return the current number of elements
     */
    public int size() {
        return size;
    }

    /**
     * Retrieves the maximum number of elements this queue can hold.
     *
     * @return the queue's fixed capacity
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Removes all elements from the queue and resets its state.
     */
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            data[i] = null;
        }
        this.front = 0;
        this.rear = 0;
        this.size = 0;
    }
}