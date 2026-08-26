package edu.ug.dcit308.hospital.app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ConsoleMenuTest {

    @Test
    void testMenuExit() {
        String simulatedInput = "0\n";
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try {
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));

            ConsoleMenu menu = new ConsoleMenu();
            menu.run();

            String output = out.toString();
            assertTrue(output.contains("GHANA HEALTH SERVICE"));
            assertTrue(output.contains("Exiting Ghana Smart Hospital Operations Optimizer"));
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    void testMenuOptionSelection() {
        ConsoleMenu menu = new ConsoleMenu();
        // Test that handleSelection does not throw on valid or invalid inputs
        assertDoesNotThrow(() -> menu.handleSelection(0));
        assertDoesNotThrow(() -> menu.handleSelection(99));
    }
}
