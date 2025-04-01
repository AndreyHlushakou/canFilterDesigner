package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Comparator;
import java.util.Objects;

import static org.example.Utils.intToHex;

@AllArgsConstructor
@Getter
public class FilterCanPair {
    private final int FilterMaskId;
    private final int FilterId;

    @Override
    public String toString() {
        return "\nFilterCanPair{" +
                "\nFilterMaskId=" + intToHex(FilterMaskId) +
                "\nFilterId=" + intToHex(FilterId) +
                "}\n";
    }

    public String toStringReport() {
        return "\nFilterMaskId=" + intToHex(FilterMaskId) +
                "\nFilterId=" + intToHex(FilterId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilterCanPair that = (FilterCanPair) o;
        return FilterMaskId == that.FilterMaskId && FilterId == that.FilterId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(FilterMaskId, FilterId);
    }

    public static final Comparator<FilterCanPair> comparator = new FilterCanPair.FilterCanId1().thenComparing(new FilterCanPair.FilterCanId2());

    static class FilterCanId1 implements Comparator<FilterCanPair> {
        @Override
        public int compare(FilterCanPair o1, FilterCanPair o2) {
            return Integer.compare(o1.getFilterMaskId(), o2.getFilterMaskId());
        }
    }

    static class FilterCanId2 implements Comparator<FilterCanPair> {
        @Override
        public int compare(FilterCanPair o1, FilterCanPair o2) {
            return Integer.compare(o1.getFilterId(), o2.getFilterId());
        }
    }

}
