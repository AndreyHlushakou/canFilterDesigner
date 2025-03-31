package org.example.entity.old.onefilter;

import org.example.entity.FilterCanPair;
import org.example.entity.old.SetFilter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ProcessLoop {

    public Set<SetFilter> startProcess(Map<Integer, Set<OneFilter>> mapSet, int[] canId_arr) {
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

        interface MyFunction {
            void process();
        }

    }

}
