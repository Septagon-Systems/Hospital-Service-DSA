package edu.ug.dcit308.hospital.app;

import edu.ug.dcit308.hospital.algo.search.BinarySearch;
import edu.ug.dcit308.hospital.algo.search.LinearSearch;

import java.util.Arrays;

public class RunSearchDemo {

    public static void execute() {

        System.out.println();
        System.out.println("========== SEARCH DEMONSTRATION ==========");

        Integer[] numbers = {11, 12, 22, 25, 64};

        System.out.println("Array: " + Arrays.toString(numbers));
        System.out.println("Searching for: 22");

        int linearResult = LinearSearch.search(numbers, 22);
        System.out.println("Linear Search result: index " + linearResult);

        BinarySearch binarySearch = new BinarySearch();
        int binaryResult = binarySearch.search(numbers, 22);
        System.out.println("Binary Search result: index " + binaryResult);

        System.out.println("===========================================");
    }
}
