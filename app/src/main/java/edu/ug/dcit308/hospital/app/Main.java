package edu.ug.dcit308.hospital.app;

import edu.ug.dcit308.hospital.app.util.StudentParameterCalculator;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            System.out.println(StudentParameterCalculator.calculate(args[0]));
        } else {
            System.out.println("Usage: ./gradlew :app:run --args=\"<studentId>\"");
        }
    }
}
