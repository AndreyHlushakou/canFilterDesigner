package org.example.utils;

import java.util.List;
import java.util.stream.IntStream;

import static org.example.utils.IntTernaryOperator.INT_TERNARY_OPERATOR;

public final class Utils {

    private Utils() {}

    private static final String TEXT_START_HEX_BIN;
    private static final String TEXT_START_HEX_BIN_DEC;

    private static final int DEFAULT_BYTE = 0x00;

    //для вывода checkCanId
    static {
        StringBuilder str_ = new StringBuilder("HEX  | BIN\n");
        IntStream.range(0, intToHexAndBin(DEFAULT_BYTE).length()).forEach(i -> str_.append("-"));
        TEXT_START_HEX_BIN = str_.toString();

        StringBuilder str_2 = new StringBuilder("HEX  | BIN  | DEC\n");
        IntStream.range(0, intToHexAndBin(DEFAULT_BYTE).length()).forEach(i -> str_2.append("-"));
        TEXT_START_HEX_BIN_DEC = str_2.toString();
    }

    public static String intToHexAndBin(int data) {
        String hex = intToHex(data);
        String bin = intToBin(data);
        return hex + " | " + bin;
    }

    public static String intToHexAndBinAndDec(int data) {
        return intToHexAndBin(data) + " | " + data;
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
    public static String soutAllCanIdByFilter(int FilterMaskId, int FilterId) {
        StringBuilder str = new StringBuilder()
                .append(TEXT_START_HEX_BIN).append("\n")
                .append(intToHexAndBin(FilterMaskId)).append(" - FilterMaskId\n")
                .append(intToHexAndBin(FilterId)).append(" - FilterId\n")
                .append("\n")
                .append(TEXT_START_HEX_BIN).append("\n")
        ;

        IntStream.rangeClosed(0x00, 0xFF).forEach(canId -> {
            boolean b = INT_TERNARY_OPERATOR.test(canId, FilterMaskId, FilterId);
            if (b) {
                str.append(intToHexAndBin(canId)).append("\n");
            }
        });
        return str.toString();
    }

    //вывод всех кэнАйди для этого фильтра
    public static String soutAllCanId(List<Integer> cnaIdList) {
        StringBuilder str = new StringBuilder()
                .append(TEXT_START_HEX_BIN_DEC).append("\n")
                ;

        cnaIdList.forEach(canId -> str.append(intToHexAndBinAndDec(canId)).append("\n"));
        return str.toString();
    }

}
