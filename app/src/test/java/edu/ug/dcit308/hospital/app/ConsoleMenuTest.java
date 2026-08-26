package edu.ug.dcit308.hospital.app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleMenuTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        outContent.reset();
        System.setOut(new PrintStream(outContent, true));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testMenuExit() {
        Scanner scanner = new Scanner("0\n");
        ConsoleMenu menu = new ConsoleMenu(scanner);
        menu.run();

        assertTrue(outContent.toString().contains("Exiting application..."));
    }

    @Test
    void testMenuOptionSelection() {
        Scanner scanner = new Scanner("99\n0\n");
        ConsoleMenu menu = new ConsoleMenu(scanner);
        menu.run();

        assertTrue(outContent.toString().contains("Invalid option. Please choose 0-6."));
    }
}