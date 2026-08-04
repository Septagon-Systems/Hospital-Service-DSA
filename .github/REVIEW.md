# 🏛️ CodeRabbit Comprehensive Code Review Guidelines & Specification
**Project**: Ghana Smart Hospital Operations Optimizer  
**Course**: DCIT 204/308 Data Structures & Algorithms (I & II) — University of Ghana  
**Document Target**: Automated GitHub PR Reviewer Guidelines (`CodeRabbit`)

---

## 1. 📍 Domain & System Context
The **Ghana Smart Hospital Operations Optimizer** models, routes, dispatches, and optimizes healthcare logistics across Ghana (including Korle Bu Teaching Hospital, Ridge Hospital, Komfo Anokye Teaching Hospital, Tema General, and regional clinics).

All code submitted via Pull Requests must align with the **DCIT 204/308 Joint Semester Project Brief**. CodeRabbit must evaluate code quality, algorithmic correctness, custom data structure compliance, and edge-case testing against the rules below.

---

## 2. 🚫 STRICT RULE #1: NO BUILT-IN JAVA COLLECTIONS
- **Forbidden Classes**: Built-in Java collection classes (`java.util.ArrayList`, `java.util.HashMap`, `java.util.TreeMap`, `java.util.PriorityQueue`, `java.util.Stack`, `java.util.ArrayDeque`, `java.util.LinkedList`, `java.util.TreeSet`, `java.util.HashSet`) are **STRICTLY FORBIDDEN** for core data structure and algorithm logic.
- **Mandatory Custom Structures**: All core operations must rely exclusively on custom-built structures inside `edu.ug.dcit308.hospital.ds.*` (e.g., `DynamicArrayList`, `SinglyLinkedList`, `DoublyLinkedList`, `CircularQueue`, `ArrayStack`, `MinHeapPriorityQueue`, `BinarySearchTree`, `RedBlackTree`, `CustomHashTable`, `DisjointSet`, `GraphAdjacencyList`, `GraphAdjacencyMatrix`).
- **Permitted Java Standard Utilities**: Standard Java packages are permitted **ONLY** for file I/O, `System.out` printing, JDBC/database interactions (`java.sql`), math operations (`java.lang.Math`), and JUnit 5 test assertions (`org.junit.jupiter`).

---

## 3. 📦 Core Data Structure Specifications (Brief Section 6)

When reviewing PRs modifying files under `datastructures/`, enforce these contract rules:

### A. Array-Backed List (`DynamicArrayList<T>`)
- **Required Operations**: `add(T)`, `add(int, T)`, `get(int)`, `set(int, T)`, `remove(int)`, `clear()`, `size()`, `isEmpty()`.
- **Resize Strategy**: Must double capacity ($2 \times$) upon overflow.
- **Memory Leak Protection**: Removed elements (`remove`, `clear`) **MUST** be explicitly set to `null` (`elements[--size] = null`) to allow garbage collection.
- **Shifting**: Index shifts must validate bounds (`0 <= index < size` for get/remove, `0 <= index <= size` for insert).

### B. Linked Lists (`SinglyLinkedList<T>` & `DoublyLinkedList<T>`)
- **Required Operations**: `addFirst`, `addLast`, `insertAfter`, `removeFirst`, `removeLast`, `remove`, `iterator`.
- **Doubly LinkedList**: Pointers must maintain both `next` and `prev` invariant consistency. `get(index)` should optimize traversal starting from `head` or `tail` depending on index location.

### C. Stack (`ArrayStack<T>`) & Queues (`CircularQueue<T>`, `CustomArrayDeque<T>`)
- **Stack**: `push`, `pop`, `peek`, `isEmpty`, `size`. Used for audit logs and recursion simulations.
- **Queue**: `enqueue`, `dequeue`, `peek`, `isEmpty`, `size`. Must implement circular array wrap-around handling (`front` and `rear` pointers using modulo arithmetic `% capacity`).

### D. Heap / Priority Queue (`MinHeapPriorityQueue<T>`)
- **Required Operations**: `insert`, `extractMin` / `extractMax`, `peek`, `heapify`.
- **Array Representation**: Child indices `2i + 1` and `2i + 2`, parent `(i - 1) / 2`.

### E. Self-Balancing Tree (`RedBlackTree<K, V>`)
- **Required Operations**: `insert`, `search`, `contains`, `delete`, `getHeight`, `getSize`, `isEmpty`.
- **Invariants**:
  1. Every node is either RED or BLACK.
  2. The root is always BLACK.
  3. Red nodes cannot have Red children (no consecutive Red nodes).
  4. Every path from a node to descendant leaf/null pointers contains the exact same number of Black nodes.
- **Encapsulation**: `delete(K key)` **MUST** return the removed payload value (`V`), NOT internal `Node` references.
- **Height Invariant**: Maximum height must satisfy $h \le 2 \log_2(n + 1)$.

### F. Custom Hash Table (`CustomHashTable<K, V>`) & Disjoint Set
- **Hash Table**: `put`, `get`, `remove`, `size`, load factor tracking (`size / capacity`). Must handle collisions via chaining (LinkedList or Red-Black Tree buckets).
- **Disjoint Set**: `makeSet`, `find`, `unionByRank` / `unionBySize`, with path compression.

---

## 4. ⚙️ Algorithm & Strategy Requirements (Brief Section 7)

When reviewing PRs modifying files under `algorithms/`, `graph/`, or `scheduling/`, enforce these specifications:

1. **Searching**:
   - `LinearSearch`: Works on unsorted input ($O(n)$).
   - `BinarySearch`: Requires sorted array precondition check ($O(\log n)$).
2. **Sorting**:
   - `SelectionSort` & `InsertionSort`: $O(n^2)$ baseline comparisons.
   - `MergeSort` & `Quicksort`: $O(n \log n)$ divide-and-conquer strategy.
3. **Graph & Network Algorithms**:
   - `BFS` & `DFS`: Graph traversals for location reachability.
   - `Dijkstra`: Shortest path routing between hospital facilities using custom priority queue.
   - `Prim` & `Kruskal`: Minimum Spanning Tree (MST) for connectivity infrastructure using custom Disjoint Set.
4. **Optimization Strategies**:
   - **Greedy Strategy**: Priority-based triage/resource dispatch.
   - **Dynamic Programming (DP)**: Knapsack-style emergency budget or request selection (tabulation/memoization table reconstruction).

---

## 5. 🧪 Testing & Correctness Expectations (Brief Section 10)

Every Pull Request adding or modifying code **MUST** include JUnit 5 test coverage in `src/test/java/` following the **Testing Triad**:

1. **Normal Case**: Standard operation, correct return values, and expected size/state mutations.
2. **Boundary / Edge Cases**:
   - Empty structure (`size == 0`).
   - Single-element structure (`size == 1`).
   - Insertion/deletion at capacity limits ($100$ to $20,000$ elements).
   - Maximum height bounds validation for trees ($h \le 2 \log_2(n + 1)$).
3. **Invalid Input Cases**:
   - Out-of-bounds index access throwing `IndexOutOfBoundsException`.
   - Null key/element handling throwing `IllegalArgumentException` or `NullPointerException`.
   - Duplicate key handling (in-place value update without increasing tree/list size).

---

## 6. 📝 Code Style & PR Guidelines

- **Concise Docstrings**: Use clean Javadoc (`/** ... */`) only on public classes, constructors, and methods. Keep comments natural and human-like.
- **Preserve Existing Comments**: Preserve original developer explanatory notes.
- **Clean Diff Scoping**: PRs must remain focused on their specific feature or bug fix without unnecessary whitespace or unrelated refactoring.
- **Build Verification**: All code submitted must compile cleanly and pass `./gradlew test` without failures.
