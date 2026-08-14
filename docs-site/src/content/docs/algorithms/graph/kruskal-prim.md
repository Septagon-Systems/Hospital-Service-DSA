---
title: "Trace Table 5: Minimum Spanning Tree (Prim / Kruskal)"
---

> [!NOTE]
> **TEMPLATE & EXAMPLE ONLY**: This is a starter template for Pod 6. Replace the sample MST edge selection steps below with your team's actual graph data before final submission.

## Algorithm Description
Minimum Spanning Tree algorithm connecting all Ghana hospital centers with minimum road cost using custom `DisjointSet` (Union-Find).

## Trace Table Execution (Kruskal's MST)
| Edge Evaluated | Distance (km) | Source Node | Target Node | Disjoint Set Check | Action | Total MST Cost |
|---|---|---|---|---|---|---|
| (5, 6) | 3.1 km | KATH (5) | Manhyia (6) | `find(5) != find(6)` | **Add Edge to MST** | 3.1 km |
| (2, 3) | 4.8 km | Ridge (2) | 37 Mil (3) | `find(2) != find(3)` | **Add Edge to MST** | 7.9 km |
| (1, 2) | 5.2 km | Korle Bu (1) | Ridge (2) | `find(1) != find(2)` | **Add Edge to MST** | 13.1 km |
