# Proof Sketch 1: Loop Invariant for Search / Sort

> [!NOTE]
> **TEMPLATE & EXAMPLE ONLY**: This is a starter template for Pod 1 / Pod 2. Replace the formal proof text below with your team's formal proof sketch before final submission.

## Theorem
The custom Binary Search algorithm (`BinarySearch.search`) correctly returns the index of the target key if present, or -1 if absent.

## Loop Invariant Definition
At the start of each iteration of the `while (low <= high)` loop, if the key `K` exists in the sorted array `A[0..n-1]`, then `K` must lie within the sub-array `A[low..high]`.

### 1. Initialization
Before the first iteration, `low = 0` and `high = n - 1`. The sub-array `A[0..n-1]` encompasses the entire array. Therefore, if `K` is in `A`, it is trivially within `A[low..high]`.

### 2. Maintenance
Assume the invariant holds at the start of an iteration. Let `mid = low + (high - low) / 2`.
- Case 1: `A[mid] == K`. The algorithm returns `mid` (Correct).
- Case 2: `A[mid] < K`. Since `A` is sorted, for all $i \le mid$, $A[i] \le A[mid] < K$. Thus `K` cannot lie in $A[low..mid]$. So `low` is updated to `mid + 1`. The invariant holds for $A[mid+1..high]$.
- Case 3: `A[mid] > K`. Similarly, `K` cannot lie in $A[mid..high]$. `high` is updated to `mid - 1`. The invariant holds for $A[low..mid-1]$.

### 3. Termination
The loop terminates either when `A[mid] == K` (returning correct index), or when `low > high`. If `low > high`, the range $A[low..high]$ is empty, so by the invariant, `K` cannot exist in `A`. Returning -1 is correct. $\blacksquare$
