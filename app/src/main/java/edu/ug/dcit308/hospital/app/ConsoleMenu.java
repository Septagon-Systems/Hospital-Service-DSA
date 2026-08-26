package edu.ug.dcit308.hospital.app;

import java.util.Scanner;

/**
 * Interactive Console Command Menu for the Ghana Smart Hospital Operations Optimizer.
 */
public class ConsoleMenu {

    private final Scanner scanner;

    public ConsoleMenu() {
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println();
        System.out.println("=============================================================");
        System.out.println("   GHANA HEALTH SERVICE — SMART OPERATIONS OPTIMIZER (DCIT 308)");
        System.out.println("=============================================================");
        System.out.println("  [1] System & Database Status (Overview of Facilities, Routes & Seed Data)");
        System.out.println("  [2] Hospital Search & Inventory Indexing (Red-Black Tree & Hash Table)");
        System.out.println("  [3] Ambulance Shortest Route Navigator (Dijkstra Highway Pathfinding)");
        System.out.println("  [4] Regional Highway Corridor Backbone (Kruskal & Prim MST)");
        System.out.println("  [5] Live Emergency Dispatch Simulator (Multi-Queue Priority & Stack Undo)");
        System.out.println("  [6] Medical Supply Payload Optimizer (0/1 Knapsack Dynamic Programming)");
        System.out.println("  [7] Empirical Algorithm Benchmark Lab (On-Demand Performance Testing)");
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
            case 0:
                System.out.println("\nExiting Ghana Smart Hospital Operations Optimizer. Goodbye!");
                break;
            default:
                System.out.println("Invalid option. Please choose a number from 0 to 7.");
        }
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
                System.out.println("Invalid input. Please enter a valid menu number (0 - 7).");
                continue;
            }

            handleSelection(option);
        } while (option != 0);
    }
}