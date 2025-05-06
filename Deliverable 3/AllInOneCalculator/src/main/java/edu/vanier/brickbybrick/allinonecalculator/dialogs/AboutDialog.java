package edu.vanier.brickbybrick.allinonecalculator.dialogs;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AboutDialog extends Dialog<Void> {
    public AboutDialog(Stage owner) {
        setTitle("About");
        setHeaderText("All-in-One Calculator");
        initOwner(owner);

        // Create the custom dialog content
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setPrefWidth(800);

        Label versionLabel = new Label("Version 1.0");
        versionLabel.setWrapText(true);
        
        Label descriptionLabel = new Label("This All-In-One Calculator features 3 distinct calculators. Arithmetic" +
                "Calculator is a simple calculator, but contains many functions that transcends the functionality of " +
                "normal calculators by performing derivative and integral calculation using riemann sum and a derivative" +
                "point-step approach. This Calculator parses and calculates everything into A MathJSON Mathfield, from your" +
                "buttons right to your screen. All buttons function, and the bottom left allows you to compute a function," +
                "while the top left allows you to clear everything. The calculator comes with a history versionning too." +
                "The Graphing calculator allows you to dynamically update the graph using functions, which we break into points" +
                "and plot as canva data. Simply enter your formula and watch it generate in real time! The programmable calculator" +
                "is our third feature, and allows us to execute code blocks and attribute variables for later use! Intertwine coding" +
                "and math computations using this calculator. ");
        descriptionLabel.setWrapText(true);
        
        Label copyrightLabel = new Label("© 2024 Brick by Brick Team");
        copyrightLabel.setWrapText(true);

        content.getChildren().addAll(versionLabel, descriptionLabel, copyrightLabel);

        getDialogPane().setContent(content);

        // Add close button
        ButtonType closeButtonType = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().add(closeButtonType);
    }
} 