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
//    private Set<Integer> defaultSa;
    private Set<Integer> neededSa;
    private Set<Integer> extraSa;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PairCanId pairCanId = (PairCanId) o;
        return Objects.equals(neededSa, pairCanId.neededSa) && Objects.equals(extraSa, pairCanId.extraSa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(neededSa, extraSa);
    }

    @Override
    public String toString() {
        return "PairCanId{" +
//                "\ndefaultSa=" +
//                defaultSa.stream()
//                        .map(v -> String.format("0x%02X", v))
//                        .sorted()
//                        .collect(Collectors.toCollection(LinkedHashSet::new)) +
                "\nneededSa=" +
                neededSa.stream()
                        .map(v -> String.format("0x%02X", v))
                        .sorted()
                        .collect(Collectors.toCollection(LinkedHashSet::new)) +
                "\n extraSa=" +
                extraSa.stream()
                        .map(v -> String.format("0x%02X", v))
                        .sorted()
                        .collect(Collectors.toCollection(LinkedHashSet::new)) +
                "}\n";
    }


    public String toStringReport() {
        return "neededSa=" +
                neededSa.stream()
                        .map(v -> String.format("0x%02X", v))
                        .sorted()
                        .collect(Collectors.toCollection(LinkedHashSet::new)) + "\n" +
                "extraSa=" +
                extraSa.stream()
                        .map(v -> String.format("0x%02X", v))
                        .sorted()
                        .collect(Collectors.toCollection(LinkedHashSet::new)) + "\n";
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
