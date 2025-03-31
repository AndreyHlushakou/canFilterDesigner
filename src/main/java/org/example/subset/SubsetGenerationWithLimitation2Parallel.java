package org.example.subset;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public final class SubsetGenerationWithLimitation2Parallel {

    private SubsetGenerationWithLimitation2Parallel() {}

    public static void main(String[] args) {
        System.out.println("SubsetGenerationWithLimitation");
        int lenSet = 30; // Длина множества
        int lenSubset = 14; // Максимальная длина подмножеств

        List<Integer> original = new ArrayList<>(lenSet);
        for (int i = 1; i <= lenSet; i++) {
            original.add(i);
        }

        ForkJoinPool pool =
//                new ForkJoinPool(Runtime.getRuntime().availableProcessors());
//                ForkJoinPool.commonPool();
                new ForkJoinPool(); // Пул потоков

//        SubsetTask<Integer> subsetTask = new SubsetTask<>(original, 0, lenSubset, new ArrayList<>());
        SubsetTaskWithoutRes<Integer> subsetTask = new SubsetTaskWithoutRes<>(original, 0, lenSubset, new ArrayList<>());
//        SubsetTaskWithLimitationThreads subsetTask= new SubsetTaskWithLimitationThreads(original, 0, lenSubset, new ArrayList<>(), 0);
        long start = System.currentTimeMillis();
//        List<List<Integer>> res = pool.invoke(subsetTask);
        pool.invoke(subsetTask);
        long stop = System.currentTimeMillis();
//        printf(res);
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

    static class SubsetTaskWithoutRes<T> extends RecursiveAction {
        private final List<T> original;
        private final int start;
        private final int lenSubset;
        private final List<T> subset;

        public SubsetTaskWithoutRes(List<T> original, int start, int lenSubset, List<T> subset) {
            this.original = original;
            this.start = start;
            this.lenSubset = lenSubset;
            this.subset = new ArrayList<>(subset); // Создаем копию, чтобы избежать конфликтов между потоками
        }

        @Override
        protected void compute() {

            // Добавляем текущее подмножество, если оно не пустое
            if (!subset.isEmpty()) {
//                System.out.println(subset);
            }

            // Если достигли лимита длины, выходим
            if (subset.size() == lenSubset) {
                return;
            }

            // Перебираем оставшиеся элементы
            List<SubsetTaskWithoutRes<T>> subTasks = new ArrayList<>();
            for (int i = start; i < original.size(); i++) {
                List<T> newSubset = new ArrayList<>(subset);
                newSubset.add(original.get(i));

                SubsetTaskWithoutRes<T> task = new SubsetTaskWithoutRes<>(original, i + 1, lenSubset, newSubset);
                task.fork(); // Запускаем подзадачу в отдельном потоке
                subTasks.add(task);
            }

            // Собираем результаты всех потоков
            for (SubsetTaskWithoutRes<T> task : subTasks) {
                task.join();
            }

        }
    }

    private static final int PARALLEL_DEPTH = 3;

    static class SubsetTaskWithLimitationThreads extends RecursiveAction {
        private final List<Integer> original;
        private final int start;
        private final int lenSubset;
        private final List<Integer> subset;
        private final int depth; // Глубина рекурсии

        public SubsetTaskWithLimitationThreads(List<Integer> original, int start, int lenSubset, List<Integer> subset, int depth) {
            this.original = original;
            this.start = start;
            this.lenSubset = lenSubset;
            this.subset = new ArrayList<>(subset);
            this.depth = depth;
        }

        @Override
        protected void compute() {
            if (!subset.isEmpty()) {
                // System.out.println(subset); // Можно раскомментировать для проверки
            }

            if (subset.size() == lenSubset) {
                return;
            }

            List<SubsetTaskWithLimitationThreads> subTasks = new ArrayList<>();
            for (int i = start; i < original.size(); i++) {
                List<Integer> newSubset = new ArrayList<>(subset);
                newSubset.add(original.get(i));

                if (depth < PARALLEL_DEPTH) {
                    // Параллелим только первые уровни рекурсии
                    SubsetTaskWithLimitationThreads task = new SubsetTaskWithLimitationThreads(original, i + 1, lenSubset, newSubset, depth + 1);
                    subTasks.add(task);
                    task.fork();
                } else {
                    // После определенной глубины работаем в однопоточном режиме
                    new SubsetTaskWithLimitationThreads(original, i + 1, lenSubset, newSubset, depth + 1).compute();
                }
            }

            for (SubsetTaskWithLimitationThreads task : subTasks) {
                task.join();
            }
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

