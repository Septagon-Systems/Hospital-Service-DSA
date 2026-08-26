package edu.ug.dcit308.hospital.db.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ug.dcit308.hospital.db.ConnectionManager;
import edu.ug.dcit308.hospital.db.model.AlgorithmRun;
import edu.ug.dcit308.hospital.db.schema.SchemaInitializer;

class AlgorithmRunRepositoryTest {

    private Connection connection;
    private AlgorithmRunRepository runRepo;

    @BeforeEach
    void setUp() throws SQLException {
        connection = ConnectionManager.getInMemoryConnection();
        SchemaInitializer.initialize(connection);
        runRepo = new AlgorithmRunRepository(connection);
    }

    @Test
    void testSaveAndFindByAlgorithmName() throws SQLException {
        runRepo.save(new AlgorithmRun("UUID-1", "MergeSort", 1000, 1500000L, 45.0, 1, "2026-08-01T10:00:00Z"));
        runRepo.save(new AlgorithmRun("UUID-2", "MergeSort", 1000, 1450000L, 44.5, 2, "2026-08-01T10:00:01Z"));
        runRepo.save(new AlgorithmRun("UUID-3", "QuickSort", 1000, 800000L, 12.0, 1, "2026-08-01T10:00:02Z"));

        List<AlgorithmRun> mergeRuns = runRepo.findByAlgorithmName("MergeSort");
        assertEquals(2, mergeRuns.size());
        assertEquals("MergeSort", mergeRuns.get(0).getAlgorithmName());

        List<AlgorithmRun> quickRuns = runRepo.findByAlgorithmName("QuickSort");
        assertEquals(1, quickRuns.size());
    }

    @Test
    void testBatchSaveAll() throws SQLException {
        List<AlgorithmRun> runs = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            runs.add(new AlgorithmRun("RUN-" + i, "DijkstraShortestPath", 500, 250000L + i, 15.0, 1, "2026-08-01T12:00:00Z"));
        }

        int inserted = runRepo.saveAll(runs);
        assertEquals(50, inserted);
        assertEquals(50, runRepo.count());
    }
}
