package core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MultiMatrixMap {
    private final Map<Integer, Map<Integer, Integer>> matrix1, matrix2;
    private final int row1, col1, row2, col2;

    public MultiMatrixMap(Map<Integer, Map<Integer, Integer>> matrix1, int row1, int col1, Map<Integer, Map<Integer, Integer>> matrix2, int row2, int col2) {
        this.matrix1 = matrix1;
        this.row1 = row1;
        this.col1 = col1;
        this.matrix2 = matrix2;
        this.row2 = row2;
        this.col2 = col2;
    }

    public Map<Integer, Map<Integer, Long>> mapMatrix() {
        Map<Integer, Map<Integer, Long>> result = new HashMap<>();
        List<Long> aux = new LinkedList<>();
        MapReduce mr = new MapReduce();
        for (int i = 0; i < row1; ++i) {
            result.put(i, new HashMap<>());
            for (int j = 0; j < col2; ++j) {
                for (int k = 0; k < row2; ++k)
                    aux.add((long) matrix1.get(i).get(k) * matrix2.get(k).get(j));
                mr.mapInput(aux);
                result.get(i).put(j, mr.parallelReduce());
                aux.clear();
            }
        }
        return result;
    }
}
