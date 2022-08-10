package appl;

import java.io.File;
import java.util.*;

public class Main {
    public static Map<Integer, Map <Integer, Integer>> getMatrixFile(String path) {
        Map<Integer, Map <Integer, Integer>> matrix = new LinkedHashMap<>();
        int i=0,j=0;
        try {
            File arq = new File(path);
            Scanner reader = new Scanner(arq);
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().replaceAll("[^\\d]+",";").split(";");
                for (String d : data) {
                    if (!d.isEmpty()) {
                        matrix.put(new Pair<>(i,j), Integer.valueOf(d));
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
        Map<Pair<Integer, Integer>, Integer> matrix1 = getMatrixFile("/home/pcarmo/Documentos/[BCC447] Programação Paralela/TP1/matrizes/m1_500_500.txt");
        Map<Pair<Integer, Integer>, Integer> matrix2 = getMatrixFile("/home/pcarmo/Documentos/[BCC447] Programação Paralela/TP1/matrizes/m2_500_500.txt");
        Map<Pair<Integer, Integer>, Integer> matrix_result;
    }
}
