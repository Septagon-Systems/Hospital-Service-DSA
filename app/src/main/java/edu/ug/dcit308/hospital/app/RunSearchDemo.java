package edu.ug.dcit308.hospital.app;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import edu.ug.dcit308.hospital.algo.search.BinarySearch;
import edu.ug.dcit308.hospital.algo.search.LinearSearch;
import edu.ug.dcit308.hospital.db.ConnectionManager;
import edu.ug.dcit308.hospital.db.model.Hospital;
import edu.ug.dcit308.hospital.db.repository.HospitalRepository;
import edu.ug.dcit308.hospital.ds.hash.CustomHashTable;
import edu.ug.dcit308.hospital.ds.tree.RedBlackTree;

/**
 * Operations Handler: Interactive Hospital Search & Tree/Hash Indexing.
 */
public class RunSearchDemo {

    public static void execute(Scanner scanner) {
        System.out.println();
        System.out.println("=============================================================");
        System.out.println("   HOSPITAL SEARCH & INDEXING LAB (HASH TABLE / RED-BLACK)   ");
        System.out.println("=============================================================");

        try (Connection conn = ConnectionManager.getConnection()) {
            HospitalRepository repo = new HospitalRepository(conn);
            List<Hospital> hospitals = repo.findAll();

            if (hospitals.isEmpty()) {
                System.out.println("Database empty. Initializing and importing seed data...");
                RunDataLoader.execute(scanner);
                hospitals = repo.findAll();
            }

            // Build in-memory custom data structures
            CustomHashTable<Integer, Hospital> hashTable = new CustomHashTable<>();
            RedBlackTree<Integer, Hospital> rbTree = new RedBlackTree<>();
            Integer[] idArray = new Integer[hospitals.size()];

            for (int i = 0; i < hospitals.size(); i++) {
                Hospital h = hospitals.get(i);
                hashTable.put(h.getHospitalId(), h);
                rbTree.insert(h.getHospitalId(), h);
                idArray[i] = h.getHospitalId();
            }

            System.out.println("Indexed 50 Ghanaian facilities into:");
            System.out.printf("  • CustomHashTable (Total Elements: %d)\n", hashTable.size());
            System.out.printf("  • RedBlackTree    (Total Elements: %d, Height: %d)\n", rbTree.getSize(), rbTree.getHeight());

            System.out.println("\nOptions:");
            System.out.println("  [1] Search Hospital by ID (Compare Linear vs Binary vs Hash vs RedBlackTree)");
            System.out.println("  [0] Return to Main Menu");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();
            if ("1".equals(choice)) {
                System.out.print("Enter Hospital ID (1 - 50, e.g. 1 for Korle Bu, 6 for Komfo Anokye): ");
                int targetId;
                try {
                    targetId = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid ID.");
                    return;
                }

                // 1. Hash Table Lookup O(1)
                long startHash = System.nanoTime();
                Hospital foundHash = hashTable.get(targetId);
                long timeHashNs = System.nanoTime() - startHash;

                // 2. Red-Black Tree Search O(log N)
                long startRb = System.nanoTime();
                Hospital foundRb = rbTree.search(targetId);
                long timeRbNs = System.nanoTime() - startRb;

                // 3. Binary Search O(log N)
                long startBinary = System.nanoTime();
                int idx = new BinarySearch().search(idArray, targetId);
                long timeBinaryNs = System.nanoTime() - startBinary;

                // 4. Linear Search O(N)
                long startLinear = System.nanoTime();
                int linearIdx = LinearSearch.search(idArray, targetId);
                long timeLinearNs = System.nanoTime() - startLinear;

                if (foundHash != null) {
                    System.out.println("\n✓ Facility Found:");
                    System.out.printf("   Name           : %s\n", foundHash.getName());
                    System.out.printf("   Region & Town  : %s, %s\n", foundHash.getRegion(), foundHash.getTown());
                    System.out.printf("   Facility Type  : %s\n", foundHash.getFacilityType());
                    System.out.printf("   ICU Capacity   : %d beds\n", foundHash.getIcuCapacity());
                    System.out.printf("   Ambulances     : %d units\n", foundHash.getAmbulanceCount());
                    System.out.println("\n--- Search Mechanism Empirical Latency ---");
                    System.out.printf("   • CustomHashTable (O(1) amortized) : %d ns\n", timeHashNs);
                    System.out.printf("   • RedBlackTree    (O(log N))       : %d ns\n", timeRbNs);
                    System.out.printf("   • BinarySearch    (O(log N))       : %d ns (Index: %d)\n", timeBinaryNs, idx);
                    System.out.printf("   • LinearSearch    (O(N))           : %d ns (Index: %d)\n", timeLinearNs, linearIdx);
                } else {
                    System.out.printf("Hospital with ID %d was not found in the database.\n", targetId);
                }
            }
        } catch (Exception e) {
            System.err.println("! Error during search: " + e.getMessage());
        }
        System.out.println("=============================================================");
    }
}
