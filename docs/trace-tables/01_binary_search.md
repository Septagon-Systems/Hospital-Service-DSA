# Trace Table 1: Binary Search Algorithm

> [!NOTE]
> **TEMPLATE & EXAMPLE ONLY**: This is a starter template for Pod 1. Replace the sample data below with your team's actual execution trace using your dataset and student index parameters before final submission.

## Algorithm Description
Custom binary search implemented in `edu.ug.dcit308.hospital.algo.search.BinarySearch`. Searches for a specific hospital ID or patient request ID in a sorted array.

## Trace Table Execution
| Step | `low` index | `high` index | `mid` index | Element at `mid` | Target | Comparison | Action |
|---|---|---|---|---|---|---|---|
| 1 | 0 | 15 | 7 | Hospital_08 (Korle Bu) | Hospital_12 (Tema Gen) | `Target > Mid` | `low = mid + 1` (8) |
| 2 | 8 | 15 | 11 | Hospital_12 (Tema Gen) | Hospital_12 (Tema Gen) | `Target == Mid` | **Match Found at index 11** |

## Correctness Invariant
At the start of each iteration of the `while (low <= high)` loop, if the target key exists in the array `A[0..N-1]`, then `A[low] <= target <= A[high]`.
