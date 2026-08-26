package edu.ug.dcit308.hospital.app;

import edu.ug.dcit308.hospital.algo.search.BinarySearch;
import edu.ug.dcit308.hospital.algo.search.LinearSearch;
import edu.ug.dcit308.hospital.ds.list.DynamicArrayList;

public class RunSearchDemo {

    public static void execute() {
        DynamicArrayList<RunDataLoader.Hospital> defaultDataset = RunDataLoader.loadData();
        execute(defaultDataset);
    }

    public static void execute(DynamicArrayList<RunDataLoader.Hospital> dataset) {
        System.out.println();
        System.out.println("========== SEARCH DEMONSTRATION ==========");

        if (dataset == null || dataset.isEmpty()) {
            System.out.println("[!] No hospital dataset available for search.");
            System.out.println("===========================================");
            return;
        }

        System.out.println("Searching across " + dataset.size() + " active hospital records...");

        // Extract bed capacities into an array for searching
        Integer[] capacities = new Integer[dataset.size()];
        for (int i = 0; i < dataset.size(); i++) {
            capacities[i] = dataset.get(i).getCapacity();
        }

        // Print capacities array without using java.util.Arrays
        System.out.print("Loaded Hospital Capacities: [");
        for (int i = 0; i < capacities.length; i++) {
            System.out.print(capacities[i] + (i < capacities.length - 1 ? ", " : ""));
        }
        System.out.println("]");

        int targetCapacity = 500; // Target: 37 Military Hospital (500 beds)
        System.out.println("Target Bed Capacity to Search: " + targetCapacity);

        // 1. Linear Search
        int linearIndex = LinearSearch.search(capacities, targetCapacity);
        if (linearIndex != -1) {
            System.out.println("Linear Search Result : Found at index " + linearIndex + " -> " + dataset.get(linearIndex));
        } else {
            System.out.println("Linear Search Result : Record not found.");
        }

        // 2. Binary Search (requires sorted array)
        // Sort capacities array for Binary Search precondition
        for (int i = 0; i < capacities.length - 1; i++) {
            for (int j = 0; j < capacities.length - i - 1; j++) {
                if (capacities[j] > capacities[j + 1]) {
                    Integer temp = capacities[j];
                    capacities[j] = capacities[j + 1];
                    capacities[j + 1] = temp;
                }
            }
        }

        BinarySearch binarySearch = new BinarySearch();
        int binaryIndex = binarySearch.search(capacities, targetCapacity);
        if (binaryIndex != -1) {
            System.out.println("Binary Search Result : Found in sorted capacities at index " + binaryIndex);
        } else {
            System.out.println("Binary Search Result : Record not found.");
        }

        System.out.println("===========================================");
    }
}