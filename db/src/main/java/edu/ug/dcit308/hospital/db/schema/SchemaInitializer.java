package edu.ug.dcit308.hospital.db.schema;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SQLite Database Schema Initializer.
 * Creates all tables, foreign key constraints, and lookup indexes.
 */
public final class SchemaInitializer {

    private static final String[] DDL_STATEMENTS = {
            // Enable Foreign Keys in SQLite
            "PRAGMA foreign_keys = ON;",

            // 1. Hospitals Table
            "CREATE TABLE IF NOT EXISTS hospitals (" +
                    "id INTEGER PRIMARY KEY, " +
                    "name TEXT NOT NULL, " +
                    "facility_type TEXT, " +
                    "region TEXT NOT NULL, " +
                    "district TEXT, " +
                    "town TEXT, " +
                    "latitude REAL NOT NULL, " +
                    "longitude REAL NOT NULL, " +
                    "ownership_type TEXT, " +
                    "icu_capacity INTEGER DEFAULT 0, " +
                    "ambulance_count INTEGER DEFAULT 0" +
                    ");",

            // 2. Routes Table
            "CREATE TABLE IF NOT EXISTS routes (" +
                    "id INTEGER PRIMARY KEY, " +
                    "from_hospital INTEGER NOT NULL, " +
                    "to_hospital INTEGER NOT NULL, " +
                    "distance_km REAL NOT NULL, " +
                    "avg_travel_time_mins REAL NOT NULL, " +
                    "road_condition TEXT, " +
                    "source TEXT, " +
                    "FOREIGN KEY (from_hospital) REFERENCES hospitals (id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (to_hospital) REFERENCES hospitals (id) ON DELETE CASCADE" +
                    ");",

            // 3. Service Requests Table
            "CREATE TABLE IF NOT EXISTS service_requests (" +
                    "request_id INTEGER PRIMARY KEY, " +
                    "origin_hospital_id INTEGER NOT NULL, " +
                    "target_hospital_id INTEGER NOT NULL, " +
                    "patient_triage_level INTEGER NOT NULL, " +
                    "request_type TEXT, " +
                    "weight_kg REAL, " +
                    "timestamp_created TEXT, " +
                    "status TEXT DEFAULT 'PENDING', " +
                    "FOREIGN KEY (origin_hospital_id) REFERENCES hospitals (id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (target_hospital_id) REFERENCES hospitals (id) ON DELETE CASCADE" +
                    ");",

            // 4. Medical Resources Table
            "CREATE TABLE IF NOT EXISTS medical_resources (" +
                    "resource_id INTEGER PRIMARY KEY, " +
                    "hospital_id INTEGER NOT NULL, " +
                    "resource_name TEXT NOT NULL, " +
                    "quantity_available INTEGER DEFAULT 0, " +
                    "resource_category TEXT, " +
                    "FOREIGN KEY (hospital_id) REFERENCES hospitals (id) ON DELETE CASCADE" +
                    ");",

            // 5. Algorithm Runs (Benchmarking) Table
            "CREATE TABLE IF NOT EXISTS algorithm_runs (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "run_id TEXT NOT NULL, " +
                    "algorithm_name TEXT NOT NULL, " +
                    "input_size INTEGER NOT NULL, " +
                    "execution_time_ns INTEGER NOT NULL, " +
                    "memory_used_kb REAL NOT NULL, " +
                    "iteration_number INTEGER NOT NULL, " +
                    "run_timestamp TEXT NOT NULL" +
                    ");",

            // 6. Audit Events (Undo Stack / Audit Log) Table
            "CREATE TABLE IF NOT EXISTS audit_events (" +
                    "event_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "action_type TEXT NOT NULL, " +
                    "entity_type TEXT NOT NULL, " +
                    "entity_id INTEGER NOT NULL, " +
                    "details TEXT, " +
                    "timestamp TEXT NOT NULL" +
                    ");",

            // Performance Indexes
            "CREATE INDEX IF NOT EXISTS idx_hospitals_region ON hospitals(region);",
            "CREATE INDEX IF NOT EXISTS idx_routes_from ON routes(from_hospital);",
            "CREATE INDEX IF NOT EXISTS idx_routes_to ON routes(to_hospital);",
            "CREATE INDEX IF NOT EXISTS idx_requests_status ON service_requests(status);",
            "CREATE INDEX IF NOT EXISTS idx_requests_triage ON service_requests(patient_triage_level);",
            "CREATE INDEX IF NOT EXISTS idx_resources_hospital ON medical_resources(hospital_id);",
            "CREATE INDEX IF NOT EXISTS idx_algo_runs_name ON algorithm_runs(algorithm_name);"
    };

    private SchemaInitializer() {}

    /**
     * Initializes the database schema on the given connection.
     *
     * @param conn SQLite JDBC connection
     * @throws SQLException if schema creation fails
     */
    public static void initialize(Connection conn) throws SQLException {
        if (conn == null || conn.isClosed()) {
            throw new IllegalArgumentException("Connection must not be null or closed");
        }

        try (Statement stmt = conn.createStatement()) {
            for (String sql : DDL_STATEMENTS) {
                stmt.execute(sql);
            }
        }
    }
}
