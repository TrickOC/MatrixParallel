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
    private int used;

    public MapReduce() {
        int core = Runtime.getRuntime().availableProcessors();
        partitions = 2 * core;
        segments = new LinkedList<>();
        pool = Executors.newFixedThreadPool(core);
        used = 0;
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
        // Atualiza o numero de vezes que foi usado o mapReduce;
        used++;
        System.out.println("Operation " + used + " of MapReduce.");
    }

    public long parallelReduce() {
        while (segments.size() >= 2) {
            List<Future<List<Long>>> results = new LinkedList<>();
            Future<List<Long>> result;

            while ((result = processSegments()) != null)
                results.add(result);
            //System.out.println("results of segments... " + results.size());

            try {
                for (Future<List<Long>> r : results) {
                    segments.add(r.get());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //System.out.println("step of segments finished");
            results.clear();
        }
        pool.shutdown();
        try {
            long aux = segments.get(0).get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        segments.clear();

        System.out.println("Finished");
        return aux;
    }

    private Future<List<Long>> processSegments() {
        try {
            return pool.submit(new GenericReduce(segments.remove(0), segments.remove(0)));
        } catch (Exception e) {
            return null;
        }
    }

}
