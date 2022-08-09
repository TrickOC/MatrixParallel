package core.reduces;

import java.util.List;

public class MatrixReduce {
    public static List<Integer> reduce(List<Integer> args1, List<Integer> args2) {
        MatrixTask task = new MatrixTask();
        return task.doSomething(args1, args2);
    }
}
