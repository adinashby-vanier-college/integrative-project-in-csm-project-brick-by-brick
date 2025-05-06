package edu.vanier.brickbybrick.allinonecalculator.controllers;

import edu.vanier.brickbybrick.allinonecalculator.MainApp;
import edu.vanier.brickbybrick.allinonecalculator.dialogs.AboutDialog;
import edu.vanier.brickbybrick.allinonecalculator.dialogs.SettingsDialog;
import edu.vanier.brickbybrick.allinonecalculator.logic.GraphingCalculatorLogic;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

/**
 * The FMXL Controller class for the Graphing Calculator.
 */
public class GraphingCalculatorFXMLController {
    private final static Logger logger = LoggerFactory.getLogger(GraphingCalculatorFXMLController.class);
    private final static GraphingCalculatorLogic logic = new GraphingCalculatorLogic();

    /**
     * Class to represent a graph equation with its color
     */
    private static class GraphEquation {
        private final String expression;
        private final Color color;
        private final XYChart.Series<Number, Number> series;

        public GraphEquation(String expression, Color color, XYChart.Series<Number, Number> series) {
            this.expression = expression;
            this.color = color;
            this.series = series;
        }

        public String getExpression() {
            return expression;
        }

        public Color getColor() {
            return color;
        }

        public XYChart.Series<Number, Number> getSeries() {
            return series;
        }
    }

    private final List<GraphEquation> graphEquations = new ArrayList<>();
    private final Color[] colors = {
        Color.ORANGE, Color.BLUE, Color.GREEN, Color.RED, 
        Color.PURPLE, Color.BROWN, Color.PINK, Color.CYAN
    };

    private String currentExpression = "";
    private String currentMathJSONStr = "";
    private List<XYChart.Data<Number, Number>> currentResult = null;

    @FXML
    private VBox equationsVBox;

    @FXML
    private WebView inputField;
    private WebEngine engine;

    @FXML
    private LineChart<Number, Number> graphingChart;

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

    @FXML
    private MenuBar menuBar;

    @FXML
    private Button arithmeticModeSwitch;
    @FXML
    private Button graphingModeSwitch;
    @FXML
    private Button programmingModeSwitch;

    @FXML
    private void initialize() {
        logger.info("Initializing GraphingCalculatorFXMLController...");

        // Setup mode switch buttons
        arithmeticModeSwitch.setOnAction(event -> MainApp.switchScene(MainApp.ARITHMETIC_CALCULATOR));
        graphingModeSwitch.setOnAction(event -> MainApp.switchScene(MainApp.GRAPHING_CALCULATOR));
        programmingModeSwitch.setOnAction(event -> MainApp.switchScene(MainApp.PROGRAMMING_MODE));
        // Setting up the WebView for the input field.
        URL url = this.getClass().getResource("/web/cortexjs.html");
        assert url != null;
        engine = inputField.getEngine();
        engine.load(url.toExternalForm());
        inputField.setStyle("color-scheme: dark;");

        arithmeticModeSwitch.setOnAction(event -> {
            MainApp.switchScene(MainApp.ARITHMETIC_CALCULATOR);
        });
        graphingModeSwitch.setOnAction(event -> {
            MainApp.switchScene(MainApp.GRAPHING_CALCULATOR);
        });
        programmingModeSwitch.setOnAction(event -> {
            MainApp.switchScene(MainApp.PROGRAMMING_MODE);
        });

        NumberAxis xAxis = (NumberAxis) graphingChart.getXAxis();
        NumberAxis yAxis = (NumberAxis) graphingChart.getYAxis();

        xAxis.setLabel("X Axis");
        yAxis.setLabel("Y Axis");

        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(true);

        graphingChart.setLegendVisible(false);

        // Clear the example equation
        equationsVBox.getChildren().clear();

        // Compute button implementation
        computeButton.setOnAction(event -> {
            try {
                logger.info("Compute button pressed");

                // Get the current expression
                String currentValue = (String) engine.executeScript("mf.getValue()");
                logger.info("Current expression: " + currentValue);

                if (currentValue == null || currentValue.trim().isEmpty()) {
                    logger.warn("Empty expression, ignoring");
                    return;
                }

                // Get the MathJSON
                String mathJson = (String) engine.executeScript("JSON.stringify(window.ce.parse(mf.getValue()).json)");
                logger.info("MathJSON: " + mathJson);

                // Calculate the result using our logic
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                List<XYChart.Data<Number, Number>> result = logic.getGraphData(mathJson);
                logger.info("Calculated result: " + result);
                for (XYChart.Data<Number, Number> data : result) {
                    series.getData().add(data);
                }

                // Get a color for this equation
                Color color = getNextColor();

                // Add the equation to our list
                GraphEquation equation = new GraphEquation(currentValue, color, series);
                graphEquations.add(equation);

                // Add the series to the chart
                graphingChart.getData().add(series);

                // Set the color of the series
                String colorString = String.format("#%02X%02X%02X", 
                    (int)(color.getRed() * 255), 
                    (int)(color.getGreen() * 255), 
                    (int)(color.getBlue() * 255));
                series.getNode().setStyle("-fx-stroke: " + colorString + ";");

                // Update the equations display
                updateEquationsDisplay();

                // Clear the old input.
                String script = "mf.setValue('')";
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
                String newValue = currentValue + "\\\\sin()";
                engine.executeScript("mf.setValue('" + newValue + "')");
            }
        });

        // Cos button implementation
        cosButton.setOnAction(event -> {
            if (engine != null) {
                String currentValue = (String) engine.executeScript("mf.getValue()");
                String newValue = currentValue + "\\\\cos()";
                engine.executeScript("mf.setValue('" + newValue + "')");
            }
        });

        // Tan button implementation
        tanButton.setOnAction(event -> {
            if (engine != null) {
                String currentValue = (String) engine.executeScript("mf.getValue()");
                String newValue = currentValue + "\\\\tan()";
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

        setupMenuBar();
    }

    private void setupMenuBar() {
        // File Menu
        Menu fileMenu = menuBar.getMenus().get(0);
        MenuItem closeItem = fileMenu.getItems().get(0);
        closeItem.setOnAction(e -> {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            stage.close();
        });

        // Settings Menu
        Menu settingsMenu = menuBar.getMenus().get(1);
        MenuItem preferencesItem = settingsMenu.getItems().get(0);
        preferencesItem.setOnAction(e -> {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            SettingsDialog settingsDialog = new SettingsDialog(stage);
            settingsDialog.showAndWait();
        });

        // About Menu
        Menu aboutMenu = menuBar.getMenus().get(2);
        MenuItem aboutItem = aboutMenu.getItems().get(0);
        aboutItem.setOnAction(e -> {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            AboutDialog aboutDialog = new AboutDialog(stage);
            aboutDialog.showAndWait();
        });
    }

    /**
     * Gets the next color from the colors array for a new equation
     * @return The next color to use
     */
    private Color getNextColor() {
        int index = graphEquations.size() % colors.length;
        return colors[index];
    }

    /**
     * Updates the equations display in the UI
     */
    private void updateEquationsDisplay() {
        equationsVBox.getChildren().clear();

        for (GraphEquation equation : graphEquations) {
            // Create an HBox for this equation
            HBox hbox = new HBox();
            hbox.setSpacing(10);
            hbox.setPadding(new Insets(5, 5, 5, 5));

            // Create a circle with the equation's color
            Circle circle = new Circle(13, equation.getColor());
            circle.setStroke(Color.BLACK);
            circle.setStrokeType(StrokeType.INSIDE);

            // Create a text with the equation's expression
            Text text = new Text("y = " + equation.getExpression());
            text.setFont(new Font(22));

            // Add the circle and text to the HBox
            hbox.getChildren().addAll(circle, text);

            // Add the HBox to the equationsVBox
            equationsVBox.getChildren().add(hbox);
        }
    }
}
