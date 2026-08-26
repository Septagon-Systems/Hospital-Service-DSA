package edu.ug.dcit308.hospital.scheduling.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the multi-queue {@link HospitalDispatchCentre}.
 * Covers admission routing, priority-based service, greedy resource matching,
 * undo support, and edge cases.
 */
public class HospitalDispatchCentreTest {

    private static final int NORMAL_CAPACITY     = 5;
    private static final int PREEMPTION_CAPACITY = 5;

    private ResourcePool resourcePool;
    private HospitalDispatchCentre centre;

    @BeforeEach
    void setUp() {
        Resource[] resources = {
            new Resource(1, 0, 100.0, 150.0),   // Ambulance #1  $100
            new Resource(2, 0, 200.0, 300.0),   // Ambulance #2  $200
            new Resource(3, 1,  50.0, 999.0),   // Blood pack    $50
            new Resource(4, 2, 500.0, 999.0),   // ICU bed       $500
        };
        resourcePool = new ResourcePool(resources);
        centre = new HospitalDispatchCentre(NORMAL_CAPACITY, PREEMPTION_CAPACITY, resourcePool);
    }

    private ServiceRequest urgent(int id) {
        return new ServiceRequest(id, 1, 2, 2, 0, 70.0, id, false); // Urgent ambulance
    }

    private ServiceRequest critical(int id) {
        return new ServiceRequest(id, 1, 2, 1, 2, 70.0, id, false); // Critical ICU
    }

    private ServiceRequest normal(int id) {
        return new ServiceRequest(id, 1, 3, 3, 0, 60.0, id, false); // Normal ambulance
    }

    private ServiceRequest preempted(int id) {
        return new ServiceRequest(id, 2, 3, 2, 1, 70.0, id, true);  // Preempted blood
    }

    // ---- Admission tests ------------------------------------------------ //

    @Test
    void testAdmitUrgentRequest() {
        assertTrue(centre.admitRequest(urgent(1)));
    }

    @Test
    void testAdmitCriticalRequest() {
        assertTrue(centre.admitRequest(critical(1)));
    }

    @Test
    void testAdmitNormalRequest() {
        assertTrue(centre.admitRequest(normal(1)));
    }

    @Test
    void testAdmitPreemptedRequest() {
        assertTrue(centre.admitRequest(preempted(1)));
    }

    @Test
    void testAdmitNormalQueueOverflow() {
        for (int i = 0; i < NORMAL_CAPACITY; i++) {
            assertTrue(centre.admitRequest(new ServiceRequest(i, 1, 3, 3, 0, 60.0, i, false)));
        }
        assertFalse(centre.admitRequest(new ServiceRequest(99, 1, 3, 3, 0, 60.0, 99, false)));
    }

    @Test
    void testAdmitRecordsAction() {
        centre.admitRequest(urgent(1));
        assertEquals(1, centre.pendingActions());
    }

    // ---- Service priority tests ----------------------------------------- //

    @Test
    void testUrgentServedBeforeNormal() {
        centre.admitRequest(normal(1));
        centre.admitRequest(urgent(2));

        DispatchAssignment a1 = centre.serveNextRequest();
        assertNotNull(a1);
        assertEquals(2, a1.getRequest().getId());
        assertEquals("URGENT", triageName(a1.getRequest()));
    }

    @Test
    void testPreemptionServedBeforeNormal() {
        centre.admitRequest(normal(1));
        centre.admitRequest(preempted(2));

        DispatchAssignment a1 = centre.serveNextRequest();
        assertEquals(2, a1.getRequest().getId());
    }

    @Test
    void testThreeTierPriority() {
        centre.admitRequest(normal(1));
        centre.admitRequest(preempted(2));
        centre.admitRequest(urgent(3));
        centre.admitRequest(critical(4));

        DispatchAssignment a1 = centre.serveNextRequest();
        assertEquals(4, a1.getRequest().getId()); // Critical first

        DispatchAssignment a2 = centre.serveNextRequest();
        assertEquals(3, a2.getRequest().getId()); // Urgent second

        DispatchAssignment a3 = centre.serveNextRequest();
        assertEquals(2, a3.getRequest().getId()); // Preemption third

        DispatchAssignment a4 = centre.serveNextRequest();
        assertEquals(1, a4.getRequest().getId()); // Normal last
    }

    @Test
    void testServeEmptyCentreReturnsNull() {
        assertNull(centre.serveNextRequest());
    }

    // ---- Greedy resource matching tests --------------------------------- //

    @Test
    void testCheapestResourceAssigned() {
        centre.admitRequest(urgent(1)); // needs ambulance (type 0)
        DispatchAssignment a = centre.serveNextRequest();
        assertNotNull(a.getResource());
        assertEquals(100.0, a.getResource().getCostPerUse()); // cheapest ambulance
    }

    @Test
    void testNoCompatibleResource() {
        ServiceRequest train = new ServiceRequest(1, 1, 2, 1, 3, 70.0, 1, false); // type 3 = train
        centre.admitRequest(train);
        DispatchAssignment a = centre.serveNextRequest();
        assertNull(a.getResource());
    }

    @Test
    void testExhaustedResource() {
        centre.admitRequest(urgent(1));
        centre.serveNextRequest(); // takes cheapest ambulance ($100)

        centre.admitRequest(urgent(2));
        DispatchAssignment a2 = centre.serveNextRequest(); // takes second ambulance ($200)
        assertEquals(200.0, a2.getResource().getCostPerUse());
    }

    @Test
    void testWeightExceedsCapacity() {
        ServiceRequest heavy = new ServiceRequest(1, 1, 2, 2, 0, 400.0, 1, false);
        centre.admitRequest(heavy);
        DispatchAssignment a = centre.serveNextRequest();
        assertNull(a.getResource()); // both ambulances under 400kg
    }

    @Test
    void testBloodPackCheapest() {
        centre.admitRequest(preempted(1)); // type 1 = blood
        DispatchAssignment a = centre.serveNextRequest();
        assertEquals(50.0, a.getResource().getCostPerUse());
    }

    // ---- Undo tests ----------------------------------------------------- //

    @Test
    void testUndoServeReAdmitsRequest() {
        centre.admitRequest(normal(1));
        centre.serveNextRequest();
        assertEquals(1, centre.getServedCount());

        ActionRecord undone = centre.undoLastAction();
        assertEquals("SERVE", undone.getActionType());
        assertEquals(0, centre.getServedCount());
        assertEquals(1, centre.pendingActions()); // original ADMIT_NORMAL still on stack
    }

    @Test
    void testUndoServeReleasesAssignedResource() {
        centre.admitRequest(urgent(1));
        DispatchAssignment a1 = centre.serveNextRequest();
        assertNotNull(a1.getResource());
        assertEquals(1, a1.getResource().getId()); // Ambulance #1 ($100)
        assertFalse(a1.getResource().isAvailable()); // Marked assigned

        centre.undoLastAction(); // Rollback serve

        assertTrue(a1.getResource().isAvailable()); // Restored to pool

        // Serving again should grab Ambulance #1 at $100 instead of Ambulance #2 at $200
        DispatchAssignment a2 = centre.serveNextRequest();
        assertNotNull(a2.getResource());
        assertEquals(1, a2.getResource().getId());
        assertEquals(100.0, a2.getResource().getCostPerUse());
    }

    @Test
    void testUndoAdmitUrgentRemovesFromQueue() {
        centre.admitRequest(urgent(1));
        centre.admitRequest(urgent(2));

        ActionRecord undone = centre.undoLastAction();
        assertEquals("ADMIT_URGENT", undone.getActionType());

        // Only request 1 should remain in urgent queue
        DispatchAssignment a = centre.serveNextRequest();
        assertEquals(1, a.getRequest().getId());
    }

    @Test
    void testUndoAdmitNormalRemovesFromQueue() {
        centre.admitRequest(normal(1));
        centre.admitRequest(normal(2));

        ActionRecord undone = centre.undoLastAction();
        assertEquals("ADMIT_NORMAL", undone.getActionType());
    }

    @Test
    void testUndoEmptyStackReturnsNull() {
        assertNull(centre.undoLastAction());
    }

    @Test
    void testUndoPreemptRemovesFromDeque() {
        centre.admitRequest(preempted(1));
        ActionRecord undone = centre.undoLastAction();
        assertEquals("ADMIT_PREEMPT", undone.getActionType());
        assertNull(centre.serveNextRequest());
    }

    // ---- Counter statistics --------------------------------------------- //

    @Test
    void testServedCountTracking() {
        centre.admitRequest(urgent(1));
        centre.admitRequest(normal(2));
        centre.admitRequest(critical(3));

        centre.serveNextRequest();
        centre.serveNextRequest();
        centre.serveNextRequest();

        assertEquals(3, centre.getServedCount());
        assertEquals(2, centre.getUrgentServed());  // critical + urgent
        assertEquals(1, centre.getNormalServed());
    }

    @Test
    void testUndoDecrementsServedCount() {
        centre.admitRequest(urgent(1));
        centre.serveNextRequest();
        assertEquals(1, centre.getServedCount());

        centre.undoLastAction();
        assertEquals(0, centre.getServedCount());
        assertEquals(0, centre.getUrgentServed());
    }

    // ---- Full lifecycle test -------------------------------------------- //

    @Test
    void testFullLifecycle() {
        centre.admitRequest(critical(1));
        centre.admitRequest(urgent(2));
        centre.admitRequest(normal(3));
        centre.admitRequest(preempted(4));

        // Serve all four
        DispatchAssignment a1 = centre.serveNextRequest(); // critical → ICU
        assertEquals(1, a1.getRequest().getId());
        assertEquals(500.0, a1.getResource().getCostPerUse());

        DispatchAssignment a2 = centre.serveNextRequest(); // urgent → ambulance
        assertEquals(2, a2.getRequest().getId());

        DispatchAssignment a3 = centre.serveNextRequest(); // preempted → blood
        assertEquals(4, a3.getRequest().getId());

        DispatchAssignment a4 = centre.serveNextRequest(); // normal → ambulance
        assertEquals(3, a4.getRequest().getId());

        assertNull(centre.serveNextRequest());
        assertEquals(4, centre.getServedCount());

        // Undo two
        centre.undoLastAction();
        centre.undoLastAction();
        assertEquals(2, centre.getServedCount());

        centre.printReport();
    }

    // ---- Helpers -------------------------------------------------------- //

    private String triageName(ServiceRequest r) {
        switch (r.getTriageLevel()) {
            case 1:  return "CRITICAL";
            case 2:  return "URGENT";
            default: return "NORMAL";
        }
    }
}
