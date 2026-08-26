package edu.ug.dcit308.hospital.app;

import java.sql.Connection;

import edu.ug.dcit308.hospital.app.util.StudentParameterCalculator;
import edu.ug.dcit308.hospital.db.ConnectionManager;
import edu.ug.dcit308.hospital.db.importer.CsvImporter;
import edu.ug.dcit308.hospital.db.schema.SchemaInitializer;

/**
 * Main Entry point for the Ghana Smart Hospital Operations Optimizer (DCIT 204/308).
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("   GHANA SMART HOSPITAL OPERATIONS OPTIMIZER (DCIT 204/308)  ");
        System.out.println("=============================================================");

        // 1. Bootstrap SQLite Schema & Ingest Seed Data
        try (Connection conn = ConnectionManager.getConnection()) {
            SchemaInitializer.initialize(conn);
            CsvImporter.importAll(conn, "data/seed");
            System.out.println("✓ SQLite Database initialized & seeded at: data/hospital_system.db");
        } catch (Exception e) {
            System.out.println("! Note: Initialized in lightweight mode (" + e.getMessage() + ")");
        }

        // 2. Process Arguments or Launch Menu
        if (args.length > 0) {
            System.out.println(StudentParameterCalculator.calculate(args[0]));
        } else {
            ConsoleMenu menu = new ConsoleMenu();
            menu.run();
        }
    }
}