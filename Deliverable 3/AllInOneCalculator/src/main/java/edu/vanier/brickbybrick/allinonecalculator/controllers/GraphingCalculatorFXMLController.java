package edu.vanier.brickbybrick.allinonecalculator.controllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.vanier.brickbybrick.allinonecalculator.logic.GraphingCalculatorLogic;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphingCalculatorFXMLController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(GraphingCalculatorFXMLController.class);
    private final GraphingCalculatorLogic logic = new GraphingCalculatorLogic();
    private XYChart.Series<Number, Number> series;

    @FXML
    private WebView functionInput;
    @FXML
    private LineChart<Number, Number> graph;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupWebView();
        setupGraph();
    }

    private void setupWebView() {
        WebEngine engine = functionInput.getEngine();
        engine.load(getClass().getResource("/web/graphing.html").toExternalForm());

        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("javaConnector", new JavaConnector());
            }
        });
    }

    private void setupGraph() {
        // Set up the graph axes
        xAxis.setLabel("x");
        yAxis.setLabel("y");

        // Set initial range
        xAxis.setLowerBound(-10);
        xAxis.setUpperBound(10);
        yAxis.setLowerBound(-10);
        yAxis.setUpperBound(10);

        // Create a new series for the graph
        series = new XYChart.Series<>();
        series.setName("Function");
        graph.getData().add(series);

        // Remove the legend
        graph.setLegendVisible(false);

        // Style the graph
        graph.setCreateSymbols(false);
        graph.setAnimated(false);
    }

    public class JavaConnector {
        public void updateFunction(String jsonStr) {
            try {
                JsonElement json = JsonParser.parseString(jsonStr);
                if (logic.parseFunction(json)) {
                    updateGraph();
                }
            } catch (Exception e) {
                logger.error("Error updating function", e);
            }
        }
    }

    private void updateGraph() {
        series.getData().clear();

        // Generate points for the graph
        double step = 0.1;
        for (double x = xAxis.getLowerBound(); x <= xAxis.getUpperBound(); x += step) {
            try {
                double y = logic.evaluateAt(x);
                if (!Double.isNaN(y) && !Double.isInfinite(y)) {
                    series.getData().add(new XYChart.Data<>(x, y));
                }
            } catch (Exception e) {
                logger.error("Error evaluating function at x = " + x, e);
            }
        }
    }
}
