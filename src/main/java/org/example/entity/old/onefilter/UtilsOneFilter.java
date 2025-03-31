package org.example.entity.old.onefilter;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.example.UtilsFilterCanPair.totalMapFilters;
import static org.example.entity.old.onefilter.OneFilter.comparatorOneFilter;

public class UtilsOneFilter {

    private UtilsOneFilter() {}

    //карта всех элементов и списком их фильтров с нужными и лишними значениями
    public static Map<Integer, Set<OneFilter>> mapSet;

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

    //создает мапу с кэнАйди и сетом фильтров подходящие этому кэнАйди (в сете еще есть перечисления нужных и лишних кэнайди)
    public static Map<Integer, Set<OneFilter>> createMapSet(int[] canId_arr) {
        mapSet = Arrays.stream(canId_arr)
                .boxed()
                .collect(Collectors.toMap(
                        //canId
                        i -> i,
                        //Set<OneFilterAndExtra>
                        i -> totalMapFilters.entrySet().stream()
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
