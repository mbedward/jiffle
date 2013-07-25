package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.JiffleType;

/**
 *
 * @author michael
 */
public class ImageRead extends Expression {
    private static String fmt = "readFromImage(%s,%s)";
    
    private final String varName;
    private final ImagePos pos;

    public ImageRead(String varName, ImagePos pos) {
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
