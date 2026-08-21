---
title: "Trace Table 4: Dijkstra's Shortest Path Algorithm"
---

## Algorithm Overview
Dijkstra's single-source shortest path algorithm computes the minimum travel distances and routes across the hospital road network using `MinHeapPriorityQueue` as the frontier structure.

## Sample Graph Configuration

```text
  0 --5.0-- 1
  |         |
 2.0       3.0
  |         |
  4 --1.0-- 2
```

- **Vertices**: 5 hospital nodes (`0` to `4`)
- **Corridors**: `0→1` (5.0 km), `0→4` (2.0 km), `4→2` (1.0 km), `1→3` (3.0 km)

---

## Step-by-Step Trace: Source = Node 0

| Step | Visited Set | Current Node $u$ | $\text{dist}[0]$ | $\text{dist}[1]$ | $\text{dist}[2]$ | $\text{dist}[3]$ | $\text{dist}[4]$ | Frontier Priority Queue `(node, dist)` |
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---|
| **Init** | $\emptyset$ | — | 0 | $\infty$ | $\infty$ | $\infty$ | $\infty$ | `[(0, 0)]` |
| **1** | $\{0\}$ | 0 | 0 | 5.0 | $\infty$ | $\infty$ | 2.0 | `[(4, 2.0), (1, 5.0)]` |
| **2** | $\{0, 4\}$ | 4 | 0 | 5.0 | 3.0 | $\infty$ | 2.0 | `[(2, 3.0), (1, 5.0)]` |
| **3** | $\{0, 4, 2\}$ | 2 | 0 | 5.0 | 3.0 | $\infty$ | 2.0 | `[(1, 5.0)]` |
| **4** | $\{0, 4, 2, 1\}$ | 1 | 0 | 5.0 | 3.0 | 8.0 | 2.0 | `[(3, 8.0)]` |
| **5** | $\{0, 4, 2, 1, 3\}$ | 3 | 0 | 5.0 | 3.0 | 8.0 | 2.0 | $\emptyset$ (Queue empty) |

---

## Final Shortest Paths from Node 0

| Destination Vertex | Minimal Distance | Reconstructed Optimal Route |
|:---|:---:|:---|
| **Node 0** | $0.0 \text{ km}$ | `[0]` |
| **Node 1** | $5.0 \text{ km}$ | `[0, 1]` |
| **Node 2** | $3.0 \text{ km}$ | `[0, 4, 2]` (Multi-hop via Node 4) |
| **Node 3** | $8.0 \text{ km}$ | `[0, 1, 3]` |
| **Node 4** | $2.0 \text{ km}$ | `[0, 4]` |

---

## Complexity Analysis
- **Time Complexity**: $O((V + E) \log V)$ with custom `MinHeapPriorityQueue`.
- **Space Complexity**: $O(V)$ for distance array `dist[]`, predecessor array `pred[]`, and `visited[]` set.

