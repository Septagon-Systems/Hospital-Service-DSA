package edu.ug.dcit308.hospital.ds.linked;

import java.util.NoSuchElementException;

public class DoublyLinkedList {

    private static class Node {
        Object data;
        Node next;
        Node prev;

        /**
         * Creates a node containing the specified data.
         *
         * @param data the value stored in the node
         */
        Node(Object data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    /**
     * Creates an empty doubly linked list.
     */
    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Inserts an element at the beginning of the list.
     *
     * @param element the element to insert
     */
    public void addFirst(Object element) {
        Node newNode = new Node(element);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    /**
     * Appends an element to the end of the list.
     *
     * @param element the element to append
     */
    public void addLast(Object element) {
        Node newNode = new Node(element);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    /**
     * Removes and returns the first element in the list.
     *
     * @return the removed first element
     * @throws NoSuchElementException if the list is empty
     */
    public Object removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        Object data = head.data;
        if (size == 1) {
            head = tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        size--;
        return data;
    }

    /**
     * Removes the last element from the list.
     *
     * @return the removed element
     * @throws NoSuchElementException if the list is empty
     */
    public Object removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        Object data = tail.data;
        if (size == 1) {
            head = tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
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
    public Object get(int index) {
        checkIndex(index);
        Node current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current.data;
    }

    /**
     * Determines whether the list contains a specified element.
     *
     * @param element the element to search for
     * @return {@code true} if the element is present, {@code false} otherwise
     */
    public boolean contains(Object element) {
        Node current = head;
        while (current != null) {
            if ((element == null && current.data == null) || (element != null && element.equals(current.data))) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Determines the number of elements in the list.
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