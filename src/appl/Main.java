package appl;

import core.MapReduce;

import java.io.File;
import java.util.*;

public class Main {
    public static List<List<Integer>> getMatrixFile(String path) {
        List<List<Integer>> matrix = new LinkedList<>();
        try {
            File arq = new File(path);
            Scanner reader = new Scanner(arq);
            while (reader.hasNextLine()) {
                List<Integer> row = new LinkedList<>();
                String[] data = reader.nextLine().replaceAll("[^\\d]+",";").split(";");
                for (String d : data) {
                    if (!d.isEmpty())
                        row.add(Integer.valueOf(d));
                }
                matrix.add(row);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return matrix;
    }

    public static void main(String[] args) {
        List<List<Integer>> matrix1 = getMatrixFile("/home/pcarmo/Documentos/[BCC447] Programação Paralela/TP1/matrizes/m1_500_500.txt");
        List<List<Integer>> matrix2 = getMatrixFile("/home/pcarmo/Documentos/[BCC447] Programação Paralela/TP1/matrizes/m2_500_500.txt");
        List<List<Long>> matrix_result;

        MapReduce mr = new MapReduce();
        matrix_result = mr.mapMatrix(matrix1,matrix2);

        System.out.println("Matriz resultado da multiplicacao:");
        matrix_result.forEach(System.out::println);
    }
}
