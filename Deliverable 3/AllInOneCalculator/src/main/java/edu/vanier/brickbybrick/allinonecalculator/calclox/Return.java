package edu.vanier.brickbybrick.allinonecalculator.calclox;

/**
 * The instance of a returned value from a function.
 */
public class Return extends RuntimeException {
    public final Object value;

    public Return(Object value) {
        super(null, null, false, false);
        this.value = value;
    }
}
