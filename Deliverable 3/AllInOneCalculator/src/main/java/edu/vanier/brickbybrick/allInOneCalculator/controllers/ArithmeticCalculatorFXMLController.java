package edu.vanier.brickbybrick.allInOneCalculator.controllers;

import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The FMXL Controller class for the Arithmetic Calculator.
 */
public class ArithmeticCalculatorFXMLController {
    private final static Logger logger = LoggerFactory.getLogger(ArithmeticCalculatorFXMLController.class);

    @FXML
    private void initialize() {
        logger.info("Initializing ArithmeticCalculatorFXMLController...");
    }
}
