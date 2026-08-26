package edu.ug.dcit308.hospital.app;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.ug.dcit308.hospital.benchmark.core.BenchmarkResult;
import edu.ug.dcit308.hospital.benchmark.exporter.ResultWriter;
import edu.ug.dcit308.hospital.benchmark.runner.BenchmarkRunner;
import edu.ug.dcit308.hospital.db.ConnectionManager;
import edu.ug.dcit308.hospital.db.model.AlgorithmRun;
import edu.ug.dcit308.hospital.db.repository.AlgorithmRunRepository;
import edu.ug.dcit308.hospital.db.schema.SchemaInitializer;

/**
 * Operations Handler: On-Demand Empirical Algorithm Benchmark Lab.
 */
public class RunBenchmarkDemo {

    public static void execute(Scanner scanner) {
        System.out.println();
        System.out.println("=============================================================");
        System.out.println("   EMPIRICAL DSA BENCHMARK LAB — ON-DEMAND EXECUTION         ");
        System.out.println("=============================================================");

        BenchmarkRunner runner = new BenchmarkRunner();
        List<String> registered = runner.getRegisteredAlgorithmNames();

        System.out.println("Select Benchmark Mode:");
        System.out.println("  [1] QuickSort vs MergeSort (O(N log N) Sorting)");
        System.out.println("  [2] Linear Search vs Binary Search (O(N) vs O(log N))");
        System.out.println("  [3] Custom Hash Table vs Red-Black Tree (O(1) vs O(log N))");
        System.out.println("  [4] Dijkstra Shortest Path vs Kruskal/Prim MST (Graph Routing)");
        System.out.println("  [5] Full Benchmark Suite (All 23 DSA Components x 6 Scales)");
        System.out.println("  [0] Return to Main Menu");
        System.out.print("Enter choice: ");

        String choice = scanner.nextLine().trim();
        List<String> toRun = new ArrayList<>();

        switch (choice) {
            case "1":
                toRun.add("QuickSort");
                toRun.add("MergeSort");
                toRun.add("InsertionSort");
                break;
            case "2":
                toRun.add("LinearSearch");
                toRun.add("BinarySearch");
                break;
            case "3":
                toRun.add("CustomHashTable-PutGet");
                toRun.add("RedBlackTree-InsertSearch");
                toRun.add("BinarySearchTree-InsertSearch");
                break;
            case "4":
                toRun.add("DijkstraShortestPath");
                toRun.add("KruskalMST");
                toRun.add("PrimMST");
                break;
            case "5":
                toRun.addAll(registered);
                break;
            case "0":
                return;
            default:
                System.out.println("Invalid selection.");
                return;
        }

        System.out.printf("\nExecuting benchmark for %d algorithm(s) across N=[100, 500, 1000, 5000, 10000, 20000]...\n", toRun.size());
        List<BenchmarkResult> allResults = new ArrayList<>();

        for (String algo : toRun) {
            System.out.printf("\n--- %s Results ---\n", algo);
            System.out.printf("  %-10s | %-12s | %-12s | %-12s\n", "Scale (N)", "Run 1 (ms)", "Run 2 (ms)", "Run 3 (ms)");
            for (int size : BenchmarkRunner.DEFAULT_SIZES) {
                List<BenchmarkResult> runs = runner.runExperiment(algo, size, 3);
                allResults.addAll(runs);
                double t1 = runs.get(0).getExecutionTimeMs();
                double t2 = runs.get(1).getExecutionTimeMs();
                double t3 = runs.get(2).getExecutionTimeMs();
                System.out.printf("  N=%-8d | %-12.4f | %-12.4f | %-12.4f\n", size, t1, t2, t3);
            }
        }

        // Export and persist results
        try {
            ResultWriter.exportToCsv(allResults);
            System.out.println("\n✓ Benchmark results appended to data/results/algorithm_runs.csv");
        } catch (Exception e) {
            System.err.println("! Could not append to CSV: " + e.getMessage());
        }

        try (Connection conn = ConnectionManager.getConnection()) {
            SchemaInitializer.initialize(conn);
            AlgorithmRunRepository repo = new AlgorithmRunRepository(conn);
            List<AlgorithmRun> entityRuns = new ArrayList<>();
            for (BenchmarkResult r : allResults) {
                entityRuns.add(new AlgorithmRun(
                        r.getRunId(), r.getAlgorithmName(), r.getInputSize(),
                        r.getExecutionTimeNs(), r.getMemoryUsedKb(), r.getRunNumber(), r.getRunTimestamp()
                ));
            }
            repo.saveAll(entityRuns);
            System.out.println("✓ Persisted runs into SQLite table 'algorithm_runs'.");
        } catch (Exception e) {
            System.err.println("! Could not persist to SQLite: " + e.getMessage());
        }

        System.out.println("=============================================================");
    }
}