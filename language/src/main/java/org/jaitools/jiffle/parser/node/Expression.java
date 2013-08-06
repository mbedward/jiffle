package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.JiffleType;

/**
 *
 * @author michael
 */
public abstract class Expression implements Node {
    
    private final JiffleType type;

    protected Expression(JiffleType type) {
        this.type = type;
    }
    
    public JiffleType getType() {
        return type;
    }
}
