package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkWithFile {

    public static final List<Integer> EMPTY_LIST = new ArrayList<>(0);

    private WorkWithFile() {}

    public static boolean checkPath(File file) {
        boolean b1 = file != null;
        assert file != null;
        boolean b2 = file.exists();
        boolean b3 = file.isFile();
        boolean b4 = file.canRead();
        System.out.println(b1 + " " + b2 + " " + b3 + " " + b4);
        return b2 && b3 && b4;
    }

    public static List<Integer> readFile(File file) {
        List<Integer> CAN_ID_LIST = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file));) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                String[] arr_str = line.replaceAll(" ", "").split(",");
                List<Integer> arr_list = Arrays.stream(arr_str).map(HandlerFiltersCanId::parseInput).filter(i -> i!=-1).toList();
                CAN_ID_LIST.addAll(arr_list);
                bufferedReader.close();
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
        return CAN_ID_LIST;

    }

    public static boolean writeFile(File file, String data) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(data);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("ERROR: write");
            return false;
        }
        return true;
    }

}
