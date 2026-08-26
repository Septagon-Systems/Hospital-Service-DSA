package edu.ug.dcit308.hospital.gui;

import java.awt.Desktop;
import java.net.URI;
import java.sql.Connection;

import edu.ug.dcit308.hospital.db.ConnectionManager;
import edu.ug.dcit308.hospital.db.importer.CsvImporter;
import edu.ug.dcit308.hospital.db.schema.SchemaInitializer;
import edu.ug.dcit308.hospital.gui.web.WebServer;

/**
 * Main Entry point for the Ghana Smart Hospital Operations Optimizer (DCIT 204 / DCIT 308) GUI.
 */
public class GuiMain {

    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("   GHANA SMART HOSPITAL OPERATIONS OPTIMIZER (DCIT 204 / 308)  ");
        System.out.println("   Dedicated Graphical User Interface & GIS Web Dashboard     ");
        System.out.println("=============================================================");

        // 1. Bootstrap SQLite Schema & Ingest Seed Data
        try (Connection conn = ConnectionManager.getConnection()) {
            SchemaInitializer.initialize(conn);
            CsvImporter.importAll(conn, "data/seed");
            System.out.println("✓ SQLite Database initialized & seeded at: data/hospital_system.db");
        } catch (Exception e) {
            System.out.println("! Note: Initialized in lightweight mode (" + e.getMessage() + ")");
        }

        // 2. Launch Web GUI Server
        try {
            WebServer webServer = new WebServer(DEFAULT_PORT);
            webServer.start();

            String url = "http://localhost:" + DEFAULT_PORT;
            System.out.println("\n🚀 Launching Interactive GUI Dashboard at: " + url);
            System.out.println("   (Press Ctrl+C in terminal to stop server)\n");

            // Attempt to open default browser if Desktop is supported
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception ignored) {
                    // Headless or non-desktop environment
                }
            }
        } catch (Exception e) {
            System.err.println("! Error starting GUI Server: " + e.getMessage());
        }
    }
}
