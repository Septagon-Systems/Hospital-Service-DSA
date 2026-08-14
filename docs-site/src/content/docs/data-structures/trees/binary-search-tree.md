---
title: "Trace Table 10: Binary Search Tree (BST) Operations"
---

## Data Structure Description
Custom generic Binary Search Tree (BST) implemented in `edu.ug.dcit308.hospital.ds.tree.BinarySearchTree`. Each node stores a key-value pair `(K key, V value)` where `K extends Comparable<K>`. No `java.util` collections are used. Maintains the fundamental BST invariant: for any node $N$, all keys in $N$'s left subtree are strictly smaller than $N.\text{key}$, and all keys in $N$'s right subtree are strictly greater than $N.\text{key}$.

---

## Part 1: Search Path Trace Table

Tracing search operations on a BST constructed by inserting hospital IDs in the order: **`50` ("Korle Bu"), `30` ("Ridge"), `70` ("Komfo Anokye"), `20` ("Tema General"), `40` ("37 Military"), `60` ("Ho Teaching"), `80` ("Tamale Teaching")**.

### Tree Structure Diagram
```
        50 (Korle Bu)
       /             \
  30 (Ridge)      70 (Komfo Anokye)
   /      \         /              \
20 (Tema) 40 (37) 60 (Ho)        80 (Tamale)
```

### Trace Table 1.1: `search(40)` — Key Present (Successful Search)
| Step | Current Node Key | Target Key | Comparison (`Target vs Node`) | Action / Branch Taken | Result |
|:---:|:---:|:---:|:---:|:---:|:---:|
| 1 | `50` | `40` | `40 < 50` | Recurse into Left Subtree (`node.left`) | Searching |
| 2 | `30` | `40` | `40 > 30` | Recurse into Right Subtree (`node.right`) | Searching |
| 3 | `40` | `40` | `40 == 40` | Key found! Return associated value | **Found: `"37 Military"`** |

### Trace Table 1.2: `search(25)` — Key Absent (Unsuccessful Search)
| Step | Current Node Key | Target Key | Comparison (`Target vs Node`) | Action / Branch Taken | Result |
|:---:|:---:|:---:|:---:|:---:|:---:|
| 1 | `50` | `25` | `25 < 50` | Recurse into Left Subtree (`node.left`) | Searching |
| 2 | `30` | `25` | `25 < 30` | Recurse into Left Subtree (`node.left`) | Searching |
| 3 | `20` | `25` | `25 > 20` | Recurse into Right Subtree (`node.right`) | Searching |
| 4 | `null` | `25` | Base Case (`node == null`) | Terminate search | **Not Found: `null`** |

---

## Part 2: Sorted In-Order Traversal Execution Trace

The `inOrder()` method performs an in-order recursive traversal ($Left \to Node \to Right$) and populates an array with keys in strictly ascending order.

### Call Stack & Output Trace Table
| Step | Action / Stack Frame | Current Node | Array State Before | Operation Performed | Array State After |
|:---:|:---:|:---:|:---:|:---:|:---:|
| 1 | Call `inOrderRec(50)` | `50` | `[]` | Descend left to `30` | `[]` |
| 2 | Call `inOrderRec(30)` | `30` | `[]` | Descend left to `20` | `[]` |
| 3 | Call `inOrderRec(20)` | `20` | `[]` | Descend left (`null`), return | `[]` |
| 4 | Process `node.key` | `20` | `[]` | Append key `20` to result array | `[20]` |
| 5 | Call `inOrderRec(null)` | `20.right` | `[20]` | Descend right (`null`), return | `[20]` |
| 6 | Return to `30` | `30` | `[20]` | Append key `30` to result array | `[20, 30]` |
| 7 | Call `inOrderRec(40)` | `40` | `[20, 30]` | Descend left (`null`), process key `40` | `[20, 30, 40]` |
| 8 | Return to `50` | `50` | `[20, 30, 40]` | Append key `50` to result array | `[20, 30, 40, 50]` |
| 9 | Call `inOrderRec(70)` | `70` | `[20, 30, 40, 50]` | Descend left to `60` | `[20, 30, 40, 50]` |
| 10 | Process `60` | `60` | `[20, 30, 40, 50]` | Append key `60` to result array | `[20, 30, 40, 50, 60]` |
| 11 | Return to `70` | `70` | `[20, 30, 40, 50, 60]` | Append key `70` to result array | `[20, 30, 40, 50, 60, 70]` |
| 12 | Call `inOrderRec(80)` | `80` | `[20, 30, 40, 50, 60, 70]` | Process key `80` to result array | `[20, 30, 40, 50, 60, 70, 80]` |

**Final Sorted Traversal Output**: `[20, 30, 40, 50, 60, 70, 80]`

---

## Part 3: Deletion Trace (Two Children Replacement Invariant)

### Operation: `delete(30)`
Node `30` has two children (`20` and `40`).

| Step | Sub-Stage | Description | Tree State |
|:---:|:---:|:---|:---|
| 1 | Locate Target Node | Found `30` with `left = 20` and `right = 40` | Target node `30` identified |
| 2 | Find In-Order Successor | Find minimum node in right subtree (`minNode(30.right)`) | Successor is `40` |
| 3 | Key & Value Copy | Copy `successor.key` (`40`) and payload into target node `30` | Node `30` becomes `40` |
| 4 | Successor Deletion | Delete original successor node from right subtree | `40` unlinked from right subtree |
| 5 | Decrement Size | Size updated from `7` to `6` | BST ordering property preserved |

---

## Correctness Invariant & Time Complexity Summary

### BST Invariants
1. **Ordering Invariant**: For every node $N$, $L.\text{key} < N.\text{key} < R.\text{key}$ for all $L \in N.\text{left}$ and $R \in N.\text{right}$.
2. **In-Order Sorting Invariant**: An in-order traversal of a valid BST always yields keys in strictly increasing sorted order (duplicate key insertions update existing values in-place).

### Complexity Bounds
| Operation | Average Case | Worst Case (Degenerate Tree) | Space Complexity |
|:---|:---:|:---:|:---|
| `insert(K, V)` | $O(\log n)$ | $O(n)$ | $O(h)$ recursive stack |
| `search(K)` | $O(\log n)$ | $O(n)$ | $O(h)$ recursive stack |
| `delete(K)` | $O(\log n)$ | $O(n)$ | $O(h)$ recursive stack |
| `inOrder()` | $O(n)$ | $O(n)$ | $O(n)$ result array + $O(h)$ stack |
