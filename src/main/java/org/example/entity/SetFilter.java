package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@Getter
public class SetFilter {
    private Set<FilterCanPair> filterCanPairs;
    private Set<Integer> extraSaBuff;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SetFilter setFilter = (SetFilter) o;
        return Objects.equals(filterCanPairs, setFilter.filterCanPairs) && Objects.equals(extraSaBuff, setFilter.extraSaBuff);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filterCanPairs, extraSaBuff);
    }

    @Override
    public String toString() {
        return "SetFilter{" +
                "filterCanPairs=" + filterCanPairs +
                ", extraSaBuff=" + extraSaBuff +
                '}';
    }
}
