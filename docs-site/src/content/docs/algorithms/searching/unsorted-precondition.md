---
title: "Unsorted Precondition Violation Counterexample"
---

# Counterexample & Boundary Case Analysis: Queue & Linked List Preconditions

## Case 1: Empty Queue Dequeue / Peek Precondition Violation

### Precondition
Operations `dequeue()` and `peek()` on `CircularQueue` require that the queue size is strictly greater than zero ($size > 0$ or `isEmpty() == false`).

### Violation Scenario
An operation calls `dequeue()` or `peek()` on a `CircularQueue` when $size = 0$ (empty queue state).

### Impact / Result
Invoking `dequeue()` or `peek()` on an empty queue violates queue state invariants and throws an `IllegalStateException` or `NoSuchElementException`, halting pipeline execution.

---

## Case 2: Out of Bounds Indexing on Linked List

### Precondition
The `get(index)` and `remove(index)` operations on `SinglyLinkedList` and `DoublyLinkedList` require that $0 \le index < size$.

### Violation Scenario
An operation attempts to access `get(5)` when $size = 3$, or passes a negative index such as `get(-1)`.

### Impact / Result
Violates boundary constraints and throws an `IndexOutOfBoundsException`, preventing invalid pointer dereferencing or `NullPointerException`.