package org.example;

import org.example.entity.FilterCanPair;

import java.util.*;

import static org.example.Utils.*;
import static org.example.Utils.filtersForCanArr;

public class Main {

    //массив canId который надо обработать и найти оптимальные фильтры
    public static final int[] canId_arr = new int[] {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,       0x0B,                   0x0F,
            0x10, 0x11, 0x12,                         0x17,       0x19,             0x1C, 0x1D, 0x1E,
            0x20, 0x21, 0x22, 0x23, 0x24,                   0x28,                                     0x2F,
            0x30, 0x31, 0x32,                                           0x3A,
                                                      0x47,                               0x4D,
                        0x52,                                     0x59,


                                           0x85,             0x88, 0x89,             0x8C,
                                           0x95,                               0x9B,
                                                                                          0xAD,

                                                                                                 0xCE, 0xCF,
            0xD0,       0xD2,                                                             0xDD,
                                           0xE5,                                                 0xEE,
                                     0xF4

    };

    public static void main(String[] args) {
//        int FilterMaskId = 0x00;
//        int FilterId     = 0x00;
        int FilterMaskId = 0b1100_1100;
        int FilterId     = 0b0000_0000;
        checkCanId(FilterMaskId, FilterId);

        /////////////////////////////////////////////////////////
//        soutMaps(maps);
//        soutQuantitySizeMaps();

        /////////////////////////////////////////////////////////
//        System.out.println("canId_arr.length=" + canId_arr.length);
//        System.out.println("filtersForCanArr.size=" + maps.size());

        /////////////////////////////////////////////////////////
        Map<FilterCanPair, Set<Integer>> filtersForCanArr = filtersForCanArr(canId_arr, 1);
//        soutSortedMapsBySize(filtersForCanArr);
//        soutMapsBySize(filtersForCanArr);


    }

}
