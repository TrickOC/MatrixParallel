package core;

import java.util.*;
import java.util.concurrent.Future;

public class MultiMatrixMap {
    private final Map<Pair<Integer, Integer>, Integer> matrix1, matrix2, result;
    private final Pair<Integer,Integer> tam1, tam2;

    public MultiMatrixMap(Map<Pair<Integer, Integer>, Integer> matrix1,Pair<Integer,Integer> tam1, Map<Pair<Integer, Integer>, Integer> matrix2, Pair<Integer,Integer> tam2) {
        this.matrix1 = matrix1;
        this.tam1 = tam1;
        this.matrix2 = matrix2;
        this.tam2 = tam2;
        this.result = new LinkedHashMap<>();
    }

    public Map<Pair<Integer, Integer>, Integer> mapMatrix() {
        Pair<Integer, Integer> key_aux = new Pair<>(0,0);
        for (int i = 0; i < tam1.first; ++i) {
            for (int j = 0; j < tam2.second; ++j){
                for (int k = 0; k < tam2.first; ++k){
                    key_aux.first = i;
                    key_aux.second = j;
                    result.put(key_aux, result.get(key_aux) + matrix1.get(new Pair<>(i,k)) * matrix2.get(new Pair<>(k,j)));
                }
            }
        }
        for (int i = 0; i < mtxf.size(); ++i) {
            mtx_result.add(new LinkedList<>());
            for (int j = 0; j < mtxs.size(); ++j){
                mapMultiInput(mtxf.get(i), aux.get(j));
                mapInput(parallelMultiReduce());
                mtx_result.get(i).add(parallelSumReduce());
            }
        }
        aux.clear();
        return result;
    }

    public void mapMultiInput(List<Integer> input1, List<Integer> input2) {
        if (input1.size() == input2.size()) {
            int partitionSize = input1.size() / partitions;
            System.out.println("Partition size: " + partitionSize + " in " + partitions + " partitions");
            for (int i = 0; i < input1.size(); i += partitionSize) {
                segments.add(input1.subList(i, i + partitionSize));
                aux_segments.add(input2.subList(i, i + partitionSize));
            }
        }
    }

    public List<Integer> parallelMultiReduce() {
        List<Integer> results = new LinkedList<>();
        while (segments.size() + aux_segments.size() >= 2) {
            Future<List<Integer>> result;

            while ((result = processSegments(1)) != null)
                try {
                    results.addAll(result.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            System.out.println("results of segments... " + results.size());
        }
        pool.shutdown();
        segments.clear();
        aux_segments.clear();
        System.out.println("Finished Multiplier");
        return results;
    }
}
