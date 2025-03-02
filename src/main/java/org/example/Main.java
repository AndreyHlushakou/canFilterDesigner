package org.example;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.Utils.maps;

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

//    [0x00], [0x01], [0x02], [0x00, 0x01], [0x00, 0x02], [0x01, 0x02], [0x00, 0x01, 0x02]

    public static void main(String[] args) {
        int FilterMaskId = 0x00;
        int FilterId     = 0x00;
//        int FilterMaskId = 0b11001000;
//        int FilterId     = 0b00000000;

//        checkCanId(FilterMaskId, FilterId);
//        soutAllMaps();
//        soutQuantitySizeMaps();

        System.out.println(canId_arr.length);
        findAllFiltersForCanId2(canId_arr);
    }

    public static void findAllFiltersForCanId1(int[] can_arr) {
        List<CanIdAndFilters> list = Arrays.stream(can_arr).mapToObj(CanIdAndFilters::new).toList();
//        list.forEach(System.out::println);
    }

    public static void findAllFiltersForCanId2(int[] can_arr) {
        List<List<Integer>> result = allCombinationList(can_arr);

        Map<Set<FilterCanPair>, List<CombinationCanIdAndFilters>> a = result.stream()
                .map(CombinationCanIdAndFilters::new)
                .filter(i -> i.getSize() > 1)
                .collect(Collectors.groupingBy( b -> new HashSet<>(b.getMapsFilters().keySet())));

        a.entrySet().forEach(entry -> {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        });

        System.out.println(result);
    }

    public static List<List<Integer>> allCombinationList(int[] can_arr) {
        List<List<Integer>> result = new ArrayList<>();
        int n = can_arr.length;
        int totalSubsets = 1 << n; // 2^n

        for (int i = 1; i < totalSubsets; i++) { // Начинаем с 1, чтобы исключить пустое множество
            List<Integer> subset = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) { // Проверяем, включен ли j-й элемент в текущее подмножество
                    subset.add(can_arr[j]);
                }
            }
            result.add(subset);
        }
        return result;
    }


}

class CombinationCanIdAndFilters {
    private final List<Integer> canIds;
    private final Map<FilterCanPair, Set<Integer>> mapsFilters;

    public CombinationCanIdAndFilters(List<Integer> canIds) {
        this.canIds = canIds;

        mapsFilters = maps.entrySet()
                .stream()
                .filter(entry -> entry.getValue().containsAll(canIds))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public int getSize() {
        return mapsFilters.size();
    }

    public List<Integer> getCanIds() {
        return canIds;
    }

    public Map<FilterCanPair, Set<Integer>> getMapsFilters() {
        return mapsFilters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CombinationCanIdAndFilters that = (CombinationCanIdAndFilters) o;
        return Objects.equals(canIds, that.canIds) && Objects.equals(mapsFilters, that.mapsFilters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(canIds, mapsFilters);
    }
}

