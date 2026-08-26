package edu.ug.dcit308.hospital.db.model;

import java.util.Objects;

/**
 * Entity model representing a medical equipment or supply unit at a hospital.
 */
public class MedicalResource {
    private int resourceId;
    private int hospitalId;
    private String resourceName;
    private int quantityAvailable;
    private String resourceCategory; // e.g. 'ICU Equipment', 'Blood Bank', 'Emergency Fleet'

    public MedicalResource() {}

    public MedicalResource(int resourceId, int hospitalId, String resourceName,
                           int quantityAvailable, String resourceCategory) {
        this.resourceId = resourceId;
        this.hospitalId = hospitalId;
        this.resourceName = resourceName;
        this.quantityAvailable = quantityAvailable;
        this.resourceCategory = resourceCategory;
    }

    public MedicalResource(int hospitalId, String resourceName,
                           int quantityAvailable, String resourceCategory) {
        this(0, hospitalId, resourceName, quantityAvailable, resourceCategory);
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public String getResourceCategory() {
        return resourceCategory;
    }

    public void setResourceCategory(String resourceCategory) {
        this.resourceCategory = resourceCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalResource that = (MedicalResource) o;
        return resourceId == that.resourceId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceId);
    }

    @Override
    public String toString() {
        return "MedicalResource{" +
                "resourceId=" + resourceId +
                ", hospitalId=" + hospitalId +
                ", resourceName='" + resourceName + '\'' +
                ", quantityAvailable=" + quantityAvailable +
                ", resourceCategory='" + resourceCategory + '\'' +
                '}';
    }
}
