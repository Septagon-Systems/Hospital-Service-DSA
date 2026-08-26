package edu.ug.dcit308.hospital.app;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import edu.ug.dcit308.hospital.db.ConnectionManager;
import edu.ug.dcit308.hospital.db.repository.ServiceRequestRepository;
import edu.ug.dcit308.hospital.scheduling.strategy.ActionRecord;
import edu.ug.dcit308.hospital.scheduling.strategy.DispatchAssignment;
import edu.ug.dcit308.hospital.scheduling.strategy.HospitalDispatchCentre;
import edu.ug.dcit308.hospital.scheduling.strategy.Resource;
import edu.ug.dcit308.hospital.scheduling.strategy.ResourcePool;
import edu.ug.dcit308.hospital.scheduling.strategy.ServiceRequest;

/**
 * Operations Handler: Live Multi-Queue Dispatch Simulator & Stack Rollback Engine.
 */
public class RunDispatchDemo {

    private static HospitalDispatchCentre centre;
    private static int requestSeq = 1000;

    public static void execute(Scanner scanner) {
        System.out.println();
        System.out.println("=============================================================");
        System.out.println("   GHANA EMERGENCY DISPATCH & MULTI-QUEUE SIMULATOR          ");
        System.out.println("=============================================================");

        if (centre == null) {
            initializeCentre();
        }

        boolean inSubMenu = true;
        while (inSubMenu) {
            System.out.println("\n--- Dispatch Command Center ---");
            System.out.printf("Queue Status: Urgent (P1/P2): %d | Preemptions: %d | Normal (P3): %d | Undo Stack Depth: %d\n",
                    centre.getPendingUrgentCount(),
                    centre.getPendingPreemptionCount(),
                    centre.getPendingNormalCount(),
                    centre.pendingActions());
            System.out.println("Options:");
            System.out.println("  [1] Serve Next Highest-Priority Request");
            System.out.println("  [2] Batch Serve 5 Requests");
            System.out.println("  [3] Admit New Emergency Patient Transfer (P1 Critical / P2 Urgent / P3 Normal)");
            System.out.println("  [4] Undo Last Dispatch Action (Rollback via ArrayStack)");
            System.out.println("  [5] Print Full Dispatch & Resource Pool Report");
            System.out.println("  [6] Reset Simulator with Seed Requests");
            System.out.println("  [0] Return to Main Menu");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    serveSingle();
                    break;
                case "2":
                    for (int i = 0; i < 5; i++) {
                        if (!serveSingle()) break;
                    }
                    break;
                case "3":
                    admitCustomRequest(scanner);
                    break;
                case "4":
                    undoAction();
                    break;
                case "5":
                    centre.printReport();
                    break;
                case "6":
                    initializeCentre();
                    System.out.println("✓ Simulator re-initialized with canonical seed requests.");
                    break;
                case "0":
                    inSubMenu = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
        System.out.println("=============================================================");
    }

    private static boolean serveSingle() {
        DispatchAssignment assignment = centre.serveNextRequest();
        if (assignment == null) {
            System.out.println("! No pending requests left to serve.");
            return false;
        }

        ServiceRequest req = assignment.getRequest();
        Resource res = assignment.getResource();

        String resDesc = (res != null)
                ? String.format("Assigned Resource #%d (Type %d, Cost: $%.2f)", res.getId(), res.getResourceType(), res.getCostPerUse())
                : "No matching resource available in pool (Held for manual allocation)";

        System.out.printf("✓ [DISPATCHED] Request #%d | Priority %d | Origin: Hospital #%d -> Target: Hospital #%d\n",
                req.getId(), req.getTriageLevel(), req.getOriginHospitalId(), req.getTargetHospitalId());
        System.out.printf("   Resource Allocation: %s\n", resDesc);
        return true;
    }

    private static void admitCustomRequest(Scanner scanner) {
        System.out.print("Enter Triage Priority (1 = P1 Critical, 2 = P2 Urgent, 3 = P3 Normal): ");
        int priority = 2;
        try {
            priority = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Defaulting to Priority 2.");
        }

        System.out.print("Enter Resource Needed (0 = Ambulance, 1 = Blood Pack, 2 = ICU Bed): ");
        int resType = 0;
        try {
            resType = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Defaulting to 0 (Ambulance).");
        }

        System.out.print("Enter Origin Hospital ID (1 - 50): ");
        int origin = 1;
        try {
            origin = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            origin = 1;
        }

        System.out.print("Enter Destination Hospital ID (1 - 50): ");
        int target = 6;
        try {
            target = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            target = 6;
        }

        ServiceRequest newReq = new ServiceRequest(
                ++requestSeq, origin, target, priority, resType, 70.0,
                requestSeq, priority == 1
        );

        boolean admitted = centre.admitRequest(newReq);
        if (admitted) {
            System.out.printf("✓ Admitted Request #%d into %s\n",
                    newReq.getId(),
                    (priority == 1 ? "Urgent Queue (P1 Preemption)" : (priority == 2 ? "Urgent MinHeap Queue" : "Normal Circular Queue")));
        } else {
            System.out.println("! Queue capacity exceeded.");
        }
    }

    private static void undoAction() {
        ActionRecord undone = centre.undoLastAction();
        if (undone == null) {
            System.out.println("! Undo stack is empty. No actions to roll back.");
        } else {
            System.out.printf("✓ [UNDO EXECUTED] Rolled back action: %s on Request #%d\n",
                    undone.getActionType(), undone.getRequest().getId());
            if (undone.getResource() != null) {
                System.out.printf("   Resource #%d returned to available pool.\n", undone.getResource().getId());
            }
        }
    }

    private static void initializeCentre() {
        Resource[] initialResources = new Resource[] {
                new Resource(1, 0, 150.0, 300.0), // Ambulance at Korle Bu
                new Resource(2, 0, 120.0, 250.0), // Ambulance at Ridge
                new Resource(3, 0, 140.0, 280.0), // Ambulance at Komfo Anokye
                new Resource(4, 2, 200.0, 150.0), // ICU Bed at Korle Bu
                new Resource(5, 2, 180.0, 150.0), // ICU Bed at Komfo Anokye
                new Resource(6, 1,  50.0,  50.0), // Blood Pack
                new Resource(7, 1,  45.0,  50.0)  // Blood Pack
        };

        ResourcePool pool = new ResourcePool(initialResources);
        centre = new HospitalDispatchCentre(50, 20, pool);

        try (Connection conn = ConnectionManager.getConnection()) {
            ServiceRequestRepository repo = new ServiceRequestRepository(conn);
            List<edu.ug.dcit308.hospital.db.model.ServiceRequest> dbRequests = repo.findAll();

            int admitted = 0;
            for (edu.ug.dcit308.hospital.db.model.ServiceRequest dr : dbRequests) {
                if (admitted >= 25) break; // Load first 25 for simulation
                int resType = 0;
                String cat = dr.getRequestType().toLowerCase();
                if (cat.contains("blood")) resType = 1;
                else if (cat.contains("icu") || cat.contains("ventilator")) resType = 2;

                ServiceRequest sr = new ServiceRequest(
                        dr.getRequestId(),
                        dr.getOriginHospitalId(),
                        dr.getTargetHospitalId(),
                        dr.getPatientTriageLevel(),
                        resType,
                        dr.getWeightKg(),
                        dr.getRequestId(),
                        dr.getPatientTriageLevel() == 1
                );
                centre.admitRequest(sr);
                admitted++;
            }
        } catch (Exception e) {
            // Fallback default sample requests
            centre.admitRequest(new ServiceRequest(101, 1, 6, 1, 0, 75.0, 1, true));
            centre.admitRequest(new ServiceRequest(102, 2, 1, 2, 1, 20.0, 2, false));
            centre.admitRequest(new ServiceRequest(103, 3, 2, 3, 0, 40.0, 3, false));
        }
    }
}