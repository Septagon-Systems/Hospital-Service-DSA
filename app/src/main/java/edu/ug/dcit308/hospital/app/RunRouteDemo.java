package edu.ug.dcit308.hospital.app;

import edu.ug.dcit308.hospital.ds.list.DynamicArrayList;
import edu.ug.dcit308.hospital.graph.core.HospitalNetworkGraph;
import edu.ug.dcit308.hospital.graph.shortestpath.DijkstraShortestPath;

public class RunRouteDemo {

    public static void execute() {
        DynamicArrayList<RunDataLoader.Hospital> defaultDataset = RunDataLoader.loadData();
        execute(defaultDataset);
    }

    public static void execute(DynamicArrayList<RunDataLoader.Hospital> dataset) {
        System.out.println();
        System.out.println("========== AMBULANCE ROUTE OPTIMIZER (DIJKSTRA) ==========");

        if (dataset == null || dataset.isEmpty()) {
            System.out.println("[!] No hospital dataset available for routing.");
            System.out.println("===========================================================");
            return;
        }

        // Extract locations dynamically from the loaded hospital dataset
        int nodeCount = dataset.size() + 1;
        String[] locations = new String[nodeCount];
        locations[0] = "Central Dispatch Hub (Circle)";

        for (int i = 0; i < dataset.size(); i++) {
            locations[i + 1] = dataset.get(i).getName();
        }

        // Distance Matrix (in km) connecting Central Dispatch to hospitals
        int[][] graph = {
                { 0,  4,  3,  6,  5, 12 }, // Central Dispatch -> Hospitals
                { 4,  0,  7,  3,  5, 10 }, // Korle-Bu
                { 3,  7,  0,  8,  4, 15 }, // Greater Accra Regional (Ridge)
                { 6,  8,  8,  0,  2,  9 }, // Komfo Anokye
                { 5,  5,  4,  2,  0, 11 }, // 37 Military
                { 12, 10, 15, 9, 11,  0 }  // Tamale Teaching
        };

        int startNode = 0;
        System.out.println("Calculating shortest ambulance routes from: " + locations[startNode] + "\n");

        computeShortestPaths(graph, startNode, locations, nodeCount);
        System.out.println("===========================================================");
    }

    private static void computeShortestPaths(int[][] graph, int start, String[] names, int n) {
        int[] dist = new int[n];
        boolean[] visited = new boolean[n];

        // Replace java.util.Arrays.fill with custom loop initialization
        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MAX_VALUE;
            visited[i] = false;
        }
        dist[start] = 0;

        for (int i = 0; i < n - 1; i++) {
            int u = minDistance(dist, visited, n);
            if (u == -1) break;
            visited[u] = true;

            for (int v = 0; v < n; v++) {
                int edgeWeight = (u < graph.length && v < graph[u].length) ? graph[u][v] : 0;
                if (!visited[v] && edgeWeight != 0 && dist[u] != Integer.MAX_VALUE
                        && dist[u] + edgeWeight < dist[v]) {
                    dist[v] = dist[u] + edgeWeight;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            String distanceStr = (dist[i] == Integer.MAX_VALUE) ? "Unreachable" : dist[i] + " km";
            System.out.printf("  Destination: %-35s | Shortest Distance: %s\n", names[i], distanceStr);
        }
    }

    private static int minDistance(int[] dist, boolean[] visited, int n) {
        int min = Integer.MAX_VALUE, minIndex = -1;
        for (int v = 0; v < n; v++) {
            if (!visited[v] && dist[v] <= min) {
                min = dist[v];
                minIndex = v;
            }
        }
        return minIndex;
    }
}