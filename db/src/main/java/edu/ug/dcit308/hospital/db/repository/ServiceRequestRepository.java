package edu.ug.dcit308.hospital.db.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.ug.dcit308.hospital.db.model.ServiceRequest;

/**
 * JDBC Repository for {@link ServiceRequest} entities.
 */
public class ServiceRequestRepository {

    private final Connection connection;

    public ServiceRequestRepository(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection must not be null");
        }
        this.connection = connection;
    }

    public List<ServiceRequest> findAll() throws SQLException {
        String sql = "SELECT request_id, origin_hospital_id, target_hospital_id, patient_triage_level, request_type, weight_kg, timestamp_created, status FROM service_requests ORDER BY request_id";
        List<ServiceRequest> requests = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                requests.add(mapResultSet(rs));
            }
        }
        return requests;
    }

    public Optional<ServiceRequest> findById(int id) throws SQLException {
        String sql = "SELECT request_id, origin_hospital_id, target_hospital_id, patient_triage_level, request_type, weight_kg, timestamp_created, status FROM service_requests WHERE request_id = ?";
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

    public List<ServiceRequest> findByStatus(String status) throws SQLException {
        String sql = "SELECT request_id, origin_hospital_id, target_hospital_id, patient_triage_level, request_type, weight_kg, timestamp_created, status FROM service_requests WHERE status = ? ORDER BY request_id";
        List<ServiceRequest> requests = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapResultSet(rs));
                }
            }
        }
        return requests;
    }

    public List<ServiceRequest> findByTriageLevel(int triageLevel) throws SQLException {
        String sql = "SELECT request_id, origin_hospital_id, target_hospital_id, patient_triage_level, request_type, weight_kg, timestamp_created, status FROM service_requests WHERE patient_triage_level = ? ORDER BY request_id";
        List<ServiceRequest> requests = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, triageLevel);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapResultSet(rs));
                }
            }
        }
        return requests;
    }

    public ServiceRequest save(ServiceRequest request) throws SQLException {
        if (request == null) {
            throw new IllegalArgumentException("ServiceRequest must not be null");
        }

        if (request.getRequestId() > 0 && findById(request.getRequestId()).isPresent()) {
            String updateSql = "UPDATE service_requests SET origin_hospital_id = ?, target_hospital_id = ?, patient_triage_level = ?, request_type = ?, weight_kg = ?, timestamp_created = ?, status = ? WHERE request_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
                stmt.setInt(1, request.getOriginHospitalId());
                stmt.setInt(2, request.getTargetHospitalId());
                stmt.setInt(3, request.getPatientTriageLevel());
                stmt.setString(4, request.getRequestType());
                stmt.setDouble(5, request.getWeightKg());
                stmt.setString(6, request.getTimestampCreated());
                stmt.setString(7, request.getStatus());
                stmt.setInt(8, request.getRequestId());
                stmt.executeUpdate();
            }
        } else {
            String insertSql = "INSERT INTO service_requests (request_id, origin_hospital_id, target_hospital_id, patient_triage_level, request_type, weight_kg, timestamp_created, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertSql)) {
                if (request.getRequestId() > 0) {
                    stmt.setInt(1, request.getRequestId());
                } else {
                    stmt.setNull(1, java.sql.Types.INTEGER);
                }
                stmt.setInt(2, request.getOriginHospitalId());
                stmt.setInt(3, request.getTargetHospitalId());
                stmt.setInt(4, request.getPatientTriageLevel());
                stmt.setString(5, request.getRequestType());
                stmt.setDouble(6, request.getWeightKg());
                stmt.setString(7, request.getTimestampCreated());
                stmt.setString(8, request.getStatus());
                stmt.executeUpdate();
            }
        }
        return request;
    }

    public boolean updateStatus(int requestId, String newStatus) throws SQLException {
        String sql = "UPDATE service_requests SET status = ? WHERE request_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, requestId);
            return stmt.executeUpdate() > 0;
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM service_requests";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public void deleteAll() throws SQLException {
        String sql = "DELETE FROM service_requests";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    private ServiceRequest mapResultSet(ResultSet rs) throws SQLException {
        return new ServiceRequest(
                rs.getInt("request_id"),
                rs.getInt("origin_hospital_id"),
                rs.getInt("target_hospital_id"),
                rs.getInt("patient_triage_level"),
                rs.getString("request_type"),
                rs.getDouble("weight_kg"),
                rs.getString("timestamp_created"),
                rs.getString("status")
        );
    }
}
