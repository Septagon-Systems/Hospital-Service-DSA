package edu.ug.dcit308.hospital.ds.linked;

import java.util.NoSuchElementException;

public class DoublyLinkedList {

    private static class Node {
        Object data;
        Node next;
        Node prev;

        Node(Object data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Inserts an element at the beginning of the list. O(1)
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
     * Appends an element to the end of the list. O(1)
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
     * Removes and returns the first element of the list. O(1)
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
     * Removes and returns the last element of the list. O(1)
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
     * Retrieves the element at the specified 0-based index.
     * Traverses from head or tail depending on index proximity. O(n/2)
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
     * Checks if a value exists in the list. O(n)
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

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}