package edu.ug.dcit308.hospital.db.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.ug.dcit308.hospital.db.model.Hospital;

/**
 * JDBC Repository for {@link Hospital} entities.
 */
public class HospitalRepository {

    private final Connection connection;

    public HospitalRepository(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection must not be null");
        }
        this.connection = connection;
    }

    public List<Hospital> findAll() throws SQLException {
        String sql = "SELECT id, name, facility_type, region, district, town, latitude, longitude, ownership_type, icu_capacity, ambulance_count FROM hospitals ORDER BY id";
        List<Hospital> hospitals = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                hospitals.add(mapResultSet(rs));
            }
        }
        return hospitals;
    }

    public Optional<Hospital> findById(int id) throws SQLException {
        String sql = "SELECT id, name, facility_type, region, district, town, latitude, longitude, ownership_type, icu_capacity, ambulance_count FROM hospitals WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Hospital> findByRegion(String region) throws SQLException {
        String sql = "SELECT id, name, facility_type, region, district, town, latitude, longitude, ownership_type, icu_capacity, ambulance_count FROM hospitals WHERE region = ? ORDER BY id";
        List<Hospital> hospitals = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, region);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    hospitals.add(mapResultSet(rs));
                }
            }
        }
        return hospitals;
    }

    public Hospital save(Hospital hospital) throws SQLException {
        if (hospital == null) {
            throw new IllegalArgumentException("Hospital must not be null");
        }

        if (hospital.getHospitalId() > 0 && findById(hospital.getHospitalId()).isPresent()) {
            String updateSql = "UPDATE hospitals SET name = ?, facility_type = ?, region = ?, district = ?, town = ?, latitude = ?, longitude = ?, ownership_type = ?, icu_capacity = ?, ambulance_count = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
                stmt.setString(1, hospital.getName());
                stmt.setString(2, hospital.getFacilityType());
                stmt.setString(3, hospital.getRegion());
                stmt.setString(4, hospital.getDistrict());
                stmt.setString(5, hospital.getTown());
                stmt.setDouble(6, hospital.getLatitude());
                stmt.setDouble(7, hospital.getLongitude());
                stmt.setString(8, hospital.getOwnershipType());
                stmt.setInt(9, hospital.getIcuCapacity());
                stmt.setInt(10, hospital.getAmbulanceCount());
                stmt.setInt(11, hospital.getHospitalId());
                stmt.executeUpdate();
            }
        } else {
            String insertSql = "INSERT INTO hospitals (id, name, facility_type, region, district, town, latitude, longitude, ownership_type, icu_capacity, ambulance_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertSql)) {
                if (hospital.getHospitalId() > 0) {
                    stmt.setInt(1, hospital.getHospitalId());
                } else {
                    stmt.setNull(1, java.sql.Types.INTEGER);
                }
                stmt.setString(2, hospital.getName());
                stmt.setString(3, hospital.getFacilityType());
                stmt.setString(4, hospital.getRegion());
                stmt.setString(5, hospital.getDistrict());
                stmt.setString(6, hospital.getTown());
                stmt.setDouble(7, hospital.getLatitude());
                stmt.setDouble(8, hospital.getLongitude());
                stmt.setString(9, hospital.getOwnershipType());
                stmt.setInt(10, hospital.getIcuCapacity());
                stmt.setInt(11, hospital.getAmbulanceCount());
                stmt.executeUpdate();
            }
        }
        return hospital;
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM hospitals";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public void deleteAll() throws SQLException {
        String sql = "DELETE FROM hospitals";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    private Hospital mapResultSet(ResultSet rs) throws SQLException {
        return new Hospital(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("region"),
                rs.getString("district"),
                rs.getString("town"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude"),
                rs.getString("ownership_type"),
                rs.getString("facility_type"),
                rs.getInt("icu_capacity"),
                rs.getInt("ambulance_count")
        );
    }
}
