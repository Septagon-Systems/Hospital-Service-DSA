# Ghana Smart Hospital Operations Optimizer

**DCIT 204/308 Data Structures & Algorithms (I & II) Joint Semester Project**  
*Department of Computer Science, University of Ghana*

[![Live Web Dashboard](https://img.shields.io/badge/Live%20Demo-Render-brightgreen?style=for-the-badge&logo=render)](https://hospital-service-dsa.onrender.com/)
[![Java 21 LTS](https://img.shields.io/badge/Java-21%20LTS-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![Build Status](https://img.shields.io/badge/Tests-399%20Passed%20(0%20Failures)-success?style=for-the-badge&logo=gradle)](https://github.com/Septagon-Systems/Hospital-Service-DSA)

---

## 🌐 Live Web Deployment
The Interactive Clinical Operations Command Center is deployed live and publicly accessible:
👉 **[https://hospital-service-dsa.onrender.com/](https://hospital-service-dsa.onrender.com/)**

*(Note: On the free tier, if inactive for 15 minutes, the instance may take ~20-30 seconds to spin up on first access).*

---

## 📌 Project Overview
The **Ghana Smart Hospital Operations Optimizer** is an operations management platform designed to model, route, dispatch, and optimize healthcare logistics across Ghana (including Korle Bu, Ridge Hospital, Komfo Anokye Teaching Hospital, Tema General, and 50 regional healthcare centers).

The project adheres strictly to the **DCIT 204/308 Joint DSA Project Brief**:
- **Custom Data Structure Library**: Zero usage of `java.util` collection classes (`HashMap`, `ArrayList`, `PriorityQueue`, `Stack`, `ArrayDeque`, `TreeMap`) in assessed core logic.
- **SQLite Database Persistence**: Embedded SQLite storage (`data/hospital_system.db`) recording operational entities, audit logs, and algorithm benchmark execution runs.
- **Java 21 LTS Multi-Module Gradle Architecture**: Divided into 8 isolated modules (`datastructures`, `algorithms`, `graph`, `scheduling`, `db`, `benchmark`, `app`, `gui`).
- **Empirical Benchmarking Suite**: Generates execution timing ($\mu\text{s}$ / $\text{ms}$) and memory footprint ($KB$) across varying dataset sizes ($100$ to $3,000,000$ records) with 4-quadrant search analysis.
- **Correctness & Verification Evidence**: 399 unit tests (0 failures), 10 trace tables, 4 proof sketches, and 2 counterexamples.

---

## 🚀 Running the System

### 1. Launching the Interactive Web GUI Dashboard
The Web GUI provides a full **Clinical Operations Command Center** featuring a real-time Leaflet GIS Ghana Map, Dijkstra Highway Routing, 0/1 Knapsack Medical Cargo DP Optimizer, Multi-Tier Triage Queues, and the Empirical Benchmark Lab.

- **Run locally via Gradle**:
  ```bash
  ./gradlew :gui:run
  ```
- Open your browser to: **[`http://localhost:8080`](http://localhost:8080)**

---

### 2. Launching the Interactive CLI Console Menu
The application provides an interactive terminal console menu (`ConsoleMenu.java`) for headless execution and oral defense demonstration.

- **Run in terminal with interactive input**:
  ```bash
  ./gradlew :app:run --console=plain
  ```
  *(On Windows PowerShell / CMD: `gradlew.bat :app:run --console=plain`)*

**Available Console Options**:
```text
=============================================================
              HOSPITAL OPERATIONS MENU
=============================================================
1. Load Ghana Hospital Data from Database
2. Run Custom Search & Sort Demonstrations
3. Run Ambulance Shortest Route Optimizer (Dijkstra)
4. Run Emergency Dispatch Queue System
5. Execute Empirical Performance Benchmarks
6. Undo Last Action (ArrayStack Rollback)
0. Exit Application
=============================================================
```

---

### 3. Calculating Student Algorithm Parameters (Brief Section 2.iii)
Each student team member must derive algorithm parameters from their University of Ghana index number (Priority weight, hash table size, route penalty factor, and random seed).

- **Run Parameter Calculator**:
  ```bash
  ./gradlew :app:run --args="10987654"
  ```
  *Example Output*:
  ```text
  === Algorithm Parameters for Student Index 10987654 ===
  - Priority Weight (Urgency): 1
  - Hash Table Prime Size: 659
  - Random Seed: 10987654
  - Route Penalty Factor: 1.40
  ```

---

### 4. Running Test Suite & POD Audit
- **Run all 399 unit tests**:
  ```bash
  ./gradlew test
  ```
- **Run the full POD compliance audit report**:
  ```bash
  ./gradlew testAudit
  ```

---

## 👥 Pod Ownership & Task Assignment Matrix

| Pod | Module | Responsibilities & Data Structures Implemented |
|---|---|---|
| **Pod 1: Search Engine** | `algorithms` / `datastructures` | `DynamicArrayList`, Linear Search ($O(N)$), Binary Search ($O(\log N)$) |
| **Pod 2: Sorting Engine** | `algorithms` | Selection Sort ($O(N^2)$), Insertion Sort ($O(N^2)$), Merge Sort ($O(N \log N)$), QuickSort ($O(N \log N)$) |
| **Pod 3: Linked & Stack** | `datastructures` | SinglyLinkedList, DoublyLinkedList, `ArrayStack` (LIFO Undo/Audit Engine) |
| **Pod 4: Queueing & Heap** | `datastructures` | `CircularQueue` (FIFO Routine), `ArrayDeque` (P1 Emergency Preemption), `MinHeapPriorityQueue` (P2 Urgent Triage) |
| **Pod 5: Trees & Hashing** | `datastructures` | BinarySearchTree, RedBlackTree ($O(\log N)$), B-Tree ($t=2$), `CustomHashTable`, `CustomMap`, `CustomSet`, `DisjointSet` |
| **Pod 6: Graph & Optimisation** | `graph` / `scheduling` | `HospitalNetworkGraph` (Adjacency Matrix/List), BFS/DFS, `DijkstraShortestPath`, `KruskalMST`, `PrimMST`, `HospitalDispatchCentre` (Greedy), `CapacityOptimizer` (0/1 Knapsack DP) |
| **Pod 7: Data & Benchmark** | `db` / `benchmark` | SQLite DB schema/loader, 399 Unit Tests, 10 Trace Tables, Benchmark Suite ($N \le 3\text{M}$), Web GUI & Render Cloud Deployment |

---

## 📁 Repository Directory Layout

```
Hospital-Service-DSA/
├── Dockerfile                       # Multi-stage Docker packaging for cloud hosting
├── render.yaml                      # Render 1-click cloud deployment configuration
├── datastructures/                  # 14 custom data structures (Zero java.util collections)
├── algorithms/                      # Searching (Linear/Binary) & Sorting (Quick/Merge/Insert/Select)
├── graph/                           # BFS, DFS, Dijkstra, Kruskal MST, Prim MST
├── scheduling/                      # 3-Tier Priority Triage & 0/1 Knapsack DP Cargo Optimizer
├── db/                              # SQLite schema, JDBC repositories, and CSV seed loader
├── benchmark/                       # Empirical performance runner, timer harness, and CSV logs
├── app/                             # CLI Console Menu & Student Parameter Calculator
├── gui/                             # Web GUI server, Leaflet GIS map, and dashboard assets
├── data/
│   ├── hospital_system.db           # SQLite database
│   └── seed/*.csv                   # Seed dataset (50 hospitals, 154 roads, 500 requests, 45 resources)
├── docs/                            # Trace tables, mathematical proofs, counterexamples, dev-log
└── docs-site/                       # Astro / Starlight documentation portal
```

---

## 📜 Development & Git Conventions
1. **Branching**: Create feature branches off `dev` (e.g. `feature/pod1-binary-search`, `feature/pod6-dijkstra`).
2. **Pull Requests**: Target `dev` branch. Ensure `./gradlew test testAudit` passes cleanly with 0 failures before merging.
3. **No `java.util` Collections**: Assessed core logic must strictly use custom data structures in `datastructures`.
