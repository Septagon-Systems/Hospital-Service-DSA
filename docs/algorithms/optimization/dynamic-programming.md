# Trace Table 6: Dynamic Programming (Hospital Capacity Optimization)

## Algorithm Description
0/1 Knapsack Dynamic Programming table for optimal medical supply payload
allocation, implemented in `CapacityOptimizer.optimizeCapacity(double[] weights,
double[] values, double capacity)` (Issue #22, Pod 6).

## Sample Input
| Item $i$ | Weight (kg) | Value |
|---|---|---|
| 0 | 6 | 7 |
| 1 | 5 | 5 |
| 2 | 4 | 6 |

Capacity = 10 kg

## Memoization DP Table $dp[i][w]$
| Item $i$ / Weight $w$ | 0 kg | 2 kg | 4 kg | 6 kg | 8 kg | 10 kg |
|---|---|---|---|---|---|---|
| — (0 items) | 0 | 0 | 0 | 0 | 0 | 0 |
| Item 0 (6 kg, Val 7) | 0 | 0 | 0 | 7 | 7 | 7 |
| Item 1 (5 kg, Val 5) | 0 | 0 | 0 | 7 | 7 | 7 |
| Item 2 (4 kg, Val 6) | 0 | 0 | 6 | 7 | 7 | 13 |

## Backtrace / Item Selection
Starting at `dp[3][10] = 13` and walking backward:
- `dp[3][10] (13) != dp[2][10] (7)` → **Item 2 included** (weight 4, value 6); move to `dp[2][6]`
- `dp[2][6] (7) == dp[1][6] (7)` → Item 1 excluded; move to `dp[1][6]`
- `dp[1][6] (7) != dp[0][6] (0)` → **Item 0 included** (weight 6, value 7); move to `dp[0][0]`

**Selected items:** Item 0, Item 2
**Total weight:** 6 + 4 = 10 kg (fits exactly within 10 kg capacity)
**Total value:** 7 + 6 = 13

## Complexity
- Time: $O(n \times W)$ where $W$ is the scaled integer capacity
- Space: $O(n \times W)$ for the memoization table
