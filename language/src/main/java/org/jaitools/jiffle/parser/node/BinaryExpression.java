package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.JiffleType;

/**
 *
 * @author michael
 */
public class BinaryExpression extends Expression {
    
    private final Expression left;
    private final Expression right;
    private final String op;

    public BinaryExpression(String op, Expression left, Expression right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public JiffleType getType() {
        return JiffleType.D;
    }
    
}
