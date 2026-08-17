package edu.ug.dcit308.hospital.db.model;

import java.util.Objects;

/**
 * Entity model representing a road connection/route between two hospitals in the SQLite database.
 */
public class Route {
    private int routeId;
    private int sourceHospitalId;
    private int destinationHospitalId;
    private double distanceKm;
    private double avgTravelTimeMins;
    private String roadCondition; // 'Paved', 'Unpaved', 'Under Maintenance'

    public Route() {}

    public Route(int routeId, int sourceHospitalId, int destinationHospitalId,
                 double distanceKm, double avgTravelTimeMins, String roadCondition) {
        this.routeId = routeId;
        this.sourceHospitalId = sourceHospitalId;
        this.destinationHospitalId = destinationHospitalId;
        this.distanceKm = distanceKm;
        this.avgTravelTimeMins = avgTravelTimeMins;
        this.roadCondition = roadCondition;
    }

    public Route(int sourceHospitalId, int destinationHospitalId,
                 double distanceKm, double avgTravelTimeMins, String roadCondition) {
        this(0, sourceHospitalId, destinationHospitalId, distanceKm, avgTravelTimeMins, roadCondition);
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getSourceHospitalId() {
        return sourceHospitalId;
    }

    public void setSourceHospitalId(int sourceHospitalId) {
        this.sourceHospitalId = sourceHospitalId;
    }

    public int getDestinationHospitalId() {
        return destinationHospitalId;
    }

    public void setDestinationHospitalId(int destinationHospitalId) {
        this.destinationHospitalId = destinationHospitalId;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public double getAvgTravelTimeMins() {
        return avgTravelTimeMins;
    }

    public void setAvgTravelTimeMins(double avgTravelTimeMins) {
        this.avgTravelTimeMins = avgTravelTimeMins;
    }

    public String getRoadCondition() {
        return roadCondition;
    }

    public void setRoadCondition(String roadCondition) {
        this.roadCondition = roadCondition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return routeId == route.routeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeId);
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + routeId +
                ", source=" + sourceHospitalId +
                ", dest=" + destinationHospitalId +
                ", dist=" + distanceKm + "km" +
                ", time=" + avgTravelTimeMins + "min" +
                ", condition='" + roadCondition + '\'' +
                '}';
    }
}
