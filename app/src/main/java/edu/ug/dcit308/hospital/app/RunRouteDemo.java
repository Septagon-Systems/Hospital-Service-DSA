package edu.ug.dcit308.hospital.app;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import edu.ug.dcit308.hospital.db.ConnectionManager;
import edu.ug.dcit308.hospital.db.model.Hospital;
import edu.ug.dcit308.hospital.db.model.Route;
import edu.ug.dcit308.hospital.db.repository.HospitalRepository;
import edu.ug.dcit308.hospital.db.repository.RouteRepository;
import edu.ug.dcit308.hospital.ds.linked.SinglyLinkedList;
import edu.ug.dcit308.hospital.graph.core.HospitalNetworkGraph;
import edu.ug.dcit308.hospital.graph.mst.KruskalMST;
import edu.ug.dcit308.hospital.graph.mst.PrimMST;
import edu.ug.dcit308.hospital.graph.shortestpath.DijkstraShortestPath;
import edu.ug.dcit308.hospital.graph.shortestpath.DijkstraShortestPath.PathResult;
import edu.ug.dcit308.hospital.graph.traversal.GraphTraversal;

/**
 * Operations Handler: Ambulance Shortest Route Navigator, MST Backbone & Graph Traversals.
 * Exercises SinglyLinkedList, GraphTraversal (BFS/DFS), Dijkstra, Kruskal, and Prim.
 */
public class RunRouteDemo {

    public static void execute(Scanner scanner) {
        System.out.println();
        System.out.println("=============================================================");
        System.out.println("   GHANA HOSPITAL NETWORK — ROUTING, MST & GRAPH TRAVERSALS  ");
        System.out.println("=============================================================");

        try (Connection conn = ConnectionManager.getConnection()) {
            HospitalRepository hospitalRepo = new HospitalRepository(conn);
            RouteRepository routeRepo = new RouteRepository(conn);

            List<Hospital> hospitals = hospitalRepo.findAll();
            List<Route> routes = routeRepo.findAll();

            if (hospitals.isEmpty() || routes.isEmpty()) {
                System.out.println("Database empty. Populating from seed...");
                RunDataLoader.execute(scanner);
                hospitals = hospitalRepo.findAll();
                routes = routeRepo.findAll();
            }

            int numHospitals = hospitals.size();
            HospitalNetworkGraph graph = new HospitalNetworkGraph(numHospitals + 1, false);

            for (Route r : routes) {
                if (r.getSourceHospitalId() <= numHospitals && r.getDestinationHospitalId() <= numHospitals) {
                    graph.addEdge(r.getSourceHospitalId(), r.getDestinationHospitalId(), r.getDistanceKm());
                }
            }

            System.out.printf("Loaded Ghana Network Graph: %d Facilities, %d Road Corridors.\n",
                    numHospitals, routes.size());

            System.out.println("\nOptions:");
            System.out.println("  [1] Find Shortest Ambulance Route (Dijkstra Algorithm with Leg Distances)");
            System.out.println("  [2] Compute Regional Highway Backbone (Kruskal & Prim MST)");
            System.out.println("  [3] Explore Regional Network Reachability (BFS & DFS Traversals)");
            System.out.println("  [0] Return to Main Menu");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();
            if ("1".equals(choice)) {
                System.out.print("Enter Origin Hospital ID (1 - 50, e.g. 1 = Korle Bu): ");
                int source;
                try {
                    source = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("! Invalid Origin ID format.");
                    return;
                }

                System.out.print("Enter Destination Hospital ID (1 - 50, e.g. 16 = Komfo Anokye): ");
                int dest;
                try {
                    dest = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("! Invalid Destination ID format.");
                    return;
                }

                Optional<Hospital> srcHosp = hospitalRepo.findById(source);
                Optional<Hospital> destHosp = hospitalRepo.findById(dest);

                if (srcHosp.isEmpty() || destHosp.isEmpty()) {
                    System.out.println("! Hospital ID out of range (1 - 50).");
                    return;
                }

                System.out.println("\n=============================================================");
                System.out.printf("AMBULANCE DISPATCH: %s -> %s\n",
                        srcHosp.get().getName(), destHosp.get().getName());
                System.out.println("=============================================================");

                long start = System.nanoTime();
                PathResult result = DijkstraShortestPath.findShortestPath(graph, source, dest);
                long elapsedNs = System.nanoTime() - start;

                double distKm = result.getDistance();
                if (distKm != Double.POSITIVE_INFINITY) {
                    int[] rawPath = result.reconstructPath();

                    // Store waypoints in SinglyLinkedList
                    SinglyLinkedList<Integer> waypointList = new SinglyLinkedList<>();
                    for (int vertex : rawPath) {
                        waypointList.addLast(vertex);
                    }

                    System.out.printf("\n--- Highway Waypoints (%d Nodes in SinglyLinkedList Chain) ---\n", waypointList.size());

                    for (int i = 0; i < waypointList.size(); i++) {
                        int nodeId = waypointList.get(i);
                        Optional<Hospital> currNode = hospitalRepo.findById(nodeId);
                        String currName = currNode.map(Hospital::getName).orElse("Facility #" + nodeId);
                        String currRegion = currNode.map(Hospital::getRegion).orElse("");

                        if (i == 0) {
                            System.out.printf("  [START]       [%2d] %-35s (%s)\n", nodeId, currName, currRegion);
                        } else {
                            int prevId = waypointList.get(i - 1);
                            double legDistance = graph.getWeight(prevId, nodeId);

                            if (i == waypointList.size() - 1) {
                                System.out.printf("    └── [Leg %d]  ───> (+%6.2f km) ───> [DESTINATION] [%2d] %-35s (%s)\n",
                                        i, legDistance, nodeId, currName, currRegion);
                            } else {
                                System.out.printf("    ├── [Leg %d]  ───> (+%6.2f km) ───> [WAYPOINT %2d]  [%2d] %-35s (%s)\n",
                                        i, legDistance, i, nodeId, currName, currRegion);
                            }
                        }
                    }

                    System.out.println("-------------------------------------------------------------");
                    System.out.printf("✓ Total Cumulative Highway Distance : %.2f km\n", distKm);
                    System.out.printf("✓ Total Estimated Ambulance Transit: %.1f minutes (%.2f hours)\n",
                            distKm, distKm / 60.0);
                    System.out.printf("✓ Total Corridors Traversed (Hops) : %d highway segments\n", waypointList.size() - 1);
                    System.out.printf("✓ Dijkstra Path Computation Latency : %d ns (%.4f ms)\n",
                            elapsedNs, elapsedNs / 1_000_000.0);
                } else {
                    System.out.println("! Destination is unreachable along mapped road network.");
                }
            } else if ("2".equals(choice)) {
                System.out.println("\n--- Computing Minimum Spanning Tree (MST) ---");

                long startKruskal = System.nanoTime();
                KruskalMST.WeightedEdge[] kruskalEdges = KruskalMST.compute(graph);
                long timeKruskal = System.nanoTime() - startKruskal;

                double totalKruskalKm = 0;
                for (KruskalMST.WeightedEdge e : kruskalEdges) {
                    totalKruskalKm += e.weight;
                }

                long startPrim = System.nanoTime();
                PrimMST.MSTEdge[] primEdges = PrimMST.compute(graph, 1);
                long timePrim = System.nanoTime() - startPrim;

                double totalPrimKm = 0;
                for (PrimMST.MSTEdge e : primEdges) {
                    totalPrimKm += e.weight;
                }

                System.out.printf("✓ Kruskal MST: %d Corridors | Total Distance: %.2f km | Time: %d ns\n",
                        kruskalEdges.length, totalKruskalKm, timeKruskal);
                System.out.printf("✓ Prim MST   : %d Corridors | Total Distance: %.2f km | Time: %d ns\n",
                        primEdges.length, totalPrimKm, timePrim);

                System.out.println("\nSample Backbone Road Corridors (Connecting Key Ghanaian Hubs):");
                int limit = Math.min(8, kruskalEdges.length);
                for (int i = 0; i < limit; i++) {
                    KruskalMST.WeightedEdge e = kruskalEdges[i];
                    String fromName = hospitalRepo.findById(e.source).map(Hospital::getName).orElse("#" + e.source);
                    String toName = hospitalRepo.findById(e.destination).map(Hospital::getName).orElse("#" + e.destination);
                    System.out.printf("   • %-32s <---> %-32s (%.1f km)\n", fromName, toName, e.weight);
                }
            } else if ("3".equals(choice)) {
                System.out.print("Enter Start Hospital ID for Traversal (1 - 50, e.g. 1 = Korle Bu): ");
                int startId;
                try {
                    startId = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    startId = 1;
                }

                int[] bfsOrder = GraphTraversal.traverseBFS(graph, startId);
                int[] dfsOrder = GraphTraversal.traverseDFS(graph, startId);

                System.out.printf("\n✓ BFS Traversal Order from Hospital #%d (%d facilities reached):\n   ", startId, bfsOrder.length);
                for (int i = 0; i < Math.min(12, bfsOrder.length); i++) {
                    System.out.print(bfsOrder[i] + (i < Math.min(12, bfsOrder.length) - 1 ? " -> " : "... \n"));
                }

                System.out.printf("✓ DFS Traversal Order from Hospital #%d (%d facilities reached):\n   ", startId, dfsOrder.length);
                for (int i = 0; i < Math.min(12, dfsOrder.length); i++) {
                    System.out.print(dfsOrder[i] + (i < Math.min(12, dfsOrder.length) - 1 ? " -> " : "... \n"));
                }
            }
        } catch (Exception e) {
            System.err.println("! Error calculating route: " + e.getMessage());
        }
        System.out.println("=============================================================");
    }
}