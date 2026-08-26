package edu.ug.dcit308.hospital.db.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.ug.dcit308.hospital.db.model.MedicalResource;

/**
 * JDBC Repository for {@link MedicalResource} entities.
 */
public class MedicalResourceRepository {

    private final Connection connection;

    public MedicalResourceRepository(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection must not be null");
        }
        this.connection = connection;
    }

    public List<MedicalResource> findAll() throws SQLException {
        String sql = "SELECT resource_id, hospital_id, resource_name, quantity_available, resource_category FROM medical_resources ORDER BY resource_id";
        List<MedicalResource> resources = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                resources.add(mapResultSet(rs));
            }
        }
        return resources;
    }

    public Optional<MedicalResource> findById(int id) throws SQLException {
        String sql = "SELECT resource_id, hospital_id, resource_name, quantity_available, resource_category FROM medical_resources WHERE resource_id = ?";
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

    public List<MedicalResource> findByHospitalId(int hospitalId) throws SQLException {
        String sql = "SELECT resource_id, hospital_id, resource_name, quantity_available, resource_category FROM medical_resources WHERE hospital_id = ? ORDER BY resource_id";
        List<MedicalResource> resources = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, hospitalId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resources.add(mapResultSet(rs));
                }
            }
        }
        return resources;
    }

    public List<MedicalResource> findByCategory(String category) throws SQLException {
        String sql = "SELECT resource_id, hospital_id, resource_name, quantity_available, resource_category FROM medical_resources WHERE resource_category = ? ORDER BY resource_id";
        List<MedicalResource> resources = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, category);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resources.add(mapResultSet(rs));
                }
            }
        }
        return resources;
    }

    public MedicalResource save(MedicalResource resource) throws SQLException {
        if (resource == null) {
            throw new IllegalArgumentException("MedicalResource must not be null");
        }

        if (resource.getResourceId() > 0 && findById(resource.getResourceId()).isPresent()) {
            String updateSql = "UPDATE medical_resources SET hospital_id = ?, resource_name = ?, quantity_available = ?, resource_category = ? WHERE resource_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
                stmt.setInt(1, resource.getHospitalId());
                stmt.setString(2, resource.getResourceName());
                stmt.setInt(3, resource.getQuantityAvailable());
                stmt.setString(4, resource.getResourceCategory());
                stmt.setInt(5, resource.getResourceId());
                stmt.executeUpdate();
            }
        } else {
            String insertSql = "INSERT INTO medical_resources (resource_id, hospital_id, resource_name, quantity_available, resource_category) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertSql)) {
                if (resource.getResourceId() > 0) {
                    stmt.setInt(1, resource.getResourceId());
                } else {
                    stmt.setNull(1, java.sql.Types.INTEGER);
                }
                stmt.setInt(2, resource.getHospitalId());
                stmt.setString(3, resource.getResourceName());
                stmt.setInt(4, resource.getQuantityAvailable());
                stmt.setString(5, resource.getResourceCategory());
                stmt.executeUpdate();
            }
        }
        return resource;
    }

    public boolean updateQuantity(int resourceId, int newQuantity) throws SQLException {
        String sql = "UPDATE medical_resources SET quantity_available = ? WHERE resource_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, newQuantity);
            stmt.setInt(2, resourceId);
            return stmt.executeUpdate() > 0;
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM medical_resources";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public void deleteAll() throws SQLException {
        String sql = "DELETE FROM medical_resources";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    private MedicalResource mapResultSet(ResultSet rs) throws SQLException {
        return new MedicalResource(
                rs.getInt("resource_id"),
                rs.getInt("hospital_id"),
                rs.getString("resource_name"),
                rs.getInt("quantity_available"),
                rs.getString("resource_category")
        );
    }
}
