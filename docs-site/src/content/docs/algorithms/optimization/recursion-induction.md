---
title: "Recursion & Induction Proof"
---

# Proof Sketch 2: Proof by Mathematical Induction for Recursive Algorithms

> [!NOTE]
> **TEMPLATE & EXAMPLE ONLY**: This is a starter template for Pod 2. Replace the formal induction proof steps below with your team's recursive algorithm proof before final submission.

## Theorem
The recursive `MergeSort` algorithm correctly sorts any array $A$ of $n$ elements in $O(n \log n)$ time.

## Proof by Strong Mathematical Induction on $n$

### Base Case ($n = 1$)
An array of size 1 is already sorted. `MergeSort` returns immediately without modification. The base case holds.

### Inductive Hypothesis
Assume that `MergeSort` correctly sorts all arrays of size $k < n$.

### Inductive Step
Consider an array $A$ of size $n > 1$.
1. `MergeSort` computes $mid = \lfloor n/2 \rfloor$ and recursively calls `MergeSort` on $A[0..mid-1]$ (size $\lfloor n/2 \rfloor < n$) and $A[mid..n-1]$ (size $\lceil n/2 \rceil < n$).
2. By the Inductive Hypothesis, both sub-arrays are correctly sorted.
3. The `merge` procedure combines the two sorted sub-arrays by comparing their smallest elements sequentially, producing a fully sorted array of size $n$.
4. Therefore, `MergeSort` correctly sorts arrays of size $n$. $\blacksquare$
