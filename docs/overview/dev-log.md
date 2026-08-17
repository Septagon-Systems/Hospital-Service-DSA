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

## Week 3: Stack/Deque/MinHeap Integration, SelectionSort PR Merge & CI Reporting
- **Date Range**: 9 August 2026 – 12 August 2026
- **Key Milestones Accomplished**:
  - **Pod 3 & 4 Integration (PR #33 Merged)**: Merged `ArrayStack`, `ArrayDeque`, and `MinHeapPriorityQueue` backed by custom `DynamicArrayList` (zero `java.util` collections used). Included unit tests and trace documents (`array-stack-trace.md`, `array-deque-trace.md`, `dynamic-array-resize-trace.md`, `min-heap-dispatch-trace.md`).
  - **Selection Sort Integration (PR #36 Merged)**: Merged `SelectionSort` featuring PECS generics `<T extends Comparable<? super T>>`, benchmark counters (`comparisonCount`, `swapCount`), unit tests, and trace table (`09_selection_sort.md`).
  - **Automated CI Reporting (PR #45 Merged)**: Enabled JaCoCo XML reports (`xml.required.set(true)` in `build.gradle.kts`), enhanced `testAudit` to count individual test cases and verify the 40+ test requirement, and added automated sticky PR coverage comments in `.github/workflows/ci.yml`.
  - **MergeSort Optimization**: Refactored `MergeSort` to use a single auxiliary workspace array (`aux`), implemented PECS generics, primitive `int[]` overload, unit test suite (`MergeSortTest.java`), and trace table (`03_merge_quick_sort.md`).
- **Challenges & Decisions**:
  - Resolved `N/A` line coverage reporting in GitHub Actions by explicitly enabling JaCoCo XML output in `build.gradle.kts` and normalizing relative path splitting in `ci.yml`.
  - Configured `testAudit` to depend on subproject `test` tasks (`dependsOn(subprojects.map { it.tasks.named("test") })`) to guarantee report availability before auditing.

---

## Week 4: Astro Documentation Portal, Canonical Restructuring & Cloudflare Deployment
- **Date Range**: 13 August 2026 – 16 August 2026
- **Key Milestones Accomplished**:
  - **Astro Starlight Documentation Portal (`docs-site`)**:
    - Scaffolded and deployed an interactive technical documentation portal built with Astro 5, Starlight, KaTeX math typesetting, and Pagefind client-side search indexing.
    - Implemented a bespoke glassmorphism design system featuring a centered floating capsule top header with backdrop blur, symmetrical detached floating sidebars, and auto-hiding scrollbars.
    - Added responsive mobile brand transitions that crossfade smoothly from the full "SEPTAGON SYSTEMS" logo to a compact "SEPTAGON" brand mark on scroll.
    - Built lightweight fullscreen table inspection modals with clean square icon toggles.
  - **Canonical `docs/` Taxonomy Restructuring**:
    - Reorganized legacy flat folders (`trace-tables/`, `proof-sketches/`, `counterexamples/`) into clean, modular categories: `overview/`, `data-structures/{linear,trees,hashing,graphs}`, and `algorithms/{searching,sorting,graph,optimization}` using `git mv` to preserve commit history.
  - **Dynamic Content Synchronization & CI Automation**:
    - Authored `docs-site/populate_docs.py` with dynamic recursive directory traversal (`rglob`) and automatic frontmatter title extraction from markdown H1 headers.
    - Added `"prebuild"` and `"predev"` lifecycle hooks in `package.json` to guarantee documentation sync on every local and cloud build.
    - Created a streamlined pure-Python GitHub Actions workflow (`.github/workflows/docs-sync.yml`) for instant PR validation and automated sync commits on merge.
  - **Cloudflare Pages Production Deployment**:
    - Configured Cloudflare Pages static assets pipeline via `docs-site/wrangler.jsonc` and approved pnpm build dependencies in `.npmrc`.
    - Deployed 27 static documentation pages globally with clean builds in ~3.8 seconds.
  - **Algorithm & Graph Complexity Verification**:
    - Verified `MergeSort` integration with $O(E \log E)$ edge sorting in `KruskalMST.java` and synchronized complexity documentation in `kruskal-prim.md`.
- **Challenges & Decisions**:
    - Resolved fixed positioning centering in CSS by combining `left: 0; right: 0;` with `margin: 0 auto; width: 95%; max-width: 1480px;`.
    - Solved Starlight TOC width overflow and text margin truncation by resetting inner container widths to 100% and applying explicit padding to the parent card container.
    - Streamlined CI docs sync to pure Python 3.11, removing Node/pnpm overhead from GitHub Actions while letting Cloudflare handle static builds.

---

## Current Status & Next Steps
- **Branch**: `dev`
- **Module Build Status**: `BUILD SUCCESSFUL` across all modules (179 passing unit tests + 27 static documentation pages compiled).
- **Next Objectives**:
  - Merge `feature/custom-map-set-wrappers` (`CustomMap` #41, `CustomSet` #42) into `dev`.
  - Complete `QuickSort` and `B-Tree` (#39) implementations and benchmarks.
  - Finalize database index page simulation and CLI application interface (`app`).
