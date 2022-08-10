package core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MultiMatrixMap {
    private final Map<Integer, Map<Integer, Integer>> matrix1, matrix2;

    public MultiMatrixMap(Map<Integer, Map<Integer, Integer>> matrix1, Map<Integer, Map<Integer, Integer>> matrix2) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
    }

    public Map<Integer, Map<Integer, Long>> mapMatrix() {
        Map<Integer, Map<Integer, Long>> result = new HashMap<>();
        MapReduce mr = new MapReduce();
        for (int i = 0; i < matrix1.size(); ++i) {
            result.put(i, new HashMap<>());
            for (int j = 0; j < matrix2.get(0).size(); ++j) {
                List<Long> aux = new LinkedList<>();
                for (int k = 0; k < matrix2.size(); ++k)
                    aux.add((long) matrix1.get(i).get(k) * matrix2.get(k).get(j));
                mr.mapInput(aux);
                result.get(i).put(j, mr.parallelReduce());
            }
        }
        mr.shutdown();
        return result;
    }
}
