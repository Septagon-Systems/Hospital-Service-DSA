---
title: "Empirical Performance Benchmarking"
---

This page provides the empirical runtime and memory efficiency evaluation for all custom data structures, search algorithms, sorting algorithms, trees, heaps, and graph routing algorithms in the **Ghana Smart Hospital Operations Optimizer**.

---

## 1. Experimental Methodology & Protocol

Per **Section 9 of the DCIT 204/308 Joint Project Brief**, all empirical experiments adhere to the following protocol:
- **Scales Evaluated**: N in {100, 500, 1000, 5000, 10000, 20000}.
- **Averaging**: 3 independent iterations per input scale (414 total test runs).
- **Deterministic Workload**: Random seed `42L` to guarantee repeatable results.
- **Hardware Environment**: Single-machine Linux baseline (x86_64, 12 cores, OpenJDK 21).
- **Zero Standard Library Collections**: Evaluated exclusively against our custom data structures.

---

## 2. Sorting Performance (O(N²) vs O(N log N))

| Scale (N) | Insertion Sort (ms) | Selection Sort (ms) | Merge Sort (ms) | QuickSort (ms) | Fastest Algorithm |
|:---|:---:|:---:|:---:|:---:|:---:|
| **100** | 0.0371 | 0.3857 | 0.0843 | **0.0350** | **QuickSort** |
| **500** | 0.3542 | 1.0305 | 0.1148 | **0.0922** | **QuickSort** |
| **1,000** | 0.3038 | 5.4383 | 0.2592 | **0.0726** | **QuickSort** |
| **5,000** | 2.5147 | 14.7431 | 0.9969 | **0.2564** | **QuickSort** |
| **10,000** | 3.6417 | 48.3618 | 0.6558 | **0.4789** | **QuickSort** |
| **20,000** | 14.5493 | 194.8436 | 1.3434 | **1.0814** | **QuickSort** |

> **Analysis**:
> - **Selection Sort** requires N(N-1)/2 unconditional comparisons without early-exit, scaling quadratically to **194.84 ms** at N=20,000.
> - **QuickSort** operates in-place with high L1/L2 cache locality, achieving **1.08 ms** at N=20,000 (~180x faster than Selection Sort).

---

## 3. Search Performance (O(N) vs O(log N))

| Scale (N) | Linear Search (ms) | Binary Search (ms) | Theoretical Complexity |
|:---|:---:|:---:|:---:|
| **100** | 0.1219 | 0.1675 | O(1) startup |
| **500** | 0.0051 | 0.0428 | O(log N) vs O(N) |
| **1,000** | 0.0114 | 0.0675 | O(log N) |
| **5,000** | 0.0508 | 0.2987 | O(log N) |
| **10,000** | 0.0666 | 0.5601 | O(log N) |
| **20,000** | 0.0823 | 1.1658 | O(log N) |

---

## 4. Trees & Custom Hash Table (O(log N) vs O(1))

| Scale (N) | Custom Hash Table (ms) | Binary Search Tree (ms) | Red-Black Tree (ms) | B-Tree (t=2) (ms) |
|:---|:---:|:---:|:---:|:---:|
| **100** | 0.2993 | 0.3016 | 0.3826 | 0.4651 |
| **500** | 0.2140 | 0.1495 | 0.2593 | 0.2536 |
| **1,000** | 0.2840 | 0.1846 | 0.2550 | 0.2458 |
| **5,000** | 1.0904 | 1.1517 | 1.1790 | 1.2742 |
| **10,000** | 1.7225 | 1.5186 | 2.0738 | 5.7096 |
| **20,000** | **1.3028** | 2.8027 | 2.8214 | 9.8715 |

> **Analysis**:
> - **Custom Hash Table**: Quadratic probing and dynamic capacity resizing keep amortized lookup times flat at **1.30 ms** at 20,000 entries.
> - **Red-Black Tree**: Color invariant balancing restricts tree height to <= 2 log2(N+1), yielding consistent **2.82 ms** operations.

---

## 5. Graph Routing & Minimum Spanning Trees

| Scale (V, E) | BFS Traversal (ms) | DFS Traversal (ms) | Dijkstra Shortest Path (ms) | Kruskal MST (ms) | Prim MST (ms) |
|:---|:---:|:---:|:---:|:---:|:---:|
| **100** | 0.4726 | 0.0628 | 0.4831 | 0.6304 | 0.3801 |
| **500** | 0.2480 | 0.1360 | 0.6060 | 0.4950 | 1.1027 |
| **1,000** | 0.1792 | 0.1053 | 0.5580 | 0.4538 | 0.7052 |
| **5,000** | 0.1651 | 0.1005 | 0.6313 | 0.9599 | 1.6282 |
| **10,000** | 0.2072 | 0.1000 | 0.4875 | 0.8468 | 3.4102 |
| **20,000** | **0.1589** | **0.1015** | **0.5001** | **0.6286** | **0.2119** |

---

## 6. Raw Data & Reproduction

- **Raw Measurements**: Exported to `data/results/algorithm_runs.csv`.
- **Database Table**: Persisted to SQLite table `algorithm_runs` in `data/hospital_system.db`.
- **Re-run Suite**: Execute via the application console menu or `./gradlew :benchmark:test`.
