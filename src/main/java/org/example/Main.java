package org.example;

import org.example.entity.FilterCanPair;
import org.example.entity.PairCanId;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.example.UtilsFilterCanPair.*;

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
    public static final Set<Integer> CAN_ID_SET = Arrays.stream(CAN_ID_ARR).boxed().collect(Collectors.toSet());

    public static void main(String[] args) {
//
//        int FilterMaskId = 0xC8;
//        int FilterId     = 0x00;
//        int FilterMaskId = 0b1110_1110;
//        int FilterId     = 0b1110_0100;
//        soutAllCanIdByFilter(FilterMaskId, FilterId);
//
//        /////////////////////////////////////////////////////////
//        soutMaps(mapAllFilterAndCanIds);
//        soutQuantitySizeMaps();
//
//        /////////////////////////////////////////////////////////
//        System.out.println("canId_arr.length=" + CAN_ID_ARR.length);
//        System.out.println("filtersForCanArr.size=" + mapAllFilterAndCanIds.size());
//
//        /////////////////////////////////////////////////////////
//        soutSortedMapsBySize(0);
//
//        /////////////////////////////////////////////////////////
        process();
    }

    public static void process() {
        System.out.println("totalMapFilters.size=" + totalMapFilters.size());
        Map<FilterCanPair, Set<Integer>> mapFilterAndCanIdsByCAN_ID_LIST = createMapFilterAndCanIdsByCAN_ID_LIST(CAN_ID_LIST);
//        soutMaps(mapFilterAndCanIdsByCAN_ID_LIST);
//        System.out.println("mapFilterAndCanIdsByCAN_ID_LIST.size=" + mapFilterAndCanIdsByCAN_ID_LIST.size());

        Map<FilterCanPair, PairCanId> mapFilterAndPairCanId = createMapFilterAndPairCanId(mapFilterAndCanIdsByCAN_ID_LIST, CAN_ID_LIST);
//        System.out.println(mapFilterAndPairCanId);
        System.out.println("mapFilterAndPairCanId.size=" + mapFilterAndPairCanId.size());


        Map<FilterCanPair, PairCanId> mapFilterAndPairCanIdFiltered = mapFilterAndPairCanId.entrySet().stream()
                .filter(e -> {
                    PairCanId value = e.getValue();
                    if (value.getNeededSa().size() != 1) return true;
                    else return value.getExtraSa().isEmpty();
                })
//                .filter(e -> e.getValue().getExtraSa().size() <= e.getValue().getNeededSa().size()*3)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//        System.out.println(mapFilterAndPairCanIdFiltered);
        System.out.println("mapFilterAndPairCanIdFiltered.size=" + mapFilterAndPairCanIdFiltered.size());


//        soutMapFilterAndPairCanIdFilteredByQuantity(mapFilterAndPairCanId);
//        soutMapFilterAndPairCanIdFilteredByQuantity(mapFilterAndPairCanIdFiltered);
//        soutMapFilterAndPairCanIdFilteredByQuantity2(mapFilterAndPairCanIdFiltered);


        double penaltyWeight = 0.5; // Штраф за `extraSa`, можно подбирать экспериментально
        boolean isInfinity = true;
        while (isInfinity) {
            Set<Map.Entry<FilterCanPair, PairCanId>> result = process2(mapFilterAndPairCanIdFiltered, penaltyWeight);
            boolean isAll = CAN_ID_SET.equals(result.stream()
                    .flatMap(e -> e.getValue().getNeededSa().stream())
                    .collect(Collectors.toSet()));

            int countNeeded = result.stream()
                    .flatMap(e -> e.getValue().getNeededSa().stream())
                    .collect(Collectors.toSet()).size();
            int countExtra = result.stream()
                    .flatMap(e -> e.getValue().getExtraSa().stream())
                    .collect(Collectors.toSet()).size();

            System.out.println(isAll);
            System.out.println(countNeeded);
            System.out.println(countExtra);

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                String str = br.readLine();
                switch (str) {
                    case "+": penaltyWeight += 0.005;
                    break;
                    case "-": penaltyWeight -= 0.005;
                    break;
                    case "0": isInfinity = false;
                    break;
                }
            } catch (IOException e) {}
            System.out.println(penaltyWeight);
        }

    }

    public static Set<Map.Entry<FilterCanPair, PairCanId>> process2(Map<FilterCanPair, PairCanId> mapFilterAndPairCanIdFiltered, double penaltyWeight) {
        List<Map.Entry<FilterCanPair, PairCanId>> allFilters = new ArrayList<>(mapFilterAndPairCanIdFiltered.entrySet());

        Set<Integer> uncoveredNeededSa = new HashSet<>(CAN_ID_LIST);  // Непокрытые `neededSa`
        Set<Map.Entry<FilterCanPair, PairCanId>> result = new HashSet<>();  // Итоговый набор фильтров

        while (!uncoveredNeededSa.isEmpty() && result.size() < 14) {
            Map.Entry<FilterCanPair, PairCanId> bestFilter = null;
            double bestScore = Double.NEGATIVE_INFINITY;

            for (Map.Entry<FilterCanPair, PairCanId> entry : allFilters) {
                Set<Integer> needed = entry.getValue().getNeededSa();
                Set<Integer> extra = entry.getValue().getExtraSa();

                int newCoverage = (int) needed.stream().filter(uncoveredNeededSa::contains).count();
                double score = newCoverage - penaltyWeight * extra.size();  // Оценка фильтра

                if (score > bestScore || (score == bestScore && extra.size() < bestFilter.getValue().getExtraSa().size())) {
                    bestScore = score;
                    bestFilter = entry;
                }
            }

            if (bestFilter == null) {
                break;
            }

            result.add(bestFilter);
            uncoveredNeededSa.removeAll(bestFilter.getValue().getNeededSa());
            allFilters.remove(bestFilter);
        }

        return result;
    }

    public static void process1(Map<FilterCanPair, PairCanId> mapFilterAndPairCanIdFiltered) {
        List<Map.Entry<FilterCanPair, PairCanId>> original = mapFilterAndPairCanIdFiltered.entrySet().stream().toList();
//        System.out.println("original.size=" + original.size());

        AtomicInteger minCountExtra = new AtomicInteger(Integer.MAX_VALUE);
        AtomicReference<List<Map.Entry<FilterCanPair, PairCanId>>> minCountExtraSetFilters = new AtomicReference<>(null);
        AtomicInteger minCountFilters = new AtomicInteger(Integer.MAX_VALUE);
        AtomicReference<List<Map.Entry<FilterCanPair, PairCanId>>> minCountFiltersSetFilters = new AtomicReference<>(null);

        Consumer<List<Map.Entry<FilterCanPair, PairCanId>>> consumerOriginal = (subset) -> {
            Set<Integer> subsetNeeded = subset.stream()
                    .flatMap(s -> s.getValue().getNeededSa().stream())
                    .collect(Collectors.toSet());

            boolean isContains = subsetNeeded.equals(CAN_ID_SET);
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

        long start = System.currentTimeMillis();
        subsetsWithoutRes(original, consumerOriginal);
        long stop = System.currentTimeMillis();
        System.out.println("time=" + (stop - start)/1000 + " s");

        System.out.println("minCountExtraSetFilters");
        System.out.println(minCountExtraSetFilters);
        System.out.println("\n\n\n");
        System.out.println("minCountFiltersSetFilters");
        System.out.println(minCountFiltersSetFilters);
    }

    static <T> void subsetsWithoutRes(List<T> original, Consumer<List<T>> consumerOriginal) {
        List<T> subset = new ArrayList<>(14);
        subsetRecurWithoutRes(0, original, subset, consumerOriginal);
    }

    static <T> void subsetRecurWithoutRes(int i, List<T> original, List<T> subset, Consumer<List<T>> consumerOriginal) {

        // Добавляем подмножество, если оно не пустое
        if (!subset.isEmpty()) {
//            System.out.println(subset.size());
            consumerOriginal.accept(subset);
        }

        // Если уже достигли максимальной длины, возвращаемся
        if (subset.size() == 14) {
            return;
        }

        // Перебираем оставшиеся элементы
        for (int j = i; j < original.size(); j++) {
            subset.add(original.get(j));
            subsetRecurWithoutRes(j + 1, original, subset, consumerOriginal);
            subset.remove(subset.size() - 1); // Убираем последний элемент (backtracking)
        }
    }

}
