package org.example;

import org.example.entity.FilterCanPair;
import org.example.entity.OneFilter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.example.IntTernaryOperator.INT_TERNARY_OPERATOR;
import static org.example.entity.OneFilter.comparatorOneFilter;

public final class Utils {

    private Utils() {}

    private static final String TEXT_START;

    private static final int DEFAULT_BYTE = 0x00;

    //список всех фильтров
    public static Set<FilterCanPair> filterCanPairs;

    //карта всех возможных фильтров (со списком значений удовлетворяющих этот фильтр) для диапазона 0x00-0xFF
    public static Map<FilterCanPair, Set<Integer>> maps;

    //карта всех элементов и списком их фильтров с нужными и лишними значениями
    public static Map<Integer, Set<OneFilter>> mapSet;


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

    public static Map<FilterCanPair, Set<Integer>> filtersForCanArr(int[] canId_arr, int valueSize) {
        return maps.entrySet().stream()
                .filter(entry -> entry.getValue().size() > valueSize)
                .filter(entry ->
                        entry.getValue().stream()
                        .allMatch(canId_filter ->
                                Arrays.stream(canId_arr)
                                .anyMatch(canId -> canId_filter == canId)))
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
        });
    }

    //сортировка и вывод количества элементов для карты фильтров
    public static void soutQuantitySizeMaps() {
        Map<Integer, Integer> map = new TreeMap<>();
        int count = 0;
        for (Map.Entry<FilterCanPair, Set<Integer>> filterCanPairListEntry : maps.entrySet()) {
            int size = filterCanPairListEntry.getValue().size();
            count +=size;
            if (map.containsKey(size)) {
                map.compute(size, (k, old_quantity) -> old_quantity + 1);
            } else {
                map.put(size, 1);
            }
        }
        System.out.println(count);
        System.out.println(map);
    }

    public static void soutMapsBySize(Map<FilterCanPair, Set<Integer>> filtersForCanArr) {
        Map<Integer, Map<FilterCanPair, Set<Integer>>> mapMap = filtersForCanArr.values()
                .stream()
                .map(Set::size)
                .distinct()
                .collect(Collectors.toMap(
                        s -> s,
                        s -> filtersForCanArr.entrySet().stream()
                                .filter(e -> e.getValue().size() == s)
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                ));

        mapMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .forEach((e) -> {
                    System.out.println("sizes=" + e.getKey());
                    System.out.println("maps=");
                    soutMaps(e.getValue());
                    System.out.println();
                });
    }

    public static void soutSortedMapsBySize(Map<FilterCanPair, Set<Integer>> filtersForCanArr) {
        Map<FilterCanPair, Set<Integer>> filtersForCanArrSorted = filtersForCanArr.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size())) // сортируем по размеру Set<Integer>
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, // если есть дубликаты (в данном случае не должно быть)
                        LinkedHashMap::new // сохраняем порядок
                ));
//        System.out.println("filtersForCanArr.size=" + filtersForCanArr.size());
        soutMaps(filtersForCanArrSorted);
    }


    public static void soutMapMapLessThan(Map<Integer, Set<OneFilter>> mapSet) {
        AtomicInteger count = new AtomicInteger();
        mapSet.entrySet().forEach(e1 -> {
            int key = e1.getKey();
            int size = e1.getValue().size();
            System.out.println("canId:" + String.format("0x%02X", key));
            System.out.println("set filters len:" + size);

            count.addAndGet(size);
            e1.getValue().stream()
                    .sorted(comparatorOneFilter)
//                    .filter(v1 -> v1.getExtraSa().size() <= 10)
                    .forEach(v1 -> {
//                        System.out.println("Filter:" + v1.getFilterCanPair());
//                        System.out.println("Extra :" + v1.getExtraSa());
//                        System.out.println("Needed :" + v1.getNeededSa());
//                        System.out.println("N.L:" + v1.getNeededSa().size() + " --- " + "E.L:" + v1.getExtraSa().size());
                    });
            System.out.println();
        });
        System.out.println(count);
    }

    public static Map<Integer, Set<OneFilter>> createMapSet(int[] canId_arr) {
        mapSet = Arrays.stream(canId_arr)
                .boxed()
                .collect(Collectors.toMap(
                        //canId
                        i -> i,
                        //Set<OneFilterAndExtra>
                        i -> maps.entrySet().stream()
                                .filter(e -> e.getValue().stream()
                                        .anyMatch(v -> v.equals(i)))
                                .map(e -> new OneFilter(
                                        e.getKey(), //filterCanPair
                                        e.getValue().stream() //extraSa
                                                .filter(v -> !v.equals(i))
                                                .collect(Collectors.toSet()),
                                        e.getValue().stream() //neededSa
                                                .filter((v) -> Arrays.stream(canId_arr)
                                                        .boxed()
                                                        .anyMatch(c -> c.equals(v)))
                                                .collect(Collectors.toSet())))
                                .sorted(comparatorOneFilter)
//                                .filter(s -> s.getExtraSa().size() < 32)
                                .filter(s -> s.getExtraSa().size() < s.getNeededSa().size()*2)
                                .collect(Collectors.toCollection(LinkedHashSet::new))
                ));
//        soutMapMapLessThan(mapSet);

//        Set<SetFilter> setFilters = new ProcessLoop().startProcess(mapSet, canId_arr);
//        System.out.println(setFilters);

        return mapSet;
    }

}
