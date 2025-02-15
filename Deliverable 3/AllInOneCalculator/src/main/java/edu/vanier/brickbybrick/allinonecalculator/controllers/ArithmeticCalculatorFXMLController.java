package edu.vanier.brickbybrick.allinonecalculator.controllers;

import edu.vanier.brickbybrick.allinonecalculator.logic.ArithmeticCalculatorLogic;
import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The FMXL Controller class for the Arithmetic Calculator.
 */
public class ArithmeticCalculatorFXMLController {
    private final static Logger logger = LoggerFactory.getLogger(ArithmeticCalculatorFXMLController.class);
    private final static ArithmeticCalculatorLogic logic = new ArithmeticCalculatorLogic();

    @FXML
    private void initialize() {
        logger.info("Initializing ArithmeticCalculatorFXMLController...");
    }
}
