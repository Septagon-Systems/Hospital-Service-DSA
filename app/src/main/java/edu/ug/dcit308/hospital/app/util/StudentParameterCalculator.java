package edu.ug.dcit308.hospital.app.util;

/**
 * Utility class to derive student-specific algorithm parameters from student index numbers
 */
public class StudentParameterCalculator {

    public static class StudentParameters {
        public final String indexNumber;
        public final int priorityWeight;
        public final int hashTableSize;
        public final long randomSeed;
        public final double routePenalty;
        public final int urgentCapacity;
        public final int normalCapacity;
        public final int preemptionCapacity;

        public StudentParameters(String indexNumber, int priorityWeight, int hashTableSize, long randomSeed, double routePenalty, int urgentCapacity, int normalCapacity, int preemptionCapacity) {
            this.indexNumber = indexNumber;
            this.priorityWeight = priorityWeight;
            this.hashTableSize = hashTableSize;
            this.randomSeed = randomSeed;
            this.routePenalty = routePenalty;
            this.urgentCapacity = urgentCapacity;
            this.normalCapacity = normalCapacity;
            this.preemptionCapacity = preemptionCapacity;
        }

        @Override
        public String toString() {
            return String.format("""
                === Algorithm Parameters for Student Index %s ===
                - Priority Weight (Urgency): %d
                - Hash Table Prime Size: %d
                - Random Seed: %d
                - Route Penalty Factor: %.2f
                - Urgent Heap Capacity Limit: %d
                - Normal CircularQueue Capacity: %d
                - Preemption Deque Shift Limit: %d
                """, indexNumber, priorityWeight, hashTableSize, randomSeed, routePenalty, urgentCapacity, normalCapacity, preemptionCapacity);
        }
    }

    public static StudentParameters calculate(String indexNumberStr) {
        long indexNum = Long.parseLong(indexNumberStr.replaceAll("\\D", ""));
        
        // Priority weight = (sum of digits % 5) + 1
        int sumDigits = 0;
        for (char c : String.valueOf(indexNum).toCharArray()) {
            sumDigits += Character.getNumericValue(c);
        }
        int priorityWeight = (sumDigits % 5) + 1;

        // Hash table size = next prime after (last 3 digits)
        int last3 = (int) (indexNum % 1000);
        int hashTableSize = nextPrime(last3);

        // Random seed = index number
        long randomSeed = indexNum;

        // Route penalty = (indexNum % 20) / 10.0
        double routePenalty = (indexNum % 20) / 10.0;

        // Queue capacities derived from student index:
        int urgentCapacity = (sumDigits % 4) + 4; // 4 to 7
        int normalCapacity = (sumDigits % 5) + 5; // 5 to 9
        int preemptionCapacity = (priorityWeight % 3) + 1; // 1 to 3

        return new StudentParameters(indexNumberStr, priorityWeight, hashTableSize, randomSeed, routePenalty, urgentCapacity, normalCapacity, preemptionCapacity);
    }

    private static int nextPrime(int n) {
        if (n <= 1) return 2;
        int candidate = n + 1;
        while (!isPrime(candidate)) {
            candidate++;
        }
        return candidate;
    }

    private static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }
}
