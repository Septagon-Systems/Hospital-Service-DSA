package edu.ug.dcit308.hospital.app;

import java.util.Scanner;

public class ConsoleMenu {

    private final Scanner scanner;

    public ConsoleMenu() {
        scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println();
        System.out.println("=============================================================");
        System.out.println("              HOSPITAL OPERATIONS MENU");
        System.out.println("=============================================================");
        System.out.println("1. Load Ghana Hospital Data from Database");
        System.out.println("2. Run Custom Search & Sort Demonstrations");
        System.out.println("3. Run Ambulance Shortest Route Optimizer (Dijkstra)");
        System.out.println("4. Run Emergency Dispatch Queue System");
        System.out.println("5. Execute Empirical Performance Benchmarks");
        System.out.println("0. Exit Application");
        System.out.println("=============================================================");
        System.out.print("Enter your choice: ");
    }

    public void handleSelection(int option) {
        switch (option) {
            case 1:
                RunDataLoader.execute();
                break;

            case 2:
                RunSearchDemo.execute();
                RunSortDemo.execute();
                break;

            case 3:
                RunRouteDemo.execute();
                break;

            case 4:
                RunDispatchDemo.execute();
                break;

            case 5:
                RunBenchmarkDemo.execute();
                break;

            case 0:
                System.out.println("Exiting application...");
                break;

            default:
                System.out.println("Invalid option. Please choose 0-5.");
        }
    }

    public void run() {
        int option = -1;

        do {
            displayMenu();

            if (!scanner.hasNextLine()) {
                System.out.println("No input received. Exiting application...");
                break;
            }

            String input = scanner.nextLine().trim();

            try {
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number from 0 to 5.");
                continue;
            }

            handleSelection(option);

        } while (option != 0);
    }
}