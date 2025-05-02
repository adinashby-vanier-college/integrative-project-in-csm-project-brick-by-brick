package edu.vanier.brickbybrick.allinonecalculator.logic;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * The compute engine powering the calculator by parsing and evaluating mathematical expressions
 * in the MathJSON format.
 * <br/>
 * This class is also responsible for serializing and deserializing MathJSON from and to
 * formats like LaTeX.
 *
 * @see <a href="https://cortexjs.io/math-json/">MathJSON Format Documentation</a>
 * @author Qian Qian
 */
public class ComputeEngine {
    private static final Logger logger = LoggerFactory.getLogger(ComputeEngine.class);
    private static final Map<String, Double> CONSTANTS = new HashMap<>();
    
    public ComputeEngine() {
        CONSTANTS.put("Pi", Math.PI);
        CONSTANTS.put("E", Math.E);
    }

    /**
     * Evaluates a MathJSON expression and returns the result as a string.
     * @param mathJson The MathJSON expression to evaluate
     * @return The result of the evaluation as a string
     */
    public String evaluate(String mathJson) {
        try {
            logger.info("Evaluating MathJSON: " + mathJson);
            Object parsed = parse(mathJson);
            Object result = evaluateExpression(parsed);
            logger.info("Evaluation result: " + result);
            return result.toString();
        } catch (JSONException e) {
            logger.error("JSON parsing error: " + e.getMessage());
            throw new RuntimeException("Invalid MathJSON format: " + e.getMessage());
        } catch (ArithmeticException e) {
            logger.error("Arithmetic error: " + e.getMessage());
            throw new RuntimeException("Arithmetic error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error: " + e.getMessage());
            throw new RuntimeException("Error evaluating expression: " + e.getMessage());
        }
    }

    /**
     * Parses a MathJSON string into a Java object structure.
     * @param mathJson The MathJSON string to parse
     * @return The parsed expression as a Java object
     */
    public Object parse(String mathJson) {
        try {
            logger.info("Parsing MathJSON: " + mathJson);
            if (mathJson.startsWith("[") && mathJson.endsWith("]")) {
                return new JSONArray(mathJson);
            } else if (mathJson.startsWith("{") && mathJson.endsWith("}")) {
                return new JSONObject(mathJson);
            } else {
                return mathJson;
            }
        } catch (JSONException e) {
            logger.error("JSON parsing error: " + e.getMessage());
            throw new JSONException("Invalid MathJSON format: " + e.getMessage());
        }
    }

    /**
     * Serializes a MathJSON object to a string.
     * @param mathJson The MathJSON object to serialize
     * @return The serialized MathJSON string
     */
    public String serialize(Object mathJson) {
        return mathJson.toString();
    }

    /**
     * Evaluates a parsed MathJSON expression.
     * @param expression The parsed expression to evaluate
     * @return The result of the evaluation
     */
    private Object evaluateExpression(Object expression) {
        if (expression instanceof JSONArray) {
            return evaluateFunction((JSONArray) expression);
        } else if (expression instanceof JSONObject) {
            return evaluateObject((JSONObject) expression);
        } else if (expression instanceof String) {
            return evaluateSymbol((String) expression);
        } else if (expression instanceof Number) {
            return expression;
        }
        throw new JSONException("Invalid expression type");
    }

    /**
     * Evaluates a MathJSON function expression.
     * @param function The function expression to evaluate
     * @return The result of the function evaluation
     */
    private Object evaluateFunction(JSONArray function) {
        if (function.length() == 0) {
            throw new JSONException("Empty function expression");
        }

        String operator = function.getString(0);
        Object[] args = new Object[function.length() - 1];
        for (int i = 1; i < function.length(); i++) {
            args[i - 1] = evaluateExpression(function.get(i));
        }

        return applyOperator(operator, args);
    }

    /**
     * Applies an operator to its arguments.
     * @param operator The operator to apply
     * @param args The arguments to apply the operator to
     * @return The result of applying the operator
     */
    private Object applyOperator(String operator, Object[] args) {
        switch (operator) {
            case "Add":
                return add(args);
            case "Subtract":
                return subtract(args);
            case "Multiply":
                return multiply(args);
            case "Divide":
                return divide(args);
            case "Power":
                return power(args);
            case "Sin":
                return sin(args);
            case "Cos":
                return cos(args);
            case "Tan":
                return tan(args);
            case "Sqrt":
                return sqrt(args);
            default:
                throw new JSONException("Unknown operator: " + operator);
        }
    }

    // Basic arithmetic operations
    private double add(Object[] args) {
        if (args.length < 2) throw new JSONException("Add requires at least 2 arguments");
        double result = toDouble(args[0]);
        for (int i = 1; i < args.length; i++) {
            result += toDouble(args[i]);
        }
        return result;
    }

    private double subtract(Object[] args) {
        if (args.length != 2) throw new JSONException("Subtract requires exactly 2 arguments");
        return toDouble(args[0]) - toDouble(args[1]);
    }

    private double multiply(Object[] args) {
        if (args.length < 2) throw new JSONException("Multiply requires at least 2 arguments");
        double result = toDouble(args[0]);
        for (int i = 1; i < args.length; i++) {
            result *= toDouble(args[i]);
        }
        return result;
    }

    private double divide(Object[] args) {
        if (args.length != 2) throw new JSONException("Divide requires exactly 2 arguments");
        double denominator = toDouble(args[1]);
        if (denominator == 0) throw new ArithmeticException("Division by zero");
        return toDouble(args[0]) / denominator;
    }

    private double power(Object[] args) {
        if (args.length != 2) throw new JSONException("Power requires exactly 2 arguments");
        return Math.pow(toDouble(args[0]), toDouble(args[1]));
    }

    // Trigonometric functions
    private double sin(Object[] args) {
        if (args.length != 1) throw new JSONException("Sin requires exactly 1 argument");
        return Math.sin(toDouble(args[0]));
    }

    private double cos(Object[] args) {
        if (args.length != 1) throw new JSONException("Cos requires exactly 1 argument");
        return Math.cos(toDouble(args[0]));
    }

    private double tan(Object[] args) {
        if (args.length != 1) throw new JSONException("Tan requires exactly 1 argument");
        return Math.tan(toDouble(args[0]));
    }

    private double sqrt(Object[] args) {
        if (args.length != 1) throw new JSONException("Sqrt requires exactly 1 argument");
        double value = toDouble(args[0]);
        if (value < 0) throw new ArithmeticException("Square root of negative number");
        return Math.sqrt(value);
    }

    // Helper methods
    private Object evaluateObject(JSONObject obj) {
        if (obj.has("num")) {
            return Double.parseDouble(obj.getString("num"));
        } else if (obj.has("sym")) {
            return evaluateSymbol(obj.getString("sym"));
        } else if (obj.has("fn")) {
            return evaluateFunction(obj.getJSONArray("fn"));
        }
        throw new JSONException("Invalid object type");
    }

    private Object evaluateSymbol(String symbol) {
        if (CONSTANTS.containsKey(symbol)) {
            return CONSTANTS.get(symbol);
        }
        throw new JSONException("Unknown symbol: " + symbol);
    }

    private double toDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        throw new JSONException("Cannot convert to number: " + value);
    }
}
