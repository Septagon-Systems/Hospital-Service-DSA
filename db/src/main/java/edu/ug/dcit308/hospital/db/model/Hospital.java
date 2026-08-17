package edu.ug.dcit308.hospital.db.model;

import java.util.Objects;

/**
 * Entity model representing a Ghanaian healthcare facility in the SQLite database.
 */
public class Hospital {
    private int hospitalId;
    private String name;
    private String region;
    private String district;
    private String town;
    private double latitude;
    private double longitude;
    private String ownershipType; // 'Government', 'Private', 'CHAG', 'Quasi-Govt'
    private String facilityType;  // e.g. 'Teaching Hospital', 'Regional Hospital', 'District Hospital'
    private int icuCapacity;
    private int ambulanceCount;

    public Hospital() {}

    public Hospital(int hospitalId, String name, String region, String district, String town,
                    double latitude, double longitude, String ownershipType,
                    String facilityType, int icuCapacity, int ambulanceCount) {
        this.hospitalId = hospitalId;
        this.name = name;
        this.region = region;
        this.district = district;
        this.town = town;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ownershipType = ownershipType;
        this.facilityType = facilityType;
        this.icuCapacity = icuCapacity;
        this.ambulanceCount = ambulanceCount;
    }

    public Hospital(String name, String region, String district, String town,
                    double latitude, double longitude, String ownershipType,
                    String facilityType, int icuCapacity, int ambulanceCount) {
        this(0, name, region, district, town, latitude, longitude, ownershipType, facilityType, icuCapacity, ambulanceCount);
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(String ownershipType) {
        this.ownershipType = ownershipType;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

    public int getIcuCapacity() {
        return icuCapacity;
    }

    public void setIcuCapacity(int icuCapacity) {
        this.icuCapacity = icuCapacity;
    }

    public int getAmbulanceCount() {
        return ambulanceCount;
    }

    public void setAmbulanceCount(int ambulanceCount) {
        this.ambulanceCount = ambulanceCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hospital hospital = (Hospital) o;
        return hospitalId == hospital.hospitalId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hospitalId);
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "id=" + hospitalId +
                ", name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", town='" + town + '\'' +
                ", lat=" + latitude +
                ", lon=" + longitude +
                ", type='" + facilityType + '\'' +
                ", icu=" + icuCapacity +
                ", ambulances=" + ambulanceCount +
                '}';
    }
}
