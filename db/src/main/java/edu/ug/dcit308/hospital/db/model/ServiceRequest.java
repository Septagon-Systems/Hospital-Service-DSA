package edu.ug.dcit308.hospital.db.model;

import java.util.Objects;

/**
 * Entity model representing a patient or medical service dispatch request in the SQLite database.
 */
public class ServiceRequest {
    private int requestId;
    private int originHospitalId;
    private int targetHospitalId;
    private int patientTriageLevel; // 1: Critical, 2: Urgent, 3: Normal
    private String requestType;     // 'Ambulance Transfer', 'Blood Packet', 'ICU Bed Request', 'Emergency Meds'
    private double weightKg;
    private String timestampCreated;
    private String status;          // 'PENDING', 'DISPATCHED', 'COMPLETED'

    public ServiceRequest() {}

    public ServiceRequest(int requestId, int originHospitalId, int targetHospitalId,
                          int patientTriageLevel, String requestType, double weightKg,
                          String timestampCreated, String status) {
        this.requestId = requestId;
        this.originHospitalId = originHospitalId;
        this.targetHospitalId = targetHospitalId;
        this.patientTriageLevel = patientTriageLevel;
        this.requestType = requestType;
        this.weightKg = weightKg;
        this.timestampCreated = timestampCreated;
        this.status = status;
    }

    public ServiceRequest(int originHospitalId, int targetHospitalId,
                          int patientTriageLevel, String requestType, double weightKg,
                          String timestampCreated, String status) {
        this(0, originHospitalId, targetHospitalId, patientTriageLevel, requestType, weightKg, timestampCreated, status);
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getOriginHospitalId() {
        return originHospitalId;
    }

    public void setOriginHospitalId(int originHospitalId) {
        this.originHospitalId = originHospitalId;
    }

    public int getTargetHospitalId() {
        return targetHospitalId;
    }

    public void setTargetHospitalId(int targetHospitalId) {
        this.targetHospitalId = targetHospitalId;
    }

    public int getPatientTriageLevel() {
        return patientTriageLevel;
    }

    public void setPatientTriageLevel(int patientTriageLevel) {
        this.patientTriageLevel = patientTriageLevel;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public String getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(String timestampCreated) {
        this.timestampCreated = timestampCreated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceRequest that = (ServiceRequest) o;
        return requestId == that.requestId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId);
    }

    @Override
    public String toString() {
        return "ServiceRequest{" +
                "id=" + requestId +
                ", origin=" + originHospitalId +
                ", target=" + targetHospitalId +
                ", triage=" + patientTriageLevel +
                ", type='" + requestType + '\'' +
                ", weight=" + weightKg + "kg" +
                ", time='" + timestampCreated + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
