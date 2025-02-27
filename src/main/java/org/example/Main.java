package org.example;

import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        long FilterMaskId = 0b11001000;
        long FilterId     = 0b00000000;

        Main.checkCanId(FilterMaskId, FilterId);


    }


    @FunctionalInterface
    interface LongTernaryOperator {
        boolean test(long canId, long FilterMaskId, long FilterId);
    }
    private static final LongTernaryOperator longTernaryOperator = (canId, FilterMaskId, FilterId) -> (canId & FilterMaskId) == FilterId;

    private static final String TEXT_START;

    private static final int DEFAULT_BYTE = 0x00;

    static {
        StringBuilder str_ = new StringBuilder("HEX  | BIN\n");
        IntStream.range(0, printData(DEFAULT_BYTE).length()).forEach(i -> str_.append("-"));
        TEXT_START = str_.toString();
    }

    static void checkCanId(long FilterMaskId, long FilterId) {
        System.out.println(TEXT_START);
        System.out.println(printData(FilterMaskId) + " - FilterMaskId");
        System.out.println(printData(FilterId) + " - FilterId");

        System.out.println();
        System.out.println(TEXT_START);

        for (long canId = 0x00000000; canId <= 0x000000FF; canId++) {
            boolean b = longTernaryOperator.test(canId, FilterMaskId, FilterId);
            if (b) {
                System.out.println(printData(canId));
            }
        }
    }

    static String printData(long data) {
        String hex = toStr(2, Long.toHexString(data));
        String bin = toStr(8, Long.toBinaryString(data));
        return "0x" + hex + " | 0b" + bin;
    }

    static String toStr(int quantityZeros, String str) {
        StringBuilder zeros = new StringBuilder();
        int len = quantityZeros - str.length();
        while (len-- != 0) zeros.append("0");
        return zeros.append(str).toString().toUpperCase();
    }
}