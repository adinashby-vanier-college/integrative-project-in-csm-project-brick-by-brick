package edu.vanier.brickbybrick.allinonecalculator.controllers;

import edu.vanier.brickbybrick.allinonecalculator.logic.GraphingCalculatorLogic;
import javafx.fxml.FXML;
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
    private void initialize() {
        logger.info("Initializing GraphingCalculatorFXMLController...");
    }
}
