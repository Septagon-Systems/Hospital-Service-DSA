package edu.ug.dcit308.hospital.db.importer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ug.dcit308.hospital.db.ConnectionManager;
import edu.ug.dcit308.hospital.db.schema.SchemaInitializer;

class CsvImporterTest {

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = ConnectionManager.getInMemoryConnection();
        SchemaInitializer.initialize(connection);
    }

    @Test
    void testImportAllSeedsCanonicalData() throws SQLException, IOException {
        CsvImporter.importAll(connection, "data/seed");

        assertEquals(50, getRowCount("hospitals"), "Must import 50 Ghanaian hospitals");
        assertEquals(128, getRowCount("routes"), "Must import 128 road corridors");
        assertEquals(350, getRowCount("service_requests"), "Must import 350 service requests");
        assertEquals(60, getRowCount("medical_resources"), "Must import 60 medical resources");
    }

    @Test
    void testImportHospitalsVerifiesKorleBuRecord() throws SQLException, IOException {
        CsvImporter.importHospitals(connection, "data/seed/hospitals.csv");

        String sql = "SELECT name, region, latitude, longitude, icu_capacity, ambulance_count FROM hospitals WHERE id = 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            assertTrue(rs.next());
            assertEquals("Korle Bu Teaching Hospital", rs.getString("name"));
            assertEquals("Greater Accra", rs.getString("region"));
            assertEquals(5.533, rs.getDouble("latitude"), 1e-6);
            assertEquals(-0.225, rs.getDouble("longitude"), 1e-6);
            assertEquals(35, rs.getInt("icu_capacity"));
            assertEquals(8, rs.getInt("ambulance_count"));
        }
    }

    @Test
    void testImportRejectsNonExistentDirectory() {
        assertThrows(IOException.class, () -> CsvImporter.importAll(connection, "non_existent_dir_xyz"));
    }

    @Test
    void testImportRejectsNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> CsvImporter.importAll(null, "data/seed"));
        assertThrows(IllegalArgumentException.class, () -> CsvImporter.importAll(connection, null));
    }

    private int getRowCount(String table) throws SQLException {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + table)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}
