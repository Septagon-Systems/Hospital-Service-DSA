# Trace Evidence: BFS and DFS Graph Traversal

## Scenario

This trace demonstrates Breadth-First Search (BFS) and Depth-First Search (DFS)
on the hospital network graph.

The test graph contains five vertices (0–4) with the following connections:

- 0 → 1
- 0 → 2
- 1 → 3
- 2 → 4

Traversal begins from vertex 0.
## Graph Diagram

The graph used for the BFS and DFS traversal tests is:

```text
        0
       / \
      1   2
      |   |
      3   4

## BFS Trace Table

| Step | Current Vertex | Queue | Visited Nodes | Notes |
|------|----------------|-------|---------------|-------|
| 0 | - | [0] | [] | BFS initialized with start vertex 0 |
| 1 | 0 | [1, 2] | [0] | Visit 0 and discover vertices 1 and 2 |
| 2 | 1 | [2, 3] | [0, 1] | Visit 1 and discover vertex 3 |
| 3 | 2 | [3, 4] | [0, 1, 2] | Visit 2 and discover vertex 4 |
| 4 | 3 | [4] | [0, 1, 2, 3] | Visit vertex 3 |
| 5 | 4 | [] | [0, 1, 2, 3, 4] | Visit vertex 4; traversal complete |

### BFS Traversal Order

0 → 1 → 2 → 3 → 4

## DFS Trace Table

| Step | Current Vertex | Visited Nodes | Notes |
|------|----------------|---------------|-------|
| 0 | 0 | [0] | DFS begins at vertex 0 |
| 1 | 1 | [0, 1] | Visit vertex 1 |
| 2 | 3 | [0, 1, 3] | Visit vertex 3 |
| 3 | 2 | [0, 1, 3, 2] | Backtrack and visit vertex 2 |
| 4 | 4 | [0, 1, 3, 2, 4] | Visit vertex 4; traversal complete |

### DFS Traversal Order

0 → 1 → 3 → 2 → 4

## Test Evidence

The JUnit tests verify the expected traversal orders:

- BFS expected result: [0, 1, 2, 3, 4]
- DFS expected result: [0, 1, 3, 2, 4]

Both traversal tests pass successfully.