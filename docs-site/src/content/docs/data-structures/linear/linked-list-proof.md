---
title: "Proof Sketch: Linked List Traversal & Boundary Invariants"
---

# Proof Sketch: Linked List Traversal & Boundary Invariants

## Problem Statement
Verification of correctness for indexed retrieval `get(index)` in `SinglyLinkedList<T>` and `DoublyLinkedList<T>`.

### Concrete Ghana Hospital Dataset Instantiation
Consider a list of Ghana hospital records:
- Node 0 (`head`): Korle Bu Teaching Hospital (`"Korle Bu"`)
- Node 1: Ridge Hospital (`"Ridge"`)
- Node 2: 37 Military Hospital (`"37 Military"`)
- Node 3: Komfo Anokye Teaching Hospital (`"KATH"`)

**Goal**: Retrieve record at `index = 2` (`"37 Military"`). Below, we prove that `get(2)` correctly returns `"37 Military"`.

---

## 1. Loop Invariant for `SinglyLinkedList.get(index)`

### Precondition & Bounds Safety
The method validates precondition $0 \le \text{index} < \text{size}$. Because $\text{size} \ge 1$ and $\text{head} \ne \text{null}$, `head` represents a valid node 0. For any iteration $i < \text{index} < \text{size}$, node $i$ is non-null and `current.next` exists.

### Invariant Definition
At the start of iteration $i$ of the traversal loop (where $0 \le i \le \text{index}$), the pointer `current` references the $i$-th node of the list, indexed from $0$ at `head`.

### Invariant Steps
1. **Initialization**:
   Prior to the first iteration ($i = 0$), `current` is initialized to `head`. By the precondition $0 \le \text{index} < \text{size}$, `head` is a valid node 0, so the invariant holds.

2. **Maintenance**:
   Assume the invariant holds for iteration $i$ (`current` points to node $i$). Because $i < \text{index} < \text{size}$, node $i$ has a valid successor `current.next`. Executing `current = current.next` advances `current` to node $i + 1$. Incrementing loop counter $i$ maintains the invariant for iteration $i + 1$.

3. **Termination**:
   The loop terminates when $i = \text{index}$. By the loop invariant, `current` points to the node at position `index`. Returning `current.data` produces the exact element requested (e.g. `get(2)` returns `"37 Military"`).

---

## 2. Loop Invariant & Structural Invariants for `DoublyLinkedList<T>`

### Structural Invariants
For a valid, non-empty `DoublyLinkedList<T>` of $N = \text{size}$ nodes:
- `head.prev == null` and `tail.next == null`.
- The list forms a finite, acyclic chain of $N$ nodes where for every node $X$:
  - If $X.\text{next} \ne \text{null}$, then $X.\text{next}.\text{prev} == X$.
  - If $X.\text{prev} \ne \text{null}$, then $X.\text{prev}.\text{next} == X$.
- If $\text{size} == 1$, then $\text{head} == \text{tail}$, $\text{head}.\text{next} == \text{null}$, and $\text{head}.\text{prev} == \text{null}$.

### Precondition & Branching Strategy
`DoublyLinkedList.get(index)` validates $0 \le \text{index} < \text{size}$ and selects traversal direction:
1. **Forward Branch (`index < size / 2`)**: Starts at `head` and steps `current = current.next` for $i = 0 \dots \text{index}$.
   - *Invariant*: At step $i$, `current` points to node $i$ from `head`. Terminates at $i = \text{index}$.
2. **Backward Branch (`index >= size / 2`)**: Starts at `tail` and steps `current = current.prev` for $k = (\text{size} - 1) \dots \text{index}$.
   - *Invariant*: At step $k$, `current` points to node $k$ from `tail`. Terminates at $k = \text{index}$.

In both branches, structural invariants guarantee that `current` lands on the exact node at `index` in $\le \lfloor\text{size}/2\rfloor$ steps.