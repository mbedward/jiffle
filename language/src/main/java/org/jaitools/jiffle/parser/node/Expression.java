package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.JiffleType;

/**
 *
 * @author michael
 */
public abstract class Expression implements Node {
    
    public abstract JiffleType getType();
}
