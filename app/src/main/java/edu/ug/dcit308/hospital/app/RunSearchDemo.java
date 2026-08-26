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
import edu.ug.dcit308.hospital.ds.tree.BTree;
import edu.ug.dcit308.hospital.ds.tree.BinarySearchTree;
import edu.ug.dcit308.hospital.ds.tree.RedBlackTree;

/**
 * Operations Handler: Interactive Hospital Search & Multi-Structure Indexing Lab.
 * Exercises CustomHashTable, RedBlackTree, BinarySearchTree, BTree, BinarySearch, and LinearSearch.
 */
public class RunSearchDemo {

    public static void execute(Scanner scanner) {
        System.out.println();
        System.out.println("=============================================================");
        System.out.println("   HOSPITAL SEARCH & MULTI-INDEX LAB (TREE & HASH STRUCTURES)");
        System.out.println("=============================================================");

        try (Connection conn = ConnectionManager.getConnection()) {
            HospitalRepository repo = new HospitalRepository(conn);
            List<Hospital> hospitals = repo.findAll();

            if (hospitals.isEmpty()) {
                System.out.println("Database empty. Initializing and importing seed data...");
                RunDataLoader.execute(scanner);
                hospitals = repo.findAll();
            }

            // Build all custom index structures
            CustomHashTable<Integer, Hospital> hashTable = new CustomHashTable<>();
            RedBlackTree<Integer, Hospital> rbTree = new RedBlackTree<>();
            BinarySearchTree<Integer, Hospital> bst = new BinarySearchTree<>();
            BTree<Integer, Hospital> bTree = new BTree<>(2); // B-Tree of degree t=2
            Integer[] idArray = new Integer[hospitals.size()];

            for (int i = 0; i < hospitals.size(); i++) {
                Hospital h = hospitals.get(i);
                int id = h.getHospitalId();
                hashTable.put(id, h);
                rbTree.insert(id, h);
                bst.insert(id, h);
                bTree.insert(id, h);
                idArray[i] = id;
            }

            System.out.println("Indexed 50 Ghanaian facilities into 4 distinct custom structures:");
            System.out.printf("  1. CustomHashTable  : %d elements | O(1) amortized lookup\n", hashTable.size());
            System.out.printf("  2. RedBlackTree     : %d elements | Height: %d | O(log N) balanced\n", rbTree.getSize(), rbTree.getHeight());
            System.out.printf("  3. BinarySearchTree : %d elements | Height: %d | O(h) hierarchical\n", bst.size(), bst.height());
            System.out.printf("  4. B-Tree (t=2)     : %d elements | Height: %d | O(log_t N) multi-way\n", bTree.size(), bTree.height());

            System.out.println("\nOptions:");
            System.out.println("  [1] Search Hospital by ID (Benchmark all 6 Search Structures)");
            System.out.println("  [0] Return to Main Menu");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();
            if ("1".equals(choice)) {
                System.out.print("Enter Hospital ID (1 - 50, e.g. 1 for Korle Bu, 16 for Komfo Anokye): ");
                String input = scanner.nextLine().trim();
                int targetId;
                try {
                    targetId = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("! Invalid ID. Please enter a valid number (1 - 50).");
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

                // 3. Binary Search Tree Search O(h)
                long startBst = System.nanoTime();
                Hospital foundBst = bst.search(targetId);
                long timeBstNs = System.nanoTime() - startBst;

                // 4. B-Tree Search O(log_t N)
                long startBTree = System.nanoTime();
                Hospital foundBTree = bTree.get(targetId);
                long timeBTreeNs = System.nanoTime() - startBTree;

                // 5. Binary Search O(log N) on sorted array
                long startBinary = System.nanoTime();
                int idx = new BinarySearch().search(idArray, targetId);
                long timeBinaryNs = System.nanoTime() - startBinary;

                // 6. Linear Search O(N)
                long startLinear = System.nanoTime();
                int linearIdx = LinearSearch.search(idArray, targetId);
                long timeLinearNs = System.nanoTime() - startLinear;

                if (foundHash != null) {
                    System.out.println("\n=============================================================");
                    System.out.println("✓ FACILITY DETAILS RETRIEVED");
                    System.out.println("=============================================================");
                    System.out.printf("   • Name           : %s\n", foundHash.getName());
                    System.out.printf("   • Region & Town  : %s, %s (District: %s)\n",
                            foundHash.getRegion(), foundHash.getTown(), foundHash.getDistrict());
                    System.out.printf("   • Facility Type  : %s (%s)\n",
                            foundHash.getFacilityType(), foundHash.getOwnershipType());
                    System.out.printf("   • ICU Bed Units  : %d beds\n", foundHash.getIcuCapacity());
                    System.out.printf("   • Ambulance Fleet: %d ambulances\n", foundHash.getAmbulanceCount());
                    System.out.println("-------------------------------------------------------------");
                    System.out.println("EMPIRICAL SEARCH LATENCY COMPARISON (ALL 6 STRUCTURES):");
                    System.out.printf("   [1] CustomHashTable  (O(1) amortized) : %6d ns\n", timeHashNs);
                    System.out.printf("   [2] RedBlackTree     (O(log N))       : %6d ns\n", timeRbNs);
                    System.out.printf("   [3] BinarySearchTree (O(h))           : %6d ns\n", timeBstNs);
                    System.out.printf("   [4] B-Tree (t=2)     (O(log_t N))     : %6d ns\n", timeBTreeNs);
                    System.out.printf("   [5] BinarySearch     (O(log N))       : %6d ns (Index: %d)\n", timeBinaryNs, idx);
                    System.out.printf("   [6] LinearSearch     (O(N))           : %6d ns (Index: %d)\n", timeLinearNs, linearIdx);
                } else {
                    System.out.printf("! Facility with ID %d was not found in the Ghanaian database.\n", targetId);
                }
            }
        } catch (Exception e) {
            System.err.println("! Error during search: " + e.getMessage());
        }
        System.out.println("=============================================================");
    }
}
