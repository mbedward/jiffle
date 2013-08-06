package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.JiffleType;

/**
 *
 * @author michael
 */
public abstract class ScalarLiteral extends Expression {
    protected final String value;

    public ScalarLiteral(String value) {
        super(JiffleType.D);
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
    
}
