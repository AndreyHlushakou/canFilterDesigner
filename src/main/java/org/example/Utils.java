package org.example;

import org.example.entity.FilterCanPair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.example.IntTernaryOperator.INT_TERNARY_OPERATOR;

public final class Utils {

    private Utils() {}

    private static final String TEXT_START;

    private static final int DEFAULT_BYTE = 0x00;

    //список всех фильтров
    public static Set<FilterCanPair> filterCanPairs;

    //карта всех возможных фильтров (со списком значений удовлетворяющих этот фильтр) для диапазона 0x00-0xFF
    public static Map<FilterCanPair, Set<Integer>> maps;


    static {
////        11
//        filterCanPairs = new HashSet<>();
//        IntStream.rangeClosed(0x00, 0xFF).forEach((FilterMaskId) -> IntStream.rangeClosed(0x00, 0xFF).forEach((FilterId) -> {
//            FilterCanPair filterCanPair = new FilterCanPair(FilterMaskId, FilterId);
//            filterCanPairs.add(filterCanPair);
//        }));
//
////        12
//        a = System.currentTimeMillis();
//        filterCanPairs = IntStream.rangeClosed(0x00, 0xFF)
//                .boxed()
//                .flatMap((FilterMaskId) -> IntStream.rangeClosed(0x00, 0xFF).mapToObj((FilterId) -> new FilterCanPair(FilterMaskId, FilterId)))
//                .collect(Collectors.toSet());
//        System.out.println("a12=" + (System.currentTimeMillis() - a));

////        21
//        maps = new HashMap<>();
//        filterCanPairs.forEach((filterCanPair) -> {
//            List<Integer> list = new ArrayList<>();
//            IntStream.rangeClosed(0x00, 0xFF).forEach(canId -> {
//                boolean b = INT_TERNARY_OPERATOR.test(canId, filterCanPair.getFilterMaskId(), filterCanPair.getFilterId());
//                if (b) {
//                    list.add(canId);
//                }
//            });
//            if (!list.isEmpty()) {
//                maps.put(filterCanPair, list);
//            }
//        });
//
////        22
//        maps = new HashMap<>();
//        filterCanPairs.forEach((filterCanPair) -> {
//            List<Integer> list = IntStream.rangeClosed(0x00, 0xFF)
//                    .filter(canId -> INT_TERNARY_OPERATOR.test(canId, filterCanPair.getFilterMaskId(), filterCanPair.getFilterId()))
//                    .boxed()
//                    .toList();
//            if (!list.isEmpty()) {
//                maps.put(filterCanPair, list);
//            }
//        });
//
////        23
//        maps = filterCanPairs.stream()
//                .filter((filterCanPair) -> IntStream.rangeClosed(0x00, 0xFF)
//                        .anyMatch(canId -> INT_TERNARY_OPERATOR.test(canId, filterCanPair.getFilterMaskId(), filterCanPair.getFilterId())))
//                .collect(Collectors.toMap(
//                        (filterCanPair) -> filterCanPair,
//                        (filterCanPair) -> IntStream.rangeClosed(0x00, 0xFF)
//                                .filter(canId -> INT_TERNARY_OPERATOR.test(canId, filterCanPair.getFilterMaskId(), filterCanPair.getFilterId()))
//                                .boxed()
//                                .toList()
//                ));


////        finally2
        maps = IntStream.rangeClosed(0x00, 0xFF)
                .boxed()
                .flatMap((FilterMaskId) -> IntStream.rangeClosed(0x00, 0xFF)
                        .mapToObj((FilterId) -> new FilterCanPair(FilterMaskId, FilterId))
                        .filter((filterCanPair) -> IntStream.rangeClosed(0x00, 0xFF)
                                .anyMatch(canId -> INT_TERNARY_OPERATOR.test(canId, filterCanPair.getFilterMaskId(), filterCanPair.getFilterId())))
                )
                .collect(Collectors.toSet())
                .stream()

                .collect(Collectors.toMap(
                        (filterCanPair) -> filterCanPair,
                        (filterCanPair) -> IntStream.rangeClosed(0x00, 0xFF)
                                .filter(canId -> INT_TERNARY_OPERATOR.test(canId, filterCanPair.getFilterMaskId(), filterCanPair.getFilterId()))
                                .boxed()
                                .collect(Collectors.toSet())
                ));

    }

    public static Map<FilterCanPair, Set<Integer>> filtersForCanArr(int[] canId_arr) {
        return maps.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 0)
                .filter(entry -> entry.getValue().stream().allMatch(in -> Arrays.stream(canId_arr).anyMatch(can -> in == can)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    //для вывода
    static {
        StringBuilder str_ = new StringBuilder("HEX  | BIN\n");
        IntStream.range(0, intToHexAndBin(DEFAULT_BYTE).length()).forEach(i -> str_.append("-"));
        TEXT_START = str_.toString();
    }

    public static String intToHexAndBin(int data) {
        String hex = intToHex(data);
        String bin = intToBin(data);
        return hex + " | " + bin;
    }

    //
    public static String intToHex(int data) {
        String hex = toStrPlusZeros(2, Integer.toHexString(data));
        return "0x" + hex;
    }
    public static String intToBin(int data) {
        String bin = toStrPlusZeros(8, Integer.toBinaryString(data));
        return "0b" + bin;
    }

    private static String toStrPlusZeros(int quantityZeros, String str) {
        StringBuilder zeros = new StringBuilder();
        int len = quantityZeros - str.length();
        while (len-- != 0) zeros.append("0");
        return zeros.append(str).toString().toUpperCase();
    }

    //проверка фильтра
    public static void checkCanId(int FilterMaskId, int FilterId) {
        System.out.println(TEXT_START);
        System.out.println(intToHexAndBin(FilterMaskId) + " - FilterMaskId");
        System.out.println(intToHexAndBin(FilterId) + " - FilterId");

        System.out.println();
        System.out.println(TEXT_START);

        IntStream.rangeClosed(0x00, 0xFF).forEach(canId -> {
            boolean b = INT_TERNARY_OPERATOR.test(canId, FilterMaskId, FilterId);
            if (b) {
                System.out.println(intToHexAndBin(canId));
            }
        });
    }

    //вывод всех значения для карты фильтров
    public static void soutMaps(Map<FilterCanPair, Set<Integer>> maps0) {
        maps0.forEach((key, value) -> {
            System.out.println(key);
            value.stream().map(Utils::intToHex).forEach((i) -> System.out.print(i +" "));
            System.out.println();
            System.out.println();
        });
    }

    //сортировка и вывод количества элементов для карты фильтров
    public static void soutQuantitySizeMaps() {
        Map<Integer, Integer> map = new TreeMap<>();
        for (Map.Entry<FilterCanPair, Set<Integer>> filterCanPairListEntry : maps.entrySet()) {
            int size = filterCanPairListEntry.getValue().size();
            if (map.containsKey(size)) {
                map.compute(size, (k, old_quantity) -> old_quantity + 1);
            } else {
                map.put(size, 1);
            }
        }
        System.out.println(map);
    }
}
