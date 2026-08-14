# Trace Table 5: Minimum Spanning Tree (Kruskal's & Prim's)

## Algorithm Description

Custom MST algorithms implemented in:
- `edu.ug.dcit308.hospital.graph.mst.KruskalMST` — uses `DisjointSet` (Union-Find)
- `edu.ug.dcit308.hospital.graph.mst.PrimMST` — uses min-key linear scan
- `edu.ug.dcit308.hospital.ds.unionfind.DisjointSet` — path compression + union by rank

**Domain context**: Finds the minimum-cost road network to connect Ghana hospital centres so that ambulances can reach any hospital from any other with the minimum total road distance.

---

## Input Graph — Ghana Hospital Network

| Vertex | Hospital |
|--------|----------|
| 0 | Korle Bu Teaching Hospital |
| 1 | Ridge Hospital |
| 2 | 37 Military Hospital |
| 3 | Komfo Anokye Teaching Hospital (KATH) |
| 4 | Manhyia Hospital |

| Edge | Source | Destination | Weight (km) |
|------|--------|-------------|-------------|
| e1 | 0 (Korle Bu) | 1 (Ridge) | 5.2 |
| e2 | 1 (Ridge) | 2 (37 Mil) | 4.8 |
| e3 | 0 (Korle Bu) | 2 (37 Mil) | 9.1 |
| e4 | 2 (37 Mil) | 3 (KATH) | 14.0 |
| e5 | 3 (KATH) | 4 (Manhyia) | 3.1 |
| e6 | 1 (Ridge) | 3 (KATH) | 7.5 |
| e7 | 0 (Korle Bu) | 3 (KATH) | 12.0 |

---

## Kruskal's Algorithm Trace

### Step 0 — Sort all edges by weight ascending

| Sorted Order | Edge | Weight (km) |
|---|---|---|
| 1st | (3, 4) KATH ↔ Manhyia | 3.1 |
| 2nd | (1, 2) Ridge ↔ 37 Mil | 4.8 |
| 3rd | (0, 1) Korle Bu ↔ Ridge | 5.2 |
| 4th | (1, 3) Ridge ↔ KATH | 7.5 |
| 5th | (0, 2) Korle Bu ↔ 37 Mil | 9.1 |
| 6th | (0, 3) Korle Bu ↔ KATH | 12.0 |
| 7th | (2, 3) 37 Mil ↔ KATH | 14.0 |

### Step 1–4 — Greedy Edge Selection

Initial `DisjointSet`: `{0},{1},{2},{3},{4}` — 5 components

| Step | Edge Evaluated | Weight (km) | `find(u)` | `find(v)` | Different? | Action | MST Total | Components |
|------|----------------|-------------|-----------|-----------|------------|--------|-----------|------------|
| 1 | (3, 4) KATH↔Manhyia | 3.1 | 3 | 4 | ✅ Yes | **Add to MST** | 3.1 | 4 |
| 2 | (1, 2) Ridge↔37 Mil | 4.8 | 1 | 2 | ✅ Yes | **Add to MST** | 7.9 | 3 |
| 3 | (0, 1) Korle Bu↔Ridge | 5.2 | 0 | 1 | ✅ Yes | **Add to MST** | 13.1 | 2 |
| 4 | (1, 3) Ridge↔KATH | 7.5 | 1 | 3 | ✅ Yes | **Add to MST** | 20.6 | 1 |
| – | (0, 2) Korle Bu↔37 Mil | 9.1 | — | — | — | *Unreached (loop exits as mstSize = V - 1)* | 20.6 | 1 |
| – | (0, 3) Korle Bu↔KATH | 12.0 | — | — | — | *Unreached (loop exits as mstSize = V - 1)* | 20.6 | 1 |

### Kruskal MST Result

**MST edges**: (3,4), (1,2), (0,1), (1,3)  
**Total MST weight**: **20.6 km**

---

## Prim's Algorithm Trace (start = vertex 0, Korle Bu)

Initial state: `key = [0.0, ∞, ∞, ∞, ∞]`, `parent = [-1, -1, -1, -1, -1]`, `inTree = [F,F,F,F,F]`

| Iteration | Pick vertex `u` (min key, not in tree) | Mark in tree | MST edge added | Relaxations applied |
|-----------|----------------------------------------|--------------|----------------|---------------------|
| 1 | **0** (key=0.0) | inTree[0]=T | — (root, no parent) | key[1]→5.2 (via 0), key[2]→9.1 (via 0), key[3]→12.0 (via 0) |
| 2 | **1** (key=5.2) | inTree[1]=T | **(0→1, 5.2 km)** | key[2]→4.8 (via 1, better!), key[3]→7.5 (via 1, better!) |
| 3 | **2** (key=4.8) | inTree[2]=T | **(1→2, 4.8 km)** | key[3] stays 7.5 (14.0 via 2 is worse) |
| 4 | **3** (key=7.5) | inTree[3]=T | **(1→3, 7.5 km)** | key[4]→3.1 (via 3) |
| 5 | **4** (key=3.1) | inTree[4]=T | **(3→4, 3.1 km)** | — (all in tree) |

### Prim MST Result

**MST edges**: (0,1)=5.2, (1,2)=4.8, (1,3)=7.5, (3,4)=3.1  
**Total MST weight**: **20.6 km** ✅ (matches Kruskal)

---

## Correctness Argument

For a connected undirected graph, both algorithms produce an optimal MST (minimum total weight spanning tree). This follows from the **Cut Property**:

> For any cut of the graph into two non-empty sets S and V∖S, a minimum-weight crossing edge is safe because some MST contains it.

- **Kruskal's**: At each step, the cheapest edge that crosses between two different components (checked via Union-Find) is the minimum-weight crossing edge for the cut `{component of u}` vs `{everything else}`. By the Cut Property, it is always safe.  
- **Prim's**: The cheapest edge from the current tree set `S` to any vertex in `V∖S` is the minimum-weight crossing edge for the cut `{S}` vs `{V∖S}`. By the Cut Property, it is always safe.

---

## Complexity Summary

| Algorithm | Time | Space | Notes |
|-----------|------|-------|-------|
| Kruskal's | O(E log E) | O(E + V) | Dominated by edge sort; Union-Find ops ≈ O(1) amortised |
| Prim's (linear scan) | O(V²) | O(V) | Optimal for dense graphs; V² ≈ E for dense |
