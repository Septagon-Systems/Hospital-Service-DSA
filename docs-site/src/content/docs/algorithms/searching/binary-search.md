---
title: "Binary Search Algorithm"
---

## Successful Search Trace

### Input

- Sorted array: `[2, 4, 6, 8, 10]`
- Target value: `8`
- Expected result: index `3`

| Step | low index | high index | mid index | Element at mid | Target | Comparison | Action |
|------|-----------|------------|-----------|----------------|--------|------------|--------|
| 1 | 0 | 4 | 2 | 6 | 8 | Target > Mid | low = mid + 1 = 3 |
| 2 | 3 | 4 | 3 | 8 | 8 | Target == Mid | Match found at index 3 |

---

## Unsuccessful Search Trace

### Input

- Sorted array: `[2, 4, 6, 8, 10]`
- Target value: `7`
- Expected result: `-1`

| Step | low index | high index | mid index | Element at mid | Target | Comparison | Action |
|------|-----------|------------|-----------|----------------|--------|------------|--------|
| 1 | 0 | 4 | 2 | 6 | 7 | Target > Mid | low = 3 |
| 2 | 3 | 4 | 3 | 8 | 7 | Target < Mid | high = 2 |
| 3 | 3 | 2 | - | - | 7 | low > high | return -1 |

---

## Binary Search Precondition

Binary Search assumes that the input array is already sorted according to the same ordering used for searching.

The implementation does not verify that the array is sorted because checking this would require O(n) time and would remove the algorithm's O(log n) efficiency.

Therefore, supplying a sorted array is the caller's responsibility.

---

## Complexity

- Best Case: O(1)
- Average Case: O(log n)
- Worst Case: O(log n)
- Auxiliary Space: O(1)