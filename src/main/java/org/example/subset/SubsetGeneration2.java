package org.example.subset;

import java.util.ArrayList;
import java.util.List;

public final class SubsetGeneration2 {

    private SubsetGeneration2() {}

    public static void main(String[] args) {
        int lenSet = 32; // Длина множества
        int lenSubset = 14; // Максимальная длина подмножеств

        List<Integer> original = new ArrayList<>(lenSet);
        for (int i = 1; i <= lenSet; i++) {
            original.add(i);
        }

        long start = System.currentTimeMillis();
//        List<List<Integer>> res = subsets(original, lenSubset);
        subsetsWithoutRes(original, lenSubset);
        long stop = System.currentTimeMillis();
//        printf(res);

        System.out.println("time: " + (stop - start));
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

    static <T> void printf(List<List<T>> res) {
        for (List<T> subset : res) {
            for (T num : subset) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }
}
