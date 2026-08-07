## Unsuccessful Search Trace

### Input

- Sorted array: `[2, 4, 6, 8, 10]`
- Target value: `7`
- Expected result: `-1`

| Step | `low` index | `high` index | `mid` index | Element at `mid` | Target | Comparison | Action |
|---|---:|---:|---:|---:|---:|---|---|
| 1 | 0 | 4 | 2 | 6 | 7 | `Target > Mid` | Set `low = mid + 1`, so `low = 3` |
| 2 | 3 | 4 | 3 | 8 | 7 | `Target < Mid` | Set `high = mid - 1`, so `high = 2` |
| 3 | 3 | 2 | — | — | 7 | `low > high` | Search terminates; return `-1` |

The target `7` is not present in the array. The search interval becomes
empty after two comparisons, so the method returns `-1`.