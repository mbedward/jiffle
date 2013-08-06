package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.JiffleType;

/**
 *
 * @author michael
 */
public class IntLiteral extends ScalarLiteral {
    
    private static String checkValue(String value) {
        // will throw a NumberFormatException if not a valid integer
        Integer.valueOf(value);
        return value;
    }

    public IntLiteral(String value) {
        super(checkValue(value));
    }

}
