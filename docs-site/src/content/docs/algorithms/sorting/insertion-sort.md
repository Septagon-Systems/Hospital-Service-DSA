---
title: "Trace Table 2: Insertion Sort Algorithm"
---

# Trace Table 2: Insertion Sort Algorithm

## Algorithm Description

Custom Insertion Sort implemented in `edu.ug.dcit308.hospital.algo.sort.InsertionSort`.

**Complexity**: Best O(n) | Average O(n²) | Worst O(n²) | Space O(1)

**Input array**: `[45, 15, 20, 10]`  
**Expected output**: `[10, 15, 20, 45]`

---

## Trace Table Execution

Variables tracked per outer-loop iteration (`i` = outer index, `key` = element being inserted, `j` = inner index used for shifting).

| Step | `i` | `key` | Array State **Before** Outer Iteration | Shift Operations (`j` walk) | Array State **After** Outer Iteration |
|------|-----|-------|----------------------------------------|-----------------------------|---------------------------------------|
| 0 (init) | – | – | `[45, 15, 20, 10]` | – (sorted prefix = `[45]`) | `[45, 15, 20, 10]` |
| 1 | 1 | **15** | `[45, 15, 20, 10]` | `j=0`: `45 > 15` → shift `45` right → `[45, 45, 20, 10]`; `j=-1` stop | `[15, 45, 20, 10]` |
| 2 | 2 | **20** | `[15, 45, 20, 10]` | `j=1`: `45 > 20` → shift `45` right → `[15, 45, 45, 10]`; `j=0`: `15 ≤ 20` stop | `[15, 20, 45, 10]` |
| 3 | 3 | **10** | `[15, 20, 45, 10]` | `j=2`: `45 > 10` → shift; `j=1`: `20 > 10` → shift; `j=0`: `15 > 10` → shift; `j=-1` stop | `[10, 15, 20, 45]` |

---

## Step-by-Step Walkthrough

### Iteration i = 1 — insert `15`

```
Before : [45,  15,  20,  10]
          ↑   ↑
          sorted  key=15
          prefix

j = 0 : array[0]=45 > 15 → array[1] = array[0] → [45, 45, 20, 10]
j = -1: loop exits

array[j + 1] = key → array[0] = 15

After  : [15,  45,  20,  10]
```

### Iteration i = 2 — insert `20`

```
Before : [15,  45,  20,  10]
                    ↑
                    key=20

j = 1 : array[1]=45 > 20 → array[2] = array[1] → [15, 45, 45, 10]
j = 0 : array[0]=15 ≤ 20 → loop exits

array[j + 1] = key → array[1] = 20

After  : [15,  20,  45,  10]
```

### Iteration i = 3 — insert `10`

```
Before : [15,  20,  45,  10]
                        ↑
                        key=10

j = 2 : array[2]=45 > 10 → array[3] = array[2] → [15, 20, 45, 45]
j = 1 : array[1]=20 > 10 → array[2] = array[1] → [15, 20, 20, 45]
j = 0 : array[0]=15 > 10 → array[1] = array[0] → [15, 15, 20, 45]
j = -1: loop exits

array[j + 1] = key → array[0] = 10

After  : [10,  15,  20,  45]
```

---

## Correctness Argument (Loop Invariant)

**Invariant**: At the start of iteration `i`, `array[0..i-1]` is sorted in ascending order.

| Point | Justification |
|-------|---------------|
| **Initialisation** | Before `i = 1`: prefix `array[0..0]` is a single element — trivially sorted. ✓ |
| **Maintenance** | After the inner `while` loop places `key` at position `j+1`, all elements in `array[0..i]` satisfy the ordering property: elements left of `j+1` are `≤ key` (loop exit condition), elements right of `j+1` (the shifted ones) are `> key`. ✓ |
| **Termination** | `i` increments by 1 per iteration and terminates when `i = n`. At that point the invariant guarantees `array[0..n-1]` is fully sorted. ✓ |

---

## Complexity Summary

| Case | Comparisons | Shifts | Time | Space |
|------|-------------|--------|------|-------|
| Best (sorted) | n – 1 | 0 | O(n) | O(1) |
| Average | ~n²/4 | ~n²/4 | O(n²) | O(1) |
| Worst (reverse) | n(n–1)/2 | n(n–1)/2 | O(n²) | O(1) |

**Stable**: Yes — equal elements are never swapped (strict `>` comparison).
