package edu.ug.dcit308.hospital.app;

import edu.ug.dcit308.hospital.ds.list.DynamicArrayList;

public class RunDataLoader {

    public static class Hospital {
        private final int id;
        private final String name;
        private final String location;
        private final int capacity;

        public Hospital(int id, String name, String location, int capacity) {
            this.id = id;
            this.name = name;
            this.location = location;
            this.capacity = capacity;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getLocation() { return location; }
        public int getCapacity() { return capacity; }

        @Override
        public String toString() {
            return String.format("[%d] %-30s | Location: %-12s | Beds: %d", id, name, location, capacity);
        }
    }

    public static DynamicArrayList<Hospital> loadData() {
        DynamicArrayList<Hospital> hospitals = new DynamicArrayList<>();
        hospitals.add(new Hospital(101, "Korle-Bu Teaching Hospital", "Accra", 2000));
        hospitals.add(new Hospital(102, "Greater Accra Regional (Ridge)", "Accra", 420));
        hospitals.add(new Hospital(103, "Komfo Anokye Teaching Hospital", "Kumasi", 1200));
        hospitals.add(new Hospital(104, "37 Military Hospital", "Accra", 500));
        hospitals.add(new Hospital(105, "Tamale Teaching Hospital", "Tamale", 800));
        return hospitals;
    }

    public static DynamicArrayList<Hospital> execute() {
        System.out.println();
        System.out.println("========== GHANA HOSPITAL DATA LOADER ==========");
        System.out.println("Fetching records from database...");

        DynamicArrayList<Hospital> hospitals = loadData();

        System.out.println("Successfully loaded " + hospitals.size() + " hospital records:\n");
        for (int i = 0; i < hospitals.size(); i++) {
            System.out.println("  " + hospitals.get(i));
        }
        System.out.println("=================================================");
        return hospitals;
    }
}