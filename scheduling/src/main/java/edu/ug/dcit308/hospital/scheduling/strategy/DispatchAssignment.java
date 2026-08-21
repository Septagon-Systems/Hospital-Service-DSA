package edu.ug.dcit308.hospital.scheduling.strategy;

/**
 * Result produced by {@link HospitalDispatchCentre#serveNextRequest()}.
 * Couples the served {@link ServiceRequest} with the {@link Resource} that was
 * assigned to it (which may be {@code null} when no compatible resource is
 * available).
 */
public final class DispatchAssignment {

    private final ServiceRequest request;
    private final Resource       resource;

    public DispatchAssignment(ServiceRequest request, Resource resource) {
        this.request  = request;
        this.resource = resource;
    }

    public ServiceRequest getRequest()  { return request; }
    public Resource       getResource() { return resource; }

    public boolean hasResource() {
        return resource != null;
    }

    public String toString() {
        if (resource != null) {
            return request + " assigned " + resource;
        }
        return request + " (no resource available)";
    }
}
