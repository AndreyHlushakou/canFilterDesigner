package org.example.subset;

import java.util.ArrayList;
import java.util.List;

import static org.example.subset.UtilsSubset.*;

public final class SubsetGenerationWithLimitation {

    private SubsetGenerationWithLimitation() {}

    public static void main(String[] args) {
        System.out.println("SubsetGenerationWithLimitation");
        int lenSet = 30; // Длина множества
        int lenSubset = 14; // Максимальная длина подмножеств

        List<Integer> original = getListByLen(lenSet);
//        calculationTimeAndPrintfResult(() -> subsets(original, lenSubset));
        calculationTime.accept(() -> subsetsWithoutRes(original, lenSubset));
    }

    static <T> List<List<T>> subsets(List<T> original, int lenSubset) {
        List<T> subset = new ArrayList<>(lenSubset);
        List<List<T>> res = new ArrayList<>(1 << original.size()); // ==2^original.size()
        subsetRecur(0, original, res, subset, lenSubset);
        return res;
    }

    static <T> void subsetRecur(int i, List<T> original, List<List<T>> res, List<T> subset, int lenSubset) {

        // Добавляем подмножество, если оно не пустое
        if (!subset.isEmpty()) {
            res.add(new ArrayList<>(subset));
        }

        // Если уже достигли максимальной длины, возвращаемся
        if (subset.size() == lenSubset) {
            return;
        }

        // Перебираем оставшиеся элементы
        for (int j = i; j < original.size(); j++) {
            subset.add(original.get(j));
            subsetRecur(j + 1, original, res, subset, lenSubset);
            subset.remove(subset.size() - 1); // Убираем последний элемент (backtracking)
        }
    }

    static <T> void subsetsWithoutRes(List<T> original, int lenSubset) {
        List<T> subset = new ArrayList<>(lenSubset);
        subsetRecurWithoutRes(0, original, subset, lenSubset);
    }

    static <T> void subsetRecurWithoutRes(int i, List<T> original, List<T> subset, int lenSubset) {

        // Добавляем подмножество, если оно не пустое
        if (!subset.isEmpty()) {
//            System.out.println(subset);
        }

        // Если уже достигли максимальной длины, возвращаемся
        if (subset.size() == lenSubset) {
            return;
        }

        // Перебираем оставшиеся элементы
        for (int j = i; j < original.size(); j++) {
            subset.add(original.get(j));
            subsetRecurWithoutRes(j + 1, original, subset, lenSubset);
            subset.remove(subset.size() - 1); // Убираем последний элемент (backtracking)
        }
    }

}
