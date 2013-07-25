package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.FunctionInfo;
import org.jaitools.jiffle.parser.FunctionLookup;
import org.jaitools.jiffle.parser.JiffleParserException;
import org.jaitools.jiffle.parser.JiffleType;
import org.jaitools.jiffle.parser.UndefinedFunctionException;
import org.jaitools.jiffle.util.Strings;

/**
 *
 * @author michael
 */
public class Function extends Expression {

    private final String runtimeName;
    private final boolean proxy;
    private final Expression[] args;

    public Function(String jiffleName, Expression ...args) throws UndefinedFunctionException {
        JiffleType[] argTypes =
                (args == null) ? new JiffleType[0] : new JiffleType[args.length];
        
        for (int i = 0; i < argTypes.length; i++) {
            argTypes[i] = args[i].getType();
        }
        
        FunctionInfo info = FunctionLookup.getInfo(jiffleName, argTypes);
        this.runtimeName = info.getRuntimeName();
        this.proxy = info.isProxy();
        this.args = args == null ? new Expression[0] : args;
    }

    @Override
    public JiffleType getType() {
        try {
            return FunctionLookup.getReturnType(runtimeName);
        } catch (UndefinedFunctionException ex) {
            throw new JiffleParserException(ex);
        }
    }

    @Override
    public String toString() {
        // All other functions
        String tail = proxy ? "" : String.format("(%s)", Strings.commas((Object []) args));
        return runtimeName + tail;
    }

}
