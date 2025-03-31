package org.example;

import org.example.entity.FilterCanPair;
import org.example.entity.PairCanId;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.example.Utils.soutAllCanIdByFilter;
import static org.example.UtilsFilterCanPair.*;
import static org.example.entity.PairCanId.comparatorPairCanId;

public class Main {

    //массив canId который надо обработать и найти оптимальные фильтры
    public static final int[] CAN_ID_ARR = new int[] {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,       0x0B,                   0x0F,
            0x10, 0x11, 0x12,                         0x17,       0x19,             0x1C, 0x1D, 0x1E,
            0x20, 0x21, 0x22, 0x23, 0x24,                   0x28,                                     0x2F,
            0x30, 0x31, 0x32,                                           0x3A,
                                                      0x47,                               0x4D,
                        0x52,                                     0x59,


                                           0x85,             0x88, 0x89,             0x8C,
                                           0x95,                               0x9B,
                                                                                          0xAD,

                                                                                                 0xCE, 0xCF,
            0xD0,       0xD2,                                                             0xDD,
                                           0xE5,                                                 0xEE,
                                     0xF4

    };

    public static final List<Integer> CAN_ID_LIST = Arrays.stream(CAN_ID_ARR).boxed().sorted().toList();

    public static void main(String[] args) {

//        int FilterMaskId = 0xC8;
//        int FilterId     = 0x00;
//        int FilterMaskId = 0b1110_1110;
//        int FilterId     = 0b1110_0100;
//        soutAllCanIdByFilter(FilterMaskId, FilterId);

        /////////////////////////////////////////////////////////
//        soutMaps(mapAllFilterAndCanIds);
//        soutQuantitySizeMaps();

        /////////////////////////////////////////////////////////
//        System.out.println("canId_arr.length=" + CAN_ID_ARR.length);
//        System.out.println("filtersForCanArr.size=" + mapAllFilterAndCanIds.size());

        /////////////////////////////////////////////////////////
//        soutSortedMapsBySize(0);

        /////////////////////////////////////////////////////////
//        process();
    }

    public static void process() {
        System.out.println("maps.size=" + mapAllFilterAndCanIds.size());
        Map<FilterCanPair, Set<Integer>> mapFilterAndCanIdsByCAN_ID_LIST = mapAllFilterAndCanIds.entrySet().stream()
                .filter(e -> e.getValue().stream()
                        .anyMatch(v -> CAN_ID_LIST.stream()
                                .anyMatch(c -> c.equals(v))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//        soutMaps(mapFilterAndCanIdsByCAN_ID_LIST);
        System.out.println("mapFilterAndCanIdsByCAN_ID_LIST.size=" + mapFilterAndCanIdsByCAN_ID_LIST.size());

        Map<FilterCanPair, PairCanId> mapFilterAndPairCanId = mapFilterAndCanIdsByCAN_ID_LIST.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> {
                            Set<Integer> neededSa = e.getValue().stream()
                                    .filter(v -> CAN_ID_LIST.stream()
                                            .anyMatch(c -> c.equals(v)))
                                    .collect(Collectors.toSet());
                            Set<Integer> extraSa = new HashSet<>(e.getValue());
                            extraSa.removeAll(neededSa);
                            return new PairCanId(neededSa, extraSa);
                        }
                ));
//        System.out.println(mapFilterAndPairCanId);
//        System.out.println("mapFilterAndPairCanId.size=" + mapFilterAndPairCanId.size());

        Map<FilterCanPair, PairCanId> mapFilterAndPairCanIdFiltered = mapFilterAndPairCanId.entrySet().stream()
                .filter(e -> e.getValue().getExtraSa().size() < e.getValue().getNeededSa().size())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//        System.out.println(mapFilterAndPairCanIdFiltered);
        System.out.println("mapFilterAndPairCanIdFiltered.size=" + mapFilterAndPairCanIdFiltered.size());

        final String[] strBuff = {""};
//        mapFilterAndPairCanId
                mapFilterAndPairCanIdFiltered
                .values().stream()
                .sorted(comparatorPairCanId)
                .forEach(e -> {
                    String str = "N.L:" + e.getNeededSa().size() + " --- " + "E.L:" + e.getExtraSa().size();
                    if (!strBuff[0].equals(str)) {
                        System.out.println(str);
                        strBuff[0] = str;
                    }
                });
        System.exit(0);

//        mapFilterAndPairCanIdFiltered.entrySet().stream()
//                .sorted(Map.Entry.comparingByValue(comparatorPairCanId))
//                .forEach(e -> {
//                    System.out.print(e.getKey());
//                    int sizeEx = e.getValue().getExtraSa().size();
//                    int sizeNeed = e.getValue().getNeededSa().size();
//                    System.out.println("N.L:" + sizeNeed + " --- " + "E.L:" + sizeEx);
//                    System.out.println(e.getValue());
//                    System.out.println();
//                });
//        System.out.println(mapFilterAndPairCanIdFiltered.size());

        List<Map.Entry<FilterCanPair, PairCanId>> original = mapFilterAndPairCanIdFiltered.entrySet().stream().toList();
//        System.out.println("original.size=" + original.size());

        AtomicInteger minCountExtra = new AtomicInteger(Integer.MAX_VALUE);
        AtomicReference<List<Map.Entry<FilterCanPair, PairCanId>>> minCountExtraSetFilters = new AtomicReference<>(null);
        AtomicInteger minCountFilters = new AtomicInteger(Integer.MAX_VALUE);
        AtomicReference<List<Map.Entry<FilterCanPair, PairCanId>>> minCountFiltersSetFilters = new AtomicReference<>(null);

        Consumer<List<Map.Entry<FilterCanPair, PairCanId>>> consumerOriginal = (subset) -> {
            List<Integer> subsetNeeded = subset.stream()
                    .flatMap(s -> s.getValue().getNeededSa().stream())
                    .sorted()
                    .toList();

            boolean isContains = subsetNeeded.equals(CAN_ID_LIST);
            if (isContains) {
                Set<Integer> subsetExtra = subset.stream()
                        .flatMap(s -> s.getValue().getExtraSa().stream())
                        .collect(Collectors.toSet());

                if (minCountExtra.get() > subsetExtra.size()) {
                    minCountExtra.set(subsetExtra.size());
                    minCountExtraSetFilters.set(subset);
                }

                if (minCountFilters.get() > subset.size() && !subset.isEmpty()) {
                    minCountFilters.set(subsetExtra.size());
                    minCountFiltersSetFilters.set(subset);
                }

            }
        };

        ForkJoinPool pool = new ForkJoinPool(); // Пул потоков
        SubsetTask2<Map.Entry<FilterCanPair, PairCanId>> subsetTask = new SubsetTask2<>(original, 0, 14, new ArrayList<>(), consumerOriginal);

        long start = System.currentTimeMillis();
        pool.invoke(subsetTask);
//        subsetsWithoutRes(original, 14, consumerOriginal);
        long stop = System.currentTimeMillis();
        System.out.println("time=" + (stop - start)/1000 + " s");

        System.out.println("minCountExtraSetFilters");
        System.out.println(minCountExtraSetFilters);
        System.out.println("\n\n\n");
        System.out.println("minCountFiltersSetFilters");
        System.out.println(minCountFiltersSetFilters);
    }

    static <T> void subsetsWithoutRes(List<T> original, int lenSubset, Consumer<List<T>> consumerOriginal) {
        List<T> subset = new ArrayList<>(lenSubset);
        subsetRecurWithoutRes(0, original, subset, lenSubset, consumerOriginal);
    }

    static <T> void subsetRecurWithoutRes(int i, List<T> original, List<T> subset, int lenSubset, Consumer<List<T>> consumerOriginal) {

        // Добавляем подмножество, если оно не пустое
        if (!subset.isEmpty()) {
//            System.out.println(subset);
            consumerOriginal.accept(subset);
        }

        // Если уже достигли максимальной длины, возвращаемся
        if (subset.size() == lenSubset) {
            return;
        }

        // Перебираем оставшиеся элементы
        for (int j = i; j < original.size(); j++) {
            subset.add(original.get(j));
            subsetRecurWithoutRes(j + 1, original, subset, lenSubset, consumerOriginal);
            subset.remove(subset.size() - 1); // Убираем последний элемент (backtracking)
        }
    }

    static class SubsetTask2 <T> extends RecursiveTask<Boolean> {
        private final List<T> original;
        private final int start;
        private final int lenSubset;
        private final List<T> subset;
        private final Consumer<List<T>> consumerOriginal;

        public SubsetTask2(List<T> original, int start, int lenSubset, List<T> subset, Consumer<List<T>> consumerOriginal) {
            this.original = original;
            this.start = start;
            this.lenSubset = lenSubset;
            this.subset = new ArrayList<>(subset); // Создаем копию, чтобы избежать конфликтов между потоками
            this.consumerOriginal = consumerOriginal;
        }

        @Override
        protected Boolean compute() {

            // Добавляем текущее подмножество, если оно не пустое
            if (!subset.isEmpty()) {
                consumerOriginal.accept(subset);
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

                SubsetTask2<T> task = new SubsetTask2<>(original, i + 1, lenSubset, newSubset, consumerOriginal);
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


}
