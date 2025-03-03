package org.example.subset;

import java.util.ArrayList;
import java.util.List;

public final class SubsetGeneration {

    private SubsetGeneration() {}


//    public static void main(String[] args) {
//        int[] arr = {1, 2, 3};
//        Set<Set<Integer>> res = subsets(arr);
//        printf(res);
//    }

    static List<List<Integer>> subsets(int[] arr) {
        List<Integer> subset = new ArrayList<>();
        List<List<Integer>> res = new ArrayList<>(1<<arr.length);
        subsetRecur(0, arr, res, subset);
        return res;
    }

    static void subsetRecur(int i, int[] arr, List<List<Integer>> res, List<Integer> subset) {

        // add subset at end of array
        if (i == arr.length) {
            res.add(new ArrayList<>(subset));
            return;
        }

        // include the current value and
        // recursively find all subsets
        subset.add(arr[i]);
        subsetRecur(i + 1, arr, res, subset);

        // exclude the current value and
        // recursively find all subsets
        subset.remove(subset.size() - 1);
        subsetRecur(i + 1, arr, res, subset);
    }



    static void printf(List<List<Integer>> res) {
        for (List<Integer> subset : res) {
            for (int num : subset) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }

}
