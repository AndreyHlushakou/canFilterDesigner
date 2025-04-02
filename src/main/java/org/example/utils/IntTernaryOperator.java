package org.example.utils;

@FunctionalInterface
public interface IntTernaryOperator {
    boolean test(int canId, int FilterMaskId, int FilterId);

    IntTernaryOperator INT_TERNARY_OPERATOR = (canId, FilterMaskId, FilterId) -> ((canId & FilterMaskId) == FilterId);
}
