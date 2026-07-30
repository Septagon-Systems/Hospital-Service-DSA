# Ghana Smart Hospital Operations Optimizer

**DCIT 204/308 Data Structures & Algorithms (I & II) Joint Semester Project**  
*Department of Computer Science, University of Ghana*

---

## 📌 Project Overview
The **Ghana Smart Hospital Operations Optimizer** is an operations management platform designed to model, route, dispatch, and optimize healthcare logistics across Ghana (including Korle Bu, Ridge Hospital, Komfo Anokye Teaching Hospital, Tema General, and regional healthcare centers).

The project adheres strictly to the **DCIT 204/308 Joint DSA Project Brief**:
- **Custom Data Structure Library**: Zero usage of `java.util` collection classes (`HashMap`, `ArrayList`, `PriorityQueue`, `Stack`, `ArrayDeque`, `TreeMap`) in assessed modules.
- **SQLite Database Persistence**: Embedded SQLite storage (`data/schema/schema.sql`) recording operational entities and algorithm benchmark execution runs.
- **Java 21 LTS Multi-Module Gradle Architecture**: Divided into 7 isolated modules mapping to team pod responsibilities.
- **Empirical Benchmarking Suite**: Generates execution timing ($ns$) and memory footprint ($KB$) across varying dataset sizes ($100$ to $20,000$ records).
- **Correctness & Verification Evidence**: Includes 40+ unit tests, 6 trace tables, 3 proof sketches, and 2 failure counterexamples under `docs/`.

---

## 👥 Pod Ownership & Task Assignment Matrix (~15 Team Members)

All package directories under `datastructures/`, `algorithms/`, `graph/`, and `scheduling/` contain clean folder scaffolding (`.gitkeep`). Each pod is responsible for building their assigned data structures and algorithms from scratch:

| Pod | Module | Responsibilities & Data Structures To Implement |
|---|---|---|
| **Pod 1: Search Engine** | `algorithms` / `datastructures` | `DynamicArrayList`, Linear Search, Binary Search ($O(\log n)$) |
| **Pod 2: Sorting Engine** | `algorithms` | Selection Sort, Insertion Sort, Merge Sort, Quicksort ($O(n \log n)$) |
| **Pod 3: Linked & Stack** | `datastructures` | Singly/Doubly Linked Lists, ArrayStack (Audit/Undo log) |
| **Pod 4: Queueing & Heap** | `datastructures` | Circular Queue, Custom ArrayDeque, Min-Heap Priority Queue |
| **Pod 5: Trees & Hashing** | `datastructures` | Binary Search Tree, Red-Black Tree, B-Tree, Custom HashTable |
| **Pod 6: Graph & Optimisation** | `graph` / `scheduling` | Adjacency Matrix/List, BFS/DFS, Dijkstra Shortest Path, Prim/Kruskal MST, Greedy, DP |
| **Pod 7: Data & Benchmark** | `db` / `benchmark` | SQLite DB schema/loader, 40+ Unit Tests, Trace Tables, Benchmark Suite |

---

## 🛠️ Build & Execution Instructions

### Prerequisites
- **JDK 21 LTS** installed and configured (`java -version`).
- **Git** installed.

### 1. Build Entire Project
```bash
./gradlew build
```

### 2. Run All Unit Tests across Modules
```bash
./gradlew test
```

### 3. Run Main Interactive Application
```bash
./gradlew :app:run
```

### 4. Derive Your Student Algorithm Parameters (Brief Section 2.iii)
Run the application passing your student index number as an argument to compute your student-specific parameters:
```bash
./gradlew :app:run --args="10982345"
```

---

## 📁 Repository Directory Layout

```
Hospital-Service-DSA/
├── .github/
│   ├── pull_request_template.md     # PR Checklist for Pod Leads
│   ├── ISSUE_TEMPLATE/              # Templates for Features, Bugs, and Evidence
│   └── workflows/ci.yml             # GitHub Actions Java 21 Test Pipeline
├── gradle/libs.versions.toml        # Version Catalog (JUnit 5, SQLite JDBC, XChart)
├── settings.gradle.kts              # Multi-Module Project Settings
├── build.gradle.kts                 # Root Gradle Configuration (Java 21 Toolchain)
├── gradlew                          # Executable Gradle Wrapper Script (Linux/macOS)
├── gradlew.bat                      # Executable Gradle Wrapper Script (Windows)
├── data/
│   ├── schema/schema.sql            # SQLite DDL Schema Definition
│   ├── seed/.gitkeep                # Directory for scraped OSM/Ghana Health dataset
│   └── raw/.gitkeep                 # Directory for raw OSM extract files
├── docs/
│   ├── dev-log.md                   # Weekly Progress Log (Brief Section 15.iv)
│   ├── trace-tables/*.md            # Template Disclaimers for Trace Tables 01 - 06
│   ├── proof-sketches/*.md          # Template Disclaimers for Proof Sketches 01 - 03
│   └── counterexamples/*.md         # Template Disclaimers for Counterexamples 01 - 02
├── results/
│   ├── csv/.gitkeep                 # Benchmark CSV Output Directory
│   └── graphs/.gitkeep              # Benchmark Plot Graphs Directory
├── datastructures/                  # Custom Data Structures (Pods 3, 4, 5) - Clean Package Folders
├── algorithms/                      # Searching & Sorting Engines (Pods 1, 2) - Clean Package Folders
├── graph/                           # Graph Algorithms & Shortest Path (Pod 6) - Clean Package Folders
├── scheduling/                      # Emergency Triage & Dispatch Engine - Clean Package Folders
├── db/                              # SQLite JDBC Data Access Layer (Pod 7)
├── benchmark/                       # Empirical Performance Test Harness (Pod 7)
├── app/                             # Interactive Console Menu Entry Point
└── gui/                             # Optional JavaFX Graphical UI
```

---

## 📜 Development Conventions
1. **Branching**: Create feature branches off `dev` (e.g. `feature/pod1-binary-search`, `feature/pod6-dijkstra`).
2. **Pull Requests**: Target `dev` branch. Ensure `./gradlew test` passes cleanly before submitting.
3. **No `java.util` Collections**: Core logic must use custom data structures implemented in `datastructures`.
