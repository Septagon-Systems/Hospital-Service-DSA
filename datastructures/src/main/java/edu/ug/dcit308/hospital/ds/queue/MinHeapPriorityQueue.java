package edu.ug.dcit308.hospital.ds.queue;

import java.util.ArrayList;
import java.util.List;

public class MinHeapPriorityQueue<T extends Comparable<T>> {

    private final List<T> heap;

    // Constructor
    public MinHeapPriorityQueue() {
        heap = new ArrayList<>();
    }

    // Add an element to the priority queue
    public void enqueue(T value) {
        heap.add(value);
        heapifyUp(heap.size() - 1);
    }

    // Remove and return the smallest element
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority Queue is empty");
        }

        T min = heap.get(0);

        // Move last element to root
        T last = heap.remove(heap.size() - 1);

        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }

        return min;
    }

    // View the smallest element without removing it
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority Queue is empty");
        }

        return heap.get(0);
    }

    // Return number of elements
    public int size() {
        return heap.size();
    }

    // Check if queue is empty
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    // Restore heap property upwards
    private void heapifyUp(int index) {

        while (index > 0) {

            int parentIndex = (index - 1) / 2;

            // If child is smaller than parent, swap
            if (heap.get(index).compareTo(heap.get(parentIndex)) < 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }


    // Restore heap property downwards
    private void heapifyDown(int index) {

        int size = heap.size();

        while (true) {

            int smallest = index;

            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;


            // Check left child
            if (leftChild < size &&
                    heap.get(leftChild).compareTo(heap.get(smallest)) < 0) {
                smallest = leftChild;
            }


            // Check right child
            if (rightChild < size &&
                    heap.get(rightChild).compareTo(heap.get(smallest)) < 0) {
                smallest = rightChild;
            }


            // If parent is already smallest
            if (smallest == index) {
                break;
            }


            swap(index, smallest);
            index = smallest;
        }
    }


    // Swap two elements
    private void swap(int i, int j) {

        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);

    }


    // Optional: display heap
    public void printHeap() {
        System.out.println(heap);
    }
}