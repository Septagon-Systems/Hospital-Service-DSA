package edu.ug.dcit308.hospital.scheduling.strategy;

import edu.ug.dcit308.hospital.ds.deque.ArrayDeque;
import edu.ug.dcit308.hospital.ds.queue.CircularQueue;
import edu.ug.dcit308.hospital.ds.queue.MinHeapPriorityQueue;
import edu.ug.dcit308.hospital.ds.stack.ArrayStack;

/**
 * Multi-queue hospital dispatch centre that routes incoming requests into
 * priority-based queues, serves them in urgency order, and greedily assigns
 * the cheapest compatible resource from a {@link ResourcePool}.
 *
 * <h3>Queue architecture</h3>
 * <ul>
 *   <li><b>Urgent queue</b> ({@link MinHeapPriorityQueue}) – Critical (P1) and
 *       Urgent (P2) requests.  Served first via min-heap ordering.</li>
 *   <li><b>Preemption deque</b> ({@link ArrayDeque}) – requests that were
 *       preempted or rerouted mid-service.  Served second.</li>
 *   <li><b>Normal queue</b> ({@link CircularQueue}) – Routine / Normal (P3)
 *       requests.  Served last, FIFO order.</li>
 *   <li><b>Action stack</b> ({@link ArrayStack}) – audit trail that records
 *       every admission and service event for undo support.</li>
 * </ul>
 *
 * <h3>Greedy resource matching</h3>
 * After a request is dequeued, the centre calls
 * {@link ResourcePool#findCheapestCompatible(ServiceRequest)} to pair it with
 * the cheapest available compatible resource.
 */
public final class HospitalDispatchCentre {

    private final MinHeapPriorityQueue<ServiceRequest> urgentQueue;
    private final ArrayDeque<ServiceRequest>           preemptionDeque;
    private final CircularQueue<ServiceRequest>        normalQueue;
    private final ArrayStack<ActionRecord>             actionStack;
    private final ResourcePool                         resourcePool;

    private int servedCount;
    private int urgentServed;
    private int normalServed;
    private int preemptionServed;

    public HospitalDispatchCentre(int normalCapacity,
                                  int preemptionCapacity,
                                  ResourcePool resourcePool) {
        this.urgentQueue      = new MinHeapPriorityQueue<>();
        this.preemptionDeque  = new ArrayDeque<>();
        this.normalQueue      = new CircularQueue<>(normalCapacity);
        this.actionStack      = new ArrayStack<>();
        this.resourcePool     = resourcePool;
    }

    // ------------------------------------------------------------------ //
    //  Admission                                                          //
    // ------------------------------------------------------------------ //

    /**
     * Routes an incoming request into the appropriate queue:
     * <ol>
     *   <li>Preempted requests go to the preemption deque.</li>
     *   <li>Critical (P1) and Urgent (P2) requests go to the urgent queue.</li>
     *   <li>Normal (P3+) requests go to the normal queue.</li>
     * </ol>
     *
     * @return {@code true} if the request was admitted, {@code false} if the
     *         bounded normal queue is full
     */
    public boolean admitRequest(ServiceRequest request) {
        if (request.isPreempted()) {
            preemptionDeque.addFirst(request);
            actionStack.push(new ActionRecord("ADMIT_PREEMPT", request));
            return true;
        }
        if (request.getTriageLevel() <= 2) {
            urgentQueue.enqueue(request);
            actionStack.push(new ActionRecord("ADMIT_URGENT", request));
            return true;
        }
        boolean added = normalQueue.enqueue(request);
        if (added) {
            actionStack.push(new ActionRecord("ADMIT_NORMAL", request));
        }
        return added;
    }

    // ------------------------------------------------------------------ //
    //  Service                                                            //
    // ------------------------------------------------------------------ //

    /**
     * Dequeues the next request in priority order (urgent → preemption →
     * normal), greedily assigns the cheapest compatible resource, and returns
     * the resulting {@link DispatchAssignment}.
     *
     * @return the assignment, or {@code null} when no requests remain
     */
    public DispatchAssignment serveNextRequest() {
        ServiceRequest req = null;

        if (!urgentQueue.isEmpty()) {
            req = urgentQueue.dequeue();
        } else if (!preemptionDeque.isEmpty()) {
            req = preemptionDeque.removeFirst();
        } else if (!normalQueue.isEmpty()) {
            req = normalQueue.dequeue();
        }

        if (req == null) {
            return null;
        }

        Resource assigned = resourcePool.findCheapestCompatible(req);
        if (assigned != null) {
            assigned.markAssigned();
        }

        servedCount++;
        if (req.getTriageLevel() <= 2) {
            urgentServed++;
        } else if (req.isPreempted()) {
            preemptionServed++;
        } else {
            normalServed++;
        }

        actionStack.push(new ActionRecord("SERVE", req, assigned));

        return new DispatchAssignment(req, assigned);
    }

    // ------------------------------------------------------------------ //
    //  Undo                                                               //
    // ------------------------------------------------------------------ //

    /**
     * Undoes the most recent action recorded on the stack.
     * <ul>
     *   <li>Serve events release the resource and re-admit the request.</li>
     *   <li>Admit events remove the request from the corresponding queue.</li>
     * </ul>
     *
     * @return the undone action, or {@code null} when the stack is empty
     */
    public ActionRecord undoLastAction() {
        if (actionStack.isEmpty()) {
            return null;
        }
        ActionRecord last = actionStack.pop();

        if ("SERVE".equals(last.getActionType())) {
            servedCount--;
            if (last.getResource() != null) {
                last.getResource().markReleased(); // Restore vehicle/bed to pool
            }
            if (last.getRequest().getTriageLevel() <= 2) {
                urgentServed--;
            } else if (last.getRequest().isPreempted()) {
                preemptionServed--;
            } else {
                normalServed--;
            }
            reAdmit(last.getRequest());
        } else if ("ADMIT_URGENT".equals(last.getActionType())) {
            removeFromUrgentQueue(last.getRequest());
        } else if ("ADMIT_NORMAL".equals(last.getActionType())) {
            removeFromNormalQueue();
        } else if ("ADMIT_PREEMPT".equals(last.getActionType())) {
            removeFromPreemptionDeque();
        }
        return last;
    }

    // ------------------------------------------------------------------ //
    //  Report                                                             //
    // ------------------------------------------------------------------ //

    public void printReport() {
        System.out.println("--- DISPATCH CENTRE REPORT ---");
        System.out.println("Served total:     " + servedCount);
        System.out.println("Urgent served:    " + urgentServed);
        System.out.println("Normal served:    " + normalServed);
        System.out.println("Preemption served:" + preemptionServed);
        System.out.println("Remaining urgent:     " + urgentQueue.size());
        System.out.println("Remaining preemptions:" + preemptionDeque.size());
        System.out.println("Remaining normal:     " + normalQueue.size());
        System.out.println("Pending actions:      " + actionStack.size());
    }

    // ------------------------------------------------------------------ //
    //  Accessors (useful for CLI, GUI and tests)                          //
    // ------------------------------------------------------------------ //

    public int getServedCount()             { return servedCount; }
    public int getUrgentServed()            { return urgentServed; }
    public int getNormalServed()            { return normalServed; }
    public int getPreemptionServed()        { return preemptionServed; }
    public int pendingActions()             { return actionStack.size(); }
    public int getPendingUrgentCount()      { return urgentQueue.size(); }
    public int getPendingPreemptionCount()  { return preemptionDeque.size(); }
    public int getPendingNormalCount()      { return normalQueue.size(); }
    public int getTotalPendingCount()       { return urgentQueue.size() + preemptionDeque.size() + normalQueue.size(); }
    public ResourcePool getResourcePool()   { return resourcePool; }
    public MinHeapPriorityQueue<ServiceRequest> getUrgentQueue()     { return urgentQueue; }
    public ArrayDeque<ServiceRequest>           getPreemptionDeque() { return preemptionDeque; }
    public CircularQueue<ServiceRequest>        getNormalQueue()     { return normalQueue; }
    public ArrayStack<ActionRecord>             getActionStack()     { return actionStack; }
    public void                                 clearActionStack()   { actionStack.clear(); }

    // ------------------------------------------------------------------ //
    //  Private helpers                                                    //
    // ------------------------------------------------------------------ //

    /**
     * Rebuild the urgent queue without the given request.  This is needed
     * because {@link MinHeapPriorityQueue} does not support arbitrary removal.
     */
    private void removeFromUrgentQueue(ServiceRequest target) {
        MinHeapPriorityQueue<ServiceRequest> temp = new MinHeapPriorityQueue<>();
        while (!urgentQueue.isEmpty()) {
            ServiceRequest r = urgentQueue.dequeue();
            if (!r.equals(target)) {
                temp.enqueue(r);
            }
        }
        while (!temp.isEmpty()) {
            urgentQueue.enqueue(temp.dequeue());
        }
    }

    /** Remove the last-enqueued normal request (LIFO undo). */
    private void removeFromNormalQueue() {
        if (!normalQueue.isEmpty()) {
            normalQueue.dequeue();
        }
    }

    /** Remove the most recently added preemption request. */
    private void removeFromPreemptionDeque() {
        if (!preemptionDeque.isEmpty()) {
            preemptionDeque.removeFirst();
        }
    }

    /**
     * Re-routes a request back into its appropriate queue without recording an
     * admission action.  Used exclusively by {@link #undoLastAction()} to avoid
     * polluting the action stack.
     */
    private void reAdmit(ServiceRequest request) {
        if (request.isPreempted()) {
            preemptionDeque.addFirst(request);
        } else if (request.getTriageLevel() <= 2) {
            urgentQueue.enqueue(request);
        } else {
            normalQueue.enqueue(request);
        }
    }
}
