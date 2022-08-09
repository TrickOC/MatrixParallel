package core;

import core.reduces.MatrixReduce;
import core.reduces.SumReduce;

import java.util.List;
import java.util.concurrent.Callable;

public class GenericReduce implements Callable<List<Integer>> {

    private final List<Integer> args1;
    private final List<Integer> args2;
    private final int op;

    public GenericReduce(List<Integer> args1, List<Integer> args2, int op) {
        this.args1 = args1;
        this.args2 = args2;
        this.op = op;
    }

    @Override
    public List<Integer> call() {
        return switch (op) {
            case 0 -> SumReduce.reduce(args1, args2);
            case 1 -> MatrixReduce.reduce(args1, args2);
            default -> null;
        };
    }

}
