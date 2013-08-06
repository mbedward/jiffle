package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.JiffleType;

/**
 *
 * @author michael
 */
public class DoubleLiteral extends ScalarLiteral {
    
    private static String checkValue(String value) {
        // will throw a NumberFormatException if not a valid double
        Double.valueOf(value);
        return value;
    }

    public DoubleLiteral(String value) {
        super(checkValue(value));
    }

}
