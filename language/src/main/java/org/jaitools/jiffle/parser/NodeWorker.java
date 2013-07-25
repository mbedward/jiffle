package org.jaitools.jiffle.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.jaitools.jiffle.parser.node.Node;

/**
 *
 * @author michael
 */
public abstract class NodeWorker extends BaseWorker {
    public final ParseTreeProperty<Node> nodes;

    public NodeWorker(ParseTreeProperty<Node> nodes) {
        this.nodes = nodes;
    }

    protected Node get(ParseTree ctx) {
        return nodes.get(ctx);
    }
    
    protected Node getOrElse(ParseTree ctx, Node fallback) {
        if (ctx == null) {
            return fallback;
        } 
        Node node = nodes.get(ctx);
        return node == null ? fallback : node;
    }

    protected void set(ParseTree ctx, Node node) {
        nodes.put(ctx, node);
    }
    
}
