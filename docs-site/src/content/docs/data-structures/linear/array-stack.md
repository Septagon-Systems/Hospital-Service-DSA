---
title: "Array Stack"
---

---

# ArrayStack Undo Log Trace

## Data Structure

`ArrayStack<T>`

## Purpose

The ArrayStack implements a Last-In, First-Out (LIFO) data structure.

The main operations are:

- `push(item)`
- `pop()`
- `peek()`
- `isEmpty()`
- `size()`

The stack is useful for representing an undo history because the most
recent action must be undone before earlier actions.

---

## Hospital Operations Undo Example

Consider a hospital operations application where an operator performs
three actions:

1. Assign a vehicle to Request A.
2. Assign a driver to Request B.
3. Update the priority of Request C.

Each completed action is pushed onto the undo stack.

### Trace

| Step | Operation | Stack | Result |
|------|-----------|-------|--------|
| 1 | Create stack | `[]` | Stack is empty |
| 2 | `push("Assign vehicle to Request A")` | `[A]` | Action A stored |
| 3 | `push("Assign driver to Request B")` | `[A, B]` | Action B becomes top |
| 4 | `push("Update priority of Request C")` | `[A, B, C]` | Action C becomes top |
| 5 | `pop()` | `[A, B]` | Action C is undone |
| 6 | `pop()` | `[A]` | Action B is undone |
| 7 | `peek()` | `[A]` | Action A remains on top |
| 8 | `pop()` | `[]` | Action A is undone |

---

## LIFO Demonstration

The insertion order was:

```text
A → B → C
```