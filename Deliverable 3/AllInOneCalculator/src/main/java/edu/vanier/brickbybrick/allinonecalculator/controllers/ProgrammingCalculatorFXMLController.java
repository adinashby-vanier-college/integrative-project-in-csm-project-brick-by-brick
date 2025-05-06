package edu.vanier.brickbybrick.allinonecalculator.controllers;

import edu.vanier.brickbybrick.allinonecalculator.MainApp;
import edu.vanier.brickbybrick.allinonecalculator.dialogs.AboutDialog;
import edu.vanier.brickbybrick.allinonecalculator.dialogs.SettingsDialog;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class ProgrammingCalculatorFXMLController {
    @FXML
    private MenuBar menuBar;
    
    @FXML
    private Button arithmeticModeSwitch;
    @FXML
    private Button graphingModeSwitch;
    @FXML
    private Button programmingModeSwitch;
    
    @FXML
    private WebView inputField;
    @FXML
    private VBox variablesVBox;
    @FXML
    private VBox instructionsVBox;
    @FXML
    private Button variablesButton;
    @FXML
    private Button addButton;

    @FXML
    private void initialize() {
        // Setup mode switch buttons
        arithmeticModeSwitch.setOnAction(event -> MainApp.switchScene(MainApp.ARITHMETIC_CALCULATOR));
        graphingModeSwitch.setOnAction(event -> MainApp.switchScene(MainApp.GRAPHING_CALCULATOR));
        programmingModeSwitch.setOnAction(event -> MainApp.switchScene(MainApp.PROGRAMMING_MODE));

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
} 