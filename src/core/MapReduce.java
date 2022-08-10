package core;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MapReduce {
    private final List<List<Integer>> segments;
    private final int partitions;
    ExecutorService pool;

    public MapReduce() {
        int core = Runtime.getRuntime().availableProcessors();
        partitions = 2 * core;
        segments = new LinkedList<>();
        pool = Executors.newFixedThreadPool(core);
    }

    public void mapInput(List<Integer> input) {
        int partitionSize = input.size() / partitions;
        System.out.println("Partition size: " + partitionSize + " in " + partitions + " partitions");
        for (int i = 0; i < input.size(); i += partitionSize)
            segments.add(input.subList(i, i + partitionSize));
    }

    public long parallelReduce() {
        while (segments.size() >= 2) {
            List<Future<List<Integer>>> results = new LinkedList<>();
            Future<List<Integer>> result;

            while ((result = processSegments()) != null)
                results.add(result);

            System.out.println("results of segments... " + results.size());

            try {
                for (Future<List<Integer>> r:results) {
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

        System.out.println("Finished");
        return aux;
    }

    private Future<List<Integer>> processSegments() {
        try {
            return pool.submit(new GenericReduce(segments.remove(0), segments.remove(0)));
        } catch (Exception e) {
            return null;
        }
    }

}
