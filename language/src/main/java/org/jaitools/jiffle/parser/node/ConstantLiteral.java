package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.ConstantLookup;

/**
 *
 * @author michael
 */
public class ConstantLiteral extends ScalarLiteral {

    /**
     * Client code should use static factory methods.
     */
    private ConstantLiteral(String value) {
        super(value);
    }
    
    public static ConstantLiteral trueValue() {
        return new ConstantLiteral("1.0");
    }
    
    
    public static ConstantLiteral falseValue() {
        return new ConstantLiteral("0.0");
    }
    
    public static ConstantLiteral nanValue() { 
        return new ConstantLiteral(
                String.valueOf(ConstantLookup.getValue("NAN")) ); 
    }
    
    
}
