package core.reduces;

import java.util.List;

public class SumTask {

    public Integer doSomething(List<Integer> args) {
        int result = 0;

        for (int aux : args)
            result += aux;

        return result;
    }


}
