package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.Errors;
import org.jaitools.jiffle.parser.FunctionInfo;
import org.jaitools.jiffle.parser.FunctionLookup;
import org.jaitools.jiffle.parser.JiffleType;
import org.jaitools.jiffle.parser.UndefinedFunctionException;
import org.jaitools.jiffle.util.Strings;

/**
 *
 * @author michael
 */
public class FunctionCall extends Expression {

    private final String runtimeName;
    private final boolean proxy;
    private final Expression[] args;
    
    public static FunctionCall of(String jiffleName, Expression ...args) 
            throws NodeException {
        
        JiffleType[] argTypes =
                (args == null) ? new JiffleType[0] : new JiffleType[args.length];
        
        for (int i = 0; i < argTypes.length; i++) {
            argTypes[i] = args[i].getType();
        }
        
        try {
            FunctionInfo info = FunctionLookup.getInfo(jiffleName, argTypes);
            return new FunctionCall(info, args);
        } catch (UndefinedFunctionException ex) {
            throw new NodeException(Errors.UNKNOWN_FUNCTION);
        }
    }

    private FunctionCall(FunctionInfo info, Expression ...args)
            throws NodeException {
        
        super(info.getReturnType());
        
        this.runtimeName = info.getRuntimeName();
        this.proxy = info.isProxy();
        this.args = args == null ? new Expression[0] : args;
    }

    @Override
    public String toString() {
        // All other functions
        String tail = proxy ? "" : String.format("(%s)", Strings.commas((Object []) args));
        return runtimeName + tail;
    }

}
