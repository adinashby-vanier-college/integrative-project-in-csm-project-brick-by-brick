package edu.vanier.brickbybrick.allinonecalculator.settings;

import javafx.scene.paint.Color;
import java.io.*;
import java.util.prefs.Preferences;

public class CalculatorSettings {
    private static final Preferences prefs = Preferences.userNodeForPackage(CalculatorSettings.class);
    
    // General Settings
    public static final String DATA_SOURCE = "dataSource";
    public static final String RESOURCE_PATH = "resourcePath";
    
    // Graph Settings
    public static final String GRAPH_ORIENTATION = "graphOrientation";
    public static final String GRAPH_SIZE = "graphSize";
    
    // Theme Settings
    public static final String BACKGROUND_COLOR = "backgroundColor";
    public static final String FONT_SIZE = "fontSize";
    
    // Default values
    private static final String DEFAULT_DATA_SOURCE = "FILE";
    private static final String DEFAULT_RESOURCE_PATH = "resources/";
    private static final String DEFAULT_GRAPH_ORIENTATION = "VERTICAL";
    private static final int DEFAULT_GRAPH_SIZE = 100;
    private static final String DEFAULT_BACKGROUND_COLOR = "#FFFFFF";
    private static final int DEFAULT_FONT_SIZE = 12;
    
    public static void setDataSource(String source) {
        prefs.put(DATA_SOURCE, source);
    }
    
    public static String getDataSource() {
        return prefs.get(DATA_SOURCE, DEFAULT_DATA_SOURCE);
    }
    
    public static void setResourcePath(String path) {
        prefs.put(RESOURCE_PATH, path);
    }
    
    public static String getResourcePath() {
        return prefs.get(RESOURCE_PATH, DEFAULT_RESOURCE_PATH);
    }
    
    public static void setGraphOrientation(String orientation) {
        prefs.put(GRAPH_ORIENTATION, orientation);
    }
    
    public static String getGraphOrientation() {
        return prefs.get(GRAPH_ORIENTATION, DEFAULT_GRAPH_ORIENTATION);
    }
    
    public static void setGraphSize(int size) {
        prefs.putInt(GRAPH_SIZE, size);
    }
    
    public static int getGraphSize() {
        return prefs.getInt(GRAPH_SIZE, DEFAULT_GRAPH_SIZE);
    }
    
    public static void setBackgroundColor(String color) {
        prefs.put(BACKGROUND_COLOR, color);
    }
    
    public static String getBackgroundColor() {
        return prefs.get(BACKGROUND_COLOR, DEFAULT_BACKGROUND_COLOR);
    }
    
    public static void setFontSize(int size) {
        prefs.putInt(FONT_SIZE, size);
    }
    
    public static int getFontSize() {
        return prefs.getInt(FONT_SIZE, DEFAULT_FONT_SIZE);
    }
    
    public static void resetToDefaults() {
        setDataSource(DEFAULT_DATA_SOURCE);
        setResourcePath(DEFAULT_RESOURCE_PATH);
        setGraphOrientation(DEFAULT_GRAPH_ORIENTATION);
        setGraphSize(DEFAULT_GRAPH_SIZE);
        setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
        setFontSize(DEFAULT_FONT_SIZE);
    }
} 