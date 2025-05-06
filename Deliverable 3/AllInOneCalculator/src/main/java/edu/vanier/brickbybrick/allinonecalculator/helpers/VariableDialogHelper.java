package edu.vanier.brickbybrick.allinonecalculator.helpers;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/**
 * Helper class for creating and managing variable dialogs.
 */
public class VariableDialogHelper {

    /**
     * Creates and shows a dialog for adding a new variable.
     * 
     * @return A pair containing the variable name and value, or null if the dialog was cancelled.
     */
    public static Pair<String, String> showAddVariableDialog() {
        // Create the custom dialog
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Variable");
        dialog.setHeaderText("Enter variable name and value");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the variable name and value labels and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField variableNameField = new TextField();
        variableNameField.setPromptText("x");
        TextField variableValueField = new TextField();
        variableValueField.setPromptText("3");

        grid.add(new Label("Variable Name:"), 0, 0);
        grid.add(variableNameField, 1, 0);
        grid.add(new Label("Variable Value:"), 0, 1);
        grid.add(variableValueField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        variableNameField.requestFocus();

        // Convert the result to a pair when the add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Pair<>(variableNameField.getText(), variableValueField.getText());
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }
}