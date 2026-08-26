package edu.ug.dcit308.hospital.db.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ug.dcit308.hospital.db.ConnectionManager;
import edu.ug.dcit308.hospital.db.model.Hospital;
import edu.ug.dcit308.hospital.db.model.ServiceRequest;
import edu.ug.dcit308.hospital.db.schema.SchemaInitializer;

class ServiceRequestRepositoryTest {

    private Connection connection;
    private ServiceRequestRepository requestRepo;
    private HospitalRepository hospitalRepo;

    @BeforeEach
    void setUp() throws SQLException {
        connection = ConnectionManager.getInMemoryConnection();
        SchemaInitializer.initialize(connection);
        requestRepo = new ServiceRequestRepository(connection);
        hospitalRepo = new HospitalRepository(connection);

        hospitalRepo.save(new Hospital(1, "H1", "Accra", "Accra", "Accra", 5.5, -0.2, "Gov", "Type", 5, 2));
        hospitalRepo.save(new Hospital(2, "H2", "Accra", "Accra", "Accra", 5.6, -0.1, "Gov", "Type", 10, 3));
    }

    @Test
    void testSaveAndFindByStatus() throws SQLException {
        requestRepo.save(new ServiceRequest(1, 1, 2, 1, "Critical Patient Transfer", 75.0, "2026-08-01 10:00:00", "PENDING"));
        requestRepo.save(new ServiceRequest(2, 1, 2, 3, "Routine Supply", 10.0, "2026-08-01 10:05:00", "COMPLETED"));

        List<ServiceRequest> pending = requestRepo.findByStatus("PENDING");
        assertEquals(1, pending.size());
        assertEquals(1, pending.get(0).getRequestId());
        assertEquals(1, pending.get(0).getPatientTriageLevel());

        List<ServiceRequest> completed = requestRepo.findByStatus("COMPLETED");
        assertEquals(1, completed.size());
    }

    @Test
    void testUpdateStatus() throws SQLException {
        requestRepo.save(new ServiceRequest(1, 1, 2, 1, "Critical Patient Transfer", 75.0, "2026-08-01 10:00:00", "PENDING"));

        boolean updated = requestRepo.updateStatus(1, "DISPATCHED");
        assertTrue(updated);

        Optional<ServiceRequest> req = requestRepo.findById(1);
        assertTrue(req.isPresent());
        assertEquals("DISPATCHED", req.get().getStatus());
    }
}
