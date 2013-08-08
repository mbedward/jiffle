package org.jaitools.jiffle.parser.node;

import java.util.List;

/**
 *
 * @author michael
 */
public class GlobalVars implements Node {
    private final List<BinaryExpression> inits;

    /**
     * Creates an empty instance.
     */
    public GlobalVars() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Creates an instance containing the given variables and optional 
     * initial values.
     */
    public GlobalVars(List<BinaryExpression> inits) {
        this.inits = inits;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (BinaryExpression init : inits) {
            sb.append(init).append(";\n");
        }
        return sb.toString();
    }

}
