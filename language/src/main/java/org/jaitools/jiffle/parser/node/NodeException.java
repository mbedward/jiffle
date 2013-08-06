package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.Errors;

/**
 *
 * @author michael
 */
public class NodeException extends Exception {
    private final Errors error;

    public NodeException(Errors error) {
        super(error.toString());
        this.error = error;
    }
    
    public Errors getError() {
        return error;
    }
}
