package org.example;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.Utils.maps;
import static org.example.Utils.intToHex;

//класс для хранения карты фильтров (со списком значений удовлетворяющих этот фильтр) для определенного canId
class CanIdAndFilters {
    private final int canId;
    private final Map<FilterCanPair, Set<Integer>> mapArr;

    public CanIdAndFilters(int canId) {
        this.canId = canId;

        mapArr = maps.entrySet()
                .stream()
                .filter((entry) -> entry.getValue().contains(canId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


//        maps.forEach((key, value) -> {
//            if (value.contains(canId)) {
//                mapArr.put(key, value);
//            };
//        });
    }

    @Override
    public String toString() {
        return "Arr{" +
                "\ncanId=" + intToHex(canId) +
                "\nmapArr=\n" + mapArr.entrySet().stream().map((entry) -> {
            StringBuilder builder = new StringBuilder();
            String key = entry.getKey().toString();
            String value = entry.getValue().stream().map(Utils::intToHex).collect(Collectors.joining(" "));
            return builder.append("\nkey=").append(key).append("\nvalue=").append(value);
        }).toList() +
                "}\n\n";
    }
}