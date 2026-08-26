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
import edu.ug.dcit308.hospital.db.model.Route;
import edu.ug.dcit308.hospital.db.schema.SchemaInitializer;

class RouteRepositoryTest {

    private Connection connection;
    private RouteRepository routeRepo;
    private HospitalRepository hospitalRepo;

    @BeforeEach
    void setUp() throws SQLException {
        connection = ConnectionManager.getInMemoryConnection();
        SchemaInitializer.initialize(connection);
        routeRepo = new RouteRepository(connection);
        hospitalRepo = new HospitalRepository(connection);

        // Seed parent hospitals for foreign key integrity
        hospitalRepo.save(new Hospital(1, "H1", "Accra", "Accra", "Accra", 5.5, -0.2, "Gov", "Type", 5, 2));
        hospitalRepo.save(new Hospital(2, "H2", "Accra", "Accra", "Accra", 5.6, -0.1, "Gov", "Type", 10, 3));
        hospitalRepo.save(new Hospital(3, "H3", "Tema", "Tema", "Tema", 5.7, 0.0, "Gov", "Type", 8, 4));
    }

    @Test
    void testSaveAndFindByHospital() throws SQLException {
        routeRepo.save(new Route(1, 1, 2, 8.4, 12.5, "Paved", "OSRM"));
        routeRepo.save(new Route(2, 2, 3, 15.2, 22.0, "Paved", "OSRM"));

        List<Route> routesForH2 = routeRepo.findByHospital(2);
        assertEquals(2, routesForH2.size());

        List<Route> routesForH1 = routeRepo.findByHospital(1);
        assertEquals(1, routesForH1.size());
    }

    @Test
    void testFindByOriginAndDestination() throws SQLException {
        routeRepo.save(new Route(1, 1, 3, 25.0, 35.0, "Paved", "OSRM"));

        Optional<Route> forward = routeRepo.findByOriginAndDestination(1, 3);
        assertTrue(forward.isPresent());
        assertEquals(25.0, forward.get().getDistanceKm(), 1e-6);

        Optional<Route> backward = routeRepo.findByOriginAndDestination(3, 1);
        assertTrue(backward.isPresent());
    }
}
