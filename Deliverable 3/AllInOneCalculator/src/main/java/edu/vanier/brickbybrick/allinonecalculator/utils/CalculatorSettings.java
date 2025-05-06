package edu.vanier.brickbybrick.allinonecalculator.utils;

import javafx.scene.paint.Color;

public class CalculatorSettings {
    private static boolean darkMode = false;
    private static String decimalPlaces = "2";
    private static String angleUnit = "Degrees";
    private static String dataSource = "Local";
    private static String resourcePath = "";
    private static String graphOrientation = "Horizontal";
    private static int graphSize = 500;
    private static Color backgroundColor = Color.WHITE;
    private static int fontSize = 12;

    public static boolean isDarkMode() {
        return darkMode;
    }

    public static void setDarkMode(boolean darkMode) {
        CalculatorSettings.darkMode = darkMode;
    }

    public static String getDecimalPlaces() {
        return decimalPlaces;
    }

    public static void setDecimalPlaces(String decimalPlaces) {
        CalculatorSettings.decimalPlaces = decimalPlaces;
    }

    public static String getAngleUnit() {
        return angleUnit;
    }

    public static void setAngleUnit(String angleUnit) {
        CalculatorSettings.angleUnit = angleUnit;
    }

    public static String getDataSource() {
        return dataSource;
    }

    public static void setDataSource(String dataSource) {
        CalculatorSettings.dataSource = dataSource;
    }

    public static String getResourcePath() {
        return resourcePath;
    }

    public static void setResourcePath(String resourcePath) {
        CalculatorSettings.resourcePath = resourcePath;
    }

    public static String getGraphOrientation() {
        return graphOrientation;
    }

    public static void setGraphOrientation(String graphOrientation) {
        CalculatorSettings.graphOrientation = graphOrientation;
    }

    public static int getGraphSize() {
        return graphSize;
    }

    public static void setGraphSize(int graphSize) {
        CalculatorSettings.graphSize = graphSize;
    }

    public static Color getBackgroundColor() {
        return backgroundColor;
    }

    public static void setBackgroundColor(Color backgroundColor) {
        CalculatorSettings.backgroundColor = backgroundColor;
    }

    public static int getFontSize() {
        return fontSize;
    }

    public static void setFontSize(int fontSize) {
        CalculatorSettings.fontSize = fontSize;
    }
} 