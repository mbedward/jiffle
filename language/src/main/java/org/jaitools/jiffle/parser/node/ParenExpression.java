package org.jaitools.jiffle.parser.node;

/**
 *
 * @author michael
 */
public class ParenExpression extends Expression {
    private final Expression expr;

    public ParenExpression(Expression e) {
        super(e.getType());
        this.expr = e;
    }

    @Override
    public String toString() {
        return "(" + expr + ")";
    }
    
}
