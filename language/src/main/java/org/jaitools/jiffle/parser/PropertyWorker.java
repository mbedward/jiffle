package org.jaitools.jiffle.parser;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 *
 * @author michael
 */
public abstract class PropertyWorker<T> extends BaseWorker {
    protected final TreeNodeProperties<T> properties;
    
    public PropertyWorker(ParseTree tree) {
        super(tree);
        this.properties = new TreeNodeProperties<T>();
    }

    public TreeNodeProperties<T> getProperties() {
        return new TreeNodeProperties<T>(properties);
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
