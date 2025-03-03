package org.example.subset;

import org.example.entity.Parametrs3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public final class SubsetGenerationParallel {

    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    private static final LinkedBlockingQueue<Set<Integer>> queue = new LinkedBlockingQueue<>(1000); // Очередь с ограничением по памяти
    private static volatile boolean finished = false; // Флаг завершения работы
    private static final String OUTPUT_FILE = "subsets.txt";
    private static final File file = new File(OUTPUT_FILE);
    public static Set<Parametrs3> parametrs3Set;

    public static
            void
//    Set<Parametrs3>
    parallel(int[] arr) {

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

//        // Поток для записи нужных результатов в сет
//        Thread writerThread = new Thread(() -> {
//            while (!finished || !queue.isEmpty()) {
//                Set<Integer> subset = queue.poll();
//                if (subset != null) {
//                    parametrs3Set = maps.entrySet().stream().parallel()
//                            .filter(entry -> entry.getValue().containsAll(subset))
//                            .map(entry -> new Parametrs3(subset, entry.getKey(), entry.getValue()))
//                            .collect(Collectors.toSet());
//                }
//            }
//
//        });
//        writerThread.start();
        // Запускаем потоки для генерации подмножеств
//        IntStream.range(0, NUM_THREADS)
//                .parallel()
//                .forEach(threadId -> executor.submit(() -> generateSubsets(arr, threadId, NUM_THREADS)));
//        for (int i = 0; i < NUM_THREADS; i++) {
//            int threadId = i;
//            executor.submit(() -> generateSubsets(arr, threadId, NUM_THREADS));
//        }
//
//        executor.shutdown();
//        while (!executor.isTerminated()); // Ждем завершения генерации
//
//        finished = true; // Сообщаем потоку записи, что генерация завершена
//        try {
//            writerThread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return parametrs3Set;


        // Поток для записи результатов в файл
        Thread writerThread = new Thread(() -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
                while (!finished || !queue.isEmpty()) {
                    Set<Integer> subset = queue.poll();
                    if (subset != null) {
                        writer.write(subset.toString());
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writerThread.start();

        // Запускаем потоки для генерации подмножеств
        for (int i = 0; i < NUM_THREADS; i++) {
            int threadId = i;
            executor.submit(() -> generateSubsets(arr, threadId, NUM_THREADS));
        }

        executor.shutdown();
        while (!executor.isTerminated()) {} // Ждем завершения генерации

        finished = true; // Сообщаем потоку записи, что генерация завершена
        try {
            writerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Подмножества записаны в файл: " + OUTPUT_FILE);


    }

    private static void generateSubsets(int[] arr, int threadId, int totalThreads) {
        int n = arr.length;
        long totalSubsets = 1L << n; // 2^n

        for (long i = threadId; i < totalSubsets; i += totalThreads) {
            Set<Integer> subset = new HashSet<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1L << j)) != 0) { // Проверяем, включен ли элемент
                    subset.add(arr[j]);
                }
            }
            try {
                queue.put(subset); // Добавляем в очередь
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

