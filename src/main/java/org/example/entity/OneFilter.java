package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@Getter
public class OneFilter {
    private FilterCanPair filterCanPair;
    private Set<Integer> extraSa;
    private Set<Integer> neededSa;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OneFilter that = (OneFilter) o;
        return Objects.equals(filterCanPair, that.filterCanPair) && Objects.equals(extraSa, that.extraSa) && Objects.equals(neededSa, that.neededSa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filterCanPair, extraSa, neededSa);
    }

    @Override
    public String toString() {
        return "OneFilterAndExtra{" +
                "\nfilterCanPair=" + filterCanPair +
                "\nextraSa=" + extraSa +
                "\nneededSa=" + neededSa +
                '}';
    }

    public static class Comparator1 implements Comparator<OneFilter> {
        public static Comparator<OneFilter> comparator = new Comparator1();

        @Override
        public int compare(OneFilter o1, OneFilter o2) {
            return Integer.compare(o1.getExtraSa().size(), o2.getExtraSa().size());
        }
    }
}

