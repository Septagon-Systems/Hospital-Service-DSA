---
title: "Dijkstra Shortest Path Algorithm"
---

# Trace Table 4: Dijkstra's Shortest Path Algorithm

> [!NOTE]
> **TEMPLATE & EXAMPLE ONLY**: This is a starter template for Pod 6. Replace the sample graph nodes below with your team's actual hospital locations and route distances before final submission.

## Scenario
Finding the shortest emergency ambulance route from **Korle Bu Teaching Hospital** (Origin) to **Tema General Hospital** (Destination) using custom `HospitalGraph` and custom `MinHeap`.

## Trace Table Execution
| Step | Current Vertex | Unvisited Set / Priority Queue | Distance Array `dist[]` | Predecessor Array `prev[]` | Notes |
|---|---|---|---|---|---|
| 0 | Korle Bu (ID 1) | `[(1, 0)]` | `[1:0, 2:∞, 3:∞, 4:∞, 5:∞]` | `[1:null, 2:null, ...]` | Start node initialised |
| 1 | Korle Bu (ID 1) | `[(2, 5.2), (3, 12.0)]` | `[1:0, 2:5.2, 3:12.0, 4:∞]` | `[2:1, 3:1]` | Relaxed edges to Ridge (2) & 37 Mil (3) |
| 2 | Ridge Hospital (2) | `[(3, 11.5), (4, 18.3)]` | `[1:0, 2:5.2, 3:11.5, 4:18.3]` | `[3:2, 4:2]` | Relaxed edge to 37 Mil (3) via Ridge |
