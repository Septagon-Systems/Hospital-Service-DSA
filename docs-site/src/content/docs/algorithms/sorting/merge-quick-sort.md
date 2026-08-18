---
title: "Merge Sort & QuickSort Algorithms"
---

## 1. Merge Sort Algorithm

### Algorithm Description
Custom recursive Merge Sort implemented in `edu.ug.dcit308.hospital.algo.sort.MergeSort`.

- **Time Complexity**: $O(n \log n)$ best, average, and worst case.
- **Auxiliary Space**: $O(n)$ using a single workspace auxiliary array passed recursively.
- **Stability**: Stable (preserves original relative order of equal elements).

---

### Hospital Patient Waiting Times Example
- **Input Array**: `[38, 12, 27, 5]`
- **Expected Output**: `[5, 12, 27, 38]`

#### Step-by-Step Recursion & Merge Trace Table

| Step | Operation | Array Range (`low..high`) | Sub-array State | Merge Action | Resulting Sub-array |
|:---:|:---|:---:|:---|:---|:---|
| 1 | Initial call | `[0..3]` | `[38, 12, 27, 5]` | Divide at `mid=1` | Left `[0..1]`, Right `[2..3]` |
| 2 | Divide left half | `[0..1]` | `[38, 12]` | Divide at `mid=0` | Left `[0..0]`, Right `[1..1]` |
| 3 | Base cases | `[0..0]`, `[1..1]` | `[38]`, `[12]` | Single elements | Base cases reached |
| 4 | Merge left sub-array | `[0..1]` | `[38]` & `[12]` | Compare $12 < 38$ | **`[12, 38]`** |
| 5 | Divide right half | `[2..3]` | `[27, 5]` | Divide at `mid=2` | Left `[2..2]`, Right `[3..3]` |
| 6 | Base cases | `[2..2]`, `[3..3]` | `[27]`, `[5]` | Single elements | Base cases reached |
| 7 | Merge right sub-array | `[2..3]` | `[27]` & `[5]` | Compare $5 < 27$ | **`[5, 27]`** |
| 8 | Final merge | `[0..3]` | `[12, 38]` & `[5, 27]` | Compare & merge halves | **`[5, 12, 27, 38]`** |

---

## 2. QuickSort Algorithm (Lomuto Partitioning)

### Algorithm Description
Custom recursive QuickSort implemented in `edu.ug.dcit308.hospital.algo.sort.QuickSort` using the Lomuto partition scheme.

- **Time Complexity**:
  - **Average Case**: $O(n \log n)$ when pivot divides partitions reasonably evenly.
  - **Worst Case**: $O(n^2)$ when array is already sorted or reverse sorted with fixed end-pivot.
- **Auxiliary Space**: $O(\log n)$ average recursion stack depth (in-place partitioning).
- **Stability**: Unstable (in-place swaps may invert equal key ordering).

---

### Step-by-Step Execution Trace

- **Input Array**: `[10, 7, 8, 9, 1, 5]` ($n = 6$)

#### Call 1: `quickSort(arr, low=0, high=5)`
- **Pivot**: `arr[5] = 5`, initialized pointer `i = low - 1 = -1`.

| $j$ | `arr[j]` | Comparison ($\le 5$) | $i$ | Action | Array State After Swap |
|:---:|:---:|:---:|:---:|:---|:---|
| 0 | 10 | $10 \le 5$ (False) | -1 | No swap | `[10, 7, 8, 9, 1, 5]` |
| 1 | 7 | $7 \le 5$ (False) | -1 | No swap | `[10, 7, 8, 9, 1, 5]` |
| 2 | 8 | $8 \le 5$ (False) | -1 | No swap | `[10, 7, 8, 9, 1, 5]` |
| 3 | 9 | $9 \le 5$ (False) | -1 | No swap | `[10, 7, 8, 9, 1, 5]` |
| 4 | 1 | $1 \le 5$ (True) | 0 | `swap(arr[0], arr[4])` | `[1, 7, 8, 9, 10, 5]` |

- **Final Pivot Placement**: `swap(arr[i + 1], arr[high])` $\to$ `swap(arr[1], arr[5])`.
- **Segment Result**: `[1, 5, 8, 9, 10, 7]`.
- **Pivot Index Returned**: $p = 1$ (value `5` in permanent sorted position).

---

#### Call 2: `quickSort(arr, low=0, high=0)`
- `low >= high` $\to$ Base case reached (single element `[1]`, trivially sorted).

---

#### Call 3: `quickSort(arr, low=2, high=5)`
- **Sub-array**: `[8, 9, 10, 7]`
- **Pivot**: `arr[5] = 7`, initialized pointer `i = low - 1 = 1`.

| $j$ | `arr[j]` | Comparison ($\le 7$) | $i$ | Action | Array State After Swap |
|:---:|:---:|:---:|:---:|:---|:---|
| 2 | 8 | $8 \le 7$ (False) | 1 | No swap | `[1, 5, 8, 9, 10, 7]` |
| 3 | 9 | $9 \le 7$ (False) | 1 | No swap | `[1, 5, 8, 9, 10, 7]` |
| 4 | 10 | $10 \le 7$ (False) | 1 | No swap | `[1, 5, 8, 9, 10, 7]` |

- **Final Pivot Placement**: `swap(arr[i + 1], arr[high])` $\to$ `swap(arr[2], arr[5])`.
- **Segment Result**: `[1, 5, 7, 9, 10, 8]`.
- **Pivot Index Returned**: $p = 2$ (value `7` in permanent sorted position).

---

#### Call 4: `quickSort(arr, low=2, high=1)`
- `low >= high` $\to$ Base case reached (empty sub-array).

---

#### Call 5: `quickSort(arr, low=3, high=5)`
- **Sub-array**: `[9, 10, 8]`
- **Pivot**: `arr[5] = 8`, initialized pointer `i = low - 1 = 2`.

| $j$ | `arr[j]` | Comparison ($\le 8$) | $i$ | Action | Array State After Swap |
|:---:|:---:|:---:|:---:|:---|:---|
| 3 | 9 | $9 \le 8$ (False) | 2 | No swap | `[1, 5, 7, 9, 10, 8]` |
| 4 | 10 | $10 \le 8$ (False) | 2 | No swap | `[1, 5, 7, 9, 10, 8]` |

- **Final Pivot Placement**: `swap(arr[i + 1], arr[high])` $\to$ `swap(arr[3], arr[5])`.
- **Segment Result**: `[1, 5, 7, 8, 10, 9]`.
- **Pivot Index Returned**: $p = 3$ (value `8` in permanent sorted position).

---

#### Call 6: `quickSort(arr, low=3, high=2)`
- `low >= high` $\to$ Base case reached.

---

#### Call 7: `quickSort(arr, low=4, high=5)`
- **Sub-array**: `[10, 9]`
- **Pivot**: `arr[5] = 9`, initialized pointer `i = low - 1 = 3`.

| $j$ | `arr[j]` | Comparison ($\le 9$) | $i$ | Action | Array State After Swap |
|:---:|:---:|:---:|:---:|:---|:---|
| 4 | 10 | $10 \le 9$ (False) | 3 | No swap | `[1, 5, 7, 8, 10, 9]` |

- **Final Pivot Placement**: `swap(arr[i + 1], arr[high])` $\to$ `swap(arr[4], arr[5])`.
- **Segment Result**: `[1, 5, 7, 8, 9, 10]`.
- **Pivot Index Returned**: $p = 4$ (value `9` in permanent sorted position).

---

### Final Result
- **Final Sorted Array**: `[1, 5, 7, 8, 9, 10]`

---

## 3. Comparison: Merge Sort vs QuickSort

| Metric | Merge Sort | QuickSort (Lomuto) |
|:---|:---|:---|
| **Best-Case Time** | $O(n \log n)$ | $O(n \log n)$ |
| **Average-Case Time** | $O(n \log n)$ | $O(n \log n)$ |
| **Worst-Case Time** | $O(n \log n)$ | $O(n^2)$ |
| **Auxiliary Space** | $O(n)$ workspace array | $O(\log n)$ recursion stack |
| **In-Place?** | No | **Yes** |
| **Stability** | **Stable** | Unstable |
| **Primary Use Case** | Linked lists, guaranteed $O(n \log n)$, external sorting | Cache-friendly in-place general array sorting |
