package edu.vanier.brickbybrick.allinonecalculator.calclox;

import edu.vanier.brickbybrick.allinonecalculator.calclox.exceptions.CalcLoxRunnerError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The runner of the CalcLox language interpreter powering the programmable feature of the calculator.
 * This is the only class in the package that is meant to be imported and used by classes outside of this package.
 *
 * @author Qian Qian
 */
public class CalcLoxRunner {
    private final static Logger logger = LoggerFactory.getLogger(CalcLoxRunner.class);

    /**
     * The CalcLox interpreter that runs the programmable feature of the calculator.
     */
    private Interpreter interpreter = new Interpreter();
    private static boolean hadError = false;
    private static boolean hadRuntimeError = false;

    /**
     * Execute the CalcLox interpreter on the given source code.
     * @param source the CalcLox source code to be executed
     */
    public static void run(String source) {
        Scanner scanner = new Scanner(source);
    }

    public static void runtimeError(RuntimeError error) {
        CalcLoxRunnerError runnerError = new CalcLoxRunnerError(error.getMessage(), error.token.line);
        logger.error("Runtime Error: {}\n\t@line {}", error.getMessage(), error.token.line);
        hadRuntimeError = true;
        throw runnerError;
    }
}
