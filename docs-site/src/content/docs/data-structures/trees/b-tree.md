---
title: "B-Tree Multi-Way Search Tree"
---

## 1. Data Structure Overview

A **B-Tree** is a self-balancing, multi-way search tree designed specifically for indexing and block/page-oriented storage. In a B-Tree of minimum degree $t \ge 2$ (Order $M = 2t$):

1. **Node Capacity Bounds**:
   - Every node contains at most $2t - 1$ keys and $2t$ child pointers.
   - Every non-root internal node contains at least $t - 1$ keys and $t$ child pointers.
   - The root contains at least 1 key if the tree is non-empty.
2. **Key Ordering & Partitioning**:
   - Within each node, keys are stored in ascending order: $k_0 < k_1 < \dots < k_{m-1}$.
   - Subtree $c_i$ holds all keys strictly between $k_{i-1}$ and $k_i$.
3. **Strict Balance (Bottom-Up Growth)**:
   - All leaf nodes reside at the **exact same depth**, ensuring a uniform tree height of $h = O(\log_t n)$.
   - Trees grow upward when the root splits, guaranteeing balanced performance across all branches.

---

## 2. Hospital Operations Scenario: National Facility Primary Index

In the Ghana Hospital Service system, facility records across Greater Accra, Ashanti, Northern, and Central regions must be indexed by unique primary alphanumeric facility codes (e.g. `KBTH-ACC-01`, `37MH-ACC-02`, `KATH-KMS-01`).

A B-Tree with minimum degree $t = 2$ (Order 4 / 2-3-4 tree) is utilized to simulate page-indexed primary key lookup.

- Maximum keys per node: $2t - 1 = 3$ keys.
- Maximum children per node: $2t = 4$ children.
- Proactive splitting: When descending, any full node ($3$ keys) is split before inserting to guarantee single-pass insertion without cascading upward splits.

---

## 3. Step-by-Step Insertion & Proactive Split Trace Table

### Insertion Sequence
1. Insert `KBTH` (Korle Bu)
2. Insert `37MH` (37 Military)
3. Insert `KATH` (Komfo Anokye)
4. Insert `UGMC` (UG Medical Centre) $\to$ *Triggers root split*
5. Insert `TTH` (Tamale Teaching)
6. Insert `CCTH` (Cape Coast Teaching)
7. Insert `EFTH` (Effia Nkwanta) $\to$ *Triggers child split*
8. Insert `TGH` (Tema General)

### Execution Trace Table

| Step | Operation | Key / Facility | Node State Before Operation | Split / Promotion Action | Resulting Tree Structure (Root & Children) | Tree Height | Total Keys ($n$) |
|:---:|:---|:---|:---|:---|:---|:---:|:---:|
| 1 | `insert` | `KBTH` | `[ ]` (empty root) | Direct insert into root | Root: `[KBTH]` | 0 | 1 |
| 2 | `insert` | `37MH` | Root: `[KBTH]` | Sorted shift in root | Root: `[37MH, KBTH]` | 0 | 2 |
| 3 | `insert` | `KATH` | Root: `[37MH, KBTH]` | Sorted shift in root | Root: `[37MH, KATH, KBTH]` *(Full: 3 keys)* | 0 | 3 |
| 4 | `insert` | `UGMC` | Root: `[37MH, KATH, KBTH]` *(Full)* | **Proactive Root Split**: Median `KATH` promoted to new root. Left child: `[37MH]`, Right child: `[KBTH]`. | Root: `[KATH]`<br>├── Child 0: `[37MH]`<br>└── Child 1: `[KBTH, UGMC]` | 1 | 4 |
| 5 | `insert` | `TTH` | Root: `[KATH]`, Child 1: `[KBTH, UGMC]` | Direct insert into Child 1 | Root: `[KATH]`<br>├── Child 0: `[37MH]`<br>└── Child 1: `[KBTH, TTH, UGMC]` *(Full: 3 keys)* | 1 | 5 |
| 6 | `insert` | `CCTH` | Root: `[KATH]`, Child 0: `[37MH]` | Direct insert into Child 0 | Root: `[KATH]`<br>├── Child 0: `[37MH, CCTH]` | 1 | 6 |
| 7 | `insert` | `EFTH` | Child 0: `[37MH, CCTH]` | Direct insert into Child 0 | Root: `[KATH]`<br>├── Child 0: `[37MH, CCTH, EFTH]` *(Full: 3 keys)*<br>└── Child 1: `[KBTH, TTH, UGMC]` *(Full: 3 keys)* | 1 | 7 |
| 8 | `insert` | `TGH` | Root: `[KATH]`, Child 1 is Full | **Proactive Child Split**: Median `TTH` promoted to root. Child 1 splits into `[KBTH]` and `[UGMC]`. Insert `TGH` into right child. | Root: `[KATH, TTH]`<br>├── Child 0: `[37MH, CCTH, EFTH]`<br>├── Child 1: `[KBTH]`<br>└── Child 2: `[TGH, UGMC]` | 1 | 8 |

---

## 4. Search Execution Trace

### Query: `get("TGH")` (Tema General Hospital)
1. **Root Inspection**:
   - Compare `TGH` with Root keys: `[KATH, TTH]`.
   - `TGH` $>$ `KATH` $\to$ proceed to next key.
   - `TGH` $<$ `TTH` $\to$ follow Child pointer between `KATH` and `TTH` (Child index 1: `[KBTH]`).
   - `TGH` $>$ `KBTH` $\to$ Child 1 is a leaf without match $\to$ follow appropriate branch.
   - Target reached in leaf `[TGH, UGMC]` $\to$ Key found in $O(\log_t n)$ comparisons.

---

## 5. Theoretical Complexity Comparison

| Data Structure | Search Time | Insertion Time | Deletion Time | Worst-Case Height | Cache / Block Locality | Primary Application |
|:---|:---:|:---:|:---:|:---:|:---:|:---|
| **Binary Search Tree (Unbalanced)** | $O(n)$ | $O(n)$ | $O(n)$ | $O(n)$ | Poor (pointer-heavy) | In-memory unconstrained lookup |
| **Red-Black Tree** | $O(\log_2 n)$ | $O(\log_2 n)$ | $O(\log_2 n)$ | $2 \log_2 (n+1)$ | Moderate (binary nodes) | In-memory balanced associative map |
| **B-Tree ($t \ge 2$)** | $O(t \log_t n)$ | $O(t \log_t n)$ | $O(t \log_t n)$ | $\le \log_t \frac{n+1}{2}$ | **Excellent** (block/page aligned) | **Database indexing & Disk storage** |
| **Custom Hash Table** | $O(1)$ amortised | $O(1)$ amortised | $O(1)$ amortised | N/A | High (bucket arrays) | Direct key-value cache (no order) |

---

## 6. Correctness & Compliance Verification

- **Zero `java.util` Collections**: `BTree` uses custom generic arrays (`Comparable[]`, `Object[]`, `BTreeNode[]`) and contains zero standard library collections.
- **Strict Height Invariant**: Every leaf in `BTree` remains at identical depth, proven by all insertion and deletion unit tests.
- **Fail-Fast Preconditions**: Null keys in `insert`, `get`, and `contains` throw `IllegalArgumentException`. Degree $t < 2$ is rejected at instantiation.
