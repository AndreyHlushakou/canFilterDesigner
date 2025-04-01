package org.example;

import java.util.regex.Pattern;

import static org.example.Utils.getFilters;

public class HandlerFiltersCanId {

    private static final String BINARY_REGEX = "^0b[01]{8}$";
    private static final Pattern binaryPattern = Pattern.compile(BINARY_REGEX);
    private static final String HEX_REGEX = "^0x[0-9a-fA-F]{2}$";
    private static final Pattern hexPattern = Pattern.compile(HEX_REGEX);

    private HandlerFiltersCanId() {}

    public static final String INCORRECT = "incorrect";

    public static String getText(String filterMaskId, String filterId) {
        int filterMaskIdInt = parseInput(filterMaskId);
        int filterIdInt = parseInput(filterId);
        if (0x00 <= filterMaskIdInt && filterMaskIdInt <= 0xFF && 0x00 <= filterIdInt && filterIdInt <= 0xFF) {
            return getFilters(filterMaskIdInt, filterIdInt);
        } else return INCORRECT;
    }

    public static int parseInput(String str) {
        String bin = str;
        int radix = 10;
        if (!str.isEmpty()) {
            if (binaryPattern.matcher(str).find()) {
                bin = str.replaceAll("0b", "");
                radix = 2;
            } else if (hexPattern.matcher(str).find()) {
                bin = str.replaceAll("0x", "");
                radix = 16;
            }
            try {
                return Integer.parseInt(bin, radix);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

}
