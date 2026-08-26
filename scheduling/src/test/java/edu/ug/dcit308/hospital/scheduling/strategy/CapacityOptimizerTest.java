package edu.ug.dcit308.hospital.scheduling.strategy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CapacityOptimizerTest {

    private final CapacityOptimizer optimizer = new CapacityOptimizer();

    // --- Normal case ---------------------------------------------------

    @Test
    void testGhanaSupplyRunSelectsBestCombination() {
        // 3 medical supply crates for a Korle Bu transport run
        double[] weights = {6, 5, 4};
        double[] values = {7, 5, 6};
        double capacity = 10;

        OptimizationResult result = optimizer.optimizeCapacity(weights, values, capacity);

        assertEquals(13.0, result.getTotalValue(), 1e-9);
        assertEquals(10.0, result.getTotalWeight(), 1e-9);
        assertEquals(2, result.getSelectedIndices().size());
        assertEquals(0, result.getSelectedIndices().get(0));
        assertEquals(2, result.getSelectedIndices().get(1));
    }

    @Test
    void testSelectedIndicesPreserveOriginalOrder() {
        double[] weights = {1, 2, 3, 4};
        double[] values = {10, 20, 30, 40};
        OptimizationResult result = optimizer.optimizeCapacity(weights, values, 10);

        // With this data every item fits, so all 4 indices should be selected in order
        assertEquals(4, result.getSelectedIndices().size());
        for (int i = 0; i < 4; i++) {
            assertEquals(i, result.getSelectedIndices().get(i));
        }
    }

    // --- Boundary / edge cases ------------------------------------------

    @Test
    void testZeroCapacitySelectsNothing() {
        double[] weights = {2, 3};
        double[] values = {5, 6};
        OptimizationResult result = optimizer.optimizeCapacity(weights, values, 0);

        assertEquals(0.0, result.getTotalValue(), 1e-9);
        assertEquals(0.0, result.getTotalWeight(), 1e-9);
        assertEquals(0, result.getSelectedIndices().size());
    }

    @Test
    void testEmptyItemListReturnsZeroValue() {
        OptimizationResult result = optimizer.optimizeCapacity(new double[0], new double[0], 10);

        assertEquals(0.0, result.getTotalValue(), 1e-9);
        assertEquals(0, result.getSelectedIndices().size());
    }

    @Test
    void testItemExactlyFillsCapacity() {
        OptimizationResult result = optimizer.optimizeCapacity(
                new double[]{10}, new double[]{99}, 10);

        assertEquals(99.0, result.getTotalValue(), 1e-9);
        assertEquals(10.0, result.getTotalWeight(), 1e-9);
        assertEquals(1, result.getSelectedIndices().size());
    }

    @Test
    void testItemHeavierThanCapacityIsExcluded() {
        OptimizationResult result = optimizer.optimizeCapacity(
                new double[]{15}, new double[]{99}, 10);

        assertEquals(0.0, result.getTotalValue(), 1e-9);
        assertEquals(0, result.getSelectedIndices().size());
    }

    @Test
    void testSingleItemUnderCapacityIsAlwaysSelected() {
        OptimizationResult result = optimizer.optimizeCapacity(
                new double[]{3}, new double[]{50}, 10);

        assertEquals(50.0, result.getTotalValue(), 1e-9);
        assertEquals(1, result.getSelectedIndices().size());
        assertEquals(0, result.getSelectedIndices().get(0));
    }

    // --- Invalid input cases ---------------------------------------------

    @Test
    void testNullWeightsThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> optimizer.optimizeCapacity(null, new double[]{1, 2}, 10));
    }

    @Test
    void testNullValuesThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> optimizer.optimizeCapacity(new double[]{1, 2}, null, 10));
    }

    @Test
    void testMismatchedArrayLengthsThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> optimizer.optimizeCapacity(new double[]{1, 2, 3}, new double[]{1, 2}, 10));
    }

    @Test
    void testNegativeCapacityThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> optimizer.optimizeCapacity(new double[]{1, 2}, new double[]{5, 6}, -1));
    }

    @Test
    void testNegativeItemWeightThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> optimizer.optimizeCapacity(new double[]{2, -3}, new double[]{5, 6}, 10));
    }
}
