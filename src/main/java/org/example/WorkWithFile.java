package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkWithFile {

    public static final List<Integer> EMPTY_LIST = new ArrayList<>(0);

    private WorkWithFile() {}

    public static boolean checkPath(File file) {
        return (file != null && file.exists() && file.isFile() && file.canRead());
    }

    public static List<Integer> readFile(File file) {
        ArrayList<Integer> list = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file));) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                String[] arr_str = line.replaceAll(" ", "").split(",");
                List<Integer> arr_list = Arrays.stream(arr_str).map(HandlerFiltersCanId::parseInput).filter(i -> i!=-1).toList();
                list.addAll(arr_list);
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: FileNotFoundException");
            return EMPTY_LIST;
        } catch (NumberFormatException e) {
            System.out.println("ERROR: incorrect number");
        } catch (IOException e) {
            System.out.println("ERROR: IOException");
            return EMPTY_LIST;
        }
        return list;

    }

}
