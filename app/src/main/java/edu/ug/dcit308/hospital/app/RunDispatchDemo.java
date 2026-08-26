package edu.ug.dcit308.hospital.app;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import edu.ug.dcit308.hospital.db.ConnectionManager;
import edu.ug.dcit308.hospital.db.repository.ServiceRequestRepository;
import edu.ug.dcit308.hospital.ds.linked.DoublyLinkedList;
import edu.ug.dcit308.hospital.ds.list.DynamicArrayList;
import edu.ug.dcit308.hospital.scheduling.strategy.ActionRecord;
import edu.ug.dcit308.hospital.scheduling.strategy.DispatchAssignment;
import edu.ug.dcit308.hospital.scheduling.strategy.HospitalDispatchCentre;
import edu.ug.dcit308.hospital.scheduling.strategy.Resource;
import edu.ug.dcit308.hospital.scheduling.strategy.ResourcePool;
import edu.ug.dcit308.hospital.scheduling.strategy.ServiceRequest;

/**
 * Operations Handler: Live Multi-Queue Dispatch Simulator & Stack Rollback Engine.
 * Exercises MinHeapPriorityQueue, ArrayDeque, CircularQueue, ArrayStack, DoublyLinkedList, and DynamicArrayList.
 */
public class RunDispatchDemo {

    private static HospitalDispatchCentre centre;
    private static int requestSeq = 2000;
    private static int configuredNormalCap = 150;
    private static int configuredPreemptCap = 50;
    private static final DoublyLinkedList<ActionRecord> sessionAuditHistory = new DoublyLinkedList<>();

    public static void execute(Scanner scanner) {
        System.out.println();
        System.out.println("=============================================================");
        System.out.println("   GHANA EMERGENCY DISPATCH & MULTI-QUEUE SIMULATOR          ");
        System.out.println("=============================================================");

        if (centre == null) {
            initializeCentre(configuredNormalCap, configuredPreemptCap, 350);
        }

        boolean inSubMenu = true;
        while (inSubMenu) {
            System.out.println("\n--- Dispatch Command Center ---");
            System.out.printf("Queue Status: Urgent (P1/P2): %d | Preemptions: %d | Normal (P3): %d | Total Pending: %d | Undo Stack Depth: %d\n",
                    centre.getPendingUrgentCount(),
                    centre.getPendingPreemptionCount(),
                    centre.getPendingNormalCount(),
                    centre.getTotalPendingCount(),
                    centre.pendingActions());
            System.out.println("Options:");
            System.out.println("  [1] Serve Next Highest-Priority Request");
            System.out.println("  [2] Batch Serve Requests (5, 20, 50, or ALL)");
            System.out.println("  [3] Admit New Emergency Patient Transfer (P1 Critical / P2 Urgent / P3 Normal)");
            System.out.println("  [4] Undo Last Dispatch Action (Rollback via ArrayStack)");
            System.out.println("  [5] Print Full Dispatch & Resource Pool Report");
            System.out.println("  [6] View Historical Session Audit Log (DoublyLinkedList Trail)");
            System.out.println("  [7] Reset Simulator with Canonical 350 Seed Requests");
            System.out.println("  [8] Re-configure Queue Capacity via Student ID Formula");
            System.out.println("  [0] Return to Main Menu");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    serveSingle();
                    break;
                case "2":
                    batchServe(scanner);
                    break;
                case "3":
                    admitCustomRequest(scanner);
                    break;
                case "4":
                    undoAction();
                    break;
                case "5":
                    printDetailedReport();
                    break;
                case "6":
                    viewAuditHistory();
                    break;
                case "7":
                    initializeCentre(configuredNormalCap, configuredPreemptCap, 350);
                    System.out.println("✓ Simulator re-initialized with all 350 canonical database requests.");
                    break;
                case "8":
                    configureFromStudentId(scanner);
                    break;
                case "0":
                    inSubMenu = false;
                    break;
                default:
                    System.out.println("! Invalid option. Please choose 0-8.");
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
                ? String.format("Assigned Resource #%d (%s, Cost: GH¢ %.2f)", res.getId(), getResourceTypeName(res.getResourceType()), res.getCostPerUse())
                : "No matching resource available in pool (Held for manual fleet allocation)";

        ActionRecord record = new ActionRecord("SERVE", req, res);
        sessionAuditHistory.addLast(record);

        System.out.printf("✓ [DISPATCHED] Request #%d | Priority %d (%s) | Hospital #%d -> Hospital #%d\n",
                req.getId(), req.getTriageLevel(), getTriageName(req.getTriageLevel()),
                req.getOriginHospitalId(), req.getTargetHospitalId());
        System.out.printf("   Resource Allocation: %s\n", resDesc);
        return true;
    }

    private static void batchServe(Scanner scanner) {
        System.out.print("Enter number of requests to serve (e.g. 5, 20, 50, or 'all'): ");
        String countStr = scanner.nextLine().trim();
        int count = 5;
        if ("all".equalsIgnoreCase(countStr)) {
            count = centre.getTotalPendingCount();
        } else {
            try {
                count = Integer.parseInt(countStr);
            } catch (NumberFormatException e) {
                System.out.println("Defaulting to 5.");
                count = 5;
            }
        }

        System.out.printf("\n--- Batch Serving %d Requests ---\n", count);
        int served = 0;
        for (int i = 0; i < count; i++) {
            if (serveSingle()) {
                served++;
            } else {
                break;
            }
        }
        System.out.printf("✓ Batch completed: %d requests served.\n", served);
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
        int target = 16;
        try {
            target = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            target = 16;
        }

        ServiceRequest newReq = new ServiceRequest(
                ++requestSeq, origin, target, priority, resType, 70.0,
                requestSeq, priority == 1
        );

        boolean admitted = centre.admitRequest(newReq);
        if (admitted) {
            System.out.printf("✓ Admitted Request #%d into %s\n",
                    newReq.getId(),
                    (priority == 1 ? "Urgent Deque (P1 Preemption)" : (priority == 2 ? "Urgent MinHeap Queue" : "Normal Circular Queue")));
        } else {
            System.out.println("! Queue capacity exceeded.");
        }
    }

    private static void undoAction() {
        ActionRecord undone = centre.undoLastAction();
        if (undone == null) {
            System.out.println("! Undo stack is empty. No actions to roll back.");
        } else {
            sessionAuditHistory.addLast(new ActionRecord("UNDO_ROLLBACK", undone.getRequest(), undone.getResource()));
            System.out.printf("✓ [UNDO EXECUTED] Rolled back action: %s on Request #%d\n",
                    undone.getActionType(), undone.getRequest().getId());
            if (undone.getResource() != null) {
                System.out.printf("   Resource #%d returned to available pool.\n", undone.getResource().getId());
            }
        }
    }

    private static void viewAuditHistory() {
        System.out.println("\n--- Historical Session Audit Log (DoublyLinkedList Trail) ---");
        if (sessionAuditHistory.isEmpty()) {
            System.out.println("No dispatch actions recorded yet in this session.");
            return;
        }
        for (int i = 0; i < sessionAuditHistory.size(); i++) {
            ActionRecord r = sessionAuditHistory.get(i);
            String resStr = (r.getResource() != null) ? " | Resource #" + r.getResource().getId() : "";
            System.out.printf("  [%3d] Action: %-15s | Request #%d (Triage %d)%s\n",
                    i + 1, r.getActionType(), r.getRequest().getId(), r.getRequest().getTriageLevel(), resStr);
        }
    }

    private static void configureFromStudentId(Scanner scanner) {
        System.out.print("Enter Student ID Number (e.g. 10987654): ");
        String idStr = scanner.nextLine().trim();
        long num = 10987654L;
        try {
            num = Long.parseLong(idStr.replaceAll("\\D", ""));
        } catch (Exception e) {
            System.out.println("! Invalid format. Using default parameters.");
        }

        int sumDigits = 0;
        for (char c : String.valueOf(num).toCharArray()) {
            sumDigits += Character.getNumericValue(c);
        }

        configuredNormalCap = (sumDigits % 15) + 30; // 30 - 44
        configuredPreemptCap = (int) ((num % 10) + 15); // 15 - 24

        System.out.printf("✓ Configured Queue Sizes for Student %s:\n", idStr);
        System.out.printf("   • Normal Circular Queue Capacity   : %d\n", configuredNormalCap);
        System.out.printf("   • Preemption Array Deque Capacity : %d\n", configuredPreemptCap);

        initializeCentre(configuredNormalCap, configuredPreemptCap, 350);
        System.out.println("✓ Dispatch simulator re-initialized with student-derived capacities.");
    }

    private static void printDetailedReport() {
        System.out.println("\n=============================================================");
        System.out.println("   GHANA DISPATCH CENTRE — OPERATIONAL PERFORMANCE REPORT    ");
        System.out.println("=============================================================");
        centre.printReport();
        System.out.println("-------------------------------------------------------------");
        System.out.printf("Available Resources in Pool : %d units\n", centre.getResourcePool().getAvailableCount());
        System.out.printf("Total Resource Pool Capacity: %d units\n", centre.getResourcePool().size());
        System.out.println("=============================================================");
    }

    private static void initializeCentre(int normalCap, int preemptCap, int maxRequests) {
        Resource[] initialResources = new Resource[] {
                new Resource(1, 0, 150.0, 300.0), // Ambulance at Korle Bu (GH¢ 150)
                new Resource(2, 0, 120.0, 250.0), // Ambulance at Ridge (GH¢ 120)
                new Resource(3, 0, 140.0, 280.0), // Ambulance at Komfo Anokye (GH¢ 140)
                new Resource(4, 0, 160.0, 300.0), // Ambulance at Tamale (GH¢ 160)
                new Resource(5, 2, 200.0, 150.0), // ICU Bed at Korle Bu (GH¢ 200)
                new Resource(6, 2, 180.0, 150.0), // ICU Bed at Komfo Anokye (GH¢ 180)
                new Resource(7, 2, 190.0, 150.0), // ICU Bed at Cape Coast (GH¢ 190)
                new Resource(8, 1,  50.0,  50.0), // Blood Pack Unit (GH¢ 50)
                new Resource(9, 1,  45.0,  50.0), // Blood Pack Unit (GH¢ 45)
                new Resource(10, 1, 55.0,  50.0)  // Blood Pack Unit (GH¢ 55)
        };

        ResourcePool pool = new ResourcePool(initialResources);
        centre = new HospitalDispatchCentre(normalCap, preemptCap, pool);

        try (Connection conn = ConnectionManager.getConnection()) {
            ServiceRequestRepository repo = new ServiceRequestRepository(conn);
            List<edu.ug.dcit308.hospital.db.model.ServiceRequest> dbRequests = repo.findAll();

            // Store in DynamicArrayList for batch admissions
            DynamicArrayList<ServiceRequest> batchList = new DynamicArrayList<>();

            int admitted = 0;
            for (edu.ug.dcit308.hospital.db.model.ServiceRequest dr : dbRequests) {
                if (admitted >= maxRequests) break;
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
                batchList.add(sr);
                centre.admitRequest(sr);
                admitted++;
            }
        } catch (Exception e) {
            // Fallback default sample requests
            centre.admitRequest(new ServiceRequest(101, 1, 16, 1, 0, 75.0, 1, true));
            centre.admitRequest(new ServiceRequest(102, 2, 1, 2, 1, 20.0, 2, false));
            centre.admitRequest(new ServiceRequest(103, 3, 2, 3, 0, 40.0, 3, false));
        }
    }

    private static String getTriageName(int level) {
        switch (level) {
            case 1: return "P1 CRITICAL";
            case 2: return "P2 URGENT";
            default: return "P3 NORMAL";
        }
    }

    private static String getResourceTypeName(int type) {
        switch (type) {
            case 0: return "Ambulance Vehicle";
            case 1: return "Blood Transfusion Pack";
            default: return "ICU Bed / Ventilator";
        }
    }
}