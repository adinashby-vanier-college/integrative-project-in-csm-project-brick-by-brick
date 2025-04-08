package edu.vanier.brickbybrick.allinonecalculator.logic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The logical operations for the Arithmetic Calculator.
 */
public class ArithmeticCalculatorLogic {
    private static long historyID = 1L;

    private final ComputeEngine computeEngine = new ComputeEngine();

    public String calculate(String expression) {
        return null;
    }

    public VBox createHistoryItem(String expression, String result) throws IOException {
        VBox historyItem = new VBox();

        TeXFormula formula = new TeXFormula(expression);
        TeXIcon icon = formula.new TeXIconBuilder().setStyle(TeXConstants.STYLE_DISPLAY).setSize(30).build();
        icon.setInsets(new Insets(5, 5, 5, 5));

        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.white);
        g2.fillRect(0,0,icon.getIconWidth(),icon.getIconHeight());
        JLabel jl = new JLabel();
        jl.setForeground(new Color(0, 0, 0));
        icon.paintIcon(jl, g2, 0, 0);

        if (!new File("history_img").exists()) {
            new File("history_img").mkdirs();
        } else {
            new File("history_img").delete();
            new File("history_img").mkdirs();
        }

        File file = new File("history_img/history_" + historyID++ + ".png");
        ImageIO.write(image, "png", file.getAbsoluteFile());

        ImageView img = new ImageView(new Image(file.toURI().toString()));
        img.setFitHeight(32);
        Text resultText = new Text(result);
        resultText.setStyle("-fx-font-size: 12px; -fx-text-fill: #888; -fx-text-alignment: right;");

        historyItem.getChildren().addAll(img, resultText);

        return historyItem;
    }
}
