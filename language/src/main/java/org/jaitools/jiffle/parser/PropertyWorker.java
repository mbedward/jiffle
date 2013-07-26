package org.jaitools.jiffle.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 *
 * @author michael
 */
public abstract class PropertyWorker<T> extends BaseWorker {
    protected final ParseTreeProperty<T> properties;
    
    public PropertyWorker() {
        this.properties = new ParseTreeProperty<T>();
    }

    public PropertyWorker(ParseTreeProperty<T> properties) {
        this.properties = properties;
    }
    
    protected T get(ParseTree ctx) {
        return properties.get(ctx);
    }
    
    protected T getOrElse(ParseTree ctx, T fallback) {
        if (ctx == null) {
            return fallback;
        } 
        T prop = properties.get(ctx);
        return prop == null ? fallback : prop;
    }

    protected void set(ParseTree ctx, T node) {
        properties.put(ctx, node);
    }
}
