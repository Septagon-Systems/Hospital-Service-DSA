package edu.ug.dcit308.hospital.db.importer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Batch CSV Importer for seeding SQLite database tables from canonical datasets.
 */
public final class CsvImporter {

    public static final String DEFAULT_SEED_DIR = "data/seed";

    private CsvImporter() {}

    /**
     * Imports all canonical seed CSV datasets from the default seed directory ({@code data/seed}).
     */
    public static void importAll(Connection conn) throws SQLException, IOException {
        importAll(conn, DEFAULT_SEED_DIR);
    }

    /**
     * Imports all canonical seed CSV datasets in foreign-key dependency order.
     */
    public static void importAll(Connection conn, String seedDir) throws SQLException, IOException {
        if (conn == null || conn.isClosed()) {
            throw new IllegalArgumentException("Connection must not be null or closed");
        }
        if (seedDir == null || seedDir.trim().isEmpty()) {
            throw new IllegalArgumentException("Seed directory must not be null or blank");
        }

        File dir = resolveDirectory(seedDir);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new IOException("Seed directory does not exist: " + seedDir);
        }

        // 1. Hospitals (Parent)
        File hospitalsFile = resolveFile(dir, "hospitals.csv");
        if (hospitalsFile.exists()) {
            importHospitals(conn, hospitalsFile.getAbsolutePath());
        }

        // 2. Roads / Routes
        File roadsFile = resolveFile(dir, "roads.csv");
        if (roadsFile.exists()) {
            importRoutes(conn, roadsFile.getAbsolutePath());
        }

        // 3. Service Requests
        File requestsFile = resolveFile(dir, "service_requests.csv");
        if (requestsFile.exists()) {
            importServiceRequests(conn, requestsFile.getAbsolutePath());
        }

        // 4. Medical Resources
        File resourcesFile = resolveFile(dir, "medical_resources.csv");
        if (resourcesFile.exists()) {
            importMedicalResources(conn, resourcesFile.getAbsolutePath());
        }

        // 5. Algorithm Runs (if present)
        File algoRunsFile = resolveFile(dir, "algorithm_runs.csv");
        if (algoRunsFile.exists()) {
            importAlgorithmRuns(conn, algoRunsFile.getAbsolutePath());
        }
    }

    public static int importHospitals(Connection conn, String csvPath) throws SQLException, IOException {
        String sql = "INSERT OR REPLACE INTO hospitals (" +
                "id, name, facility_type, region, district, town, latitude, longitude, ownership_type, icu_capacity, ambulance_count" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        File file = resolvePath(csvPath);
        List<String[]> rows = readCsv(file.getAbsolutePath());
        boolean autoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);

        int count = 0;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (String[] row : rows) {
                if (row.length < 11) continue;
                stmt.setInt(1, Integer.parseInt(row[0].trim()));
                stmt.setString(2, row[1].trim());
                stmt.setString(3, row[2].trim());
                stmt.setString(4, row[3].trim());
                stmt.setString(5, row[4].trim());
                stmt.setString(6, row[5].trim());
                stmt.setDouble(7, Double.parseDouble(row[6].trim()));
                stmt.setDouble(8, Double.parseDouble(row[7].trim()));
                stmt.setString(9, row[8].trim());
                stmt.setInt(10, Integer.parseInt(row[9].trim()));
                stmt.setInt(11, Integer.parseInt(row[10].trim()));
                stmt.addBatch();
                count++;
            }
            stmt.executeBatch();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new SQLException("Failed to import hospitals: " + e.getMessage(), e);
        } finally {
            conn.setAutoCommit(autoCommit);
        }
        return count;
    }

    public static int importRoutes(Connection conn, String csvPath) throws SQLException, IOException {
        String sql = "INSERT OR REPLACE INTO routes (" +
                "id, from_hospital, to_hospital, distance_km, avg_travel_time_mins, road_condition, source" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?)";

        File file = resolvePath(csvPath);
        List<String[]> rows = readCsv(file.getAbsolutePath());
        boolean autoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);

        int count = 0;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (String[] row : rows) {
                if (row.length < 6) continue;
                stmt.setInt(1, Integer.parseInt(row[0].trim()));
                stmt.setInt(2, Integer.parseInt(row[1].trim()));
                stmt.setInt(3, Integer.parseInt(row[2].trim()));
                stmt.setDouble(4, Double.parseDouble(row[3].trim()));
                stmt.setDouble(5, Double.parseDouble(row[4].trim()));
                stmt.setString(6, row[5].trim());
                stmt.setString(7, row.length > 6 ? row[6].trim() : "OpenStreetMap / OSRM");
                stmt.addBatch();
                count++;
            }
            stmt.executeBatch();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new SQLException("Failed to import routes: " + e.getMessage(), e);
        } finally {
            conn.setAutoCommit(autoCommit);
        }
        return count;
    }

    public static int importServiceRequests(Connection conn, String csvPath) throws SQLException, IOException {
        String sql = "INSERT OR REPLACE INTO service_requests (" +
                "request_id, origin_hospital_id, target_hospital_id, patient_triage_level, request_type, weight_kg, timestamp_created, status" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        File file = resolvePath(csvPath);
        List<String[]> rows = readCsv(file.getAbsolutePath());
        boolean autoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);

        int count = 0;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (String[] row : rows) {
                if (row.length < 8) continue;
                stmt.setInt(1, Integer.parseInt(row[0].trim()));
                stmt.setInt(2, Integer.parseInt(row[1].trim()));
                stmt.setInt(3, Integer.parseInt(row[2].trim()));
                stmt.setInt(4, Integer.parseInt(row[3].trim()));
                stmt.setString(5, row[4].trim());
                stmt.setDouble(6, Double.parseDouble(row[5].trim()));
                stmt.setString(7, row[6].trim());
                stmt.setString(8, row[7].trim());
                stmt.addBatch();
                count++;
            }
            stmt.executeBatch();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new SQLException("Failed to import service requests: " + e.getMessage(), e);
        } finally {
            conn.setAutoCommit(autoCommit);
        }
        return count;
    }

    public static int importMedicalResources(Connection conn, String csvPath) throws SQLException, IOException {
        String sql = "INSERT OR REPLACE INTO medical_resources (" +
                "resource_id, hospital_id, resource_name, quantity_available, resource_category" +
                ") VALUES (?, ?, ?, ?, ?)";

        File file = resolvePath(csvPath);
        List<String[]> rows = readCsv(file.getAbsolutePath());
        boolean autoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);

        int count = 0;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (String[] row : rows) {
                if (row.length < 5) continue;
                stmt.setInt(1, Integer.parseInt(row[0].trim()));
                stmt.setInt(2, Integer.parseInt(row[1].trim()));
                stmt.setString(3, row[2].trim());
                stmt.setInt(4, Integer.parseInt(row[3].trim()));
                stmt.setString(5, row[4].trim());
                stmt.addBatch();
                count++;
            }
            stmt.executeBatch();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new SQLException("Failed to import medical resources: " + e.getMessage(), e);
        } finally {
            conn.setAutoCommit(autoCommit);
        }
        return count;
    }

    public static int importAlgorithmRuns(Connection conn, String csvPath) throws SQLException, IOException {
        String sql = "INSERT INTO algorithm_runs (" +
                "run_id, algorithm_name, input_size, execution_time_ns, memory_used_kb, iteration_number, run_timestamp" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?)";

        File file = resolvePath(csvPath);
        List<String[]> rows = readCsv(file.getAbsolutePath());
        boolean autoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);

        int count = 0;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (String[] row : rows) {
                if (row.length < 7) continue;
                stmt.setString(1, row[0].trim());
                stmt.setString(2, row[1].trim());
                stmt.setInt(3, Integer.parseInt(row[2].trim()));
                stmt.setLong(4, Long.parseLong(row[3].trim()));
                stmt.setDouble(5, Double.parseDouble(row[4].trim()));
                stmt.setInt(6, Integer.parseInt(row[5].trim()));
                stmt.setString(7, row[6].trim());
                stmt.addBatch();
                count++;
            }
            stmt.executeBatch();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new SQLException("Failed to import algorithm runs: " + e.getMessage(), e);
        } finally {
            conn.setAutoCommit(autoCommit);
        }
        return count;
    }

    private static File resolveDirectory(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            File parentDir = new File("../" + dirPath);
            if (parentDir.exists()) {
                return parentDir;
            }
        }
        return dir;
    }

    private static File resolveFile(File dir, String fileName) {
        return new File(dir, fileName);
    }

    private static File resolvePath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            File parentFile = new File("../" + path);
            if (parentFile.exists()) {
                return parentFile;
            }
        }
        return file;
    }

    private static List<String[]> readCsv(String filePath) throws IOException {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirst = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (isFirst) {
                    isFirst = false;
                    continue; // Skip header
                }
                records.add(parseCsvLine(line));
            }
        }
        return records;
    }

    private static String[] parseCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        tokens.add(sb.toString());
        return tokens.toArray(new String[0]);
    }
}
