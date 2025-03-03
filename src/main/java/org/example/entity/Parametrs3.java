package org.example.entity;

import java.util.Set;

public class Parametrs3 {
    private final Set<Integer> subset;
    private final FilterCanPair filterCanPair;
    private final Set<Integer> allCanId;

    public Parametrs3(Set<Integer> subset, FilterCanPair filterCanPair, Set<Integer> allCanId) {
        this.subset = subset;
        this.filterCanPair = filterCanPair;
        this.allCanId = allCanId;
    }

    @Override
    public String toString() {
        return "Parametrs3{" +
                "\nsubset=" + subset +
                "\nfilterCanPair=" + filterCanPair +
                "\nallCanId=" + allCanId +
                "}\n";
    }
}
