package edu.ug.dcit308.hospital.db.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.ug.dcit308.hospital.db.model.AuditEvent;

/**
 * JDBC Repository for {@link AuditEvent} entities.
 */
public class AuditEventRepository {

    private final Connection connection;

    public AuditEventRepository(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection must not be null");
        }
        this.connection = connection;
    }

    public List<AuditEvent> findAll() throws SQLException {
        String sql = "SELECT event_id, action_type, entity_type, entity_id, details, timestamp FROM audit_events ORDER BY event_id";
        List<AuditEvent> events = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                events.add(mapResultSet(rs));
            }
        }
        return events;
    }

    public List<AuditEvent> findRecent(int limit) throws SQLException {
        String sql = "SELECT event_id, action_type, entity_type, entity_id, details, timestamp FROM audit_events ORDER BY event_id DESC LIMIT ?";
        List<AuditEvent> events = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Math.max(1, limit));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    events.add(mapResultSet(rs));
                }
            }
        }
        return events;
    }

    public AuditEvent save(AuditEvent event) throws SQLException {
        if (event == null) {
            throw new IllegalArgumentException("AuditEvent must not be null");
        }

        String sql = "INSERT INTO audit_events (action_type, entity_type, entity_id, details, timestamp) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, event.getActionType());
            stmt.setString(2, event.getEntityType());
            stmt.setInt(3, event.getEntityId());
            stmt.setString(4, event.getDetails());
            stmt.setString(5, event.getTimestamp());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    event.setEventId(keys.getInt(1));
                }
            }
        }
        return event;
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM audit_events";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public void deleteAll() throws SQLException {
        String sql = "DELETE FROM audit_events";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    private AuditEvent mapResultSet(ResultSet rs) throws SQLException {
        return new AuditEvent(
                rs.getInt("event_id"),
                rs.getString("action_type"),
                rs.getString("entity_type"),
                rs.getInt("entity_id"),
                rs.getString("details"),
                rs.getString("timestamp")
        );
    }
}
