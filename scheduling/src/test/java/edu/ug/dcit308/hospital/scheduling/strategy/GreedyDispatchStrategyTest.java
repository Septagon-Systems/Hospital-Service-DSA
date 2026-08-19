package edu.ug.dcit308.hospital.scheduling.strategy;

import edu.ug.dcit308.hospital.scheduling.strategy.GreedyDispatchStrategy.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GreedyDispatchStrategyTest {

    @Test
    void testUrgentRequestGetsCheapestCompatibleResource() {
        Request r1 = new Request(0, 1, 0, 60.0);
        Request r2 = new Request(1, 3, 0, 50.0);

        Resource a1 = new Resource(0, 0, 200.0, 80.0);
        Resource a2 = new Resource(1, 0, 100.0, 80.0);

        DispatchPlan plan = GreedyDispatchStrategy.dispatchGreedy(
                new Request[]{r1, r2}, new Resource[]{a1, a2});

        assertEquals(2, plan.getFulfilledCount());
        assertEquals(300.0, plan.getTotalCost(), 1e-9);
        assertEquals(1, plan.getAssignedResource(0));
        assertEquals(0, plan.getAssignedResource(1));
    }

    @Test
    void testNoCompatibleResourceLeavesRequestUnfulfilled() {
        Request r1 = new Request(0, 1, 1, 50.0);
        Resource a1 = new Resource(0, 0, 100.0, 80.0);

        DispatchPlan plan = GreedyDispatchStrategy.dispatchGreedy(
                new Request[]{r1}, new Resource[]{a1});

        assertEquals(0, plan.getFulfilledCount());
        assertEquals(0.0, plan.getTotalCost(), 1e-9);
        assertEquals(-1, plan.getAssignedResource(0));
    }

    @Test
    void testCapacityExceededLeavesRequestUnfulfilled() {
        Request r1 = new Request(0, 1, 0, 120.0);
        Resource a1 = new Resource(0, 0, 100.0, 80.0);

        DispatchPlan plan = GreedyDispatchStrategy.dispatchGreedy(
                new Request[]{r1}, new Resource[]{a1});

        assertEquals(0, plan.getFulfilledCount());
    }

    @Test
    void testAllResourcesConsumed() {
        Request r1 = new Request(0, 1, 0, 50.0);
        Request r2 = new Request(1, 2, 0, 50.0);
        Request r3 = new Request(2, 3, 0, 50.0);

        Resource a1 = new Resource(0, 0, 100.0, 80.0);

        DispatchPlan plan = GreedyDispatchStrategy.dispatchGreedy(
                new Request[]{r1, r2, r3}, new Resource[]{a1});

        assertEquals(1, plan.getFulfilledCount());
        assertEquals(100.0, plan.getTotalCost(), 1e-9);
    }

    @Test
    void testEmptyRequests() {
        DispatchPlan plan = GreedyDispatchStrategy.dispatchGreedy(
                new Request[0], new Resource[]{new Resource(0, 0, 100, 80)});

        assertEquals(0, plan.getFulfilledCount());
        assertEquals(0.0, plan.getTotalCost(), 1e-9);
    }

    @Test
    void testEmptyResources() {
        Request r1 = new Request(0, 1, 0, 50.0);
        DispatchPlan plan = GreedyDispatchStrategy.dispatchGreedy(
                new Request[]{r1}, new Resource[0]);

        assertEquals(0, plan.getFulfilledCount());
        assertEquals(-1, plan.getAssignedResource(0));
    }

    @Test
    void testNullRequestsThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> GreedyDispatchStrategy.dispatchGreedy(null, new Resource[0]));
    }

    @Test
    void testNullResourcesThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> GreedyDispatchStrategy.dispatchGreedy(new Request[0], null));
    }

    @Test
    void testNullElementInRequestsThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> GreedyDispatchStrategy.dispatchGreedy(
                        new Request[]{null}, new Resource[0]));
    }

    @Test
    void testSortsByTriagePriority() {
        Request r1 = new Request(0, 3, 0, 50.0);
        Request r2 = new Request(1, 1, 0, 50.0);
        Request r3 = new Request(2, 2, 0, 50.0);

        Resource a1 = new Resource(0, 0, 300.0, 80.0);
        Resource a2 = new Resource(1, 0, 200.0, 80.0);
        Resource a3 = new Resource(2, 0, 100.0, 80.0);

        DispatchPlan plan = GreedyDispatchStrategy.dispatchGreedy(
                new Request[]{r1, r2, r3}, new Resource[]{a1, a2, a3});

        assertEquals(3, plan.getFulfilledCount());
        assertEquals(2, plan.getAssignedResource(1));
        assertEquals(1, plan.getAssignedResource(2));
        assertEquals(0, plan.getAssignedResource(0));
        assertEquals(600.0, plan.getTotalCost(), 1e-9);
    }
}
