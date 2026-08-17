package edu.ug.dcit308.hospital.app;

import java.util.*;

public class RunRouteDemo {

    public static void execute() {
        System.out.println();
        System.out.println("========== AMBULANCE ROUTE OPTIMIZER (DIJKSTRA) ==========");

        // Nodes representing locations: 0: Circle, 1: Ridge, 2: Osu, 3: Korle-Bu
        String[] locations = {"Circle", "Ridge Hospital", "Osu", "Korle-Bu"};
        int numNodes = locations.length;

        // Adjacency Matrix (distances in km, 0 means no direct edge)
        int[][] graph = {
                { 0,  4,  6,  0 }, // Circle to Ridge (4km), Osu (6km)
                { 4,  0,  3,  7 }, // Ridge to Circle (4km), Osu (3km), Korle-Bu (7km)
                { 6,  3,  0,  5 }, // Osu to Circle (6km), Ridge (3km), Korle-Bu (5km)
                { 0,  7,  5,  0 }  // Korle-Bu to Ridge (7km), Osu (5km)
        };

        int startNode = 0; // Starting at Circle
        System.out.println("Calculating shortest dispatch routes from: " + locations[startNode] + "\n");

        dijkstra(graph, startNode, locations, numNodes);
        System.out.println("===========================================================");
    }

    private static void dijkstra(int[][] graph, int start, String[] names, int n) {
        int[] dist = new int[n];
        boolean[] visited = new boolean[n];

        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;

        for (int i = 0; i < n - 1; i++) {
            int u = minDistance(dist, visited, n);
            if (u == -1) break;
            visited[u] = true;

            for (int v = 0; v < n; v++) {
                if (!visited[v] && graph[u][v] != 0 && dist[u] != Integer.MAX_VALUE
                        && dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];
                }
            }
        }

        for (int i = 0; i < n; i++) {
            System.out.printf("  Destination: %-20s | Shortest Distance: %d km\n", names[i], dist[i]);
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