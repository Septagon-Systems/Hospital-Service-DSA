package edu.ug.dcit308.hospital.algo.sort;

public class SelectionSort<T extends Comparable<T>> {

    private int comparisonCount;
    private int swapCount;

    public void sort(T[] array) {
        comparisonCount = 0;
        swapCount = 0;

        for (int i = 0; i < array.length - 1; i++) {
            int minimumIndex = i;

            for (int j = i + 1; j < array.length; j++) {
                comparisonCount++;

                if (array[j].compareTo(array[minimumIndex]) < 0) {
                    minimumIndex = j;
                }
            }

            if (minimumIndex != i) {
                T temporary = array[i];
                array[i] = array[minimumIndex];
                array[minimumIndex] = temporary;
                swapCount++;
            }
        }
    }

    public int getComparisonCount() {
        return comparisonCount;
    }

    public int getSwapCount() {
        return swapCount;
    }
}