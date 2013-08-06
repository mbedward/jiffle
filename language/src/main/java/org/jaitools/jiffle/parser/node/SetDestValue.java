package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.Jiffle;
import org.jaitools.jiffle.Jiffle.RuntimeModel;
import org.jaitools.jiffle.parser.DirectSources;


/**
 *
 * @author michael
 */
public class SetDestValue implements Node {
    private final RuntimeModel runtimeModel;
    private final String destVar;
    private final Expression expr;

    public SetDestValue(Jiffle.RuntimeModel runtimeModel, String varName, Expression expr) {
        this.runtimeModel = runtimeModel;
        this.destVar = varName;
        this.expr = expr;
    }
    
    @Override
    public String toString() {
        return DirectSources.setDestValue(runtimeModel, destVar, expr.toString());
    }
}
