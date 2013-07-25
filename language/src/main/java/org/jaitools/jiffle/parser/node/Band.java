package org.jaitools.jiffle.parser.node;

/**
 *
 * @author michael
 */
public class Band implements Node {
    public static Band DEFAULT = new Band(new IntLiteralNode("0"));
    
    private final Expression index;

    public Band(Expression index) {
        this.index = index;
    }

    @Override
    public String toString() {
        if (index instanceof IntLiteralNode) {
            return index.toString();
        } else {
            return "(int)(" + index.toString() + ")";
        }
    }
    
}
