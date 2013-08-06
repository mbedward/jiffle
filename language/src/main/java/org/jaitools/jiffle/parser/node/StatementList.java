package org.jaitools.jiffle.parser.node;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author michael
 */
public class StatementList implements Node {
    
    private final List<Statement> stmts;

    public StatementList(List<Statement> stmts) {
        this.stmts = new ArrayList<Statement>(stmts);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Statement stmt : stmts) {
            sb.append(stmt).append('\n');
        }
        return sb.toString();
    }
    
    
}
