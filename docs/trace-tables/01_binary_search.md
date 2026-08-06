# Trace Table 1: Binary Search Algorithm

## Algorithm Description

The custom Binary Search algorithm is implemented in
`edu.ug.dcit308.hospital.algo.search.BinarySearch`.

It searches for a target value in a sorted array by repeatedly dividing the
search interval into two halves. The algorithm has a time complexity of
`O(log n)`.

## Input Used

- Sorted array: `[2, 4, 6, 8, 10]`
- Target value: `8`
- Expected result: index `3`

## Trace Table Execution

| Step | `low` index | `high` index | `mid` index | Element at `mid` | Target | Comparison | Action |
|---|---:|---:|---:|---:|---:|---|---|
| 1 | 0 | 4 | 2 | 6 | 8 | `Target > Mid` | Set `low = mid + 1`, therefore `low = 3` |
| 2 | 3 | 4 | 3 | 8 | 8 | `Target == Mid` | **Match found at index 3** |

## Result

The target value `8` was found at index `3` after two iterations.

## Correctness Invariant

At the beginning of every iteration of the `while (low <= high)` loop, if the
target exists in the array, then it is located within the current search
interval from index `low` to index `high`.

When the midpoint value is smaller than the target, all elements from `low`
through `mid` can be safely discarded. When the midpoint value is greater than
the target, all elements from `mid` through `high` can be safely discarded.

Therefore, the algorithm never removes the target from the search interval when
the target exists.

## Complexity

- Best case: `O(1)` — the target is found at the first midpoint.
- Average case: `O(log n)`
- Worst case: `O(log n)`
- Auxiliary space: `O(1)` for the iterative implementation.