package org.example.entity.old;

import org.example.entity.FilterCanPair;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.UtilsFilterCanPair.totalMapFilters;

public class CombinationCanIdAndFilters {
    private final Set<Integer> canIds;
    private final Map<FilterCanPair, Set<Integer>> mapsFilters;

    public CombinationCanIdAndFilters(Set<Integer> canIds) {
        this.canIds = canIds;

        mapsFilters = totalMapFilters.entrySet()
                .stream()
                .filter(entry -> entry.getValue().containsAll(canIds))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public int getSize() {
        return mapsFilters.size();
    }

    public Set<Integer> getCanIds() {
        return canIds;
    }

    public Map<FilterCanPair, Set<Integer>> getMapsFilters() {
        return mapsFilters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CombinationCanIdAndFilters that = (CombinationCanIdAndFilters) o;
        return Objects.equals(canIds, that.canIds) && Objects.equals(mapsFilters, that.mapsFilters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(canIds, mapsFilters);
    }
}
