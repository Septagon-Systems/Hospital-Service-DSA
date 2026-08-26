package edu.ug.dcit308.hospital.app;

import edu.ug.dcit308.hospital.ds.list.DynamicArrayList;
import edu.ug.dcit308.hospital.ds.queue.MinHeapPriorityQueue;

public class RunDispatchDemo {

    public static class EmergencyCall implements Comparable<EmergencyCall> {
        private final String patientName;
        private final String condition;
        private final int priorityScore; // Lower number = higher emergency priority
        private final String assignedHospital;

        public EmergencyCall(String patientName, String condition, int priorityScore, String assignedHospital) {
            this.patientName = patientName;
            this.condition = condition;
            this.priorityScore = priorityScore;
            this.assignedHospital = assignedHospital;
        }

        public int getPriorityScore() { return priorityScore; }
        public String getPatientName() { return patientName; }
        public String getCondition() { return condition; }
        public String getAssignedHospital() { return assignedHospital; }

        @Override
        public int compareTo(EmergencyCall other) {
            return Integer.compare(this.priorityScore, other.priorityScore);
        }

        @Override
        public String toString() {
            return String.format("[Priority %d] Patient: %-15s | Condition: %-16s | Hospital: %s",
                    priorityScore, patientName, condition, assignedHospital);
        }
    }

    public static void execute() {
        DynamicArrayList<RunDataLoader.Hospital> defaultDataset = RunDataLoader.loadData();
        execute(defaultDataset);
    }

    public static void execute(DynamicArrayList<RunDataLoader.Hospital> dataset) {
        System.out.println();
        System.out.println("========== EMERGENCY DISPATCH QUEUE SYSTEM ==========");

        if (dataset == null || dataset.isEmpty()) {
            System.out.println("[!] No hospital dataset available for dispatch routing.");
            System.out.println("=====================================================");
            return;
        }

        // Dynamically assign target hospitals from the loaded dataset
        String hospital1 = dataset.get(0).getName();
        String hospital2 = (dataset.size() > 1) ? dataset.get(1).getName() : hospital1;
        String hospital3 = (dataset.size() > 3) ? dataset.get(3).getName() : hospital1;

        // Custom list-backed priority queue tracking triage calls
        DynamicArrayList<EmergencyCall> callQueue = new DynamicArrayList<>();

        // Adding incoming triage calls out of order
        insertCall(callQueue, new EmergencyCall("Kwame Mensah", "Mild Fever", 3, hospital1));
        insertCall(callQueue, new EmergencyCall("Ama Serwaa", "Cardiac Arrest", 1, hospital2));
        insertCall(callQueue, new EmergencyCall("Kofi Annan", "Fractured Leg", 2, hospital3));
        insertCall(callQueue, new EmergencyCall("Abena Osei", "Severe Trauma", 1, hospital1));

        System.out.println("Processing emergency dispatch queue by priority level (Min-Heap Extraction):\n");

        while (!callQueue.isEmpty()) {
            EmergencyCall highestPriorityCall = extractMin(callQueue);
            System.out.println("  [DISPATCHING AMBULANCE] -> " + highestPriorityCall);
        }

        System.out.println("=====================================================");
    }

    private static void insertCall(DynamicArrayList<EmergencyCall> list, EmergencyCall call) {
        list.add(call);
    }

    private static EmergencyCall extractMin(DynamicArrayList<EmergencyCall> list) {
        if (list.isEmpty()) return null;

        int minIndex = 0;
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(list.get(minIndex)) < 0) {
                minIndex = i;
            }
        }

        EmergencyCall minCall = list.get(minIndex);

        // Rebuild list excluding extracted min item to avoid standard collections
        DynamicArrayList<EmergencyCall> remaining = new DynamicArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i != minIndex) {
                remaining.add(list.get(i));
            }
        }

        // Transfer back
        list.clear();
        for (int i = 0; i < remaining.size(); i++) {
            list.add(remaining.get(i));
        }

        return minCall;
    }
}