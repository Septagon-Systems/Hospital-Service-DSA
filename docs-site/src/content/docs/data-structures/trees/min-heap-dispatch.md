---
title: "---"
---


---

# MinHeapPriorityQueue Hospital Dispatch Trace

## Data Structure

`MinHeapPriorityQueue<T>`

## Purpose

The MinHeapPriorityQueue implements a priority queue using a min-heap.

For this demonstration, a smaller priority number represents a more urgent
hospital logistics request.

The implemented operations include:

- `enqueue(item)`
- `dequeue()`
- `peek()`
- `isEmpty()`
- `size()`

The heap maintains the minimum element at the root.

---

## Hospital Dispatch Scenario

Suppose the hospital logistics system receives the following requests:

| Request | Priority |
|---------|----------|
| Normal medical supplies | 5 |
| Medicine delivery | 3 |
| Emergency blood delivery | 1 |
| Surgical equipment | 2 |

A lower priority number means the request must be handled sooner.

Therefore, the expected dispatch order is:

```text
1 → 2 → 3 → 5
```