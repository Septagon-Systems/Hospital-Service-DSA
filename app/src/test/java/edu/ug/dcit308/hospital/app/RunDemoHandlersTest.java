package edu.ug.dcit308.hospital.app;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class RunDemoHandlersTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testRunDataLoader() {
        assertDoesNotThrow(() -> RunDataLoader.execute(new Scanner("0\n")));
    }

    @Test
    void testRunSearchDemo() {
        assertDoesNotThrow(() -> RunSearchDemo.execute(new Scanner("0\n")));
    }

    @Test
    void testRunRouteDemo() {
        assertDoesNotThrow(() -> RunRouteDemo.execute(new Scanner("0\n")));
    }

    @Test
    void testRunDispatchDemo() {
        assertDoesNotThrow(() -> RunDispatchDemo.execute(new Scanner("0\n")));
    }

    @Test
    void testRunOptimizerDemo() {
        assertDoesNotThrow(() -> RunOptimizerDemo.execute(new Scanner("35.0\n")));
    }

    @Test
    void testRunBenchmarkDemo() {
        assertDoesNotThrow(() -> RunBenchmarkDemo.execute(new Scanner("0\n")));
    }
}
