package org.example.subset;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

public class UtilsSubset {

    private UtilsSubset() {}

    public static List<Integer> getListByLen(int lenSet) {
        List<Integer> original = new ArrayList<>(lenSet);
        for (int i = 1; i <= lenSet; i++) {
            original.add(i);
        }
        return original;
    }

    public static <T> void calculationTimeAndPrintfResult(Supplier<List<List<T>>> voidConsumer) {
        final long[] time = {0};
        List<List<T>> res = UtilsSubset.<T>calculationTimeAndGetResult(time).apply(voidConsumer);
        printf(res);
        System.out.println("Time: " + time[0] + " ms");
    }

    interface VoidConsumer <T>{
        void execute();
    }

    public static Consumer<VoidConsumer> calculationTime = (function) -> {
        long start = System.currentTimeMillis();
        function.execute();
        long stop = System.currentTimeMillis();

        System.out.println("Time: " + (stop - start) + " ms");
    };

    public static <T> Function<Supplier<List<List<T>>>, List<List<T>>> calculationTimeAndGetResult(final long[] time) {
        return (function) -> {
            long start = System.currentTimeMillis();
            List<List<T>> res = function.get();
            long stop = System.currentTimeMillis();

            time[0] = (stop - start);
            return res;
        };
    }


    public static <T> void printf(List<List<T>> res) {
        for (List<T> subset : res) {
            for (T num : subset) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }

}
