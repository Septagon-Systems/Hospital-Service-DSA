---
title: "Trace Table 6: Dynamic Programming (Hospital Capacity Optimization)"
---

# Trace Table 6: Dynamic Programming (Hospital Capacity Optimization)

> [!NOTE]
> **TEMPLATE & EXAMPLE ONLY**: This is a starter template for Pod 6 / Pod 2. Replace the sample DP memoization table below with your team's actual dataset before final submission.

## Algorithm Description
0/1 Knapsack Dynamic Programming table for optimal medical supply payload allocation.

## Memoization DP Table $dp[i][w]$
| Item $i$ / Weight $w$ | 0 kg | 2 kg | 4 kg | 6 kg | 8 kg | 10 kg |
|---|---|---|---|---|---|---|
| Item 0 (0 kg) | 0 | 0 | 0 | 0 | 0 | 0 |
| Item 1 (6 kg, Val 7) | 0 | 0 | 0 | 7 | 7 | 7 |
| Item 2 (5 kg, Val 5) | 0 | 0 | 0 | 7 | 7 | 12 |
