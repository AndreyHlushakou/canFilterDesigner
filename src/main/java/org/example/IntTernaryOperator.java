package org.example;

@FunctionalInterface
interface IntTernaryOperator <T>{
    boolean test(int canId, int FilterMaskId, int FilterId);

    IntTernaryOperator INT_TERNARY_OPERATOR = (canId, FilterMaskId, FilterId) -> ((canId & FilterMaskId) == FilterId);
}
