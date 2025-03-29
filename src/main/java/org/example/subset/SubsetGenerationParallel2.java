package org.example.subset;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public final class SubsetGenerationParallel2 {

    private SubsetGenerationParallel2() {}

    public static void main(String[] args) {
        int lenSet = 29; // Длина множества
        int lenSubset = 14; // Максимальная длина подмножеств

        List<Integer> original = new ArrayList<>(lenSet);
        for (int i = 1; i <= lenSet; i++) {
            original.add(i);
        }

        long start = System.currentTimeMillis();

        ForkJoinPool pool =
                new ForkJoinPool(Runtime.getRuntime().availableProcessors());
//                ForkJoinPool.commonPool();
//                new ForkJoinPool(); // Пул потоков

//        SubsetTask<Integer> subsetTask = new SubsetTask<>(original, 0, lenSubset, new ArrayList<>());
//        List<List<Integer>> res = pool.invoke(subsetTask);
//        printf(res);

        SubsetTask2<Integer> subsetTask = new SubsetTask2<>(original, 0, lenSubset, new ArrayList<>());
        pool.invoke(subsetTask);

        long stop = System.currentTimeMillis();
        System.out.println("time: " + (stop - start));

         pool.shutdown(); // Можно явно закрыть пул, но ForkJoinPool закрывается сам.
    }

    static class SubsetTask <T> extends RecursiveTask<List<List<T>>> {
        private final List<T> original;
        private final int start;
        private final int lenSubset;
        private final List<T> subset;

        public SubsetTask(List<T> original, int start, int lenSubset, List<T> subset) {
            this.original = original;
            this.start = start;
            this.lenSubset = lenSubset;
            this.subset = new ArrayList<>(subset); // Создаем копию, чтобы избежать конфликтов между потоками
        }

        @Override
        protected List<List<T>> compute() {
            List<List<T>> res = new ArrayList<>();

            // Добавляем текущее подмножество, если оно не пустое
            if (!subset.isEmpty()) {
                res.add(new ArrayList<>(subset));
            }

            // Если достигли лимита длины, выходим
            if (subset.size() == lenSubset) {
                return res;
            }

            // Перебираем оставшиеся элементы
            List<SubsetTask<T>> subTasks = new ArrayList<>();
            for (int i = start; i < original.size(); i++) {
                List<T> newSubset = new ArrayList<>(subset);
                newSubset.add(original.get(i));

                SubsetTask<T> task = new SubsetTask<>(original, i + 1, lenSubset, newSubset);
                task.fork(); // Запускаем подзадачу в отдельном потоке
                subTasks.add(task);
            }

            // Собираем результаты всех потоков
            for (SubsetTask<T> task : subTasks) {
                res.addAll(task.join());
            }

            return res;
        }
    }

    static class SubsetTask2 <T> extends RecursiveTask<Boolean> {
        private final List<T> original;
        private final int start;
        private final int lenSubset;
        private final List<T> subset;

        public SubsetTask2(List<T> original, int start, int lenSubset, List<T> subset) {
            this.original = original;
            this.start = start;
            this.lenSubset = lenSubset;
            this.subset = new ArrayList<>(subset); // Создаем копию, чтобы избежать конфликтов между потоками
        }

        @Override
        protected Boolean compute() {

            // Добавляем текущее подмножество, если оно не пустое
            if (!subset.isEmpty()) {
//                System.out.println(subset);
            }

            // Если достигли лимита длины, выходим
            if (subset.size() == lenSubset) {
                return true;
            }

            // Перебираем оставшиеся элементы
            List<SubsetTask2<T>> subTasks = new ArrayList<>();
            for (int i = start; i < original.size(); i++) {
                List<T> newSubset = new ArrayList<>(subset);
                newSubset.add(original.get(i));

                SubsetTask2<T> task = new SubsetTask2<>(original, i + 1, lenSubset, newSubset);
                task.fork(); // Запускаем подзадачу в отдельном потоке
                subTasks.add(task);
            }

            // Собираем результаты всех потоков
            for (SubsetTask2<T> task : subTasks) {
                task.join();
            }

            return true;
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

