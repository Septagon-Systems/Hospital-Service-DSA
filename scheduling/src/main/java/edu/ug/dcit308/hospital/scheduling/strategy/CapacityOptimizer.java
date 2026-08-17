package edu.ug.dcit308.hospital.scheduling.strategy;

import edu.ug.dcit308.hospital.ds.list.DynamicArrayList;


/**
 * Pod 6 — Graph & Optimisation
 * Issue #22: Implement 0/1 Knapsack DP for optimal medical supply transport
 * under capacity constraints.
 *
 * No java.util collection classes are used, per the project brief.
 */
public class CapacityOptimizer {

    // Scale factor used to convert double weights/capacity into integer
    // indices for the DP table (preserves 2 decimal places of precision).
    private static final int SCALE = 100;

    public OptimizationResult optimizeCapacity(double[] weights, double[] values, double capacity) {
        int n = weights.length;
        int scaledCapacity = (int) Math.round(capacity * SCALE);

        int[] scaledWeights = new int[n];
        for (int i = 0; i < n; i++) {
            scaledWeights[i] = (int) Math.round(weights[i] * SCALE);
        }

        // dp[i][w] = max value achievable using the first i items with capacity w (scaled)
        double[][] dp = new double[n + 1][scaledCapacity + 1];

        for (int i = 1; i <= n; i++) {
            int wi = scaledWeights[i - 1];
            double vi = values[i - 1];
            for (int w = 0; w <= scaledCapacity; w++) {
                dp[i][w] = dp[i - 1][w]; // exclude item i-1
                if (wi <= w) {
                    double included = dp[i - 1][w - wi] + vi;
                    if (included > dp[i][w]) {
                        dp[i][w] = included; // include item i-1
                    }
                }
            }
        }

        // Backtrack through the table to recover which items were selected.
        DynamicArrayList<Integer> selectedIndices = new DynamicArrayList<>();
        int w = scaledCapacity;
        double totalWeight = 0.0;

        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                selectedIndices.add(0, i - 1); // insert at front to keep original order
                w -= scaledWeights[i - 1];
                totalWeight += weights[i - 1];
            }
        }

        double totalValue = dp[n][scaledCapacity];

        return new OptimizationResult(totalValue, totalWeight, selectedIndices, dp);
    }
}
