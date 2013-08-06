package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.JiffleType;

/**
 *
 * @author michael
 */
public class ScalarVar extends Expression {
    private final String name;

    public ScalarVar(String name) {
        super(JiffleType.D);
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
