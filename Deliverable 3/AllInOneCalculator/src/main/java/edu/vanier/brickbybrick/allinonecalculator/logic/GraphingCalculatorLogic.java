package edu.vanier.brickbybrick.allinonecalculator.logic;

import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The logical operations for the Graphing Calculator.
 */
public class GraphingCalculatorLogic {
    private final ComputeEngine computeEngine = new ComputeEngine();

    public static final double MIN_X = -10.0;
    public static final double MAX_X = 160.0;

    public List<XYChart.Data<Number, Number>> getGraphData(String expression) {
        List<XYChart.Data<Number, Number>> data = new ArrayList<>();
        for (double x = MIN_X; x <= MAX_X; x += 0.1) {
            computeEngine.setVariables(Map.of("x", x));
            double y = Double.parseDouble(computeEngine.evaluate(expression));
            XYChart.Data<Number, Number> point = new XYChart.Data<>(x, y);
            data.add(point);
        }
        return data;
    }
}
