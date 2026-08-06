# Counterexample & Boundary Case Analysis

## Case 1: Empty Queue Dequeue / Peek Precondition Violation

### Precondition

`dequeue()` and `peek()` require that the queue size $S > 0$ (`isEmpty() == false`).

### Violation Scenario

An operation calls `dequeue()` or `peek()` on a `CircularQueue` when `size == 0`.

### Handled Behavior

```java
if (isEmpty()) {
    throw new NoSuchElementException("Queue is empty");
}
```

## Case 2: Out of Bounds Indexing on Linked Lists

### Precondition

Indexed retrieval `get(index)` requires $0 \le \text{index} < \text{size}$.

### Violation Scenario

Calling `get(-1)` or `get(size)` on a `SinglyLinkedList` or `DoublyLinkedList`.

### Handled Behavior

```java
if (index < 0 || index >= size) {
        throw new IndexOutOfBoundsException("Index out of bounds: " + index);
}
```