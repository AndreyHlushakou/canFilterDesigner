package org.example;

import org.example.entity.FilterCanPair;
import org.example.entity.OneFilter;
import org.example.entity.SetFilter;

import java.util.*;
import java.util.stream.Collectors;

import static org.example.Utils.*;
import static org.example.entity.OneFilter.ComparatorOneFilter.comparatorOneFilter;

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
        process(canId_arr);
    }

    static Map<Integer, Set<OneFilter>> mapSet;

    public static void process(int[] canId_arr) {
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
                                .limit(50)
                                .collect(Collectors.toCollection(LinkedHashSet::new))
                ));
//        soutMapMapLessThan10(mapSet);

        Set<SetFilter> setFilters = new Main().startProcess(mapSet, canId_arr);
        System.out.println(setFilters);
    }

    public  Set<SetFilter> startProcess(Map<Integer, Set<OneFilter>> mapSet, int[] canId_arr) {
        Node head;
        Node tail = new Node(null, null);
        head = tail;
        tail.extraSa = new HashSet<>();
        tail.neededSa = new HashSet<>();
        tail.filterCanPairs = new HashSet<>();
        for (Set<OneFilter> value : mapSet.values()) {
            Node newNode = new Node(tail, null);
            newNode.createProcess(value);

            tail.next = newNode;
            tail = newNode;
        }
        Node endNode = new Node(tail, null);
        tail.next = endNode;
        Set<Integer> canId_set = Arrays.stream(canId_arr).boxed().collect(Collectors.toSet());
        Set<SetFilter> setFilters = new HashSet<>();
//        System.gc();
        endNode.createEndProcess(canId_set, setFilters);
        head.runProcess();

        return setFilters;
    }

    interface MyFunction {
        void process();
    }

    class Node {
        private final Node prev;
        private Node next;

        public Node(Node prev, Node next) {
            this.prev = prev;
            this.next = next;
        }

        MyFunction function;
        Set<Integer> extraSa;
        Set<Integer> neededSa;
        Set<FilterCanPair> filterCanPairs;

        public void createProcess(Set<OneFilter> value) {
            this.function = () -> {
                for (OneFilter v : value) {
                    Set<Integer> extraSaBuff = new HashSet<>(v.getExtraSa());
                    Set<Integer> neededSaBuff = new HashSet<>(v.getNeededSa());
                    Set<FilterCanPair> filterCanPairsBuff = new HashSet<>();
                    filterCanPairsBuff.add(v.getFilterCanPair());

                    extraSaBuff.addAll(this.prev.extraSa);
                    neededSaBuff.addAll(this.prev.neededSa);
                    filterCanPairsBuff.addAll(this.prev.filterCanPairs);

                    this.extraSa = extraSaBuff;
                    this.neededSa = neededSaBuff;
                    this.filterCanPairs = filterCanPairsBuff;

                    this.next.function.process();
                }
            };
        }

        public void createEndProcess(Set<Integer> canId_arr, Set<SetFilter> setFilters) {
            this.function = () -> {
                Set<Integer> neededSaBuff = this.prev.neededSa;
                if (neededSaBuff.containsAll(canId_arr)) {
                    Set<FilterCanPair> filterCanPairsBuff = this.prev.filterCanPairs;

                    if (filterCanPairsBuff.size() < 14) {
                        Set<Integer> extraSaBuff = this.prev.extraSa;
                        SetFilter setFilter = new SetFilter(filterCanPairsBuff, extraSaBuff);
                        setFilters.add(setFilter);
//                        if (extraSaBuff.isEmpty()) {
//                            System.out.println(filterCanPairsBuff.size());
//                            System.out.println(filterCanPairsBuff);
//                            System.exit(0);
//                        }
                    }

                }
//                System.gc();
            };
        }

        public void runProcess() {
            this.next.function.process();
        }

    }


}
