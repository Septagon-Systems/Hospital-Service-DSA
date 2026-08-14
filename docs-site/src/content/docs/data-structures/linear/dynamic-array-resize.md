---
title: "Dynamic Array Resize Trace"
---

## Data Structure

`DynamicArrayList<T>`

## Purpose

The DynamicArrayList is a resizable array-backed list used by the data
structures module. It provides indexed access and automatically increases
its storage capacity when the current array becomes full.

The implementation supports:

- `add(element)`
- `add(index, element)`
- `get(index)`
- `set(index, element)`
- `remove(index)`
- `size()`
- `isEmpty()`
- `clear()`

The initial capacity is 10 elements. When the array becomes full, the
implementation creates a new array with twice the previous capacity and
copies the existing elements into it.

---

## Resize Trace

The following trace demonstrates the resizing behavior.

| Step | Operation | Size | Capacity | Contents |
|------|-----------|------|----------|----------|
| 1 | Create DynamicArrayList | 0 | 10 | `[]` |
| 2 | `add(1)` | 1 | 10 | `[1]` |
| 3 | `add(2)` | 2 | 10 | `[1, 2]` |
| 4 | `add(3)` | 3 | 10 | `[1, 2, 3]` |
| 5 | `add(4)` | 4 | 10 | `[1, 2, 3, 4]` |
| 6 | `add(5)` | 5 | 10 | `[1, 2, 3, 4, 5]` |
| 7 | `add(6)` | 6 | 10 | `[1, 2, 3, 4, 5, 6]` |
| 8 | `add(7)` | 7 | 10 | `[1, 2, 3, 4, 5, 6, 7]` |
| 9 | `add(8)` | 8 | 10 | `[1, 2, 3, 4, 5, 6, 7, 8]` |
| 10 | `add(9)` | 9 | 10 | `[1, 2, 3, 4, 5, 6, 7, 8, 9]` |
| 11 | `add(10)` | 10 | 10 | `[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]` |
| 12 | `add(11)` | 11 | 20 | `[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]` |

### Resize Event

Before adding the 11th element:

```text
Size     = 10
Capacity = 10
```