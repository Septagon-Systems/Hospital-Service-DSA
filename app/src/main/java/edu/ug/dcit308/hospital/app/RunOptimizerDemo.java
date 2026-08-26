package edu.ug.dcit308.hospital.app;

import java.util.Scanner;

import edu.ug.dcit308.hospital.ds.list.DynamicArrayList;
import edu.ug.dcit308.hospital.scheduling.strategy.CapacityOptimizer;
import edu.ug.dcit308.hospital.scheduling.strategy.OptimizationResult;

/**
 * Operations Handler: Medical Supply Payload Optimization (0/1 Knapsack Dynamic Programming).
 */
public class RunOptimizerDemo {

    private static final String[] ITEM_NAMES = {
            "Emergency Blood Plasma Packs (O-Neg)",
            "Portable ICU Ventilator Unit",
            "Emergency Surgical Trauma Kit",
            "Advanced Life Support Defibrillator",
            "Broad-Spectrum Antivenom Vials",
            "Pediatric Resuscitation Kit",
            "Portable Oxygen Concentrator",
            "Dialysis Filtration Membrane Pack"
    };

    private static final double[] ITEM_WEIGHTS = { 12.0, 25.0,  8.0, 15.0,  3.0,  6.0, 18.0, 10.0 };
    private static final double[] ITEM_VALUES  = { 90.0, 95.0, 80.0, 85.0, 70.0, 75.0, 65.0, 60.0 };

    public static void execute(Scanner scanner) {
        System.out.println();
        System.out.println("=============================================================");
        System.out.println("   MEDICAL SUPPLY PAYLOAD OPTIMIZER (0/1 KNAPSACK DP)        ");
        System.out.println("=============================================================");
        System.out.println("Available Emergency Medical Supply Cargo:");
        for (int i = 0; i < ITEM_NAMES.length; i++) {
            System.out.printf("  [%d] %-40s | Weight: %4.1f kg | Clinical Value: %4.1f pts\n",
                    i + 1, ITEM_NAMES[i], ITEM_WEIGHTS[i], ITEM_VALUES[i]);
        }

        System.out.print("\nEnter Emergency Vehicle Max Payload Capacity in kg (e.g. 35.0, 50.0): ");
        double capacity = 35.0;
        try {
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                capacity = Double.parseDouble(input);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Defaulting to 35.0 kg.");
        }

        System.out.printf("\nCalculating optimal supply combination for %.1f kg payload...\n", capacity);
        long start = System.nanoTime();
        CapacityOptimizer optimizer = new CapacityOptimizer();
        OptimizationResult result = optimizer.optimizeCapacity(ITEM_WEIGHTS, ITEM_VALUES, capacity);
        long elapsedNs = System.nanoTime() - start;

        System.out.println("\n✓ Optimal Supply Selection Result:");
        System.out.printf("   Total Clinical Value Score : %.1f points\n", result.getTotalValue());
        System.out.printf("   Total Payload Weight       : %.1f kg / %.1f kg max\n", result.getTotalWeight(), capacity);
        System.out.printf("   DP Computation Time        : %d ns (%.4f ms)\n", elapsedNs, elapsedNs / 1_000_000.0);

        System.out.println("\n--- Selected Supply Cargo Manifest ---");
        DynamicArrayList<Integer> selected = result.getSelectedIndices();
        for (int i = 0; i < selected.size(); i++) {
            int idx = selected.get(i);
            System.out.printf("  ✓ %-40s | %4.1f kg | %4.1f pts\n",
                    ITEM_NAMES[idx], ITEM_WEIGHTS[idx], ITEM_VALUES[idx]);
        }

        System.out.println("=============================================================");
    }
}
