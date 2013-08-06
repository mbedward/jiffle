package org.jaitools.jiffle.parser;

/**
 *
 * @author michael
 */
public class InternalCompilerException extends IllegalStateException {

    public InternalCompilerException() {
        super("Internal compiler error");
    }

    public InternalCompilerException(String msg) {
        super("Internal compiler error: " + msg);
    }
    
}
