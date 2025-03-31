package org.example.subset;

import java.util.LinkedList;
import java.util.List;

import static org.example.subset.UtilsSubset.*;

public class SubsetGeneration2UsingBitWithLimitation {

    private SubsetGeneration2UsingBitWithLimitation() {}

    public static void main(String[] args) {
        int lenSet = 27; // Длина множества
        int lenSubset = 14; // Максимальная длина подмножеств

        List<Integer> original = getListByLen(lenSet);
        calculationTime.accept(() -> subsetsWithoutRes(original, lenSubset));
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
