package edu.ug.dcit308.hospital.app;

import edu.ug.dcit308.hospital.app.util.StudentParameterCalculator;

/**
 * Main Entry point for Ghana Smart Hospital Operations Optimizer Application.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("   GHANA SMART HOSPITAL OPERATIONS OPTIMIZER (DCIT 204/308)  ");
        System.out.println("=============================================================");

        if (args.length > 0) {
            System.out.println(StudentParameterCalculator.calculate(args[0]));
        } else {
            System.out.println("\n[Console Menu System Initializing...]");
            System.out.println("1. Load Ghana Hospital Data from Database");
            System.out.println("2. Run Custom Search & Sort Demonstrations");
            System.out.println("3. Run Ambulance Shortest Route Optimizer (Dijkstra)");
            System.out.println("4. Run Emergency Dispatch Queue System");
            System.out.println("5. Execute Empirical Performance Benchmarks");
            System.out.println("0. Exit Application");
        }
    }
}
