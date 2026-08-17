package edu.ug.dcit308.hospital.app;

import java.util.PriorityQueue;

public class RunDispatchDemo {

    public static class EmergencyCall implements Comparable<EmergencyCall> {
        private final String patientName;
        private final String condition;
        private final int priorityScore; // Lower number = higher emergency priority

        public EmergencyCall(String patientName, String condition, int priorityScore) {
            this.patientName = patientName;
            this.condition = condition;
            this.priorityScore = priorityScore;
        }

        @Override
        public int compareTo(EmergencyCall other) {
            return Integer.compare(this.priorityScore, other.priorityScore);
        }

        @Override
        public String toString() {
            return String.format("[Priority %d] Patient: %-15s | Condition: %s", priorityScore, patientName, condition);
        }
    }

    public static void execute() {
        System.out.println();
        System.out.println("========== EMERGENCY DISPATCH QUEUE SYSTEM ==========");

        PriorityQueue<EmergencyCall> dispatchQueue = new PriorityQueue<>();

        // Adding incoming incoming triage calls out of order
        dispatchQueue.add(new EmergencyCall("Kwame Mensah", "Mild Fever", 3));
        dispatchQueue.add(new EmergencyCall("Ama Serwaa", "Cardiac Arrest", 1));
        dispatchQueue.add(new EmergencyCall("Kofi Annan", "Fractured Leg", 2));
        dispatchQueue.add(new EmergencyCall("Abena Osei", "Severe Trauma", 1));

        System.out.println("Processing emergency dispatch queue by priority level:\n");

        while (!dispatchQueue.isEmpty()) {
            EmergencyCall call = dispatchQueue.poll();
            System.out.println("  [DISPATCHING AMBULANCE] -> " + call);
        }

        System.out.println("=====================================================");
    }
}