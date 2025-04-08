package edu.vanier.brickbybrick.allinonecalculator.logic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphingCalculatorLogic {
    private static final Logger logger = LoggerFactory.getLogger(GraphingCalculatorLogic.class);
    private JsonElement functionTree;

    public boolean parseFunction(JsonElement json) {
        try {
            functionTree = json;
            return true;
        } catch (Exception e) {
            logger.error("Error parsing function", e);
            return false;
        }
    }

    public double evaluateAt(double x) {
        if (functionTree == null) {
            return Double.NaN;
        }
        return evaluateNode(functionTree, x);
    }

    private double evaluateNode(JsonElement node, double x) {
        if (node.isJsonPrimitive()) {
            String value = node.getAsString();
            if (value.equals("x")) {
                return x;
            }
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                logger.error("Invalid number format: " + value);
                return Double.NaN;
            }
        }

        if (node.isJsonObject()) {
            JsonObject obj = node.getAsJsonObject();
            if (obj.has("fn")) {
                String fn = obj.get("fn").getAsString();
                JsonArray args = obj.getAsJsonArray("args");

                switch (fn) {
                    case "add":
                        return evaluateNode(args.get(0), x) + evaluateNode(args.get(1), x);
                    case "subtract":
                        return evaluateNode(args.get(0), x) - evaluateNode(args.get(1), x);
                    case "multiply":
                        return evaluateNode(args.get(0), x) * evaluateNode(args.get(1), x);
                    case "divide":
                        return evaluateNode(args.get(0), x) / evaluateNode(args.get(1), x);
                    case "power":
                        return Math.pow(evaluateNode(args.get(0), x), evaluateNode(args.get(1), x));
                    case "sqrt":
                        return Math.sqrt(evaluateNode(args.get(0), x));
                    case "sin":
                        return Math.sin(evaluateNode(args.get(0), x));
                    case "cos":
                        return Math.cos(evaluateNode(args.get(0), x));
                    case "tan":
                        return Math.tan(evaluateNode(args.get(0), x));
                    default:
                        logger.error("Unknown function: " + fn);
                        return Double.NaN;
                }
            }
        }

        logger.error("Invalid node type");
        return Double.NaN;
    }
}
