package edu.ug.dcit308.hospital.ds.unionfind;

public class DisjointSet {

    private final int[] parent;
    private final int[] rank;
    private int componentCount;

    public DisjointSet(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive, got: " + size);
        }
        parent = new int[size];
        rank   = new int[size];
        componentCount = size;
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i]   = 0;
        }
    }

    public int find(int x) {
        checkIndex(x);
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // path compression
        }
        return parent[x];
    }

    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return false;
        }

        // union by rank
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }

        componentCount--;
        return true;
    }

    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }

    public int componentCount() {
        return componentCount;
    }

    public int size() {
        return parent.length;
    }

    private void checkIndex(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IndexOutOfBoundsException(
                    "Index " + x + " out of bounds for size " + parent.length);
        }
    }
}
