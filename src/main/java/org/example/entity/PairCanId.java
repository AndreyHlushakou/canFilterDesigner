package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class PairCanId {
    private Set<Integer> neededSa;
    private Set<Integer> extraSa;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PairCanId pairCanId = (PairCanId) o;
        return Objects.equals(extraSa, pairCanId.extraSa) && Objects.equals(neededSa, pairCanId.neededSa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(extraSa, neededSa);
    }

    @Override
    public String toString() {
        return "PairCanId{" +
                "\nneededSa=" +
                neededSa.stream()
                        .map(v -> String.format("0x%02X", v))
                        .sorted()
                        .collect(Collectors.toCollection(LinkedHashSet::new)) +
                "\nextraSa=" +
                extraSa.stream()
                        .map(v -> String.format("0x%02X", v))
                        .sorted()
                        .collect(Collectors.toCollection(LinkedHashSet::new)) +
                '}';
    }

    public static Comparator<PairCanId> comparatorPairCanId = new ComparatorByExtra().thenComparing(new ComparatorByNeeded().reversed());

    static class ComparatorByExtra implements Comparator<PairCanId> {

        @Override
        public int compare(PairCanId o1, PairCanId o2) {
            return Integer.compare(o1.getExtraSa().size(), o2.getExtraSa().size());
        }
    }

    static class ComparatorByNeeded implements Comparator<PairCanId> {

        @Override
        public int compare(PairCanId o1, PairCanId o2) {
            return Integer.compare(o1.getNeededSa().size(), o2.getNeededSa().size());
        }
    }
}
