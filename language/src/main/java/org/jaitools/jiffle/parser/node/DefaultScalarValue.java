package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.*;

/**
 * A placeholder node representing a default value for some
 * scalar variable.
 * 
 * @author michael
 */
public class DefaultScalarValue extends Expression {

    public DefaultScalarValue() {
        super(JiffleType.D);
    }
    
}
