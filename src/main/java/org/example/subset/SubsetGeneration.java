package org.example.subset;

import java.util.ArrayList;
import java.util.List;

public final class SubsetGeneration {

    private SubsetGeneration() {}

    public static void main(String[] args) {
        int lenSet = 25;


        List<Integer> original = new ArrayList<>(lenSet);
        for (int i = 1; i <= lenSet; i++) {
            original.add(i);
        }

        long start = System.currentTimeMillis();
        List<List<Integer>> res = subsets(original);
        long stop = System.currentTimeMillis();
//        printf(res);
        System.out.println("time: " + (stop - start));
    }

    static <T> List<List<T>> subsets(List<T> original) {
        List<T> subset = new ArrayList<>();
        List<List<T>> res = new ArrayList<>(1<<original.size());
        subsetRecur(0, original, res, subset);
        return res;
    }

    static <T> void subsetRecur(int i, List<T> original, List<List<T>> res, List<T> subset) {

        // add subset at end of array
        if (i == original.size()) {
            res.add(new ArrayList<>(subset));
            return;
        }

        // include the current value and recursively find all subsets
        subset.add(original.get(i));
        subsetRecur(i + 1, original, res, subset);

        // exclude the current value and recursively find all subsets
        subset.remove(subset.size() - 1);
        subsetRecur(i + 1, original, res, subset);
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
