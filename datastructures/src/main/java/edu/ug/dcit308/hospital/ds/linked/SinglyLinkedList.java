package edu.ug.dcit308.hospital.ds.linked;

import java.util.NoSuchElementException;

public class SinglyLinkedList<T> {
    private static class Node<T> {
        T data;
        Node<T> next;

        /**
         * Creates a node containing the specified data.
         *
         * @param data the value stored in the node
         */
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    /**
     * Creates an empty singly linked list.
     */
    public SinglyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Adds an element to the beginning of the list.
     *
     * @param element the element to add
     */
    public void addFirst(T element) {
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
        size++;
    }

    /**
     * Appends an element to the end of the list.
     *
     * @param element the element to append
     */
    public void addLast(T element) {
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    /**
     * Removes the first element from the list.
     *
     * @return the removed element
     * @throws NoSuchElementException if the list is empty
     */
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        T data = head.data;
        head = head.next;
        size--;
        if (isEmpty()) {
            tail = null;
        }
        return data;
    }

    /**
     * Removes and returns the last element in the list.
     *
     * @return the removed last element
     * @throws NoSuchElementException if the list is empty
     */
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        if (size == 1) {
            return removeFirst();
        }

        Node<T> current = head;
        while (current.next != tail) {
            current = current.next;
        }

        T data = tail.data;
        tail = current;
        tail.next = null;
        size--;
        return data;
    }

    /**
     * Retrieves the element at the specified index.
     *
     * @param index the zero-based position of the element
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if the index is outside the list bounds
     */
    public T get(int index) {
        checkIndex(index);
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    /**
     * Determines whether the list contains the specified element.
     *
     * @param element the element to search for
     * @return {@code true} if the element is present, {@code false} otherwise
     */
    public boolean contains(T element) {
        Node<T> current = head;
        while (current != null) {
            if ((element == null && current.data == null) || (element != null && element.equals(current.data))) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Gets the number of elements in the list.
     *
     * @return the number of elements in the list
     */
    public int size() {
        return size;
    }

    /**
     * Determines whether the list contains no elements.
     *
     * @return {@code true} if the list is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all elements from the list.
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Validates that an index refers to an element in the list.
     *
     * @param index the index to validate
     * @throws IndexOutOfBoundsException if the index is negative or greater than or equal to the list size
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}