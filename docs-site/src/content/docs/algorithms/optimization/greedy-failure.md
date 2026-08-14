---
title: "Greedy Failure Counterexample"
---

# Counterexample 1: Greedy Strategy Failure in Hospital Bed / Resource Budget Allocation

## Context & Requirement
Per Brief Section 7 (Greedy and DP section) & Brief Section 10 (Counterexamples requirement), the project must document a counterexample where a greedy heuristic fails to yield the optimal solution.

## Problem Formulation
Select a subset of hospital medical supply requests to maximize total urgency priority score without exceeding an emergency transport weight capacity $W = 10 \text{ kg}$.

## Available Request Packages
- **Item A (Oxygen Tank)**: Weight = $6 \text{ kg}$, Priority Score = $7$ (Value/Weight ratio = $1.17$)
- **Item B (ICU Monitor)**: Weight = $5 \text{ kg}$, Priority Score = $5$ (Value/Weight ratio = $1.00$)
- **Item C (Blood Pack)**: Weight = $5 \text{ kg}$, Priority Score = $5$ (Value/Weight ratio = $1.00$)

## Greedy Strategy (Ratio Pick)
1. Pick **Item A** first (Highest ratio 1.17). Remaining Capacity = $10 - 6 = 4 \text{ kg}$.
2. Item B (5 kg) and Item C (5 kg) cannot fit in remaining 4 kg.
3. **Greedy Total Priority Score**: $7$.

## Optimal Solution (Dynamic Programming 0/1 Knapsack)
1. Pick **Item B** (5 kg) and **Item C** (5 kg). Total Weight = $10 \text{ kg}$.
2. **Optimal Total Priority Score**: $5 + 5 = 10$.

## Conclusion
The greedy ratio heuristic fails because it causes fragmentation of remaining capacity. Dynamic programming (`HospitalCapacityDP.java`) guarantees the optimal selection score of **10** vs greedy's **7**.
