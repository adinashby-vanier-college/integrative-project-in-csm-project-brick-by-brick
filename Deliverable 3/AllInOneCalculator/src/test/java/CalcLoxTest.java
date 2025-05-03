import edu.vanier.brickbybrick.allinonecalculator.calclox.CalcLoxRunner;
import edu.vanier.brickbybrick.allinonecalculator.calclox.CalculatorFrontend;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class SuccessResult extends RuntimeException {
    public final String result;

    public SuccessResult(String result) {
        this.result = result;
    }
}

class CalculatorFrontendImpl extends CalculatorFrontend {
    public CalculatorFrontendImpl() {
        this.variables = new HashMap<>();
        variables.put("a", 0.25);
        variables.put("b", 0.5);
    }

    @Override
    public void output(String result) {
        throw new SuccessResult(result);
    }
}

public class CalcLoxTest {
    @Test
    public void testOutput() {
        CalculatorFrontendImpl frontend = new CalculatorFrontendImpl();

        String source = """
                output 3;
                """;

        try {
            CalcLoxRunner.run(frontend, source);
        } catch (SuccessResult e) {
            assert e.result != null;
            assert e.result.equals("3.0");
            return;
        }

        assert false;
    }
}
