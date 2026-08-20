# Trace Evidence: BFS and DFS Graph Traversal

## Scenario
This trace demonstrates Breadth-First Search (BFS) and Depth-First Search (DFS) graph exploration on the hospital road network implemented in `GraphTraversal.java` (`graph` module).

The test graph contains five hospital vertices (`0` to `4`) with the following road corridors:
- `0 → 1` (Korle Bu to 37 Military)
- `0 → 2` (Korle Bu to Ridge)
- `1 → 3` (37 Military to UGMC)
- `2 → 4` (Ridge to Tema General)

Traversal begins from **Vertex 0** (Korle Bu).

---

## Graph Topology

```text
        0 (Korle Bu)
       / \
      1   2 (Ridge)
      |   |
      3   4 (Tema General)
```

---

## BFS Trace Table (Queue-Based Level-Order)

| Step | Current Vertex $u$ | FIFO Queue State | Visited Set $\text{visited}[]$ | Notes |
|:---:|:---:|:---:|:---:|:---|
| **0** | — | `[0]` | $\emptyset$ | BFS initialized with start vertex 0 |
| **1** | 0 | `[1, 2]` | $\{0\}$ | Dequeue 0; discover neighbors 1 and 2 |
| **2** | 1 | `[2, 3]` | $\{0, 1\}$ | Dequeue 1; discover neighbor 3 |
| **3** | 2 | `[3, 4]` | $\{0, 1, 2\}$ | Dequeue 2; discover neighbor 4 |
| **4** | 3 | `[4]` | $\{0, 1, 2, 3\}$ | Dequeue 3; no new unvisited neighbors |
| **5** | 4 | `[]` | $\{0, 1, 2, 3, 4\}$ | Dequeue 4; queue empty $\rightarrow$ complete |

### BFS Traversal Order
$$\mathbf{0 \to 1 \to 2 \to 3 \to 4}$$

---

## DFS Trace Table (Stack / Recursive Depth-Order)

| Step | Current Vertex $u$ | Call Stack / Visited Set | Action & Notes |
|:---:|:---:|:---:|:---|
| **0** | 0 | $\{0\}$ | DFS begins at start vertex 0 |
| **1** | 1 | $\{0, 1\}$ | Recurse to first neighbor 1 |
| **2** | 3 | $\{0, 1, 3\}$ | Recurse to neighbor 3 (leaf); backtrack to 0 |
| **3** | 2 | $\{0, 1, 3, 2\}$ | Recurse to second neighbor 2 |
| **4** | 4 | $\{0, 1, 3, 2, 4\}$ | Recurse to neighbor 4 (leaf); backtrack to 0 $\rightarrow$ complete |

### DFS Traversal Order
$$\mathbf{0 \to 1 \to 3 \to 2 \to 4}$$

---

## Test Evidence & Invariants
The JUnit tests in `GraphTraversalTest.java` verify these exact sequences:
- **BFS Expected Output**: `[0, 1, 2, 3, 4]`
- **DFS Expected Output**: `[0, 1, 3, 2, 4]`
- **Disconnected Graph**: Correctly returns only the connected component reachable from the source.
- **Cycles**: Terminate cleanly without duplicate vertex entries.
- **Complexity**: $O(V + E)$ time and $O(V)$ auxiliary space.