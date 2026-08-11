---

# MergeSort Trace

## Algorithm

`MergeSort`

## Purpose

MergeSort is a divide-and-conquer sorting algorithm that recursively divides an array into smaller sub-arrays, sorts them, and merges them back together in sorted order.

The implementation uses a single auxiliary array during the sorting process to avoid repeatedly creating temporary arrays during recursion.

The main operations are:

- Divide the array into two halves
- Recursively sort the left half
- Recursively sort the right half
- Merge the two sorted halves

---

## Hospital Sorting Example

Consider a hospital system where patient waiting times, measured in minutes, need to be arranged from smallest to largest.

Suppose the waiting times are:

`[38, 12, 27, 5]`

MergeSort divides the values until each sub-array contains one element and then merges them back in sorted order.

### Trace

| Step | Operation | Result |
|------|-----------|--------|
| 1 | Initial array | `[38, 12, 27, 5]` |
| 2 | Divide array | `[38, 12]` and `[27, 5]` |
| 3 | Divide left half | `[38]` and `[12]` |
| 4 | Merge left half | `[12, 38]` |
| 5 | Divide right half | `[27]` and `[5]` |
| 6 | Merge right half | `[5, 27]` |
| 7 | Merge both sorted halves | `[5, 12, 27, 38]` |

---

## Result

The final sorted array is:

```text
[5, 12, 27, 38]