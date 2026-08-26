package edu.ug.dcit308.hospital.app;

import edu.ug.dcit308.hospital.ds.list.DynamicArrayList;
import edu.ug.dcit308.hospital.ds.stack.ArrayStack;

import java.util.Scanner;

public class ConsoleMenu {

    private final Scanner scanner;
    private final ArrayStack<String> actionHistory;
    private DynamicArrayList<RunDataLoader.Hospital> loadedDataset;

    public ConsoleMenu() {
        this(new Scanner(System.in));
    }

    public ConsoleMenu(Scanner scanner) {
        this.scanner = scanner;
        this.actionHistory = new ArrayStack<>();
        this.loadedDataset = null;
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
        System.out.println("6. Undo Last Action");
        System.out.println("0. Exit Application");
        System.out.println("=============================================================");

        if (!actionHistory.isEmpty()) {
            System.out.println("Last Action Performed: " + actionHistory.peek());
            System.out.println("-------------------------------------------------------------");
        }

        System.out.print("Enter your choice (0-6): ");
    }

    public void handleSelection(int option) {
        switch (option) {
            case 1:
                this.loadedDataset = RunDataLoader.loadData();
                int recordCount = (loadedDataset != null) ? loadedDataset.size() : 0;
                actionHistory.push("Loaded Ghana Hospital Dataset (" + recordCount + " records)");
                break;

            case 2:
                if (ensureDatasetLoaded()) {
                    RunSearchDemo.execute(loadedDataset);
                    RunSortDemo.execute(loadedDataset);
                    actionHistory.push("Executed Search & Sort Demos");
                }
                break;

            case 3:
                if (ensureDatasetLoaded()) {
                    RunRouteDemo.execute(loadedDataset);
                    actionHistory.push("Executed Route Optimization Demo");
                }
                break;

            case 4:
                if (ensureDatasetLoaded()) {
                    RunDispatchDemo.execute(loadedDataset);
                    actionHistory.push("Executed Dispatch Queue Demo");
                }
                break;

            case 5:
                if (ensureDatasetLoaded()) {
                    RunBenchmarkDemo.execute(loadedDataset);
                    actionHistory.push("Executed Performance Benchmarks");
                }
                break;

            case 6:
                if (!actionHistory.isEmpty()) {
                    String undoneAction = actionHistory.pop();
                    System.out.println("[UNDO] Reverted last action: " + undoneAction);
                } else {
                    System.out.println("[!] No actions to undo.");
                }
                break;

            case 0:
                System.out.println("Exiting application...");
                break;

            default:
                System.out.println("Invalid option. Please choose 0-6.");
        }
    }

    private boolean ensureDatasetLoaded() {
        if (loadedDataset == null || loadedDataset.isEmpty()) {
            System.out.println("[!] Dataset empty or not loaded. Fetching default records...");
            this.loadedDataset = RunDataLoader.loadData();
        }
        return loadedDataset != null;
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
                System.out.println("Invalid input. Please enter a number from 0 to 6.");
                continue;
            }

            handleSelection(option);

        } while (option != 0);
    }
}