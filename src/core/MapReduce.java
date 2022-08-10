package core;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MapReduce {
    private final List<List<Long>> segments;
    private final int partitions;
    ExecutorService pool;

    public MapReduce() {
        int core = Runtime.getRuntime().availableProcessors();
        partitions = 2 * core;
        segments = new LinkedList<>();
        pool = Executors.newFixedThreadPool(core);
    }

    public void mapInput(List<Long> input) {
        int partitionSize = input.size() / partitions;
        if (input.size() % partitions > 0)
            partitionSize++;
        //System.out.println("Partition size: " + partitionSize + " in " + partitions + " partitions");
        for (int i = 0; i < input.size(); i += partitionSize) {
            if (i + partitionSize < input.size())
                segments.add(input.subList(i, i + partitionSize));
            else
                segments.add(input.subList(i, input.size()));
        }
    }

    public long parallelReduce() {
        while (segments.size() >= 2) {
            List<Future<List<Long>>> results = new LinkedList<>();
            Future<List<Long>> result;

            while ((result = processSegments()) != null)
                results.add(result);

            try {
                for (Future<List<Long>> r : results) {
                    segments.add(r.get());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            results.clear();
        }
        pool.shutdown();
        long aux = segments.get(0).get(0);
        segments.clear();
        return aux;
    }

    public void shutdown() {
        segments.clear();
        System.out.println("Pool shutdown");
    }

    private Future<List<Long>> processSegments() {
        try {
            return pool.submit(new GenericReduce(segments.remove(0), segments.remove(0)));
        } catch (Exception e) {
            return null;
        }
    }

}
