package edu.ug.dcit308.hospital.scheduling.strategy;

/**
 * A pool of {@link Resource} objects managed by the dispatch centre.
 * Provides a greedy cheapest-compatible matching algorithm used during
 * each dispatch cycle.
 *
 * <p>The pool is backed by a simple array (no {@code java.util} collections)
 * to stay within assessed module boundaries.</p>
 */
public final class ResourcePool {

    private final Resource[] resources;
    private final int count;

    public ResourcePool(Resource[] resources) {
        this.resources = resources;
        this.count     = resources.length;
    }

    /**
     * Greedily selects the cheapest available resource that is compatible with
     * the given request.  Iterates through all resources (O(n)) and picks the
     * one with the lowest {@code costPerUse} among those that satisfy
     * {@link Resource#isCompatible(ServiceRequest)}.
     *
     * @param request the request to match
     * @return the cheapest compatible available resource, or {@code null} if
     *         none is available
     */
    public Resource findCheapestCompatible(ServiceRequest request) {
        Resource cheapest = null;
        for (int i = 0; i < count; i++) {
            Resource r = resources[i];
            if (r.isAvailable() && r.isCompatible(request)) {
                if (cheapest == null || r.getCostPerUse() < cheapest.getCostPerUse()) {
                    cheapest = r;
                }
            }
        }
        return cheapest;
    }

    public int size() { return count; }
}
