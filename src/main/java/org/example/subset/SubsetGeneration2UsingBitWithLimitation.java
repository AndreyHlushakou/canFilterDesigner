package org.example.subset;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SubsetGeneration2UsingBitWithLimitation {

    private SubsetGeneration2UsingBitWithLimitation() {}

    public static void main(String[] args) {
        int lenSet = 27; // Длина множества
        int lenSubset = 14; // Максимальная длина подмножеств

        List<Integer> original = new ArrayList<>(lenSet);
        for (int i = 1; i <= lenSet; i++) {
            original.add(i);
        }

        long start = System.currentTimeMillis();
        subsetsWithoutRes(original, lenSubset);
        long stop = System.currentTimeMillis();

        System.out.println("time: " + (stop - start));
    }

    static <T> void subsetsWithoutRes(List<T> arr, int lenSubset) {
        int n = arr.size();

        for (int i = 0; i < (1 << n); i++) {
            // Быстрая проверка, сколько элементов в подмножестве
            if (Integer.bitCount(i) > lenSubset) {
                continue;
            }

            LinkedList<T> subset = new LinkedList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    subset.add(arr.get(j));
                }
            }
        }
    }
}
