package edu.ug.dcit308.hospital.db.schema;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ug.dcit308.hospital.db.ConnectionManager;

class SchemaInitializerTest {

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = ConnectionManager.getInMemoryConnection();
    }

    @Test
    void testInitializeCreatesAllTables() throws SQLException {
        SchemaInitializer.initialize(connection);

        DatabaseMetaData meta = connection.getMetaData();
        Set<String> tables = new HashSet<>();
        try (ResultSet rs = meta.getTables(null, null, "%", new String[]{"TABLE"})) {
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME").toLowerCase());
            }
        }

        assertTrue(tables.contains("hospitals"), "hospitals table must exist");
        assertTrue(tables.contains("routes"), "routes table must exist");
        assertTrue(tables.contains("service_requests"), "service_requests table must exist");
        assertTrue(tables.contains("medical_resources"), "medical_resources table must exist");
        assertTrue(tables.contains("algorithm_runs"), "algorithm_runs table must exist");
        assertTrue(tables.contains("audit_events"), "audit_events table must exist");
    }

    @Test
    void testInitializeIsIdempotent() throws SQLException {
        // Calling initialize multiple times must not throw
        SchemaInitializer.initialize(connection);
        assertDoesNotThrow(() -> SchemaInitializer.initialize(connection));
    }

    @Test
    void testRejectsNullConnection() {
        assertThrows(IllegalArgumentException.class, () -> SchemaInitializer.initialize(null));
    }
}
