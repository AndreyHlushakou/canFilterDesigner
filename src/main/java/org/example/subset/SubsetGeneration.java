package org.example.subset;

import java.util.ArrayList;
import java.util.List;

import static org.example.subset.UtilsSubset.*;

public final class SubsetGeneration {

    private SubsetGeneration() {}

    public static void main(String[] args) {
        System.out.println("SubsetGeneration");
        int lenSet = 10;


        List<Integer> original = getListByLen(lenSet);
//        calculationTimeAndPrintfResult(() -> subsets(original));
        calculationTime.accept(() -> subsetsWithoutRes(original));
    }

    static <T> List<List<T>> subsets(List<T> original) {
        List<T> subset = new ArrayList<>();
        List<List<T>> res = new ArrayList<>(1<<original.size());
        subsetRecur(0, original, res, subset);
        return res;
    }

    static <T> void subsetsWithoutRes(List<T> original) {
        List<T> subset = new ArrayList<>();
        subsetRecurWithoutRes(0, original, subset);
    }

    static <T> void subsetRecur(int index, List<T> original, List<List<T>> res, List<T> subset) {

        // add subset at end of array
        if (index == original.size()) {
            res.add(new ArrayList<>(subset));
            return;
        }

        // include the current value and recursively find all subsets
        subset.add(original.get(index));
        subsetRecur(index + 1, original, res, subset);

        // exclude the current value and recursively find all subsets
        subset.remove(subset.size() - 1);
        subsetRecur(index + 1, original, res, subset);
    }

    static <T> void subsetRecurWithoutRes(int index, List<T> original, List<T> subset) {

        // add subset at end of array
        if (index == original.size()) {
//            res.add(new ArrayList<>(subset));
            return;
        }

        // include the current value and recursively find all subsets
        subset.add(original.get(index));
        subsetRecurWithoutRes(index + 1, original, subset);

        // exclude the current value and recursively find all subsets
        subset.remove(subset.size() - 1);
        subsetRecurWithoutRes(index + 1, original, subset);
    }

}
