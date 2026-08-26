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
import edu.ug.dcit308.hospital.db.schema.SchemaInitializer;

class HospitalRepositoryTest {

    private Connection connection;
    private HospitalRepository repository;

    @BeforeEach
    void setUp() throws SQLException {
        connection = ConnectionManager.getInMemoryConnection();
        SchemaInitializer.initialize(connection);
        repository = new HospitalRepository(connection);
    }

    @Test
    void testSaveAndFindById() throws SQLException {
        Hospital h = new Hospital(101, "Ridge Hospital", "Greater Accra", "Accra", "Accra",
                5.56, -0.20, "Government", "Regional Hospital", 20, 5);

        repository.save(h);

        Optional<Hospital> found = repository.findById(101);
        assertTrue(found.isPresent());
        assertEquals("Ridge Hospital", found.get().getName());
        assertEquals("Greater Accra", found.get().getRegion());
        assertEquals(20, found.get().getIcuCapacity());
        assertEquals(5, found.get().getAmbulanceCount());
    }

    @Test
    void testFindByRegion() throws SQLException {
        repository.save(new Hospital(1, "H1", "Ashanti", "Kumasi", "Kumasi", 6.7, -1.6, "Gov", "Type", 5, 2));
        repository.save(new Hospital(2, "H2", "Ashanti", "Kumasi", "Kumasi", 6.8, -1.5, "Gov", "Type", 10, 3));
        repository.save(new Hospital(3, "H3", "Northern", "Tamale", "Tamale", 9.4, -0.8, "Gov", "Type", 8, 4));

        List<Hospital> ashantiHospitals = repository.findByRegion("Ashanti");
        assertEquals(2, ashantiHospitals.size());

        List<Hospital> northernHospitals = repository.findByRegion("Northern");
        assertEquals(1, northernHospitals.size());
    }

    @Test
    void testCountAndClear() throws SQLException {
        assertEquals(0, repository.count());

        repository.save(new Hospital(1, "H1", "Volta", "Ho", "Ho", 6.6, 0.47, "Gov", "Type", 2, 1));
        assertEquals(1, repository.count());

        repository.deleteAll();
        assertEquals(0, repository.count());
    }
}
