package appl;

import core.MultiMatrixMap;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static Map<Integer, Map<Integer, Integer>> getMatrixFile(String path) {
        Map<Integer, Map<Integer, Integer>> matrix = new HashMap<>();
        int i = 0, j = 0;
        try {
            File arq = new File(path);
            Scanner reader = new Scanner(arq);
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().replaceAll("[^\\d]+", ";").split(";");
                matrix.put(i, new LinkedHashMap<>());
                for (String d : data) {
                    if (!d.isEmpty()) {
                        matrix.get(i).put(j, Integer.valueOf(d));
                        j++;
                    }
                }
                j = 0;
                i++;
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return matrix;
    }

    public static void main(String[] args) {
        Map<Integer, Map<Integer, Integer>> matrix1 = getMatrixFile("/home/pcarmo/Documentos/[BCC447] Programação Paralela/TP1/matrizes/m1_100_100.txt");
        Map<Integer, Map<Integer, Integer>> matrix2 = getMatrixFile("/home/pcarmo/Documentos/[BCC447] Programação Paralela/TP1/matrizes/m2_100_100.txt");
        Map<Integer, Map<Integer, Long>> matrix_result;
        long tempo;

        System.out.println("Calculo em Paralelo com reduce:");
        tempo = System.currentTimeMillis();
        matrix_result = new MultiMatrixMap(matrix1, matrix2).mapMatrix();
        tempo = System.currentTimeMillis() - tempo;

        System.out.println("Tempo de execucao: " + tempo + "ms");
        System.out.println("Resultado da multiplicacao das Matrizes:");
        matrix_result.forEach((k, r) -> System.out.println(k + ": " + r));

        System.out.println("\nCalculo em com for:");
        matrix_result.clear();
        matrix_result = new HashMap<>();
        tempo = System.currentTimeMillis();
        for (int i = 0; i < matrix1.size(); ++i) {
            matrix_result.put(i, new HashMap<>());
            for (int j = 0; j < matrix2.get(0).size(); ++j) {
                matrix_result.get(i).put(j, 0L);
                for (int k = 0; k < matrix2.size(); ++k)
                    matrix_result.get(i).put(j, matrix_result.get(i).get(j) + ((long) matrix1.get(i).get(k) * matrix2.get(k).get(j)));
            }
        }
        tempo = System.currentTimeMillis() - tempo;
        System.out.println("Tempo de execucao: " + tempo + "ms");
        System.out.println("Resultado da multiplicacao das Matrizes:");
        matrix_result.forEach((k, r) -> System.out.println(k + ": " + r));
    }
}
