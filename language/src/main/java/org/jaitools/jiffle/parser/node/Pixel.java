package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.util.Strings;

/**
 *
 * @author michael
 */
public class Pixel implements Node {
    public static final Pixel DEFAULT;
    
    static {
        try {
            DEFAULT = new Pixel( FunctionCall.of("x"), FunctionCall.of("y") );
            
        } catch (NodeException ex) {
            // Getting here means something must be wrong with
            // the function lookup
            throw new ExceptionInInitializerError();
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
