package org.example;

import org.example.entity.FilterCanPair;
import org.example.entity.PairCanId;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.example.Utils.*;
import static org.example.entity.PairCanId.comparatorPairCanId;

public class Main {

    //массив canId который надо обработать и найти оптимальные фильтры
    public static final int[] canId_arr = new int[] {
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

    public static List<Integer> canId_list = Arrays.stream(canId_arr).boxed().collect(Collectors.toList());

    public static void main(String[] args) {
//        int FilterMaskId = 0xC8;
//        int FilterId     = 0x00;
//        int FilterMaskId = 0b1110_1110;
//        int FilterId     = 0b1110_0100;
//        checkCanId(FilterMaskId, FilterId);

        /////////////////////////////////////////////////////////
//        soutMaps(maps);
//        soutQuantitySizeMaps();

        /////////////////////////////////////////////////////////
//        System.out.println("canId_arr.length=" + canId_arr.length);
//        System.out.println("filtersForCanArr.size=" + maps.size());

        /////////////////////////////////////////////////////////
//        Map<FilterCanPair, Set<Integer>> filtersForCanArr = filtersForCanArr(canId_arr, 0);
//        soutSortedMapsBySize(filtersForCanArr);
//        soutMapsBySize(filtersForCanArr);

        /////////////////////////////////////////////////////////
//        createMapSet(canId_arr);

        process();
    }

    public static void process() {
        Map<FilterCanPair, Set<Integer>> maps2 = maps.entrySet().stream()
                .filter(e -> e.getValue().stream()
                        .anyMatch(v -> canId_list.stream()
                                .anyMatch(c -> c.equals(v))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//        soutMaps(maps2);

        Map<FilterCanPair, PairCanId> maps3 = maps2.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> {
                            Set<Integer> neededSa = e.getValue().stream()
                                    .filter(v -> canId_list.stream()
                                            .anyMatch(c -> c.equals(v)))
                                    .collect(Collectors.toSet());
                            Set<Integer> extraSa = e.getValue().stream()
                                    .filter(v -> canId_list.stream()
                                            .anyMatch(c -> !c.equals(v)))
                                    .collect(Collectors.toSet());
                            return new PairCanId(neededSa, extraSa);
                        }
                ));

        Map<FilterCanPair, PairCanId> maps4 = maps3.entrySet().stream()
                .filter(e -> e.getValue().getExtraSa().size() < e.getValue().getNeededSa().size()*2)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

//        maps4.entrySet().stream()
//                .sorted(Map.Entry.comparingByValue(comparatorPairCanId))
//                .forEach(e -> {
//                    System.out.print(e.getKey());
//                    int sizeEx = e.getValue().getExtraSa().size();
//                    int sizeNeed = e.getValue().getNeededSa().size();
//                    System.out.println("N.L:" + sizeNeed + " --- " + "E.L:" + sizeEx);
//                    System.out.println(e.getValue());
//                    System.out.println();
//                });
//        System.out.println(maps4.size());

        List<Map.Entry<FilterCanPair, PairCanId>> listMaps4 = maps4.entrySet().stream().toList();
        Consumer<List<Map.Entry<FilterCanPair, PairCanId>>> consumer = (subset) -> {
            Set<Integer> subsetNeeded = subset.stream()
                    .flatMap(s -> s.getValue().getNeededSa().stream())
                    .collect(Collectors.toSet());
            boolean isContains = canId_list.containsAll(subsetNeeded);
            if (isContains) {
                System.out.println(subset);
                System.exit(0);
            }
        };

    }



}
