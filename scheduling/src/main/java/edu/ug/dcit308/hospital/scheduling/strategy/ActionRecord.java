package edu.ug.dcit308.hospital.scheduling.strategy;

/**
 * An immutable audit record pushed onto the action stack after every admission
 * or service event.  Used by {@link HospitalDispatchCentre} for undo support.
 */
public final class ActionRecord {

    private final String         actionType; // ADMIT_URGENT, ADMIT_NORMAL, ADMIT_PREEMPT, SERVE
    private final ServiceRequest request;

    public ActionRecord(String actionType, ServiceRequest request) {
        this.actionType = actionType;
        this.request    = request;
    }

    public String         getActionType() { return actionType; }
    public ServiceRequest getRequest()    { return request; }

    public String toString() {
        return actionType + " -> " + request;
    }
}
