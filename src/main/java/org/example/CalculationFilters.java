package org.example;

import lombok.Getter;
import org.example.entity.FilterCanPair;
import org.example.entity.PairCanId;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.example.IntTernaryOperator.INT_TERNARY_OPERATOR;

public class CalculationFilters {

    private static List<Integer> CAN_ID_LIST;
    @Getter
    private static Set<Map.Entry<FilterCanPair, PairCanId>> result;  // Итоговый набор фильтров
    @Getter
    private static boolean isAll;

    private CalculationFilters() {}

    //карта всех возможных фильтров (со списком значений удовлетворяющих этот фильтр) для диапазона 0x00-0xFF
    public static Map<FilterCanPair, Set<Integer>> totalMapFilters;

    static {
        totalMapFilters = IntStream.rangeClosed(0x00, 0xFF)
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

    public static void process(List<Integer> CAN_ID_LIST0, double penaltyWeight) {
        CAN_ID_LIST = CAN_ID_LIST0;
        Set<Integer> CAN_ID_SET = new HashSet<>(CAN_ID_LIST);

        Map<FilterCanPair, Set<Integer>> mapFilterAndCanIdsByCAN_ID_LIST = totalMapFilters.entrySet().stream()
                .filter(e -> e.getValue().stream()
                        .anyMatch(v -> CAN_ID_LIST.stream()
                                .anyMatch(c -> c.equals(v))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

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

        Map<FilterCanPair, PairCanId> mapFilterAndPairCanIdFiltered = mapFilterAndPairCanId.entrySet().stream()
                .filter(e -> {
                    PairCanId value = e.getValue();
                    if (value.getNeededSa().size() != 1) return true;
                    else return value.getExtraSa().isEmpty();
                })
//                .filter(e -> e.getValue().getExtraSa().size() <= e.getValue().getNeededSa().size()*3)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<Map.Entry<FilterCanPair, PairCanId>> listMapEntryFilters = new ArrayList<>(mapFilterAndPairCanIdFiltered.entrySet());

        Set<Integer> CAN_ID_SET_buff = new HashSet<>(CAN_ID_SET);  // Непокрытые `neededSa`
        result = new HashSet<>();  // Итоговый набор фильтров
        while (!CAN_ID_SET_buff.isEmpty() && result.size() < 14) {
            Map.Entry<FilterCanPair, PairCanId> bestFilter = null;
            double bestScore = Double.NEGATIVE_INFINITY;

            for (Map.Entry<FilterCanPair, PairCanId> entry : listMapEntryFilters) {
                Set<Integer> needed = entry.getValue().getNeededSa();
                Set<Integer> extra = entry.getValue().getExtraSa();

                int newCoverage = (int) needed.stream().filter(CAN_ID_SET_buff::contains).count();
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
            CAN_ID_SET_buff.removeAll(bestFilter.getValue().getNeededSa());
            listMapEntryFilters.remove(bestFilter);
        }

        isAll = CAN_ID_SET.equals(result.stream()
                .flatMap(e -> e.getValue().getNeededSa().stream())
                .collect(Collectors.toSet()));
    }

    public static String getReport() {
        int countNeeded = result.stream()
                .flatMap(e -> e.getValue().getNeededSa().stream())
                .collect(Collectors.toSet()).size();
        int countExtra = result.stream()
                .flatMap(e -> e.getValue().getExtraSa().stream())
                .collect(Collectors.toSet()).size();
        StringBuilder builder = new StringBuilder();
        return builder.append("Все ли фильтры: ").append(isAll ? "ДА." : "НЕТ.").append("\n")
                .append("Нужные: ").append(countNeeded).append("\n")
                .append("Лишние: ").append(countExtra).append("\n")
                .toString();
    }

    public static String getData(Set<Map.Entry<FilterCanPair, PairCanId>> resultSet) {
        AtomicInteger atomicInteger = new AtomicInteger();
        return resultSet.stream().map(e -> "numberFilter=" + atomicInteger.getAndAdd(1) + "\n" +
                e.getKey().toStringReport())
                .collect(Collectors.joining("\n"));
    }

}
