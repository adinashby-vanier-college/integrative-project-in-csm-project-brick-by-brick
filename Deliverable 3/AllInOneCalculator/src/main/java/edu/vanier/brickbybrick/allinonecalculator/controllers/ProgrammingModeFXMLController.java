package edu.vanier.brickbybrick.allinonecalculator.controllers;

import edu.vanier.brickbybrick.allinonecalculator.MainApp;
import edu.vanier.brickbybrick.allinonecalculator.helpers.VariableDialogHelper;
import edu.vanier.brickbybrick.allinonecalculator.logic.ProgrammingModeLogic;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Pair;
import netscape.javascript.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The FMXL Controller class for the scene for the calculator's programming mode.
 */
public class ProgrammingModeFXMLController {
    private final static Logger logger = LoggerFactory.getLogger(ProgrammingModeFXMLController.class);
    private final static ProgrammingModeLogic logic = new ProgrammingModeLogic();

    private final HashMap<String, String> variables = new HashMap<>();
    private final List<String> addedInstructions = new ArrayList<>();

    // Fields for math input
    private String currentExpression = "";
    private String currentMathJSONStr = "";
    private String currentResult = "";
    private WebEngine engine;

    @FXML
    private WebView inputField;

    @FXML
    private Button addButton;

    @FXML
    private Button variablesButton;

    @FXML
    private Text variablesText;

    @FXML
    private VBox variablesVBox;
    @FXML
    private VBox secondVBox;
    @FXML
    private VBox instructionsVBox;

    @FXML
    private Button arithmeticModeSwitch;
    @FXML
    private Button graphingModeSwitch;
    @FXML
    private Button programmingModeSwitch;

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
    private Button derButton;
    @FXML
    private Button intButton;
    @FXML
    private Button computeButton;

    @FXML
    private void initialize() {
        logger.info("Initializing CalculatorProgrammingFXMLController...");

        // Setting up the WebView for the input field
        URL url = this.getClass().getResource("/web/cortexjs.html");
        assert url != null;
        engine = inputField.getEngine();
        engine.load(url.toExternalForm());
        inputField.setStyle("color-scheme: dark;");

        // Setup event listeners for the WebView
        engine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                logger.info("WebView loaded successfully");
                try {
                    JSObject window = (JSObject) engine.executeScript("window");
                    window.setMember("app", this);
                    engine.executeScript("""
                            mf.addEventListener('input', (evt) => {
                                app.updateExpression(evt.target.value);
                                app.updateMathJSONStr(JSON.stringify(window.ce.parse(evt.target.value).json));
                                app.updateResult(logic.calculate(JSON.stringify(window.ce.parse(evt.target.value).json)));
                            });
                            """);
                    logger.info("WebView event listeners setup complete");
                } catch (Exception e) {
                    logger.error("Error setting up WebView event listeners: " + e.getMessage(), e);
                }
            }
        });

        // Setup keyboard event handling
        setupKeyboard();

        variablesVBox.setAlignment(Pos.TOP_CENTER);
        variablesVBox.setSpacing(10);

        initializeVariablesList();

        arithmeticModeSwitch.setOnAction(event -> {
            MainApp.switchScene(MainApp.ARITHMETIC_CALCULATOR);
        });
        graphingModeSwitch.setOnAction(event -> {
            MainApp.switchScene(MainApp.GRAPHING_CALCULATOR);
        });
        programmingModeSwitch.setOnAction(event -> {
            MainApp.switchScene(MainApp.PROGRAMMING_MODE);
        });

        variablesButton.setOnAction(event -> {
            addVariable();
        });

        // Compute button implementation
        computeButton.setOnAction(event -> {
            try {
                logger.info("Compute button pressed");

                // Get the current expression
                String currentValue = (String) engine.executeScript("mf.getValue()");
                logger.info("Current expression: " + currentValue);

                // Get the MathJSON
                String mathJson = (String) engine.executeScript("JSON.stringify(window.ce.parse(mf.getValue()).json)");
                logger.info("MathJSON: " + mathJson);

                // Calculate the result using our logic
                String result = logic.calculate(mathJson);
                logger.info("Calculated result: " + result);

                // Update the display with the result
                String script = "mf.setValue('" + result + "')";
                logger.info("Executing script: " + script);
                engine.executeScript(script);

                // Update internal state
                currentExpression = currentValue;
                currentMathJSONStr = mathJson;
                currentResult = result;

                logger.info("Compute operation completed successfully");
            } catch (Exception e) {
                logger.error("Error in compute operation: " + e.getMessage(), e);
                String errorMessage = "Error: " + e.getMessage();
                engine.executeScript("mf.setValue('" + errorMessage + "')");
            }
        });

        // Clear button implementation
        clearButton.setOnAction(event -> {
            if (engine != null) {
                engine.executeScript("mf.setValue('')");
                currentExpression = "";
                currentMathJSONStr = "";
                currentResult = "";
            }
        });

        // Square button implementation
        squareButton.setOnAction(event -> {
            if (engine != null) {
                String currentValue = (String) engine.executeScript("mf.getValue()");
                String newValue = currentValue + "^2";
                engine.executeScript("mf.setValue('" + newValue + "')");
            }
        });

        // Radiant button implementation
        radiantButton.setOnAction(event -> {
            if (engine != null) {
                String currentValue = (String) engine.executeScript("mf.getValue()");
                String newValue = currentValue + " rad";
                engine.executeScript("mf.setValue('" + newValue + "')");
            }
        });

        // Sin button implementation
        sinButton.setOnAction(event -> {
            if (engine != null) {
                String currentValue = (String) engine.executeScript("mf.getValue()");
                String newValue = currentValue + "sin()";
                engine.executeScript("mf.setValue('" + newValue + "')");
            }
        });

        // Cos button implementation
        cosButton.setOnAction(event -> {
            if (engine != null) {
                String currentValue = (String) engine.executeScript("mf.getValue()");
                String newValue = currentValue + "cos()";
                engine.executeScript("mf.setValue('" + newValue + "')");
            }
        });

        // Tan button implementation
        tanButton.setOnAction(event -> {
            if (engine != null) {
                String currentValue = (String) engine.executeScript("mf.getValue()");
                String newValue = currentValue + "tan()";
                engine.executeScript("mf.setValue('" + newValue + "')");
            }
        });

        // Square root button implementation
        rootButton.setOnAction(event -> {
            if (engine != null) {
                String currentValue = (String) engine.executeScript("mf.getValue()");
                String newValue = currentValue + "\\\\sqrt{x}";
                engine.executeScript("mf.setValue('" + newValue + "')");
            }
        });

        // Nth root button implementation
        xrootButton.setOnAction(event -> {
            if (engine != null) {
                String currentValue = (String) engine.executeScript("mf.getValue()");
                // Clear previous content and set a clear template with placeholders
                String newValue = "\\\\sqrt[n]{x}";
                engine.executeScript("mf.setValue('" + newValue + "')");
            }
        });

        // Fraction button implementation
        fracButton.setOnAction(event -> {
            if (engine != null) {
                String currentValue = (String) engine.executeScript("mf.getValue()");
                // Clear previous content and set a clear template with placeholders
                String newValue = "\\frac{\\text{numerator}}{\\text{denominator}}";
                engine.executeScript("mf.setValue('" + newValue + "')");
            }
        });

        // Derivative button implementation
        derButton.setOnAction(event -> {
            if (engine != null) {
                String currentValue = (String) engine.executeScript("mf.getValue()");
                // Clear previous content and set a clear template with placeholders
                String newValue = "\\\\dfrac{d}{dx}2x\\\\bigm{|}_{x=a}";
                engine.executeScript("mf.setValue('" + newValue + "')");
            }
        });

        // Integral button implementation
        intButton.setOnAction(event -> {
            if (engine != null) {
                String currentValue = (String) engine.executeScript("mf.getValue()");
                // Clear previous content and set a clear template with placeholders
                String newValue = "\\\\[ \\\\int_{\\\\text{lower bound}}^{\\\\text{upper bound}} \\\\text{function(x)} \\\\,dx \\\\]";
                engine.executeScript("mf.setValue('" + newValue + "')");
            }
        });

        addButton.setOnAction(event -> {
            variablesVBox.getChildren().clear();
            variablesVBox.getChildren().add(variablesText);

            variablesText.setText("Block Storage");

            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER); 

            Text text1 = new Text("If condition then,");
            text1.setStyle("-fx-font-size: 20px;");
            vBox.getChildren().add(text1);
            Text text2 = new Text("Else then,");
            text2.setStyle("-fx-font-size: 20px;");
            vBox.getChildren().add(text2);
            Text text3 = new Text("While condition do,");
            text3.setStyle("-fx-font-size: 20px;");
            vBox.getChildren().add(text3);
            Text text4 = new Text("When clicked do,");
            text4.setStyle("-fx-font-size: 20px;");
            vBox.getChildren().add(text4);

            text1.setOnMouseClicked(event2 -> {
                System.out.println("Clicked on If condition");
                VBox vBox2 = new VBox();
                vBox2.getChildren().add(new Text("If condition"));
                vBox2.getChildren().add(new TextField("Enter condition"));
                vBox2.setPadding(new Insets(5, 0, 5, 0));
                instructionsVBox.getChildren().add(vBox2);
            });

            text2.setOnMouseClicked(event2 -> {
                System.out.println("Clicked on else condition");
                VBox vBox2 = new VBox();
                vBox2.getChildren().add(new Text("Else condition"));
                vBox2.getChildren().add(new TextField("Enter condition"));
                vBox2.setPadding(new Insets(5, 0, 5, 0));
                instructionsVBox.getChildren().add(vBox2);
            });

            text3.setOnMouseClicked(event2 -> {
                System.out.println("Clicked on While condition");
                VBox vBox2 = new VBox();
                vBox2.getChildren().add(new Text("While condition"));
                vBox2.getChildren().add(new TextField("Enter condition"));
                vBox2.setPadding(new Insets(5, 0, 5, 0));
                instructionsVBox.getChildren().add(vBox2);
            });

            text4.setOnMouseClicked(event2 -> {
                System.out.println("clicked on When clicked");
                VBox vBox2 = new VBox();
                vBox2.getChildren().add(new Text("When clicked"));
                vBox2.getChildren().add(new TextField("Enter event"));
                vBox2.setPadding(new Insets(5, 0, 5, 0));
                instructionsVBox.getChildren().add(vBox2);
            });

            vBox.setAlignment(Pos.CENTER); 
            vBox.setPadding(new Insets(10));

            Region spacerTop = new Region();
            spacerTop.setMinHeight(10);
            VBox.setVgrow(spacerTop, Priority.ALWAYS);
            variablesVBox.getChildren().add(spacerTop);

            variablesVBox.getChildren().add(vBox);

            Region spacerBottom = new Region();
            spacerBottom.setMinHeight(10);
            VBox.setVgrow(spacerBottom, Priority.ALWAYS);
            variablesVBox.getChildren().add(spacerBottom);

            variablesButton.setText("Click to leave");
            Region spacerBeforeButton = new Region();
            spacerBeforeButton.setMinHeight(10);
            VBox.setVgrow(spacerBeforeButton, Priority.ALWAYS);
            variablesVBox.getChildren().add(spacerBeforeButton);
            variablesVBox.getChildren().add(variablesButton);

            variablesButton.setOnAction(event2 -> {
                showVariablesView();
            });
        });
    }

    /**
     * Shows the variables view
     */
    private void showVariablesView() {
        variablesVBox.getChildren().clear();
        variablesVBox.getChildren().add(variablesText);

        variablesText.setText("Variables");

        Region topSpacer = new Region();
        topSpacer.setMinHeight(10);
        VBox.setVgrow(topSpacer, Priority.ALWAYS);
        variablesVBox.getChildren().add(topSpacer);

        // Update the secondVBox with the current variables
        updateVariablesUI();
        variablesVBox.getChildren().add(secondVBox);

        Region bottomSpacer = new Region();
        bottomSpacer.setMinHeight(10);
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);
        variablesVBox.getChildren().add(bottomSpacer);

        variablesButton.setText("Click to Add");
        variablesVBox.getChildren().add(variablesButton);

        variablesButton.setOnAction(event -> {
            addVariable();
        });
    }

    private void initializeVariablesList() {
        updateVariablesUI();
    }

    private void updateVariablesUI() {
        secondVBox.getChildren().clear();

        for (String varName : variables.keySet()) {
            String varValue = variables.get(varName);

            Text variableText = new Text(varName + " = " + varValue);
            variableText.setFont(new Font(23.0));

            VBox.setMargin(variableText, new Insets(0, 0, 20, 10));

            secondVBox.getChildren().add(variableText);
        }
    }

    private void addVariable() {
        Pair<String, String> result = VariableDialogHelper.showAddVariableDialog();

        if (result != null) {
            String varName = result.getKey();
            String varValue = result.getValue();

            if (varName == null || varName.trim().isEmpty()) {
                varName = "x"; 
            }

            if (varValue == null || varValue.trim().isEmpty()) {
                varValue = "0"; 
            }

            variables.put(varName, varValue);

            // add the variable to ComputeEngine
            boolean success = logic.addVariableToComputeEngine(varName, varValue);
            if (!success) {
                logger.warn("Failed to add variable to ComputeEngine: " + varName + " = " + varValue + " (not a valid number)");
            }

            updateVariablesUI();

            logger.info("Added variable: " + varName + " = " + varValue);

            showVariablesView();
        }
    }

    // WebView Event Handlers
    /**
     * This method is called by the WebView when the expression is updated.
     * @param expression the new expression
     */
    public void updateExpression(String expression) {
        logger.info("Expression Updates: " + expression);
        currentExpression = expression;
    }

    /**
     * This method is called by the WebView when the expression is updated.
     * @param mathJSONStr the new math JSON string
     */
    public void updateMathJSONStr(String mathJSONStr) {
        logger.info("Math JSON String Updates: " + mathJSONStr);
        currentMathJSONStr = mathJSONStr;
    }

    /**
     * This method is called by the WebView when the expression is updated.
     * @param result the result of the expression
     */
    public void updateResult(String result) {
        logger.info("Result Updates: " + result);
        currentResult = result;
    }

    // Keyboard Event Handlers
    private void handleDelete(boolean forward) {
        if (engine != null) {
            try {
                String currentValue = (String) engine.executeScript("mf.getValue()");

                if (currentValue.isEmpty()) return;

                // Always remove the last character, regardless of forward/backward
                String newValue = currentValue.substring(0, currentValue.length() - 1);
                engine.executeScript("mf.setValue('" + newValue + "')");

                // Move cursor to end after deletion
                engine.executeScript("mf.setCaretPosition(" + newValue.length() + ")");
            } catch (Exception e) {
                logger.error("Error handling delete: " + e.getMessage());
            }
        }
    }

    private void handleMove(boolean next) {
        if (engine != null) {
            try {
                // Move to end of expression
                String currentValue = (String) engine.executeScript("mf.getValue()");
                engine.executeScript("mf.setCaretPosition(" + currentValue.length() + ")");
            } catch (Exception e) {
                logger.error("Error handling move: " + e.getMessage());
            }
        }
    }

    private void setupKeyboard() {
        logger.info("Setting up the keyboard...");
        inputField.setOnKeyPressed(event -> {
            logger.info("Key pressed. Code: " + event.getCode());
            switch (event.getCode()) {
                case BACK_SPACE, DELETE -> handleDelete(true); // Both keys remove last character
                case LEFT, RIGHT -> handleMove(true); // Both keys move to end
                case HOME -> {
                    if (engine != null) {
                        engine.executeScript("mf.setCaretPosition(0)");
                    }
                }
                case END -> {
                    if (engine != null) {
                        String currentValue = (String) engine.executeScript("mf.getValue()");
                        engine.executeScript("mf.setCaretPosition(" + currentValue.length() + ")");
                    }
                }
            }
        });
        logger.info("Input Field Keyboard Event Handlers setup complete.");
    }
}
