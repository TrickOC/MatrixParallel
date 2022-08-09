package core.reduces;

import java.util.LinkedList;
import java.util.List;

public class SumReduce {

    public static List<Integer> reduce(List<Integer> args1, List<Integer> args2) {
        SumTask task = new SumTask();
        Integer aux1 = task.doSomething(args1);
        Integer aux2 = task.doSomething(args2);
        List<Integer> result = new LinkedList<>();
        result.add(aux1 + aux2);
        return result;
    }

}
