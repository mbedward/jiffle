package org.jaitools.jiffle.parser.node;

/**
 *
 * @author michael
 */
public class Statement implements Node {
    private final Expression expr;

    public Statement(Expression e) {
        this.expr = e;
    }

    @Override
    public String toString() {
        return expr + ";" ;
    }
    
}
