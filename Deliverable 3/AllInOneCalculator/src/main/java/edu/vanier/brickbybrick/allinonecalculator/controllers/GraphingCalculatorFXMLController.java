package edu.vanier.brickbybrick.allinonecalculator.controllers;

import edu.vanier.brickbybrick.allinonecalculator.MainApp;
import edu.vanier.brickbybrick.allinonecalculator.logic.GraphingCalculatorLogic;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The FMXL Controller class for the Graphing Calculator.
 */
public class GraphingCalculatorFXMLController {
    private final static Logger logger = LoggerFactory.getLogger(GraphingCalculatorFXMLController.class);
    private final static GraphingCalculatorLogic logic = new GraphingCalculatorLogic();

    private final List<String> graphExpressions = new ArrayList<>();

    @FXML
    private LineChart<Integer, Integer> graphingChart;

    @FXML
    private Button arithmeticModeSwitch;
    @FXML
    private Button graphingModeSwitch;
    @FXML
    private Button programmingModeSwitch;

    @FXML
    private void initialize() {
        logger.info("Initializing GraphingCalculatorFXMLController...");

        arithmeticModeSwitch.setOnAction(event -> {
            MainApp.switchScene(MainApp.ARITHMETIC_CALCULATOR);
        });
        graphingModeSwitch.setOnAction(event -> {
            MainApp.switchScene(MainApp.GRAPHING_CALCULATOR);
        });
        programmingModeSwitch.setOnAction(event -> {
            MainApp.switchScene(MainApp.PROGRAMMING_MODE);
        });
    }
}
