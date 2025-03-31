package org.example.subset;

import java.util.ArrayList;
import java.util.List;

public class SubsetGeneration2UsingBit {

    private SubsetGeneration2UsingBit() {}

    public static void main(String[] args) {
        System.out.println("SubsetGenerationWithLimitation");
        int lenSet = 27; // Длина множества


        List<Integer> original = new ArrayList<>(lenSet);
        for (int i = 1; i <= lenSet; i++) {
            original.add(i);
        }

        long start = System.currentTimeMillis();
//        List<List<Integer>> res = subsets(original);
        subsetsWithoutRes(original);
        long stop = System.currentTimeMillis();
//        printf(res);
        System.out.println("time: " + (stop - start));
    }

    // Function to find the subsets of the given array
    static <T> List<List<T>> subsetsWithRes(List<T> arr) {
        int n = arr.size();
        List<List<T>> res = new ArrayList<>();

        // Loop through all possible
        // subsets using bit manipulation
        for (int i = 0; i < (1 << n); i++) {
            List<T> subset = new ArrayList<>();

            // Loop through all elements
            // of the input array
            for (int j = 0; j < n; j++) {

                // Check if the jth bit is
                // set in the current subset
                if ((i & (1 << j)) != 0) {

                    // If the jth bit is set, add
                    // the jth element to the subset
                    subset.add(arr.get(j));
                }
            }

            // Push the subset into result
            res.add(subset);
        }

        return res;
    }

    // Function to find the subsets of the given array
    static <T> void subsetsWithoutRes(List<T> arr) {
        int n = arr.size();

        // Loop through all possible
        // subsets using bit manipulation
        for (int i = 0; i < (1 << n); i++) {
            List<T> subset = new ArrayList<>();

            // Loop through all elements
            // of the input array
            for (int j = 0; j < n; j++) {

                // Check if the jth bit is
                // set in the current subset
                if ((i & (1 << j)) != 0) {

                    // If the jth bit is set, add
                    // the jth element to the subset
                    subset.add(arr.get(j));
                }
            }

            // Push the subset into result
//            res.add(subset);
        }


    }

    static <T> void printf(List<List<T>> res) {
        for (List<T> subset : res) {
            for (T num : subset) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }

}
