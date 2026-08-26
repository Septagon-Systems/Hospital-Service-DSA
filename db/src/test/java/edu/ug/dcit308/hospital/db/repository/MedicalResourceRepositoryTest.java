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
import edu.ug.dcit308.hospital.db.model.MedicalResource;
import edu.ug.dcit308.hospital.db.schema.SchemaInitializer;

class MedicalResourceRepositoryTest {

    private Connection connection;
    private MedicalResourceRepository resourceRepo;
    private HospitalRepository hospitalRepo;

    @BeforeEach
    void setUp() throws SQLException {
        connection = ConnectionManager.getInMemoryConnection();
        SchemaInitializer.initialize(connection);
        resourceRepo = new MedicalResourceRepository(connection);
        hospitalRepo = new HospitalRepository(connection);

        hospitalRepo.save(new Hospital(1, "Korle Bu", "Accra", "Accra", "Accra", 5.5, -0.2, "Gov", "Type", 35, 8));
    }

    @Test
    void testSaveAndFindByHospitalId() throws SQLException {
        resourceRepo.save(new MedicalResource(1, 1, "Dialysis Machine", 4, "ICU Equipment"));
        resourceRepo.save(new MedicalResource(2, 1, "O-Negative Blood Units", 25, "Blood Bank"));

        List<MedicalResource> resources = resourceRepo.findByHospitalId(1);
        assertEquals(2, resources.size());

        List<MedicalResource> bloodUnits = resourceRepo.findByCategory("Blood Bank");
        assertEquals(1, bloodUnits.size());
        assertEquals("O-Negative Blood Units", bloodUnits.get(0).getResourceName());
    }

    @Test
    void testUpdateQuantity() throws SQLException {
        resourceRepo.save(new MedicalResource(1, 1, "Dialysis Machine", 4, "ICU Equipment"));

        boolean updated = resourceRepo.updateQuantity(1, 10);
        assertTrue(updated);

        Optional<MedicalResource> res = resourceRepo.findById(1);
        assertTrue(res.isPresent());
        assertEquals(10, res.get().getQuantityAvailable());
    }
}
