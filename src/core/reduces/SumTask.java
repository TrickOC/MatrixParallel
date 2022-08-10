package core.reduces;

import java.util.List;

public class SumTask {

    public Long doSomething(List<Long> args) {
        long result = 0;

        for (long aux : args)
            result += aux;

        return result;
    }


}
