package edu.vanier.brickbybrick.allinonecalculator.calclox;

import edu.vanier.brickbybrick.allinonecalculator.calclox.token.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * The lexical scanner for the CalcLox interpreter.
 *
 * @author Qian Qian
 */
public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    public Scanner(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        return null;
    }
}
