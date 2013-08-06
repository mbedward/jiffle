package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.JiffleType;

/**
 *
 * @author michael
 */
public class GetSourceValue extends Expression {
    private static String fmt = "readFromImage(%s,%s)";
    
    private final String varName;
    private final ImagePos pos;

    public GetSourceValue(String varName, ImagePos pos) {
        super(JiffleType.D);
        this.varName = varName;
        this.pos = pos;
    }

    @Override
    public JiffleType getType() {
        return JiffleType.D;
    }

    @Override
    public String toString() {
        return String.format(fmt, varName, pos);
    }
    
}
