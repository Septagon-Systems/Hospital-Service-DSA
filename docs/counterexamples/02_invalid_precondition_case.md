# Counterexample 2: Invalid Precondition (Binary Search on Unsorted Input)

> [!NOTE]
> **TEMPLATE & EXAMPLE ONLY**: This is a starter template for Pod 1 / Pod 7. Replace the sample counterexample details below before final submission.

## Precondition Requirement
Binary search ($O(\log n)$ complexity) relies on the strict precondition that the input array $A$ is sorted in ascending (or descending) order.

## Failure Scenario
An unsorted array of hospital ID records is passed directly to `BinarySearch.search()` without prior sorting:
```java
Integer[] unsortedHospitals = {45, 12, 8, 99, 3, 20};
int target = 8;
```

## Step-by-Step Execution Failure
1. `low = 0`, `high = 5`, `mid = 2`.
2. `A[mid] = 8`. Target is `8`.
3. `cmp = 8.compareTo(8) == 0`. Match found by coincidence.

Now search for target `12`:
1. `low = 0`, `high = 5`, `mid = 2`.
2. `A[mid] = 8`. Target `12 > 8`.
3. Algorithm assumes elements to the left of index 2 are $\le 8$ (which is false! `45` and `12` are at index 0 and 1).
4. `low` is updated to `mid + 1 = 3`. Sub-array `[0..2]` is permanently discarded.
5. Search terminates returning `-1` (Not Found), even though `12` exists at index 1!

## Conclusion
Binary search fails silently or produces incorrect results if its precondition (sorted input array) is violated. The unit test `BinarySearchTest.testInvalidPreconditionCase()` explicitly verifies that unsorted inputs fail.
