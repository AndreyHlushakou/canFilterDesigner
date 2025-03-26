package org.example;

import lombok.NoArgsConstructor;
import org.example.entity.OneFilterAndExtra;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.example.Utils.*;

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
//        int FilterMaskId = 0xC8;
//        int FilterId     = 0x00;
//        int FilterMaskId = 0b1110_1110;
//        int FilterId     = 0b1110_0100;
//        checkCanId(FilterMaskId, FilterId);

        /////////////////////////////////////////////////////////
//        soutMaps(maps);
//        soutQuantitySizeMaps();

        /////////////////////////////////////////////////////////
//        System.out.println("canId_arr.length=" + canId_arr.length);
//        System.out.println("filtersForCanArr.size=" + maps.size());

        /////////////////////////////////////////////////////////
//        Map<FilterCanPair, Set<Integer>> filtersForCanArr = filtersForCanArr(canId_arr, 0);
//        soutSortedMapsBySize(filtersForCanArr);
//        soutMapsBySize(filtersForCanArr);

        /////////////////////////////////////////////////////////
        process(canId_arr);
    }

    static Map<Integer, Set<OneFilterAndExtra>> mapSet;

    public static void process(int[] canId_arr) {
        mapSet = Arrays.stream(canId_arr)
                .boxed()
                .collect(Collectors.toMap(
                        //canId
                        i -> i,
                        //Set<OneFilterAndExtra>
                        i -> maps.entrySet().stream()
                                .filter(e -> e.getValue().stream()
                                        .anyMatch(v -> v.equals(i)))
                                .map(e -> new OneFilterAndExtra(
                                        e.getKey(), //filterCanPair
                                        e.getValue().stream() //extraSa
                                                .filter(v -> !v.equals(i))
                                                .collect(Collectors.toSet()),
                                        e.getValue().stream() //neededSa
                                                .filter((v) -> Arrays.stream(canId_arr)
                                                        .boxed()
                                                        .anyMatch(c -> c.equals(v)))
                                                .collect(Collectors.toSet())))
                                .collect(Collectors.toSet())
                ));
//        soutMapMap(mapSet);

    }

    public void startRecursive(Map<Integer, Set<OneFilterAndExtra>> mapSet, int[] canId_arr, int position) {
        Chain chain = new Chain();
        for (Set<OneFilterAndExtra> value : mapSet.values()) {
            value
        }
    }

    public static void recursive() {

    }


    class Chain {

        Function<Set<OneFilterAndExtra>,Integer> function = (value, list, nextFunction) -> {
            for (OneFilterAndExtra oneFilterAndExtra : value) {

            }
            return
        };

        public void join(Set<OneFilterAndExtra> value) {

        }

    }


}
