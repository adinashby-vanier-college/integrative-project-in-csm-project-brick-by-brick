package edu.vanier.brickbybrick.allinonecalculator.dialogs;

import edu.vanier.brickbybrick.allinonecalculator.utils.CalculatorSettings;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class SettingsDialog extends Dialog<Void> {
    private final CheckBox darkModeCheckBox;
    private final ComboBox<String> decimalPlacesComboBox;
    private final ComboBox<String> angleUnitComboBox;
    private final TextField resourcePathField;
    private final Button browseButton;
    private final Spinner<Integer> graphSizeSpinner;
    private final Spinner<Integer> fontSizeSpinner;
    private final ColorPicker backgroundColorPicker;

    public SettingsDialog(Stage owner) {
        setTitle("Settings");
        setHeaderText("Calculator Settings");
        initOwner(owner);

        // Create the custom dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Dark Mode
        Label darkModeLabel = new Label("Dark Mode:");
        darkModeCheckBox = new CheckBox();
        darkModeCheckBox.setSelected(CalculatorSettings.isDarkMode());
        grid.add(darkModeLabel, 0, 0);
        grid.add(darkModeCheckBox, 1, 0);

        // Decimal Places
        Label decimalPlacesLabel = new Label("Decimal Places:");
        decimalPlacesComboBox = new ComboBox<>();
        decimalPlacesComboBox.getItems().addAll("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        decimalPlacesComboBox.setValue(CalculatorSettings.getDecimalPlaces());
        grid.add(decimalPlacesLabel, 0, 1);
        grid.add(decimalPlacesComboBox, 1, 1);

        // Angle Unit
        Label angleUnitLabel = new Label("Angle Unit:");
        angleUnitComboBox = new ComboBox<>();
        angleUnitComboBox.getItems().addAll("Degrees", "Radians");
        angleUnitComboBox.setValue(CalculatorSettings.getAngleUnit());
        grid.add(angleUnitLabel, 0, 2);
        grid.add(angleUnitComboBox, 1, 2);

        // Resource Path
        Label resourcePathLabel = new Label("Resource Path:");
        resourcePathField = new TextField(CalculatorSettings.getResourcePath());
        resourcePathField.setEditable(false);
        browseButton = new Button("Browse");
        HBox resourcePathBox = new HBox(10, resourcePathField, browseButton);
        grid.add(resourcePathLabel, 0, 3);
        grid.add(resourcePathBox, 1, 3);

        // Set up the browse button action
        browseButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Resource Directory");
            File selectedDirectory = directoryChooser.showDialog(owner);
            if (selectedDirectory != null) {
                resourcePathField.setText(selectedDirectory.getAbsolutePath());
            }
        });

        // Graph Size
        graphSizeSpinner = new Spinner<>(100, 1000, CalculatorSettings.getGraphSize(), 50);
        grid.add(new Label("Graph Size:"), 0, 4);
        grid.add(graphSizeSpinner, 1, 4);

        // Font Size
        fontSizeSpinner = new Spinner<>(8, 24, CalculatorSettings.getFontSize(), 1);
        grid.add(new Label("Font Size:"), 0, 5);
        grid.add(fontSizeSpinner, 1, 5);

        // Background Color
        backgroundColorPicker = new ColorPicker(CalculatorSettings.getBackgroundColor());
        grid.add(new Label("Background Color:"), 0, 6);
        grid.add(backgroundColorPicker, 1, 6);

        // Add the grid to the dialog
        getDialogPane().setContent(grid);

        // Add buttons
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Handle the save button action
        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                saveSettings();
            }
            return null;
        });
    }

    private void saveSettings() {
        CalculatorSettings.setDarkMode(darkModeCheckBox.isSelected());
        CalculatorSettings.setDecimalPlaces(decimalPlacesComboBox.getValue());
        CalculatorSettings.setAngleUnit(angleUnitComboBox.getValue());
        CalculatorSettings.setResourcePath(resourcePathField.getText());
        CalculatorSettings.setGraphSize(graphSizeSpinner.getValue());
        CalculatorSettings.setFontSize(fontSizeSpinner.getValue());
        CalculatorSettings.setBackgroundColor(backgroundColorPicker.getValue());
    }
} 