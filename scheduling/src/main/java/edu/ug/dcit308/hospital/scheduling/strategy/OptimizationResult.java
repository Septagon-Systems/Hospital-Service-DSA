package edu.ug.dcit308.hospital.scheduling.strategy;

import edu.ug.dcit308.hospital.ds.list.DynamicArrayList;


/**
 * Result of a 0/1 knapsack capacity optimization run: the best achievable
 * value, the total weight used, which item indices were selected, and the
 * full DP memoization table (exposed for the trace-table evidence doc).
 */
public class OptimizationResult {

    private final double totalValue;
    private final double totalWeight;
    private final DynamicArrayList<Integer> selectedIndices;
    private final double[][] dpTable;

    public OptimizationResult(double totalValue, double totalWeight,
                               DynamicArrayList<Integer> selectedIndices, double[][] dpTable) {
        this.totalValue = totalValue;
        this.totalWeight = totalWeight;
        this.selectedIndices = selectedIndices;
        this.dpTable = dpTable;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public DynamicArrayList<Integer> getSelectedIndices() {
        return selectedIndices;
    }

    public double[][] getDpTable() {
        return dpTable;
    }
}
