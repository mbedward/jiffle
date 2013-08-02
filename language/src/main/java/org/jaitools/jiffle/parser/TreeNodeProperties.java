package org.jaitools.jiffle.parser;

import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 * Extends the ANTLR ParseTreeProperties class and adds 
 * a copy constructor.
 *
 * @author michael
 */
public class TreeNodeProperties<T> extends ParseTreeProperty<T> {

    public TreeNodeProperties() {
    }

    public TreeNodeProperties(TreeNodeProperties<T> other) {
        if (other == null) {
            throw new IllegalArgumentException("Null argument");
        }
        
        this.annotations.putAll(other.annotations);
    }
}
