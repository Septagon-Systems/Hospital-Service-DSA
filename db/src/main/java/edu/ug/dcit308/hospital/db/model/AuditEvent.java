package edu.ug.dcit308.hospital.db.model;

import java.util.Objects;

/**
 * Entity model representing an audit log event in the hospital system.
 */
public class AuditEvent {
    private int eventId;
    private String actionType;  // e.g. 'ADMIT_REQUEST', 'SERVE_REQUEST', 'UNDO_ACTION', 'REROUTE'
    private String entityType;  // e.g. 'SERVICE_REQUEST', 'RESOURCE', 'HOSPITAL'
    private int entityId;
    private String details;
    private String timestamp;

    public AuditEvent() {}

    public AuditEvent(int eventId, String actionType, String entityType,
                      int entityId, String details, String timestamp) {
        this.eventId = eventId;
        this.actionType = actionType;
        this.entityType = entityType;
        this.entityId = entityId;
        this.details = details;
        this.timestamp = timestamp;
    }

    public AuditEvent(String actionType, String entityType,
                      int entityId, String details, String timestamp) {
        this(0, actionType, entityType, entityId, details, timestamp);
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditEvent that = (AuditEvent) o;
        return eventId == that.eventId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "AuditEvent{" +
                "eventId=" + eventId +
                ", actionType='" + actionType + '\'' +
                ", entityType='" + entityType + '\'' +
                ", entityId=" + entityId +
                ", details='" + details + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
