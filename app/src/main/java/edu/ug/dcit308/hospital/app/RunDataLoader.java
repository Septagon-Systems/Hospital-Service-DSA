package edu.ug.dcit308.hospital.app;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import edu.ug.dcit308.hospital.db.ConnectionManager;
import edu.ug.dcit308.hospital.db.importer.CsvImporter;
import edu.ug.dcit308.hospital.db.model.Hospital;
import edu.ug.dcit308.hospital.db.repository.HospitalRepository;
import edu.ug.dcit308.hospital.db.repository.MedicalResourceRepository;
import edu.ug.dcit308.hospital.db.repository.RouteRepository;
import edu.ug.dcit308.hospital.db.repository.ServiceRequestRepository;
import edu.ug.dcit308.hospital.db.schema.SchemaInitializer;
import edu.ug.dcit308.hospital.ds.linked.DoublyLinkedList;
import edu.ug.dcit308.hospital.ds.map.CustomMap;
import edu.ug.dcit308.hospital.ds.set.CustomSet;

/**
 * Operations Handler: Database Status, Regional Explorer & Multi-Collection Aggregations.
 * Exercises CustomSet, CustomMap, and DoublyLinkedList.
 */
public class RunDataLoader {

    public static void execute(Scanner scanner) {
        System.out.println();
        System.out.println("=============================================================");
        System.out.println("     GHANA HEALTH SERVICE — DATABASE STATUS & OVERVIEW       ");
        System.out.println("=============================================================");

        try (Connection conn = ConnectionManager.getConnection()) {
            SchemaInitializer.initialize(conn);
            CsvImporter.importAll(conn, "data/seed");

            HospitalRepository hospitalRepo = new HospitalRepository(conn);
            RouteRepository routeRepo = new RouteRepository(conn);
            ServiceRequestRepository requestRepo = new ServiceRequestRepository(conn);
            MedicalResourceRepository resourceRepo = new MedicalResourceRepository(conn);

            List<Hospital> allHospitals = hospitalRepo.findAll();

            // Populate CustomSet, CustomMap, and DoublyLinkedList
            CustomSet<String> uniqueRegions = new CustomSet<>();
            CustomSet<String> uniqueOwnershipTypes = new CustomSet<>();
            CustomMap<String, Integer> regionalCounts = new CustomMap<>();
            DoublyLinkedList<Hospital> tertiaryHospitals = new DoublyLinkedList<>();

            for (Hospital h : allHospitals) {
                uniqueRegions.add(h.getRegion());
                uniqueOwnershipTypes.add(h.getOwnershipType());

                Integer currentCount = regionalCounts.get(h.getRegion());
                regionalCounts.put(h.getRegion(), (currentCount == null ? 1 : currentCount + 1));

                if (h.getIcuCapacity() >= 15) {
                    tertiaryHospitals.addLast(h);
                }
            }

            System.out.println("Current Database Status (data/hospital_system.db):");
            System.out.printf("  • Healthcare Facilities (Hospitals)     : %d records\n", hospitalRepo.count());
            System.out.printf("  • Inter-Facility Corridors (Road Routes): %d records\n", routeRepo.count());
            System.out.printf("  • Active Service & Triage Requests      : %d records\n", requestRepo.count());
            System.out.printf("  • Critical Medical Resource Units       : %d records\n", resourceRepo.count());
            System.out.printf("  • Unique Ghanaian Regions (CustomSet)   : %d regions\n", uniqueRegions.size());
            System.out.printf("  • Major Tertiary Referral Hubs (DoublyLL): %d facilities\n", tertiaryHospitals.size());

            System.out.println("\nOptions:");
            System.out.println("  [1] List all 50 Ghanaian Hospitals");
            System.out.println("  [2] Filter Hospitals by Region");
            System.out.println("  [3] View Top Tertiary Referral Hubs (DoublyLinkedList Audit)");
            System.out.println("  [0] Return to Main Menu");
            System.out.print("Enter choice: ");

            String input = scanner.nextLine().trim();
            if ("1".equals(input)) {
                System.out.println("\n--- All 50 Ghanaian Facilities ---");
                for (Hospital h : allHospitals) {
                    System.out.printf("  [%2d] %-35s | Region: %-14s | Town: %-15s | ICU: %2d | Amb: %2d\n",
                            h.getHospitalId(), h.getName(), h.getRegion(), h.getTown(), h.getIcuCapacity(), h.getAmbulanceCount());
                }
            } else if ("2".equals(input)) {
                System.out.print("Enter region name (e.g. Greater Accra, Ashanti, Northern, Volta, Western): ");
                String region = scanner.nextLine().trim();
                List<Hospital> list = hospitalRepo.findByRegion(region);
                if (list.isEmpty()) {
                    System.out.println("! No facilities found matching region: " + region);
                } else {
                    System.out.printf("\n--- %s Facilities (%d found) ---\n", region, list.size());
                    for (Hospital h : list) {
                        System.out.printf("  [%2d] %-35s | District: %-18s | ICU: %2d | Amb: %2d\n",
                                h.getHospitalId(), h.getName(), h.getDistrict(), h.getIcuCapacity(), h.getAmbulanceCount());
                    }
                }
            } else if ("3".equals(input)) {
                System.out.println("\n--- Major Tertiary Referral Hubs (ICU Capacity >= 15) ---");
                for (int i = 0; i < tertiaryHospitals.size(); i++) {
                    Hospital h = tertiaryHospitals.get(i);
                    System.out.printf("  [%2d] %-35s | Region: %-14s | ICU: %2d beds | Amb: %2d units\n",
                            h.getHospitalId(), h.getName(), h.getRegion(), h.getIcuCapacity(), h.getAmbulanceCount());
                }
            }
        } catch (Exception e) {
            System.err.println("! Error accessing database: " + e.getMessage());
        }
        System.out.println("=============================================================");
    }
}