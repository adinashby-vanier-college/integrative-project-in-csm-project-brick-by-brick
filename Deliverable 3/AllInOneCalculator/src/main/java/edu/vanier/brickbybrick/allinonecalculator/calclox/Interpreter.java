package edu.vanier.brickbybrick.allinonecalculator.calclox;

/**
 * The interpreter for the CalcLox language powering the calculator's programmable features.
 *
 * @author Qian Qian
 */
class Interpreter implements Expr.Visitor<Object> {
    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        return null;
    }
}
