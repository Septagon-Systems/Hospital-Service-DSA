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

/**
 * Operations Handler: Database Status & Facility Regional Explorer.
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

            System.out.println("Current Database Status (data/hospital_system.db):");
            System.out.printf("  • Healthcare Facilities (Hospitals)     : %d records\n", hospitalRepo.count());
            System.out.printf("  • Inter-Facility Corridors (Road Routes): %d records\n", routeRepo.count());
            System.out.printf("  • Active Service & Triage Requests      : %d records\n", requestRepo.count());
            System.out.printf("  • Critical Medical Resource Units       : %d records\n", resourceRepo.count());

            System.out.println("\nOptions:");
            System.out.println("  [1] List all 50 Ghanaian Hospitals");
            System.out.println("  [2] Filter Hospitals by Region");
            System.out.println("  [0] Return to Main Menu");
            System.out.print("Enter choice: ");

            String input = scanner.nextLine().trim();
            if ("1".equals(input)) {
                List<Hospital> list = hospitalRepo.findAll();
                System.out.println("\n--- All 50 Ghanaian Facilities ---");
                for (Hospital h : list) {
                    System.out.printf("  [%2d] %-35s | Region: %-14s | Town: %-15s | ICU: %2d | Amb: %2d\n",
                            h.getHospitalId(), h.getName(), h.getRegion(), h.getTown(), h.getIcuCapacity(), h.getAmbulanceCount());
                }
            } else if ("2".equals(input)) {
                System.out.print("Enter region name (e.g. Greater Accra, Ashanti, Northern, Volta, Western): ");
                String region = scanner.nextLine().trim();
                List<Hospital> list = hospitalRepo.findByRegion(region);
                if (list.isEmpty()) {
                    System.out.println("No facilities found matching region: " + region);
                } else {
                    System.out.printf("\n--- %s Facilities (%d found) ---\n", region, list.size());
                    for (Hospital h : list) {
                        System.out.printf("  [%2d] %-35s | District: %-18s | ICU: %2d | Amb: %2d\n",
                                h.getHospitalId(), h.getName(), h.getDistrict(), h.getIcuCapacity(), h.getAmbulanceCount());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("! Error accessing database: " + e.getMessage());
        }
        System.out.println("=============================================================");
    }
}