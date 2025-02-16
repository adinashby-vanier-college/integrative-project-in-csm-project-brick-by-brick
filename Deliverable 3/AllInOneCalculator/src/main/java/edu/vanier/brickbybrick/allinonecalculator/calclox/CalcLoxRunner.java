package edu.vanier.brickbybrick.allinonecalculator.calclox;

/**
 * The runner of the CalcLox language interpreter powering the programmable feature of the calculator.
 * This is the only class in the package that is meant to be imported and used by classes outside of this package.
 *
 * @author Qian Qian
 */
public class CalcLoxRunner {
    /**
     * The CalcLox interpreter that runs the programmable feature of the calculator.
     */
    private Interpreter interpreter = new Interpreter();

    /**
     * Execute the CalcLox interpreter on the given source code.
     * @param source the CalcLox source code to be executed
     */
    public static void run(String source) {
        Scanner scanner = new Scanner(source);
    }
}
