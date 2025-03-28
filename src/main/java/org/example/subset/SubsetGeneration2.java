package org.example.subset;

import java.util.ArrayList;
import java.util.List;

public final class SubsetGeneration2 {

    private SubsetGeneration2() {}

    public static void main(String[] args) {
        int lenSet = 5; // Длина множества
        int lenSubset = 3; // Максимальная длина подмножеств

        List<Integer> original = new ArrayList<>(lenSet);
        for (int i = 1; i <= lenSet; i++) {
            original.add(i);
        }

        long start = System.currentTimeMillis();
        List<List<Integer>> res = subsets(original, lenSubset);
        long stop = System.currentTimeMillis();
        printf(res);
        System.out.println("time: " + (stop - start));
    }

    static <T> List<List<T>> subsets(List<T> original, int lenSubset) {
        List<T> subset = new ArrayList<>();
        List<List<T>> res = new ArrayList<>();
        subsetRecur(0, original, res, subset, lenSubset);
        return res;
    }

    static <T> void subsetRecur(int i, List<T> original, List<List<T>> res, List<T> subset, int lenSubset) {

        // Добавляем подмножество, если оно не пустое и не превышает lenSubset
        if (!subset.isEmpty() && subset.size() <= lenSubset) {
            res.add(new ArrayList<>(subset));
        }

        // Если достигли конца или подмножество уже lenSubset, останавливаемся
        if (i == original.size() || subset.size() == lenSubset) {
            return;
        }

        // Включаем текущий элемент
        subset.add(original.get(i));
        subsetRecur(i + 1, original, res, subset, lenSubset);

        // Исключаем текущий элемент (backtracking)
        subset.remove(subset.size() - 1);
        subsetRecur(i + 1, original, res, subset, lenSubset);
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
