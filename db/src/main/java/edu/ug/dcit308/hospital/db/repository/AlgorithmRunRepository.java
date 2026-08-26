package edu.ug.dcit308.hospital.db.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.ug.dcit308.hospital.db.model.AlgorithmRun;

/**
 * JDBC Repository for {@link AlgorithmRun} benchmark entities.
 */
public class AlgorithmRunRepository {

    private final Connection connection;

    public AlgorithmRunRepository(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection must not be null");
        }
        this.connection = connection;
    }

    public List<AlgorithmRun> findAll() throws SQLException {
        String sql = "SELECT run_id, algorithm_name, input_size, execution_time_ns, memory_used_kb, iteration_number, run_timestamp FROM algorithm_runs ORDER BY id";
        List<AlgorithmRun> runs = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                runs.add(mapResultSet(rs));
            }
        }
        return runs;
    }

    public List<AlgorithmRun> findByAlgorithmName(String algorithmName) throws SQLException {
        String sql = "SELECT run_id, algorithm_name, input_size, execution_time_ns, memory_used_kb, iteration_number, run_timestamp FROM algorithm_runs WHERE algorithm_name = ? ORDER BY input_size, iteration_number";
        List<AlgorithmRun> runs = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, algorithmName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    runs.add(mapResultSet(rs));
                }
            }
        }
        return runs;
    }

    public AlgorithmRun save(AlgorithmRun run) throws SQLException {
        if (run == null) {
            throw new IllegalArgumentException("AlgorithmRun must not be null");
        }

        String sql = "INSERT INTO algorithm_runs (run_id, algorithm_name, input_size, execution_time_ns, memory_used_kb, iteration_number, run_timestamp) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, run.getRunId());
            stmt.setString(2, run.getAlgorithmName());
            stmt.setInt(3, run.getInputSize());
            stmt.setLong(4, run.getExecutionTimeNs());
            stmt.setDouble(5, run.getMemoryUsedKb());
            stmt.setInt(6, run.getIterationNumber());
            stmt.setString(7, run.getRunTimestamp());
            stmt.executeUpdate();
        }
        return run;
    }

    public int saveAll(List<AlgorithmRun> runs) throws SQLException {
        if (runs == null || runs.isEmpty()) {
            return 0;
        }

        String sql = "INSERT INTO algorithm_runs (run_id, algorithm_name, input_size, execution_time_ns, memory_used_kb, iteration_number, run_timestamp) VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        int count = 0;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (AlgorithmRun run : runs) {
                stmt.setString(1, run.getRunId());
                stmt.setString(2, run.getAlgorithmName());
                stmt.setInt(3, run.getInputSize());
                stmt.setLong(4, run.getExecutionTimeNs());
                stmt.setDouble(5, run.getMemoryUsedKb());
                stmt.setInt(6, run.getIterationNumber());
                stmt.setString(7, run.getRunTimestamp());
                stmt.addBatch();
                count++;
            }
            stmt.executeBatch();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw new SQLException("Failed to batch save algorithm runs: " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(autoCommit);
        }
        return count;
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM algorithm_runs";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public void deleteAll() throws SQLException {
        String sql = "DELETE FROM algorithm_runs";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    private AlgorithmRun mapResultSet(ResultSet rs) throws SQLException {
        return new AlgorithmRun(
                rs.getString("run_id"),
                rs.getString("algorithm_name"),
                rs.getInt("input_size"),
                rs.getLong("execution_time_ns"),
                rs.getDouble("memory_used_kb"),
                rs.getInt("iteration_number"),
                rs.getString("run_timestamp")
        );
    }
}
