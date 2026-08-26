package edu.ug.dcit308.hospital.app;

import java.util.Scanner;

import edu.ug.dcit308.hospital.app.util.StudentParameterCalculator;

/**
 * Interactive Console Command Menu for the Ghana Smart Hospital Operations Optimizer (DCIT 204 / DCIT 308).
 */
public class ConsoleMenu {

    private final Scanner scanner;

    public ConsoleMenu() {
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println();
        System.out.println("=============================================================");
        System.out.println("   GHANA HEALTH SERVICE — SMART OPERATIONS OPTIMIZER (DCIT 204 / 308)");
        System.out.println("=============================================================");
        System.out.println("  [1] System & Database Status (Overview of Facilities, Routes & Seed Data)");
        System.out.println("  [2] Hospital Search & Multi-Index Lab (Hash Table, Red-Black, BST, B-Tree)");
        System.out.println("  [3] Ambulance Shortest Route Navigator (Dijkstra Highway Pathfinding)");
        System.out.println("  [4] Regional Highway Corridor Backbone (Kruskal & Prim MST)");
        System.out.println("  [5] Live Emergency Dispatch Simulator (Multi-Queue Priority & Stack Undo)");
        System.out.println("  [6] Medical Supply Payload Optimizer (0/1 Knapsack Dynamic Programming)");
        System.out.println("  [7] Empirical Algorithm Benchmark Lab (On-Demand Performance Testing)");
        System.out.println("  [8] Student Index Parameter Calculator (Coursework Formula Derivation)");
        System.out.println("  [0] Exit Application");
        System.out.println("=============================================================");
        System.out.print("Enter your choice: ");
    }

    public void handleSelection(int option) {
        switch (option) {
            case 1:
                RunDataLoader.execute(scanner);
                break;
            case 2:
                RunSearchDemo.execute(scanner);
                break;
            case 3:
            case 4:
                RunRouteDemo.execute(scanner);
                break;
            case 5:
                RunDispatchDemo.execute(scanner);
                break;
            case 6:
                RunOptimizerDemo.execute(scanner);
                break;
            case 7:
                RunBenchmarkDemo.execute(scanner);
                break;
            case 8:
                runStudentIdCalculator();
                break;
            case 0:
                System.out.println("\nExiting Ghana Smart Hospital Operations Optimizer. Goodbye!");
                break;
            default:
                System.out.println("Invalid option. Please choose a number from 0 to 8.");
        }
    }

    private void runStudentIdCalculator() {
        System.out.println();
        System.out.println("=============================================================");
        System.out.println("   STUDENT INDEX NUMBER — ALGORITHM PARAMETER CALCULATOR     ");
        System.out.println("=============================================================");
        System.out.print("Enter Student Index Number (e.g. 10987654): ");
        String idStr = scanner.nextLine().trim();
        if (idStr.isEmpty()) {
            System.out.println("! Student ID cannot be empty.");
            return;
        }
        try {
            System.out.println(StudentParameterCalculator.calculate(idStr));
        } catch (Exception e) {
            System.out.println("! Invalid student ID format. Please enter numerical digits.");
        }
        System.out.println("=============================================================");
    }

    public void run() {
        int option = -1;
        do {
            displayMenu();
            if (!scanner.hasNextLine()) {
                break;
            }

            String input = scanner.nextLine().trim();
            try {
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid menu number (0 - 8).");
                continue;
            }

            handleSelection(option);
        } while (option != 0);
    }
}