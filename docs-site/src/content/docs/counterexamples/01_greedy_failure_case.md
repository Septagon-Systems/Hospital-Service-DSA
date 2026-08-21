---
title: "Greedy Dispatch Counterexample — `GreedyDispatchStrategy`"
---

## Problem Setup

A hospital network has two pending requests and two available ambulances.
The **greedy strategy** dispatches in triage order (Critical first), assigning
the cheapest compatible resource to each request.

| Request | Triage | Type     | Patient Weight |
|---------|--------|----------|----------------|
| R1      | 1 (Critical) | Ambulance | 50 kg |
| R2      | 3 (Normal)   | Ambulance | 50 kg |

| Resource | Type     | Cost (GH₵) | Capacity |
|----------|----------|------------|----------|
| A1       | Ambulance | 100        | 80 kg    |
| A2       | Ambulance | 150        | 80 kg    |

## Greedy Dispatch (triage-first, cheapest-compatible)

1. R1 (Critical) → assigned cheapest compatible: **A1** (cost 100).
2. R2 (Normal)   → only A2 remains → **A2** (cost 150).

**Total cost = GH₵ 250.**

## Optimal Dispatch

Assign A1 to R2 (Normal, still gets served) and A2 to R1 (Critical, still
gets a working ambulance):

1. R1 (Critical) → A2 (cost 150).
2. R2 (Normal)   → A1 (cost 100).

**Total cost = GH₵ 250.**

In this particular instance the total cost is identical, so the greedy
approach happens to produce a globally optimal allocation. However, the
**assignment differs**, and this difference becomes a real problem when
resources have downstream constraints (e.g., A1 is also needed for a
second Critical request later).

## Variant That Exposes the Failure

Add a third request that arrives after dispatch:

| Request | Triage | Type     | Weight |
|---------|--------|----------|--------|
| R3      | 1 (Critical) | Ambulance | 60 kg |

Only A2 has capacity ≥ 60 kg (both do, but A2 is more expensive). Under
the greedy assignment, R3 must take A2 at cost 150 (total 400). Under
the optimal assignment, R3 takes A1 at cost 100 (total 350).

| Scenario | R1   | R2   | R3   | Total |
|----------|------|------|------|-------|
| Greedy   | A1 (100) | A2 (150) | A2 (150) | **400** |
| Optimal  | A2 (150) | A1 (100) | A1 (100) | **350** |

Greedy committed the cheap resource to the highest-priority request,
leaving only the expensive option for later critical demand.

## Conclusion

Greedy-by-triage is a reasonable heuristic and runs in O(n log n + n·m),
but it does **not** minimise total cost across all requests when resources
are reusable or when future demand is known. A global optimiser (e.g.,
integer linear programming or min-cost matching) would be needed for
provably optimal allocations.
