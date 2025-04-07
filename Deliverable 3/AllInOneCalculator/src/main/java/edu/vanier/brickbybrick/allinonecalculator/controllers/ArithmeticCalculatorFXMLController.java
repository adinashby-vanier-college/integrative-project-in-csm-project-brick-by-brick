package edu.vanier.brickbybrick.allinonecalculator.controllers;

import edu.vanier.brickbybrick.allinonecalculator.logic.ArithmeticCalculatorLogic;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.control.Button;
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
    @FXML
    private Button squareButton;
    @FXML
    private Button radiantButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button sinButton;
    @FXML
    private Button cosButton;
    @FXML
    private Button tanButton;
    @FXML
    private Button rootButton;
    @FXML
    private Button xrootButton;
    @FXML
    private Button fracButton;
    @FXML
    private Button limitButton;
    @FXML
    private Button derButton;
    @FXML
    private Button intButton;
    @FXML
    private Button computeButton;

    private WebEngine engine;

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

        // Compute button implementation
        computeButton.setOnAction(event -> {
            if (engine != null) {
                // Get the current expression from the WebView
                String expression = (String) engine.executeScript("mf.getValue()");
                if (expression != null && !expression.isEmpty()) {
                    try {
                        // Evaluate the expression using CortexJS
                        String result = (String) engine.executeScript(
                            "ce.parse('" + expression + "').evaluate().toString()"
                        );
                        // Insert the result into the input field
                        engine.executeScript("mf.setValue('" + result + "')");
                    } catch (Exception e) {
                        logger.error("Error evaluating expression: " + e.getMessage());
                    }
                }
            }
        });

        // Clear button implementation
        clearButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.executeCommand(\"deleteAll\");");
            }
        });

        // Square button implementation
        squareButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.executeCommand([\"insert\", \"^2\" ,\"insertAfter\"]);");
            }
        });

        // Radiant button implementation
        radiantButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.executeCommand([\"insert\", \"\\text{rad}\" ,\"insertAfter\"]);");
            }
        });

        // Sin button implementation
        sinButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.executeCommand([\"insert\", \"\\sin\" ,\"insertAfter\"]);");
            }
        });

        // Cos button implementation
        cosButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.executeCommand([\"insert\", \"\\cos\" ,\"insertAfter\"]);");
            }
        });

        // Tan button implementation
        tanButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.executeCommand([\"insert\", \"\\tan\" ,\"insertAfter\"]);");
            }
        });

        // Square root button implementation
        rootButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.executeCommand([\"insert\", \"\\sqrt{}\" ,\"insertAfter\"]);");
                engine.executeScript("mf.executeCommand(\"moveToPreviousChar\");");
            }
        });

        // Nth root button implementation
        xrootButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.executeCommand([\"insert\", \"\\sqrt[n]{}\" ,\"insertAfter\"]);");
                engine.executeScript("mf.executeCommand(\"moveToPreviousChar\");");
            }
        });

        // Fraction button implementation
        fracButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.executeCommand([\"insert\", \"\\frac{}{}\" ,\"insertAfter\"]);");
                engine.executeScript("mf.executeCommand(\"moveToPreviousChar\");");
            }
        });

        // Limit button implementation
        limitButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.executeCommand([\"insert\", \"\\lim_{x \\to }\" ,\"insertAfter\"]);");
                engine.executeScript("mf.executeCommand(\"moveToPreviousChar\");");
            }
        });

        // Derivative button implementation
        derButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.executeCommand([\"insert\", \"\\frac{d}{dx}\" ,\"insertAfter\"]);");
            }
        });

        // Integral button implementation
        intButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.executeCommand([\"insert\", \"\\int\" ,\"insertAfter\"]);");
            }
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
