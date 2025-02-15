package edu.vanier.brickbybrick.allinonecalculator.controllers;

import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The FMXL Controller class for the scene for the calculator's programming mode.
 */
public class ProgrammingModeFXMLController {
    private final static Logger logger = LoggerFactory.getLogger(ProgrammingModeFXMLController.class);

    @FXML
    private void initialize() {
        logger.info("Initializing CalculatorProgrammingFXMLController...");
    }
}
