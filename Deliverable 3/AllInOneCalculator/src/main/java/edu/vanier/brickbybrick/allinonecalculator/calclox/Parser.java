package edu.vanier.brickbybrick.allinonecalculator.calclox;

import edu.vanier.brickbybrick.allinonecalculator.calclox.token.Token;

import java.util.List;

/**
 * The expression parser for the CalcLox language interpreter.
 *
 * @author Qian Qian
 */
class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Statement> parse() {
        return null;
    }
}
