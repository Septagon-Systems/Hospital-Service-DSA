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
import edu.ug.dcit308.hospital.graph.core.HospitalNetworkGraph;
import edu.ug.dcit308.hospital.graph.mst.KruskalMST;
import edu.ug.dcit308.hospital.graph.mst.PrimMST;
import edu.ug.dcit308.hospital.graph.shortestpath.DijkstraShortestPath;
import edu.ug.dcit308.hospital.graph.shortestpath.DijkstraShortestPath.PathResult;

/**
 * Operations Handler: Ambulance Shortest Route Navigator & Regional MST Backbone.
 */
public class RunRouteDemo {

    public static void execute(Scanner scanner) {
        System.out.println();
        System.out.println("=============================================================");
        System.out.println("   GHANA HOSPITAL NETWORK — AMBULANCE ROUTING & MST BACKBONE ");
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

            System.out.printf("Loaded Ghana Network Graph: %d Facilities, %d Road Routes.\n",
                    numHospitals, routes.size());

            System.out.println("\nOptions:");
            System.out.println("  [1] Find Shortest Ambulance Route (Dijkstra Algorithm)");
            System.out.println("  [2] Compute Regional Highway Backbone (Kruskal & Prim MST)");
            System.out.println("  [0] Return to Main Menu");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();
            if ("1".equals(choice)) {
                System.out.print("Enter Origin Hospital ID (1 - 50, e.g. 1 = Korle Bu): ");
                int source = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Enter Destination Hospital ID (1 - 50, e.g. 6 = Komfo Anokye): ");
                int dest = Integer.parseInt(scanner.nextLine().trim());

                Optional<Hospital> srcHosp = hospitalRepo.findById(source);
                Optional<Hospital> destHosp = hospitalRepo.findById(dest);

                if (srcHosp.isEmpty() || destHosp.isEmpty()) {
                    System.out.println("Invalid hospital IDs.");
                    return;
                }

                System.out.printf("\nRouting Ambulance from: %s -> %s\n",
                        srcHosp.get().getName(), destHosp.get().getName());

                long start = System.nanoTime();
                PathResult result = DijkstraShortestPath.findShortestPath(graph, source);
                long elapsedNs = System.nanoTime() - start;

                double distKm = result.distanceTo(dest);
                if (distKm != Double.POSITIVE_INFINITY) {
                    System.out.printf("✓ Shortest Driving Distance : %.2f km\n", distKm);
                    System.out.printf("✓ Estimated Driving Time    : %.1f minutes\n", (distKm / 60.0) * 60.0);
                    System.out.printf("✓ Algorithm Execution Time  : %d ns (%.4f ms)\n", elapsedNs, elapsedNs / 1_000_000.0);

                    int[] path = result.reconstructPath();
                    System.out.println("\n--- Ambulance Waypoint Sequence ---");
                    for (int i = 0; i < path.length; i++) {
                        Optional<Hospital> node = hospitalRepo.findById(path[i]);
                        String name = node.map(Hospital::getName).orElse("Facility #" + path[i]);
                        if (i == 0) {
                            System.out.printf("  [START]       [%2d] %s\n", path[i], name);
                        } else if (i == path.length - 1) {
                            System.out.printf("  [DESTINATION] [%2d] %s\n", path[i], name);
                        } else {
                            System.out.printf("  [WAYPOINT %2d]  [%2d] %s\n", i, path[i], name);
                        }
                    }
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

                System.out.printf("Kruskal MST: %d Corridors | Total Distance: %.2f km | Time: %d ns\n",
                        kruskalEdges.length, totalKruskalKm, timeKruskal);
                System.out.printf("Prim MST   : %d Corridors | Total Distance: %.2f km | Time: %d ns\n",
                        primEdges.length, totalPrimKm, timePrim);

                System.out.println("\nSample Backbone Road Corridors (Connecting Key Ghanaian Hubs):");
                int limit = Math.min(8, kruskalEdges.length);
                for (int i = 0; i < limit; i++) {
                    KruskalMST.WeightedEdge e = kruskalEdges[i];
                    String fromName = hospitalRepo.findById(e.source).map(Hospital::getName).orElse("#" + e.source);
                    String toName = hospitalRepo.findById(e.destination).map(Hospital::getName).orElse("#" + e.destination);
                    System.out.printf("   • %-32s <---> %-32s (%.1f km)\n", fromName, toName, e.weight);
                }
            }
        } catch (Exception e) {
            System.err.println("! Error calculating route: " + e.getMessage());
        }
        System.out.println("=============================================================");
    }
}