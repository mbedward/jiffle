package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.JiffleType;

/**
 *
 * @author michael
 */
public class ListLiteral extends Expression {
    private final Expression[] args;
    
    public static ListLiteral of(Expression ...args) {
        return new ListLiteral(args);
    }

    private ListLiteral(Expression ...args) {
        super(JiffleType.LIST);
        this.args = args == null ? new Expression[0] : args;
    }
}
