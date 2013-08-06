package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.DirectSources;
import org.jaitools.jiffle.parser.Errors;
import org.jaitools.jiffle.parser.JiffleType;

/**
 * Separate node type for con functions which are implemented as directly
 * injected source fragments in the runtime class rather than by the
 * function lookup mechanism.
 * 
 * @author michael
 */
public class ConFunction extends Expression {
    private final Expression[] args;
    
    public ConFunction(Expression ...args) throws NodeException {
        super( args[0].getType() );
        
        // first arg (condition) must be scalar
        if (args[0].getType() != JiffleType.D) {
            throw new NodeException(Errors.CON_CONDITION_MUST_BE_SCALAR);
        } 
        
        if (args.length > 2) {
            for (int i = 2; i < args.length; i++) {
                if (args[1].getType() != args[i].getType()) {
                    throw new NodeException(Errors.CON_RESULTS_MUST_BE_SAME_TYPE);
                }
            }
        }

        this.args = args;
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
