# Team Weekly Development Log

Per Brief Section 15.iv, each team must maintain a development log showing weekly progress, challenges, and decisions across all project phases.

---

## Week 0: Project Setup & Architecture Initialisation
- **Date Range**: 29 July 2026 – 31 July 2026
- **Context Selected**: Ghana Hospital & Clinic Operations (`Korle Bu`, `Ridge`, `37 Military`, `KATH`, `Manhyia`)
- **Key Milestones Accomplished**:
  - Gradle Multi-Module repository scaffolded with Java 21 LTS toolchain (`datastructures`, `algorithms`, `graph`, `db`, `app`, `scheduling`, `benchmark`).
  - SQLite JDBC database schema drafted (`data/schema/schema.sql`).
  - GitHub Actions CI pipeline configured (`.github/workflows/ci.yml`).
  - Pod responsibility matrix established (7 pods across team members).
- **Challenges & Decisions**:
  - Selected Hospital domain due to rich availability of geocoded facility lists from `data.gov.gh` and HOTOSM Ghana exports.
  - Enforced strict custom data structure rules across core modules (zero `java.util` collections allowed in data structure implementations).

---

## Week 1: Core Data Structures & Graph Foundation
- **Date Range**: 1 August 2026 – 4 August 2026
- **Key Milestones Accomplished**:
  - **Core Collections**: Implemented `SinglyLinkedList`, `DoublyLinkedList`, `CircularQueue`, and `DynamicArrayList` with fail-fast precondition validation.
  - **Red-Black Tree**: Implemented self-balancing `RedBlackTree` with color rotations, node unlinking, and 100% passing unit tests (`datastructure/red-black`).
  - **Hospital Network Graph**: Implemented `HospitalNetworkGraph` (adjacency array representation) supporting directed/undirected edges, weight updates, and isolated vertex management (`#34`).
  - **CI & Code Quality**: Integrated CodeRabbit review rules (`.github/REVIEW.md`) and customized `testAudit` Gradle task for test coverage tracking.
- **Challenges & Decisions**:
  - Fixed graph duplicate edge accumulation bug in `addEdge` by updating weights in-place when an edge already exists (`9a567ae`).
  - Established PECS rule (`<T extends Comparable<? super T>>`) across generic signatures to ensure subtype compatibility.

---

## Week 2: Algorithms, Search/Sort Pods & Complete Evidence Pack
- **Date Range**: 5 August 2026 – 8 August 2026
- **Key Milestones Accomplished**:
  - **Pod 1 (Search)**: Completed `BinarySearch` ($O(\log n)$) and `LinearSearch` ($O(n)$) with PECS generics, null guards, and unsorted input counterexample tests (`BinarySearchTest`).
  - **Pod 2 (Sorting)**: Implemented `InsertionSort` and `SelectionSort` with generic and primitive overloads, stability verification, benchmark counters (`comparisonCount`, `swapCount`), and trace tables (`02_insertion_sort.md`, `09_selection_sort.md`).
  - **Pod 5 (Tree & Hash)**: Implemented `BinarySearchTree` (type-safe `inOrder()`, `height()`) and `CustomHashTable` (bitwise hash masking `0x7fff_ffff`, `0.75` load factor threshold, dynamic rehashing) with 100% passing unit tests (`#43`).
  - **Pod 6 (Minimum Spanning Tree)**: Implemented `KruskalMST` (using custom `DisjointSet` union-find with path compression) and `PrimMST` (min-key selection) for Ghana hospital road network distance optimization (`#20`).
  - **Evidence Pack Completeness**: Authored proof sketches (`04_linked_list_loop_invariant.md` for `SinglyLinkedList` and `DoublyLinkedList`), trace tables (`01_binary_search.md`, `05_kruskal_prim.md`, `07_circular_queue_trace.md`, `10_binary_search_tree.md`, `11_custom_hash_table.md`), and counterexamples (`02_invalid_precondition_case.md`).
- **Challenges & Decisions**:
  - Resolved `ClassCastException` in `BinarySearchTree.inOrder()` by returning `Comparable<K>[]` to avoid generic array runtime cast traps (`f5fccac`).
  - Verified bitwise hash safety (`key.hashCode() & 0x7fff_ffff`) to ensure non-negative bucket indexing without sign-bit corruption.
  - Maintained pure $O(\log n)$ search behavior in `BinarySearch` by enforcing sortedness as a Javadoc precondition rather than performing linear scan checks.

---

## Current Status & Next Steps
- **Branch**: `dev`
- **Module Build Status**: `BUILD SUCCESSFUL` across all modules (100% unit tests passing).
- **Next Objectives**:
  - Merge remaining Pod PRs (MergeSort #6, QuickSort #7, B-Tree #39).
  - Implement higher-level collection wrappers (`CustomMap` #41, `CustomSet` #42).
  - Construct DB index page simulation and CLI application interface (`app`).
