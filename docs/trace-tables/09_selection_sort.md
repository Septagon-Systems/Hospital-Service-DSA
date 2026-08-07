# Selection Sort Implementation

## Algorithm Description

Selection Sort is implemented from scratch in:

`edu.ug.dcit308.hospital.algo.sort.SelectionSort`

The algorithm repeatedly finds the smallest element in the unsorted portion of the array and swaps it into its correct position.

## Time Complexity

Selection Sort performs comparisons using nested loops.

- Best case: `O(n²)`
- Average case: `O(n²)`
- Worst case: `O(n²)`

Even if the array is already sorted, Selection Sort still performs the required comparisons.

## Space Complexity

The algorithm uses only a few extra variables such as the minimum index and a temporary variable for swapping.

Therefore, the auxiliary space complexity is:

`O(1)`

## In-Place Property

This Selection Sort implementation is in-place.

Elements are rearranged directly inside the original array, and no additional array proportional to the input size is created.

## Stability

Standard Selection Sort is not stable.

A stable sorting algorithm preserves the original relative order of elements that have equal values.

Selection Sort may swap elements across equal-valued elements, so their original relative order may change.

Therefore, this implementation is not stable.

## Benchmark Counters

The implementation keeps track of:

- Number of comparisons
- Number of swaps

These values can be accessed using:

`getComparisonCount()`

`getSwapCount()`

## Testing

The Selection Sort implementation is tested using:

- Normal integer arrays
- Already sorted arrays
- Reverse sorted arrays
- String arrays
- Null input

These tests verify the correctness of the algorithm and its handling of edge cases.