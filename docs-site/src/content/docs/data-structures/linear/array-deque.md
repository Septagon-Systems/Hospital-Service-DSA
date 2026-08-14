---
title: "ArrayDeque Urgent Request Trace"
---

## Hospital Urgent Request Example

Consider a hospital logistics system receiving requests for medical
supplies.

Normal requests enter through the rear of the deque. An urgent request
can be inserted at the front so that it receives attention before the
normal requests.

---

## Trace

| Step | Operation | Deque | Result |
|------|-----------|-------|--------|
| 1 | Create deque | `[]` | Empty |
| 2 | `addLast("Request A")` | `[A]` | Normal request added |
| 3 | `addLast("Request B")` | `[A, B]` | Normal request added |
| 4 | `addLast("Request C")` | `[A, B, C]` | Normal request added |
| 5 | `addFirst("URGENT Request")` | `[URGENT, A, B, C]` | Urgent request inserted at front |
| 6 | `getFirst()` | `[URGENT, A, B, C]` | Returns `URGENT` |
| 7 | `removeFirst()` | `[A, B, C]` | Urgent request removed first |
| 8 | `removeLast()` | `[A, B]` | Request C removed from rear |

---

## Double-Ended Behavior

The trace demonstrates insertion from both ends:

```text
addLast(A)
addLast(B)
addLast(C)
addFirst(URGENT)
```