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
     * Inserts an element at the rear of the queue. O(1)
     *
     * @param element The item to add.
     * @return true if successfully added, false if the queue is full.
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
     * Returns the element at the front of the queue without removing it. O(1)
     *
     * @return The front element.
     * @throws NoSuchElementException if the queue is empty.
     */
    public Object peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return data[front];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }

    public void clear() {
        for (int i = 0; i < capacity; i++) {
            data[i] = null;
        }
        this.front = 0;
        this.rear = 0;
        this.size = 0;
    }
}