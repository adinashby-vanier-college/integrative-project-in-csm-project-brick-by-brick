package edu.vanier.brickbybrick.allinonecalculator.controllers;

import edu.vanier.brickbybrick.allinonecalculator.MainApp;
import edu.vanier.brickbybrick.allinonecalculator.logic.ArithmeticCalculatorLogic;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.net.URL;

/**
 * The FMXL Controller class for the Arithmetic Calculator.
 */
public class ArithmeticCalculatorFXMLController {
    private final static Logger logger = LoggerFactory.getLogger(ArithmeticCalculatorFXMLController.class);
    private final static ArithmeticCalculatorLogic logic = new ArithmeticCalculatorLogic();

    private String currentExpression = "";

    @FXML
    private WebView inputField;
    private WebEngine engine;

    @FXML
    private Button arithmeticModeSwitch;
    @FXML
    private Button graphingModeSwitch;
    @FXML
    private Button programmingModeSwitch;

    @FXML
    private void initialize() {
        logger.info("Initializing ArithmeticCalculatorFXMLController...");

        // Setting up the WebView for the input field.
        URL url = this.getClass().getResource("/web/cortexjs.html");
        assert url != null;
        engine = inputField.getEngine();
        // Load the WebView with the CortexJS calculator HTML.
        engine.load(url.toExternalForm());
        inputField.setStyle("color-scheme: dark;");
        // Setup event listeners for the WebView.
        engine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("app", this);
                engine.executeScript("mf.addEventListener('input', function (evt) { app.updateExpression(evt.target.value); });");
            }
        });
        logger.info("Input Field WebView setup complete.");

        // Setup the keyboard event listeners.
        setupKeyboard();

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

    //> Keyboard Event Handlers
    private void handleDelete(boolean forward) {
        if (engine != null) {
            engine.executeScript("mf.executeCommand(\"delete" + (forward ? "Forward" : "Backward") + "\");");
        }
    }

    private void handleMove(boolean next) {
        if (engine != null) {
            engine.executeScript("mf.executeCommand(\"move" + (next ? "ToNextChar" : "ToPreviousChar") + "\");");
        }
    }

    private void setupKeyboard() {
        logger.info("Setting up the keyboard...");
        inputField.setOnKeyPressed(event -> {
            logger.info("Key pressed. Code: " + event.getCode());
            switch (event.getCode()) {
                case BACK_SPACE -> handleDelete(false);
                case DELETE -> handleDelete(true);
                case LEFT -> handleMove(false);
                case RIGHT -> handleMove(true);
            }
        });
        logger.info("Input Field Keyboard Event Handlers setup complete.");
    }
    //< Keyboard Event Handlers

    //> WebView Event Handlers
    /**
     * This method is called by the WebView when the expression is updated.
     * <p>
     * Notes:
     * <br/>
     * - This method *must* be public for WebView to be able to call it.
     * <br/>
     * - This method *is being used* despite the IDE's warning that it is not (as it is not called directly by Java code).
     * </p>
     *
     * @param expression the new expression
     */
    public void updateExpression(String expression) {
        logger.info("Expression Updates: " + expression);
        currentExpression = expression;
    }
    //< WebView Event Handlers
}
