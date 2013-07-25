package org.jaitools.jiffle.parser.node;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jaitools.jiffle.parser.UndefinedFunctionException;
import org.jaitools.jiffle.util.Strings;

/**
 *
 * @author michael
 */
public class Pixel implements Node {
    public static final Pixel DEFAULT;
    static {
        try {
            DEFAULT = new Pixel( new Function("x"), new Function("y"));
        } catch (UndefinedFunctionException ex) {
            // something awful must have happened to get here
            throw new IllegalStateException(ex);
        }
    }
    
    private final Expression x;
    private final Expression y;

    public Pixel(Expression x, Expression y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return Strings.commas(x, y);
    }
    
}
