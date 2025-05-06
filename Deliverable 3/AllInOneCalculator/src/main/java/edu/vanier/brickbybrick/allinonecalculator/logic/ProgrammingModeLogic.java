package edu.vanier.brickbybrick.allinonecalculator.logic;

import edu.vanier.brickbybrick.allinonecalculator.calclox.CalcLoxRunner;

import java.util.ArrayList;

/**
 * This class contains the logic for the programming mode of the calculator.
 */
public class ProgrammingModeLogic {
    private final ComputeEngine computeEngine = new ComputeEngine();
    private final CalcLoxRunner calcLoxRunner = new CalcLoxRunner();

    private ArrayList<String> instructions = new ArrayList<>();

    /**
     * Adds a variable to the ComputeEngine's variables.
     * @param name The name of the variable
     * @param value The value of the variable as a string
     * @return true if the variable was added successfully, false otherwise
     */
    public boolean addVariableToComputeEngine(String name, String value) {
        try {
            double doubleValue = Double.parseDouble(value);
            computeEngine.getVariables().put(name, doubleValue);
            System.out.println(computeEngine.getVariables());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String generateCode() {
        return null;
    }

    public void addInstruction(String instruction) {
    }
}
