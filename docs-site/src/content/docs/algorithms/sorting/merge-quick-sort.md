---
title: "Trace Table 3: Merge Sort Algorithm"
---

## Algorithm Description

Custom Merge Sort implemented in `edu.ug.dcit308.hospital.algo.sort.MergeSort`.

- **Time Complexity**: $O(n \log n)$ best, average, and worst case.
- **Auxiliary Space**: $O(n)$ using a single workspace auxiliary array passed recursively.
- **Stability**: Stable (preserves original order of equal elements).

---

## Hospital Patient Waiting Times Example

Input array: `[38, 12, 27, 5]`  
Expected output: `[5, 12, 27, 38]`

---

## Step-by-Step Recursion & Merge Trace Table

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

## Correctness & Stability Proof

- **Divide and Conquer**: The array is recursively halved until sub-arrays of size 1 are formed (trivially sorted).
- **Merging**: Two sorted halves `A[low..mid]` and `A[mid+1..high]` are merged into a single sorted array by taking the smaller element at each step.
- **Stability Guarantee**: During merging, when `aux[i] == aux[j]`, the left element `aux[i]` is selected first (`aux[i] <= aux[j]`), preserving the original relative ordering of equal keys.
