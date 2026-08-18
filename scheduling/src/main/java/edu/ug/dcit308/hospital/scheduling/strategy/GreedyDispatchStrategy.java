package edu.ug.dcit308.hospital.scheduling.strategy;

import edu.ug.dcit308.hospital.ds.list.DynamicArrayList;

/**
 * Priority-based greedy dispatch strategy for emergency resource allocation.
 * Requests are sorted by triage level (1 = Critical, 2 = Urgent, 3 = Normal)
 * and each is assigned the cheapest compatible resource that is still available.
 *
 * <p>This greedy approach is locally optimal but can be globally suboptimal;
 * see {@code docs/counterexamples/01_greedy_failure_case.md} for a concrete
 * failure scenario.</p>
 */
public final class GreedyDispatchStrategy {

    private GreedyDispatchStrategy() {}

    // ------------------------------------------------------------------
    // Domain types
    // ------------------------------------------------------------------

    /** A service request from one hospital to another. */
    public static final class Request {
        private final int    id;
        private final int    triageLevel;   // 1 = Critical, 2 = Urgent, 3 = Normal
        private final int    resourceType;  // 0 = Ambulance, 1 = Blood Packet, 2 = ICU Bed
        private final double weightKg;       // patient weight (relevant for ambulances)

        public Request(int id, int triageLevel, int resourceType, double weightKg) {
            this.id           = id;
            this.triageLevel  = triageLevel;
            this.resourceType = resourceType;
            this.weightKg     = weightKg;
        }

        public int    getId()           { return id; }
        public int    getTriageLevel()  { return triageLevel; }
        public int    getResourceType() { return resourceType; }
        public double getWeightKg()     { return weightKg; }
    }

    /** An available resource (ambulance, blood packet, ICU bed, etc.). */
    public static final class Resource {
        private final int     id;
        private final int     type;        // must match Request.resourceType
        private final double  cost;
        private final double  capacityKg;  // max patient weight (ambulances)
        private       boolean available;

        public Resource(int id, int type, double cost, double capacityKg) {
            this.id         = id;
            this.type       = type;
            this.cost       = cost;
            this.capacityKg = capacityKg;
            this.available  = true;
        }

        public int     getId()         { return id; }
        public int     getType()       { return type; }
        public double  getCost()       { return cost; }
        public double  getCapacityKg() { return capacityKg; }
        public boolean isAvailable()   { return available; }

        public void markAssigned() { available = false; }
        public void markReleased() { available = true;  }
    }

    /** Result of a dispatch run. */
    public static final class DispatchPlan {
        private final int[]   assignment;    // assignment[reqIndex] = resIndex, or -1
        private final double  totalCost;
        private final int     fulfilledCount;

        DispatchPlan(int[] assignment, double totalCost, int fulfilledCount) {
            this.assignment      = assignment;
            this.totalCost       = totalCost;
            this.fulfilledCount  = fulfilledCount;
        }

        /** Resource index assigned to request {@code i}, or -1 if unfulfilled. */
        public int   getAssignedResource(int i) { return assignment[i]; }
        public double getTotalCost()             { return totalCost; }
        public int   getFulfilledCount()         { return fulfilledCount; }
        public int   getRequestCount()           { return assignment.length; }
    }

    // ------------------------------------------------------------------
    // Algorithm
    // ------------------------------------------------------------------

    /**
     * Dispatches resources to requests using a greedy priority strategy.
     *
     * <ol>
     *   <li>Requests are sorted by triage level (ascending: 1 before 2 before 3).</li>
     *   <li>For each request in order, the cheapest available compatible resource
     *       whose capacity covers the patient weight is assigned.</li>
     *   <li>If no compatible resource is available the request is left unfulfilled.</li>
     * </ol>
     *
     * @param requests  array of pending requests (non-null, no null elements)
     * @param resources array of available resources (non-null, no null elements)
     * @return a {@link DispatchPlan} recording assignments and total cost
     * @throws IllegalArgumentException if either argument is null or contains null
     */
    public static DispatchPlan dispatchGreedy(Request[] requests, Resource[] resources) {
        if (requests == null || resources == null) {
            throw new IllegalArgumentException("Requests and resources must not be null");
        }
        for (Request r : requests) {
            if (r == null) throw new IllegalArgumentException("Request must not be null");
        }
        for (Resource res : resources) {
            if (res == null) throw new IllegalArgumentException("Resource must not be null");
        }

        // Step 1: sort requests by triage level (insertion sort — small input OK)
        int n = requests.length;
        Request[] sorted = new Request[n];
        for (int i = 0; i < n; i++) sorted[i] = requests[i];
        insertionSortByTriage(sorted);

        // Step 2: greedy assignment
        int[] assignment = new int[n];
        double totalCost = 0.0;
        int fulfilled = 0;

        for (int i = 0; i < n; i++) {
            Request req = sorted[i];
            Resource best = null;
            int bestIdx = -1;

            for (int j = 0; j < resources.length; j++) {
                Resource res = resources[j];
                if (!res.isAvailable()) continue;
                if (res.getType() != req.getResourceType()) continue;
                if (res.getCapacityKg() < req.getWeightKg()) continue;

                if (best == null || res.getCost() < best.getCost()) {
                    best    = res;
                    bestIdx = j;
                }
            }

            if (best != null) {
                best.markAssigned();
                totalCost += best.getCost();
                fulfilled++;
                // record assignment using the original request id to find the slot
                assignment[req.getId()] = bestIdx;
            } else {
                assignment[req.getId()] = -1;
            }
        }

        return new DispatchPlan(assignment, totalCost, fulfilled);
    }

    // ------------------------------------------------------------------
    // Private helpers
    // ------------------------------------------------------------------

    private static void insertionSortByTriage(Request[] arr) {
        for (int i = 1; i < arr.length; i++) {
            Request key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j].getTriageLevel() > key.getTriageLevel()) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }
}
