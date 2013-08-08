package org.jaitools.jiffle.parser.node;

/**
 *
 * @author michael
 */
public class Script implements Node {
    private final StatementList stmts;
    private final GlobalVars globals;

    public Script(GlobalVars globals, StatementList stmts) {
        this.globals = globals;
        this.stmts = stmts;
    }

    @Override
    public String toString() {
        // TODO - format for runtime class source
        return globals + "\n\n" + stmts;
    }
    
}
