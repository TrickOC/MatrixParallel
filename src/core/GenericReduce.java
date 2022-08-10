package core;

import core.reduces.SumReduce;

import java.util.List;
import java.util.concurrent.Callable;

public class GenericReduce implements Callable<List<Long>> {

    private final List<Long> args1;
    private final List<Long> args2;

    public GenericReduce(List<Long> args1, List<Long> args2) {
        this.args1 = args1;
        this.args2 = args2;
    }

    @Override
    public List<Long> call() {
        return SumReduce.reduce(args1, args2);
    }

}
