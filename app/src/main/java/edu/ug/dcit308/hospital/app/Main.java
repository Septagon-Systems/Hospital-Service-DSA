package edu.ug.dcit308.hospital.app;

import edu.ug.dcit308.hospital.app.util.StudentParameterCalculator;

public class Main {

    public static void main(String[] args) {
        System.out.println("   GHANA SMART HOSPITAL OPERATIONS OPTIMIZER (DCIT 204/308)  ");
        System.out.println("=============================================================");

        if (args.length > 0) {
            System.out.println(StudentParameterCalculator.calculate(args[0]));
        } else {
            ConsoleMenu menu = new ConsoleMenu();
            menu.run();
        }
    }
}