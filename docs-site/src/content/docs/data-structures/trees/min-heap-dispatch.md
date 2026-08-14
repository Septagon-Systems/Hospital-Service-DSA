---
title: "MinHeap Priority Queue Dispatch Trace"
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