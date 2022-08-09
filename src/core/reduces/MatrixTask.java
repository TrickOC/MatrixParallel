package core.reduces;

import java.util.LinkedList;
import java.util.List;

public class MatrixTask {
    public List<Integer> doSomething(List<Integer> args1, List<Integer> args2) {
        List<Integer> result = new LinkedList<>();

        for (int i = 0; i < args1.size(); ++i)
            result.add(args1.get(i) * args2.get(i));

        return result;
    }
}
