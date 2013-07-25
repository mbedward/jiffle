package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.JiffleType;

/**
 *
 * @author michael
 */
public class IntLiteralNode extends LiteralNode {
    
    private static String checkValue(String value) {
        // will throw a NumberFormatException if not a valid integer
        Integer.valueOf(value);
        return value;
    }

    public IntLiteralNode(String value) {
        super(checkValue(value));
    }

    @Override
    public JiffleType getType() {
        return JiffleType.D;
    }

    @Override
    public String toString() {
        return value;
    }
    
    
}
