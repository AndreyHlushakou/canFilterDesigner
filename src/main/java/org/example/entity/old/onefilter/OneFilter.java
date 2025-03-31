package org.example.entity.old.onefilter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.entity.FilterCanPair;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@Getter
public class OneFilter {
    private FilterCanPair filterCanPair;
    private Set<Integer> neededSa;
    private Set<Integer> extraSa;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OneFilter oneFilter = (OneFilter) o;
        return Objects.equals(filterCanPair, oneFilter.filterCanPair) && Objects.equals(neededSa, oneFilter.neededSa) && Objects.equals(extraSa, oneFilter.extraSa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filterCanPair, neededSa, extraSa);
    }

    @Override
    public String toString() {
        return "OneFilter{" +
                "\nfilterCanPair=" + filterCanPair +
                "\nneededSa=" + neededSa +
                "\nextraSa=" + extraSa +
                '}';
    }

    public static Comparator<OneFilter> comparatorOneFilter = new ComparatorByNeededSa().reversed();//new ComparatorByExtraSa().thenComparing(new ComparatorByNeededSa().reversed());

    public static class ComparatorByExtraSa implements Comparator<OneFilter> {
        @Override
        public int compare(OneFilter o1, OneFilter o2) {
            return Integer.compare(o1.getExtraSa().size(), o2.getExtraSa().size());
        }
    }

    public static class ComparatorByNeededSa implements Comparator<OneFilter> {
        @Override
        public int compare(OneFilter o1, OneFilter o2) {
            return Integer.compare(o1.getNeededSa().size(), o2.getNeededSa().size());
        }
    }

}

