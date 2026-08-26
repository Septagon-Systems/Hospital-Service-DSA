package edu.ug.dcit308.hospital.app;

import edu.ug.dcit308.hospital.ds.list.DynamicArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class RunDemoHandlersTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private DynamicArrayList<RunDataLoader.Hospital> dataset;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        dataset = RunDataLoader.loadData();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testRunDataLoader() {
        assertDoesNotThrow(() -> RunDataLoader.execute());
    }

    @Test
    void testRunSearchDemo() {
        assertDoesNotThrow(() -> RunSearchDemo.execute(dataset));
    }

    @Test
    void testRunRouteDemo() {
        assertDoesNotThrow(() -> RunRouteDemo.execute(dataset));
    }

    @Test
    void testRunDispatchDemo() {
        assertDoesNotThrow(() -> RunDispatchDemo.execute(dataset));
    }

    @Test
    void testRunOptimizerDemo() {
        assertDoesNotThrow(() -> RunOptimizerDemo.execute(new Scanner("35.0\n")));
    }

    @Test
    void testRunBenchmarkDemo() {
        assertDoesNotThrow(() -> RunBenchmarkDemo.execute(dataset));
    }
}