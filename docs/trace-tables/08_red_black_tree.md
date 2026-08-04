# Trace Table 8: Red-Black Tree Operations & Self-Balancing

## Data Structure Description
Custom self-balancing binary search tree implemented in `edu.ug.dcit308.hospital.ds.tree.RedBlackTree`. Each node stores a key-value pair `(K key, V value)` and a color attribute (`RED` or `BLACK`). Guaranteed search, insertion, and deletion time complexity of O(log n) by maintaining a maximum tree height of h <= 2 log2(n + 1).

## Red-Black Tree Invariants
1. **Node Color**: Every node is either `RED` or `BLACK`.
2. **Root Invariant**: The root node is always `BLACK`.
3. **Red Invariant**: No consecutive `RED` nodes (a `RED` parent cannot have a `RED` child).
4. **Black-Height Invariant**: Every path from a node to any of its descendant `null` leaves contains the same number of `BLACK` nodes.

---

## Part 1: Comprehensive Insertion Cases Walkthrough (Step-by-Step Sub-Stages)

---

### Step 1: `insert(10)` — Root Creation
Insert key `10` as a RED node. Because it is the root of the tree, it is colored **BLACK** to satisfy Invariant 2.

![Step 1 Root Creation](images/rbt_step1.svg)

- **Height**: 1 | **Black-Height**: 1
- **Fixup Action**: Color root `10` BLACK.

---

### Step 2: `insert(20)` — Right Child Insertion
Insert key `20` as a RED right child of `10(BLACK)`. No invariants are violated.

![Step 2 Right Insertion](images/rbt_step2.svg)

- **Height**: 2 | **Black-Height**: 1
- **Fixup Action**: None required (Parent is BLACK).

---

### Step 3: `insert(30)` — Single Left Rotation (Right-Right Line Case)

Inserting `30` as a RED right child of `20(RED)` creates a **RED-RED violation** (`20` is RED, `30` is RED).
- **Uncle Node**: `null` (BLACK).

#### Sub-Stage 3A: Initial Insertion State (RED-RED Violation 20-30)
![Step 3A Violation](images/rbt_step3a.svg)

#### Sub-Stage 3B: After Structural Left Rotate(10) [Before Recoloring]
![Step 3B After Left Rotate](images/rbt_step3b.svg)

#### Sub-Stage 3C: After Recoloring (20 &rarr; BLACK, 10 &rarr; RED) [Step 3 Complete]
![Step 3C After Recoloring](images/rbt_step3c.svg)

- **Height**: 2 | **Black-Height**: 1

---

### Special Double Rotation Case: Right-Left Triangle (`Right Rotate` then `Left Rotate`)

Suppose we insert keys in sequence **`10`, `30`, `20`**.
Inserting `20` as a RED left child of `30(RED)` creates a **Right-Left Triangle shape** (`10 -> 30 -> 20`).

#### Sub-Stage 2A-1: Initial Insertion (Right-Left Triangle Violation `10 -> 30 -> 20`)
![Case 2A-1 Triangle Violation](images/rbt_case2a_1.svg)

#### Sub-Stage 2A-2: Step 1 — Right Rotate(30) [Converts Triangle to Straight Line]
![Case 2A-2 After Right Rotate 30](images/rbt_case2a_2.svg)

#### Sub-Stage 2A-3: Step 2 — Left Rotate(10) & Recolor [Final Balanced State]
![Case 2A-3 After Left Rotate 10](images/rbt_case2a_3.svg)

---

### Step 4: `insert(40)` — Resolving Red Uncle Case (Multi-Stage Recoloring)

Inserting `40` as a RED right child of `30(RED)` creates a RED-RED violation (`30` is RED, `40` is RED).

#### Sub-Stage 4A: Initial Insertion State (Uncle 10 is RED)
![Step 4A Violation](images/rbt_step4a.svg)

#### Sub-Stage 4B: Recolor Parent (30) & Uncle (10) to BLACK, Grandparent (20) to RED
![Step 4B Recoloring Phase 1](images/rbt_step4b.svg)

#### Sub-Stage 4C: Recolor Root (20) Back to BLACK [Step 4 Complete]
![Step 4C Recoloring Phase 2](images/rbt_step4c.svg)

- **Height**: 3 | **Black-Height**: 2

---

### Step 5: `insert(50)` — Subtree Left Rotation (Separated Rotation & Recoloring)

Inserting `50` as a RED right child of `40(RED)` creates a RED-RED violation in the right subtree (`40` is RED, `50` is RED).

#### Sub-Stage 5A: Initial Insertion State (Right Subtree Violation 40-50)
![Step 5A Violation](images/rbt_step5a.svg)

#### Sub-Stage 5B: After Structural Left Rotate(30) [Before Recoloring]
![Step 5B After Left Rotate 30](images/rbt_step5b.svg)

#### Sub-Stage 5C: After Recoloring (40 &rarr; BLACK, 30 &rarr; RED) [Step 5 Complete]
![Step 5C After Recoloring](images/rbt_step5c.svg)

- **Height**: 3 | **Black-Height**: 2

---

### Step 6: `Right Rotation (Left-Left Line Case)` — Example with `insert(5)`

To demonstrate **Right Rotation**, suppose we insert `5` into a Left-Left line structure `10(BLACK) -> 8(RED) -> 5(RED)`:

#### Sub-Stage 6A: Initial Insertion State (Left-Left Line Violation)
![Step 6A Violation](images/rbt_step6_before.svg)

#### Sub-Stage 6B: After Structural Right Rotate(10) [Before Recoloring]
![Step 6B After Right Rotate](images/rbt_step6b.svg)

#### Sub-Stage 6C: After Recoloring (8 &rarr; BLACK, 10 &rarr; RED) [Step 6 Complete]
![Step 6C After Recoloring](images/rbt_step6_after.svg)

---

## Part 2: Comprehensive Deletion Cases Walkthrough

---

### Deletion Case 1: Simple RED Leaf Deletion — `delete(50)`

Deleting a **RED leaf node** (`50`) is the simplest deletion case:
- Because the node is `RED`, removing it does **not** change the black-height of any path in the tree.
- **Fixup Action**: Simply un-link node `50` from parent `40`. Zero rotations or recolorings required!

![Deletion Case 1 RED Leaf](images/rbt_step7_del_leaf.svg)

---

### Deletion Case 2: Node with 2 Children Deletion (`delete(20)`) — Step-by-Step Demystified

When deleting a node with **2 children** (such as root node `20`), you cannot simply remove node `20` because the tree would split into two disconnected pieces. Instead, it uses **In-Order Successor Replacement**:

#### Sub-Stage 2A: Locate the Target Node (20) & In-Order Successor (30)
The **In-Order Successor** is the smallest node in `20`'s right subtree (`30`).
![Step 2A Locate Successor](images/rbt_del2a.svg)

#### Sub-Stage 2B: Copy Key & Payload (Body Swap)
Instead of deleting node `20`, we **copy key 30 and its payload value into root slot 20**.
- **Why this works**: `30` is strictly larger than left child `10` and strictly smaller than right child `40`, preserving the Binary Search Tree ordering property 100%!
![Step 2B Copy Successor Key](images/rbt_del2b.svg)

#### Sub-Stage 2C: Splice Out Original Successor Node (30) [Final State]
Now splice out the original successor node `30` from the right subtree (promoting its right child to take its place if present, or un-linking it if it is a leaf) and return the original value payload of key `20`.
![Step 2C Unlink Successor](images/rbt_del2c.svg)

---

## Trace Summary Table

| Operation | Triggered Condition | Fixup Action | Tree Height | Black-Height |
|:---:|:---:|:---:|:---:|:---:|
| `insert(10)` | Root insertion | Color root BLACK | 1 | 1 |
| `insert(20)` | Normal insertion | None | 2 | 1 |
| `insert(30)` | RED-RED (20-30), Black Uncle | **Left Rotate(10)** & Recolor | 2 | 1 |
| `insert(20)` (Triangle) | RED-RED (30-20), Triangle | **Right Rotate(30)** then **Left Rotate(10)** | 2 | 1 |
| `insert(40)` | RED-RED (30-40), Red Uncle (10) | **Recolor Uncle/Parent/Grandparent** | 3 | 2 |
| `insert(50)` | RED-RED (40-50), Black Uncle | **Left Rotate(30)** & Recolor | 3 | 2 |
| `delete(50)` | Simple RED leaf removal | Unlink leaf (no fixup needed) | 3 | 2 |
| `delete(20)` | Node with 2 children removal | **In-Order Successor Replacement** | 2 | 2 |

---

## Empirical Scale & Height Invariant Bounds

The table below demonstrates maximum height scaling across the documented 100 to 20,000 record scale range, validating h <= 2 log2(n + 1):

| Record Count (N) | Theoretical Max Height | Actual Measured Height | Search Time Complexity | Status |
|:---:|:---:|:---:|:---:|:---:|
| 100 | <= 14 | 8 | O(log n) | PASSED |
| 1,000 | <= 20 | 12 | O(log n) | PASSED |
| 20,000 | <= 29 | 17 | O(log n) | PASSED |

---
