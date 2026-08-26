package edu.ug.dcit308.hospital.scheduling.strategy;

/**
 * A hospital resource that can be dispatched to fulfil a {@link ServiceRequest}.
 * Each resource has a type (0 = Ambulance, 1 = Blood Packet, 2 = ICU Bed),
 * a per-use cost, a maximum patient-weight capacity, and a current availability
 * flag.
 */
public final class Resource {

    private final int    id;
    private final int    resourceType;
    private final double costPerUse;
    private final double maxWeightKg;
    private boolean      available;

    public Resource(int id, int resourceType, double costPerUse, double maxWeightKg) {
        this.id           = id;
        this.resourceType = resourceType;
        this.costPerUse   = costPerUse;
        this.maxWeightKg  = maxWeightKg;
        this.available    = true;
    }

    public int    getId()           { return id; }
    public int    getResourceType() { return resourceType; }
    public double getCostPerUse()   { return costPerUse; }
    public double getMaxWeightKg()  { return maxWeightKg; }
    public boolean isAvailable()    { return available; }

    /**
     * Returns {@code true} when this resource can serve a request:
     * same resource type and patient weight within capacity.
     */
    public boolean isCompatible(ServiceRequest req) {
        return this.resourceType == req.getResourceType()
            && this.maxWeightKg >= req.getWeightKg();
    }

    public void markAssigned()   { this.available = false; }
    public void markReleased()   { this.available = true;  }

    public String toString() {
        String type;
        switch (resourceType) {
            case 0:  type = "AMBULANCE"; break;
            case 1:  type = "BLOOD";     break;
            default: type = "ICU_BED";   break;
        }
        return "Resource#" + id + "[" + type + " $" + costPerUse + "]";
    }
}
