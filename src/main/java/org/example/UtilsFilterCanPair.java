package org.example;

import org.example.entity.FilterCanPair;
import org.example.entity.PairCanId;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.example.IntTernaryOperator.INT_TERNARY_OPERATOR;
import static org.example.entity.PairCanId.comparatorPairCanId;

public class UtilsFilterCanPair {

    private UtilsFilterCanPair() {}

    //список всех фильтров
    public static Set<FilterCanPair> filterCanPairs;

    //карта всех возможных фильтров (со списком значений удовлетворяющих этот фильтр) для диапазона 0x00-0xFF
    public static Map<FilterCanPair, Set<Integer>> mapAllFilterAndCanIds;

    static {
        //для понимания как создается mapAllFilterAndCanIds
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
        mapAllFilterAndCanIds = IntStream.rangeClosed(0x00, 0xFF)
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

    //вывод всех значения для карты фильтров
    public static void soutMaps(Map<FilterCanPair, Set<Integer>> maps0) {
        maps0.forEach((key, value) -> {
            System.out.print(key);
            System.out.print("canId:");
            value.stream().map(Utils::intToHex).forEach((i) -> System.out.print(i +" "));
            System.out.println();
        });
    }

    //сортировка и вывод количества элементов покрывающий фильтром
    public static void soutQuantitySizeMaps() {
        Map<Integer, Integer> map = new TreeMap<>();
        int count = 0;
        for (Map.Entry<FilterCanPair, Set<Integer>> filterCanPairListEntry : mapAllFilterAndCanIds.entrySet()) {
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

    public static void soutSortedMapsBySize(int typeSort) {
        soutSortedMapsBySize(mapAllFilterAndCanIds, typeSort);
    }

    private static final Comparator<Map.Entry<FilterCanPair, Set<Integer>>> ASC = Comparator.comparingInt(e -> e.getValue().size());
    private static final Comparator<Map.Entry<FilterCanPair, Set<Integer>>> DESC = (e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size());

    public static void soutSortedMapsBySize(Map<FilterCanPair, Set<Integer>> filtersForCanArr, int typeSort) {
        Comparator<Map.Entry<FilterCanPair, Set<Integer>>> comparator = typeSort == 0 ? ASC : DESC;
        Map<FilterCanPair, Set<Integer>> filtersForCanArrSorted = filtersForCanArr.entrySet().stream()
                .sorted(comparator) // сортируем по размеру Set<Integer>
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, // если есть дубликаты (в данном случае не должно быть)
                        LinkedHashMap::new // сохраняем порядок
                ));
//        System.out.println("filtersForCanArr.size=" + filtersForCanArr.size());
        soutMaps(filtersForCanArrSorted);
    }

    public static Map<FilterCanPair, Set<Integer>> createMapFilterAndCanIdsByCAN_ID_LIST(List<Integer> CAN_ID_LIST) {
        return mapAllFilterAndCanIds.entrySet().stream()
                .filter(e -> e.getValue().stream()
                        .anyMatch(v -> CAN_ID_LIST.stream()
                                .anyMatch(c -> c.equals(v))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Map<FilterCanPair, PairCanId> createMapFilterAndPairCanId(Map<FilterCanPair, Set<Integer>> mapFilterAndCanIdsByCAN_ID_LIST, List<Integer> CAN_ID_LIST) {
        return mapFilterAndCanIdsByCAN_ID_LIST.entrySet().stream()
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
    }

    public static void soutMapFilterAndPairCanIdFilteredByQuantity(Map<FilterCanPair, PairCanId> mapFilterAndPairCanIdFiltered) {
        final String[] strBuff = {""};
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
    }

    public static void soutMapFilterAndPairCanIdFilteredByQuantity2(Map<FilterCanPair, PairCanId> mapFilterAndPairCanIdFiltered){
        mapFilterAndPairCanIdFiltered.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(comparatorPairCanId))
                .forEach(e -> {
                    System.out.print(e.getKey());
                    int sizeEx = e.getValue().getExtraSa().size();
                    int sizeNeed = e.getValue().getNeededSa().size();
                    System.out.println("N.L:" + sizeNeed + " --- " + "E.L:" + sizeEx);
                    System.out.println(e.getValue());
                    System.out.println();
                });
        System.out.println(mapFilterAndPairCanIdFiltered.size());
    }

}
