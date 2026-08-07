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

### Invariant Definition
At the start of iteration $i$ of the traversal loop (where $0 \le i \le \text{index}$), the pointer `current` references the $i$-th node of the list, indexed from $0$ at `head`.

### Invariant Steps
1. **Initialization**:
   Prior to the first iteration ($i = 0$), `current` is initialized to `head`. Since `head` represents node $0$, the invariant holds.

2. **Maintenance**:
   Assume the invariant holds for iteration $i$ (`current` points to node $i$). The loop executes `current = current.next`, advancing `current` to node $i + 1$. Incrementation of loop counter $i$ maintains the relationship for iteration $i + 1$.

3. **Termination**:
   The loop terminates when $i = \text{index}$. By the loop invariant, `current` points to the node at position `index`. Returning `current.data` produces the exact element requested.

---

## 2. Structural Invariants for `DoublyLinkedList<T>`

For any node $N$ in a non-empty `DoublyLinkedList`:
* If $N.\text{next} \ne \text{null}$, then $N.\text{next}.\text{prev} == N$.
* If $N.\text{prev} \ne \text{null}$, then $N.\text{prev}.\text{next} == N$.
* If $\text{size} == 1$, $\text{head} == \text{tail}$, $\text{head}.\text{next} == \text{null}$, and $\text{head}.\text{prev} == \text{null}$.