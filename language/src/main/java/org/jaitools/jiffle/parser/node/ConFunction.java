package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.DirectSources;
import org.jaitools.jiffle.parser.JiffleType;

/**
 * Separate node type for con functions which are mapped to directly
 * created source fragments by the compiler.
 * 
 * @author michael
 */
public class ConFunction extends Expression {
    private final Expression[] args;

    public ConFunction(Expression... args) {
        this.args = args;
    }

    @Override
    public JiffleType getType() {
        return JiffleType.D;
    }

    @Override
    public String toString() {
        String[] argStrs = new String[args.length];
        
        int i = 0;
        for (Expression arg : args) {
            argStrs[i++] = arg.toString();
        }
        
        return DirectSources.conCall(argStrs);
    }
    
    
}
