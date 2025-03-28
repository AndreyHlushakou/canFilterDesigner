package org.example.subset;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public final class SubsetGenerationParallel2 {

    private SubsetGenerationParallel2() {}

    public static void main(String[] args) {
        int lenSet = 5; // Длина множества
        int lenSubset = 3; // Максимальная длина подмножеств

        List<Integer> original = new ArrayList<>(lenSet);
        for (int i = 1; i <= lenSet; i++) {
            original.add(i);
        }

        long start = System.currentTimeMillis();

        ForkJoinPool pool = new ForkJoinPool(); // Пул потоков
        List<List<Integer>> res = pool.invoke(new SubsetTask(original, 0, lenSubset, new ArrayList<>()));
        printf(res);
        long stop = System.currentTimeMillis();
        System.out.println("time: " + (stop - start));

        // pool.shutdown(); // Можно явно закрыть пул, но ForkJoinPool закрывается сам.
    }

    static class SubsetTask extends RecursiveTask<List<List<Integer>>> {
        private final List<Integer> original;
        private final int start;
        private final int lenSubset;
        private final List<Integer> subset;

        public SubsetTask(List<Integer> original, int start, int lenSubset, List<Integer> subset) {
            this.original = original;
            this.start = start;
            this.lenSubset = lenSubset;
            this.subset = new ArrayList<>(subset); // Создаем копию, чтобы избежать конфликтов между потоками
        }

        @Override
        protected List<List<Integer>> compute() {
            List<List<Integer>> res = new ArrayList<>();

            // Добавляем текущее подмножество, если оно не пустое
            if (!subset.isEmpty()) {
                res.add(new ArrayList<>(subset));
            }

            // Если достигли лимита длины, выходим
            if (subset.size() == lenSubset) {
                return res;
            }

            // Перебираем оставшиеся элементы
            List<SubsetTask> subTasks = new ArrayList<>();
            for (int i = start; i < original.size(); i++) {
                List<Integer> newSubset = new ArrayList<>(subset);
                newSubset.add(original.get(i));

                SubsetTask task = new SubsetTask(original, i + 1, lenSubset, newSubset);
                task.fork(); // Запускаем подзадачу в отдельном потоке
                subTasks.add(task);
            }

            // Собираем результаты всех потоков
            for (SubsetTask task : subTasks) {
                res.addAll(task.join());
            }

            return res;
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

