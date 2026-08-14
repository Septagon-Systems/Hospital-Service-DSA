# Trace Table 12: Custom Map & Custom Set Implementation

## 1. Data Structure Overview

- **`CustomMap<K, V>`**: A generic key-value mapping wrapper encapsulating two backing storage strategies:
  1. `StorageStrategy.HASH_TABLE` (default): Amortised $O(1)$ lookup, insertion, and removal backed by `CustomHashTable`.
  2. `StorageStrategy.BINARY_SEARCH_TREE`: $O(h)$ search and ordered key management backed by `BinarySearchTree` ($h$ = tree height; skewed input can yield $O(n)$ worst-case performance).
- **`CustomSet<T>`**: A generic set collection guaranteeing unique elements built on top of `CustomMap<T, Boolean>`.

---

## 2. Hospital Operations Use Case: Unique Facility Registry & Bed Lookup

In the Ghana Hospital Service operations optimizer:
1. **`CustomSet<String>` (Unique Facility Registry)**:
   - Ensures no duplicate hospital facility codes are registered across regional networks (e.g., `KB-ACC-01`, `37M-ACC-02`, `KATH-KMS-01`).
2. **`CustomMap<String, HospitalStatus>` (Live Bed & Triage Lookup)**:
   - Maps each unique hospital code to its real-time operational status (e.g. available ICU beds, ambulance count).

---

## 3. Membership & Lookup Trace Execution

### Scenario: Facility Registration & Live Status Query

Input Operations Sequence:
1. `CustomSet.contains("37M-ACC-02")` $\to$ Returns `false` (Unregistered facility query).
2. `CustomSet.add("KB-ACC-01")` $\to$ Returns `true` (New registration).
3. `CustomSet.add("KB-ACC-01")` $\to$ Returns `false` (Duplicate rejected).
4. `CustomSet.add("37M-ACC-02")` $\to$ Returns `true` (Facility registration).
5. `CustomMap.put("KB-ACC-01", Status(ICU=15, Ambulances=3))` $\to$ Key-value pair mapped.
6. `CustomMap.get("KB-ACC-01")` $\to$ Returns `Status(ICU=15, Ambulances=3)` in $O(1)$ amortised time.
7. `CustomSet.contains("37M-ACC-02")` $\to$ Returns `true` (Registered facility query).
8. `CustomSet.remove("KB-ACC-01")` $\to$ Returns `true` (Registration removal).

### Step-by-Step Execution Trace Table

| Step | Data Structure | Operation | Input Parameter | Internal Action / Backing Call | Result / Output | Map/Set Size |
|:---:|:---|:---|:---|:---|:---|:---:|
| 1 | `CustomSet` | `contains(item)`| `"37M-ACC-02"` | `map.containsKey("37M-ACC-02")` returns `false` | `false` | 0 |
| 2 | `CustomSet` | `add(item)` | `"KB-ACC-01"` | `map.containsKey("KB-ACC-01")` returns `false` $\to$ `map.put("KB-ACC-01", true)` | `true` | 1 |
| 3 | `CustomSet` | `add(item)` | `"KB-ACC-01"` | `map.containsKey("KB-ACC-01")` returns `true` | `false` | 1 |
| 4 | `CustomSet` | `add(item)` | `"37M-ACC-02"` | `map.containsKey("37M-ACC-02")` returns `false` $\to$ `map.put("37M-ACC-02", true)` | `true` | 2 |
| 5 | `CustomMap` | `put(key, val)` | `"KB-ACC-01", ICU:15` | Hash code calculated $\to$ Bucket insertion in `CustomHashTable` | Key mapped | 1 |
| 6 | `CustomMap` | `get(key)` | `"KB-ACC-01"` | Bitwise mask hash lookup `key.hashCode() & 0x7fff_ffff` | `ICU:15` | 1 |
| 7 | `CustomSet` | `contains(item)`| `"37M-ACC-02"` | `map.containsKey("37M-ACC-02")` returns `true` | `true` | 2 |
| 8 | `CustomSet` | `remove(item)` | `"KB-ACC-01"` | `map.remove("KB-ACC-01")` | `true` | 1 |

---

## 4. Theoretical & Empirical Complexity Bounds

| Operation | `HASH_TABLE` Backend | `BINARY_SEARCH_TREE` Backend | Space Complexity |
|:---|:---:|:---:|:---:|
| `add(element)` / `put(key, val)` | $O(1)$ amortised | $O(h)$ ($h$ = tree height) | $O(1)$ per entry |
| `contains(element)` / `get(key)` | $O(1)$ amortised | $O(h)$ ($h$ = tree height) | $O(1)$ |
| `remove(element)` / `remove(key)` | $O(1)$ amortised | $O(h)$ ($h$ = tree height) | $O(1)$ |
| `size()` / `isEmpty()` / `clear()` | $O(1)$ | $O(1)$ | $O(1)$ |

---

## 5. Correctness & Compliance Verification

- **Zero Built-In Collections**: `CustomMap` and `CustomSet` use zero `java.util` collection classes (`HashMap`, `HashSet`, `TreeMap`, `TreeSet`).
- **Storage Strategy Flexibility**: Allows switching between hash-based and tree-based data structures without altering application client code.
- **Fail-Fast Safety**: Null elements/keys in `add` and `remove` reject `null` with `IllegalArgumentException`, while `CustomSet.contains(null)` returns `false`. Non-`Comparable` keys with BST strategy fail fast with `ClassCastException`.
