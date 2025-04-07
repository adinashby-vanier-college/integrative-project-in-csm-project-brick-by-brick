package edu.vanier.brickbybrick.allinonecalculator.controllers;

import edu.vanier.brickbybrick.allinonecalculator.MainApp;
import edu.vanier.brickbybrick.allinonecalculator.logic.ProgrammingModeLogic;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The FMXL Controller class for the scene for the calculator's programming mode.
 */
public class ProgrammingModeFXMLController {
    private final static Logger logger = LoggerFactory.getLogger(ProgrammingModeFXMLController.class);
    private final static ProgrammingModeLogic logic = new ProgrammingModeLogic();

    private final List<String> variables = new ArrayList<>();
    private final List<String> addedInstructions = new ArrayList<>();

    @FXML
    private Button addButton;

    @FXML
    private Button variablesButton;

    @FXML
    private Text variablesText;

    @FXML
    private VBox variablesVBox;

    @FXML
    private Button arithmeticModeSwitch;
    @FXML
    private Button graphingModeSwitch;
    @FXML
    private Button programmingModeSwitch;

    @FXML
    private void initialize() {
        logger.info("Initializing CalculatorProgrammingFXMLController...");

        arithmeticModeSwitch.setOnAction(event -> {
            MainApp.switchScene(MainApp.ARITHMETIC_CALCULATOR);
        });
        graphingModeSwitch.setOnAction(event -> {
            MainApp.switchScene(MainApp.GRAPHING_CALCULATOR);
        });
        programmingModeSwitch.setOnAction(event -> {
            MainApp.switchScene(MainApp.PROGRAMMING_MODE);
        });

        addButton.setOnAction(event -> {
            logger.info("Add Button clicked.");
            variablesText.setText("Block Storage");
            variablesVBox.getChildren().clear();
            variablesVBox.setPadding(new Insets(10, 10, 10, 10));
            Text text1 = new Text("If condition then,");
            text1.setStyle("-fx-font-size: 15px;");
            variablesVBox.getChildren().add(text1);
            Text text2 = new Text("Else then,");
            text2.setStyle("-fx-font-size: 15px;");
            variablesVBox.getChildren().add(text2);
            Text text3 = new Text("While condition do,");
            text3.setStyle("-fx-font-size: 15px;");
            variablesVBox.getChildren().add(text3);
            Text text4 = new Text("When clicked do,");
            text4.setStyle("-fx-font-size: 15px;");
            variablesVBox.getChildren().add(text4);

            variablesButton.setText("Click to leave");
        });
    }

}
