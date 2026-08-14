# Proof Sketch 3: Correctness of Dijkstra's Algorithm (Greedy Choice Property)

> [!NOTE]
> **TEMPLATE & EXAMPLE ONLY**: This is a starter template for Pod 6. Replace the formal greedy choice proof below with your team's proof before final submission.

## Theorem
Dijkstra's algorithm correctly finds the shortest path distance from a single source $S$ to all reachable vertices in a graph with non-negative edge weights.

## Proof Sketch (Greedy Stays Ahead)
Let $S$ be the set of vertices whose shortest distances from $s$ are finalized.

1. **Base Case**: Initially $S = \{s\}$ with $dist[s] = 0$, which is trivially correct.
2. **Inductive Step**: Assume for all $u \in S$, $dist[u]$ is the exact shortest path distance from $s$. Let $v$ be the next vertex outside $S$ chosen by the greedy choice property (the vertex in $V \setminus S$ with the minimum tentative distance $dist[v]$).
3. **Contradiction Argument**: Suppose there exists a shorter path $P$ from $s$ to $v$ through some intermediate vertex $y \in V \setminus S$.
   - Because all edge weights are non-negative, the length of path $P$ up to $y$ must be $\ge dist[y]$.
   - But since $v$ was chosen by the greedy step, $dist[v] \le dist[y] \le \text{length}(P)$.
   - This contradicts the assumption that $P$ is shorter than $dist[v]$.
4. Therefore, $dist[v]$ is indeed the true shortest path distance. $\blacksquare$
