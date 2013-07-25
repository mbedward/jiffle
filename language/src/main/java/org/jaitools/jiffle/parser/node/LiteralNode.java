package org.jaitools.jiffle.parser.node;

/**
 *
 * @author michael
 */
public abstract class LiteralNode extends Expression {
    protected final String value;

    public LiteralNode(String value) {
        this.value = value;
    }

}
