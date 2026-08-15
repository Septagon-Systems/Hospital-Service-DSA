\# QuickSort Trace Table



Input: \[10, 7, 8, 9, 1, 5]



\## Call: quickSort(arr, low=0, high=5)

Pivot = arr\[5] = 5



| j | arr\[j] | Comparison (<=5) | i  | Array state after swap   |

|---|--------|-------------------|----|---------------------------|

| 0 | 10     | No                | -1 | \[10, 7, 8, 9, 1, 5]      |

| 1 | 7      | No                | -1 | \[10, 7, 8, 9, 1, 5]      |

| 2 | 8      | No                | -1 | \[10, 7, 8, 9, 1, 5]      |

| 3 | 9      | No                | -1 | \[10, 7, 8, 9, 1, 5]      |

| 4 | 1      | Yes               | 0  | \[1, 7, 8, 9, 10, 5]      |



Final swap: swap(arr\[i+1], arr\[high]) -> swap(arr\[1], arr\[5])

Result: \[1, 5, 8, 9, 10, 7]

Pivot index returned: 1



\## Call: quickSort(arr, low=0, high=0)

Base case (low >= high) -> returns immediately, no work done.



\## Call: quickSort(arr, low=2, high=5)

Array segment: \[8, 9, 10, 7]

Pivot = arr\[5] = 7



| j | arr\[j] | Comparison (<=7) | i | Array state after swap        |

|---|--------|-------------------|---|---------------------------------|

| 2 | 8      | No                | 1 | \[1, 5, 8, 9, 10, 7]            |

| 3 | 9      | No                | 1 | \[1, 5, 8, 9, 10, 7]            |

| 4 | 10     | No                | 1 | \[1, 5, 8, 9, 10, 7]            |



Final swap: swap(arr\[i+1], arr\[high]) -> swap(arr\[2], arr\[5])

Result: \[1, 5, 7, 9, 10, 8]

Pivot index returned: 2



\## Call: quickSort(arr, low=2, high=1)

Base case (low >= high) -> returns immediately.



\## Call: quickSort(arr, low=3, high=5)

Array segment: \[9, 10, 8]

Pivot = arr\[5] = 8



| j | arr\[j] | Comparison (<=8) | i | Array state after swap   |

|---|--------|-------------------|---|----------------------------|

| 3 | 9      | No                | 2 | \[1, 5, 7, 9, 10, 8]       |

| 4 | 10     | No                | 2 | \[1, 5, 7, 9, 10, 8]       |



Final swap: swap(arr\[i+1], arr\[high]) -> swap(arr\[3], arr\[5])

Result: \[1, 5, 7, 8, 10, 9]

Pivot index returned: 3



\## Call: quickSort(arr, low=3, high=2)

Base case -> returns.



\## Call: quickSort(arr, low=4, high=5)

Array segment: \[10, 9]

Pivot = arr\[5] = 9



| j | arr\[j] | Comparison (<=9) | i | Array state after swap |

|---|--------|-------------------|---|--------------------------|

| 4 | 10     | No                | 3 | \[1, 5, 7, 8, 10, 9]     |



Final swap: swap(arr\[i+1], arr\[high]) -> swap(arr\[4], arr\[5])

Result: \[1, 5, 7, 8, 9, 10]

Pivot index returned: 4



\## Final sorted array: \[1, 5, 7, 8, 9, 10]



\## Complexity Notes

\- Average case: O(n log n) — pivot roughly splits the array each time.

\- Worst case: O(n^2) — occurs on already-sorted or reverse-sorted input,

&#x20; since this implementation always picks the last element as the pivot,

&#x20; producing maximally unbalanced partitions.

