package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.util.Strings;

/**
 *
 * @author michael
 */
public class PrefixUnaryExpression extends Expression {
    
    private final Expression arg;
    private final String op;
    
    public PrefixUnaryExpression(String op, Expression arg) {
        super(arg.getType());
        this.op = op;
        this.arg = arg;
    }

    @Override
    public String toString() {
        return Strings.spaces(op, arg);
    }

    
}
