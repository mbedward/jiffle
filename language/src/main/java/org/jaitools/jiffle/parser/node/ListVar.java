package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.JiffleType;

/**
 *
 * @author michael
 */
public class ListVar extends Expression {
    private final String name;

    public ListVar(String name) {
        super(JiffleType.LIST);
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
