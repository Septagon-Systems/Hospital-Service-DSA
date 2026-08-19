package edu.ug.dcit308.hospital.scheduling.strategy;

/**
 * A service request from one hospital to another, carrying triage priority,
 * resource requirements, and routing information.
 *
 * <p>Implements {@link Comparable} so that requests can be stored in a
 * {@link edu.ug.dcit308.hospital.ds.queue.MinHeapPriorityQueue}.  Lower
 * triage levels sort first (1 = Critical before 2 = Urgent before 3 = Normal).</p>
 */
public final class ServiceRequest implements Comparable<ServiceRequest> {

    private final int    id;
    private final int    originHospitalId;
    private final int    targetHospitalId;
    private final int    triageLevel;     // 1 = Critical, 2 = Urgent, 3 = Normal
    private final int    resourceType;    // 0 = Ambulance, 1 = Blood Packet, 2 = ICU Bed
    private final double weightKg;        // patient weight (relevant for ambulances)
    private final int    arrivalOrder;    // sequence number for FIFO tie-breaking
    private final boolean preempted;

    public ServiceRequest(int id, int originHospitalId, int targetHospitalId,
                          int triageLevel, int resourceType, double weightKg,
                          int arrivalOrder, boolean preempted) {
        this.id               = id;
        this.originHospitalId = originHospitalId;
        this.targetHospitalId = targetHospitalId;
        this.triageLevel      = triageLevel;
        this.resourceType     = resourceType;
        this.weightKg         = weightKg;
        this.arrivalOrder     = arrivalOrder;
        this.preempted        = preempted;
    }

    public int    getId()               { return id; }
    public int    getOriginHospitalId() { return originHospitalId; }
    public int    getTargetHospitalId() { return targetHospitalId; }
    public int    getTriageLevel()      { return triageLevel; }
    public int    getResourceType()     { return resourceType; }
    public double getWeightKg()         { return weightKg; }
    public int    getArrivalOrder()     { return arrivalOrder; }
    public boolean isPreempted()        { return preempted; }

    /**
     * Orders by triage level ascending (Critical before Normal), breaking ties
     * by arrival order (earlier arrival served first).
     */
    public int compareTo(ServiceRequest other) {
        int cmp = Integer.compare(this.triageLevel, other.triageLevel);
        if (cmp != 0) return cmp;
        return Integer.compare(this.arrivalOrder, other.arrivalOrder);
    }

    public String toString() {
        String triage;
        switch (triageLevel) {
            case 1:  triage = "CRITICAL"; break;
            case 2:  triage = "URGENT";   break;
            default: triage = "NORMAL";   break;
        }
        return "Request#" + id + "[" + triage
                + " " + originHospitalId + "->" + targetHospitalId + "]";
    }
}
