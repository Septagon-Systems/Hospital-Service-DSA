package edu.ug.dcit308.hospital.db.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.ug.dcit308.hospital.db.model.Route;

/**
 * JDBC Repository for {@link Route} entities.
 */
public class RouteRepository {

    private final Connection connection;

    public RouteRepository(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection must not be null");
        }
        this.connection = connection;
    }

    public List<Route> findAll() throws SQLException {
        String sql = "SELECT id, from_hospital, to_hospital, distance_km, avg_travel_time_mins, road_condition, source FROM routes ORDER BY id";
        List<Route> routes = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                routes.add(mapResultSet(rs));
            }
        }
        return routes;
    }

    public Optional<Route> findById(int id) throws SQLException {
        String sql = "SELECT id, from_hospital, to_hospital, distance_km, avg_travel_time_mins, road_condition, source FROM routes WHERE id = ?";
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

    public List<Route> findByHospital(int hospitalId) throws SQLException {
        String sql = "SELECT id, from_hospital, to_hospital, distance_km, avg_travel_time_mins, road_condition, source FROM routes WHERE from_hospital = ? OR to_hospital = ? ORDER BY id";
        List<Route> routes = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, hospitalId);
            stmt.setInt(2, hospitalId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    routes.add(mapResultSet(rs));
                }
            }
        }
        return routes;
    }

    public Optional<Route> findByOriginAndDestination(int fromId, int toId) throws SQLException {
        String sql = "SELECT id, from_hospital, to_hospital, distance_km, avg_travel_time_mins, road_condition, source FROM routes WHERE (from_hospital = ? AND to_hospital = ?) OR (from_hospital = ? AND to_hospital = ?) LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, fromId);
            stmt.setInt(2, toId);
            stmt.setInt(3, toId);
            stmt.setInt(4, fromId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
        }
        return Optional.empty();
    }

    public Route save(Route route) throws SQLException {
        if (route == null) {
            throw new IllegalArgumentException("Route must not be null");
        }

        if (route.getRouteId() > 0 && findById(route.getRouteId()).isPresent()) {
            String updateSql = "UPDATE routes SET from_hospital = ?, to_hospital = ?, distance_km = ?, avg_travel_time_mins = ?, road_condition = ?, source = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
                stmt.setInt(1, route.getSourceHospitalId());
                stmt.setInt(2, route.getDestinationHospitalId());
                stmt.setDouble(3, route.getDistanceKm());
                stmt.setDouble(4, route.getAvgTravelTimeMins());
                stmt.setString(5, route.getRoadCondition());
                stmt.setString(6, route.getSource());
                stmt.setInt(7, route.getRouteId());
                stmt.executeUpdate();
            }
        } else {
            String insertSql = "INSERT INTO routes (id, from_hospital, to_hospital, distance_km, avg_travel_time_mins, road_condition, source) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertSql)) {
                if (route.getRouteId() > 0) {
                    stmt.setInt(1, route.getRouteId());
                } else {
                    stmt.setNull(1, java.sql.Types.INTEGER);
                }
                stmt.setInt(2, route.getSourceHospitalId());
                stmt.setInt(3, route.getDestinationHospitalId());
                stmt.setDouble(4, route.getDistanceKm());
                stmt.setDouble(5, route.getAvgTravelTimeMins());
                stmt.setString(6, route.getRoadCondition());
                stmt.setString(7, route.getSource());
                stmt.executeUpdate();
            }
        }
        return route;
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM routes";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public void deleteAll() throws SQLException {
        String sql = "DELETE FROM routes";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    private Route mapResultSet(ResultSet rs) throws SQLException {
        return new Route(
                rs.getInt("id"),
                rs.getInt("from_hospital"),
                rs.getInt("to_hospital"),
                rs.getDouble("distance_km"),
                rs.getDouble("avg_travel_time_mins"),
                rs.getString("road_condition"),
                rs.getString("source")
        );
    }
}
