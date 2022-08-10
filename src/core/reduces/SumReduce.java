package core.reduces;

import java.util.LinkedList;
import java.util.List;

public class SumReduce {

    public static List<Long> reduce(List<Long> args1, List<Long> args2) {
        SumTask task = new SumTask();
        Long aux1 = task.doSomething(args1);
        Long aux2 = task.doSomething(args2);
        List<Long> result = new LinkedList<>();
        result.add(aux1 + aux2);
        return result;
    }

}
