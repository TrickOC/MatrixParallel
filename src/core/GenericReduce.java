package core;

import core.reduces.SumReduce;

import java.util.List;
import java.util.concurrent.Callable;

public class GenericReduce implements Callable<List<Integer>> {

    private final List<Integer> args1;
    private final List<Integer> args2;

    public GenericReduce(List<Integer> args1, List<Integer> args2) {
        this.args1 = args1;
        this.args2 = args2;
    }

    @Override
    public List<Integer> call() {
        return SumReduce.reduce(args1, args2);
    }

}
