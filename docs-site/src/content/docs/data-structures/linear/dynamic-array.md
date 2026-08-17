---
title: "Trace Table 7: DynamicArrayList Operations"
---

## Data Structure Description
Custom resizable array list implemented in `edu.ug.dcit308.hospital.ds.list.DynamicArrayList`. Backed by a raw `Object[]` buffer that doubles in capacity whenever a write operation would overflow it. Implements `Iterable<T>` for enhanced-for support.

For clarity, this trace uses a reduced initial capacity of 4 (`new DynamicArrayList<>(4)`) instead of the library default of 10, so the resize event is reachable in a small number of steps.

## Trace Table Execution
| Step | Operation | Underlying Capacity Before | List State Before | Action | List State After | Underlying Capacity After |
|---|---|---|---|---|---|---|
| 1 | `add(10)` | 4 | `[]` | Append at index 0 | `[10]` | 4 |
| 2 | `add(20)` | 4 | `[10]` | Append at index 1 | `[10, 20]` | 4 |
| 3 | `add(30)` | 4 | `[10, 20]` | Append at index 2 | `[10, 20, 30]` | 4 |
| 4 | `add(40)` | 4 | `[10, 20, 30]` | `size == capacity`, no resize needed yet; append at index 3 | `[10, 20, 30, 40]` | 4 |
| 5 | `add(50)` | 4 | `[10, 20, 30, 40]` | `size == capacity` triggers `ensureCapacity()`: allocate new `Object[8]`, copy 4 elements, then append at index 4 | `[10, 20, 30, 40, 50]` | 8 |
| 6 | `remove(1)` | 8 | `[10, 20, 30, 40, 50]` | Shift elements at indices 2..4 left by one, decrement size | `[10, 30, 40, 50]` | 8 |
| 7 | `add(1, 99)` | 8 | `[10, 30, 40, 50]` | Shift elements at indices 1..3 right by one, insert at index 1 | `[10, 99, 30, 40, 50]` | 8 |

## Correctness Invariant
At all times, `0 <= size <= elements.length`, and for every valid index `i` in `[0, size)`, `elements[i]` holds the logical element at position `i` with no gaps. `ensureCapacity()` preserves this invariant across a resize: the new buffer's first `size` slots are an exact, order-preserving copy of the old buffer's first `size` slots before the appending write proceeds.
