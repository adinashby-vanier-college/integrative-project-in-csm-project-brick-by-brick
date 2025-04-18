package edu.vanier.brickbybrick.allinonecalculator.controllers;

import edu.vanier.brickbybrick.allinonecalculator.MainApp;
import edu.vanier.brickbybrick.allinonecalculator.logic.ArithmeticCalculatorLogic;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 * The FMXL Controller class for the Arithmetic Calculator.
 */
public class ArithmeticCalculatorFXMLController {
    private final static Logger logger = LoggerFactory.getLogger(ArithmeticCalculatorFXMLController.class);
    private final static ArithmeticCalculatorLogic logic = new ArithmeticCalculatorLogic();

    private String currentExpression = "";
    private String currentMathJSONStr = "";
    private String currentResult = "";

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
    //    @FXML
//    private Button limitButton;
    @FXML
    private Button derButton;
    @FXML
    private Button intButton;
    @FXML
    private Button computeButton;

    private WebEngine engine;

    @FXML
    private Button arithmeticModeSwitch;
    @FXML
    private Button graphingModeSwitch;
    @FXML
    private Button programmingModeSwitch;

    @FXML
    private VBox historyVBox;

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
                engine.executeScript("""
                        mf.addEventListener('input', (evt) => {
                            app.updateExpression(evt.target.value);
                            app.updateMathJSONStr(JSON.stringify(window.ce.parse(evt.target.value).json));
                            app.updateResult(window.ce.parse(evt.target.value).evaluate());
                        });
                        """);
            }
        });
        logger.info("Input Field WebView setup complete.");

        historyVBox.getChildren().clear();

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

        // Compute button implementation
        computeButton.setOnAction(event -> {
            // Show the result in the input field.
            engine.executeScript("mf.setValue('" + currentResult + "')");
            // Add the expression to the history.
            try {
                historyVBox.getChildren().add(logic.createHistoryItem(currentExpression, currentResult));
            } catch (IOException e) {
                logger.error(e.getMessage());
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
                engine.executeScript("mf.executeCommand([\"insert\", \"\\\\text{rad}\" ,\"insertAfter\"]);");
            }
        });

        // Sin button implementation
        sinButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.executeCommand([\"insert\", \"\\\\sin\\\\left(\\\\right)\" ,\"insertAfter\"]);");
            }
        });

        // Cos button implementation
        cosButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.executeCommand([\"insert\", \"\\\\cos\\\\left(\\\\right)\" ,\"insertAfter\"]);");
            }
        });

        // Tan button implementation
        tanButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.executeCommand([\"insert\", \"\\\\tan\\\\left(\\\\right)\" ,\"insertAfter\"]);");
            }
        });

        // Square root button implementation
        rootButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.executeCommand([\"insert\", \"\\\\sqrt{}\" ,\"insertAfter\"]);");
                engine.executeScript("mf.executeCommand(\"moveToPreviousChar\");");
            }
        });

        // Nth root button implementation
        xrootButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.executeCommand([\"insert\", \"\\\\sqrt[n]{}\" ,\"insertAfter\"]);");
                engine.executeScript("mf.executeCommand(\"moveToPreviousChar\");");
            }
        });

        // Fraction button implementation
        fracButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.executeCommand([\"insert\", \"\\\\frac{}{}\" ,\"insertAfter\"]);");
                engine.executeScript("mf.executeCommand(\"moveToPreviousChar\");");
            }
        });

        // Derivative button implementation
        derButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.executeCommand([\"insert\", \"\\\\frac{d}{dx}\" ,\"insertAfter\"]);");
            }
        });

        // Integral button implementation
        intButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.executeCommand([\"insert\", \"\\\\int\" ,\"insertAfter\"]);");
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

    /**
     * This method is called by the WebView when the expression is updated.
     *
     * @param mathJSONStr the new math JSON string
     */
    public void updateMathJSONStr(String mathJSONStr) {
        logger.info("Math JSON String Updates: " + mathJSONStr);
        currentMathJSONStr = mathJSONStr;
    }

    /**
     * This method is called by the WebView when the expression is updated.
     *
     * @param result the result of the expression
     */
    public void updateResult(String result) {
        logger.info("Result Updates: " + result);
        currentResult = result;
    }
    //< WebView Event Handlers
}
