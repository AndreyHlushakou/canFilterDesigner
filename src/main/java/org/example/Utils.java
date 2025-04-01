package org.example;

import java.util.stream.IntStream;

import static org.example.IntTernaryOperator.INT_TERNARY_OPERATOR;

public final class Utils {

    private Utils() {}

    private static final String TEXT_START;

    private static final int DEFAULT_BYTE = 0x00;

    //для вывода checkCanId
    static {
        StringBuilder str_ = new StringBuilder("HEX  | BIN\n");
        IntStream.range(0, intToHexAndBin(DEFAULT_BYTE).length()).forEach(i -> str_.append("-"));
        TEXT_START = str_.toString();
    }

    public static String intToHexAndBin(int data) {
        String hex = intToHex(data);
        String bin = intToBin(data);
        return hex + " | " + bin;
    }

    public static String intToHex(int data) {
        String hex = toStrPlusZeros(2, Integer.toHexString(data));
        return "0x" + hex;
    }
    public static String intToBin(int data) {
        String bin = toStrPlusZeros(8, Integer.toBinaryString(data));
        return "0b" + bin;
    }

    private static String toStrPlusZeros(int quantityZeros, String str) {
        StringBuilder zeros = new StringBuilder();
        int len = quantityZeros - str.length();
        while (len-- != 0) zeros.append("0");
        return zeros.append(str).toString().toUpperCase();
    }

    //вывод всех кэнАйди для этого фильтра
    public static void soutAllCanIdByFilter(int FilterMaskId, int FilterId) {
        System.out.println(getFilters(FilterMaskId, FilterId));
    }

    public static String getFilters(int FilterMaskId, int FilterId) {
        StringBuilder str = new StringBuilder()
                .append(TEXT_START).append("\n")
                .append(intToHexAndBin(FilterMaskId)).append(" - FilterMaskId\n")
                .append(intToHexAndBin(FilterId)).append(" - FilterId\n")
                .append("\n")
                .append(TEXT_START).append("\n")
        ;

        IntStream.rangeClosed(0x00, 0xFF).forEach(canId -> {
            boolean b = INT_TERNARY_OPERATOR.test(canId, FilterMaskId, FilterId);
            if (b) {
                str.append(intToHexAndBin(canId)).append("\n");
            }
        });
        return str.toString();
    }

}
