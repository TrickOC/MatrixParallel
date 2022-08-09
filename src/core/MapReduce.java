package core;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MapReduce {
    private final List<List<Integer>> segments;
    private final List<List<Integer>> aux_segments;
    private final int partitions;
    ExecutorService pool;

    public MapReduce() {
        int core = Runtime.getRuntime().availableProcessors();
        partitions = 2 * core;
        segments = new LinkedList<>();
        aux_segments = new LinkedList<>();
        pool = Executors.newFixedThreadPool(core);
    }

    public void mapInput(List<Integer> input) {
        int partitionSize = input.size() / partitions;
        System.out.println("Partition size: " + partitionSize + " in " + partitions + " partitions");
        for (int i = 0; i < input.size(); i += partitionSize)
            segments.add(input.subList(i, i + partitionSize));
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

    public List<List<Long>> mapMatrix(List<List<Integer>> mtxf, List<List<Integer>> mtxs) {
        List<List<Long>> mtx_result = new LinkedList<>();
        List<List<Integer>> aux = new LinkedList<>();

        for (int i = 0; i < mtxs.size(); ++i) {
            aux.add(new LinkedList<>());
            for (int j = 0; j < mtxs.get(i).size(); ++j) {
                aux.get(i).add(mtxs.get(j).get(i));
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
        return mtx_result;
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

    public long parallelSumReduce() {
        while (segments.size() >= 2) {
            List<Future<List<Integer>>> results = new LinkedList<>();
            Future<List<Integer>> result;

            while ((result = processSegments(0)) != null)
                results.add(result);

            System.out.println("results of segments... " + results.size());

            try {
                for (Future<List<Integer>> r : results) {
                    segments.add(r.get());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("step of segments finished");
            results.clear();
        }
        pool.shutdown();
        long aux = segments.get(0).get(0);
        segments.clear();

        System.out.println("Finished Sum");
        return aux;
    }

    private Future<List<Integer>> processSegments(int op) {
        try {
            return op == 0 ?
                    pool.submit(new GenericReduce(segments.remove(0), segments.remove(0), 0)) :
                    pool.submit(new GenericReduce(segments.remove(0), aux_segments.remove(0), op));
        } catch (Exception e) {
            return null;
        }
    }

}
