package edu.ug.dcit308.hospital.db.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ug.dcit308.hospital.db.ConnectionManager;
import edu.ug.dcit308.hospital.db.model.AuditEvent;
import edu.ug.dcit308.hospital.db.schema.SchemaInitializer;

class AuditEventRepositoryTest {

    private Connection connection;
    private AuditEventRepository auditRepo;

    @BeforeEach
    void setUp() throws SQLException {
        connection = ConnectionManager.getInMemoryConnection();
        SchemaInitializer.initialize(connection);
        auditRepo = new AuditEventRepository(connection);
    }

    @Test
    void testSaveAndFindRecent() throws SQLException {
        auditRepo.save(new AuditEvent("ADMIT_REQUEST", "SERVICE_REQUEST", 1, "Admitted P1 patient", "2026-08-01 10:00:00"));
        auditRepo.save(new AuditEvent("SERVE_REQUEST", "SERVICE_REQUEST", 1, "Dispatched ambulance AMB-1", "2026-08-01 10:05:00"));
        auditRepo.save(new AuditEvent("UNDO_ACTION", "SERVICE_REQUEST", 1, "Rolled back ambulance dispatch", "2026-08-01 10:06:00"));

        List<AuditEvent> recent = auditRepo.findRecent(2);
        assertEquals(2, recent.size());
        assertEquals("UNDO_ACTION", recent.get(0).getActionType());
        assertEquals("SERVE_REQUEST", recent.get(1).getActionType());
    }

    @Test
    void testCountAndClear() throws SQLException {
        assertEquals(0, auditRepo.count());

        auditRepo.save(new AuditEvent("ADMIT_REQUEST", "SERVICE_REQUEST", 2, "Admitted", "2026-08-01 11:00:00"));
        assertEquals(1, auditRepo.count());

        auditRepo.deleteAll();
        assertEquals(0, auditRepo.count());
    }
}
