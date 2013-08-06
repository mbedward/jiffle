package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.util.Strings;

/**
 *
 * @author michael
 */
public class PostfixUnaryExpression extends Expression {
    
    private final Expression arg;
    private final String op;
    
    public PostfixUnaryExpression(Expression arg, String op) {
        super(arg.getType());
        this.arg = arg;
        this.op = op;
    }

    @Override
    public String toString() {
        return Strings.spaces(arg, op);
    }

    
}
