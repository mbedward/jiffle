package org.jaitools.jiffle.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * Extends the base parse tree listener and adds a messages
 * field and a method to walk a given tree.
 * 
 * @author michael
 */
public abstract class TreeWorker extends JiffleBaseListener {
    
    public CompilerMessages messages = new CompilerMessages();
    
    protected void walkTree(ParseTree tree) {
        new ParseTreeWalker().walk(this, tree);
    }
    
}
