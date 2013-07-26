package org.jaitools.jiffle.parser;

import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.jaitools.jiffle.Jiffle;
import org.jaitools.jiffle.Jiffle.ImageRole;
import org.jaitools.jiffle.parser.JiffleParser.BlockContext;
import org.jaitools.jiffle.parser.JiffleParser.InitBlockContext;
import org.jaitools.jiffle.parser.JiffleParser.ScriptContext;
import org.jaitools.jiffle.parser.JiffleParser.VarDeclarationContext;
import org.jaitools.jiffle.parser.JiffleParser.VarIDContext;
import org.jaitools.jiffle.parser.node.*;

/**
 * Inspects variables in the parse tree, labels their type and checks 
 * usage.
 * <p>
 * Parts of this code are adapted from 
 * "The Definitive ANTLR 4 Reference" by Terence Parr, 
 * published by The Pragmatic Bookshelf, 2012
 * 
 * @author michael
 */
public class VarWorker extends PropertyWorker<SymbolScope> {

    private final Map<String, ImageRole> imageParams;
    
    final GlobalScope globalScope;
    private SymbolScope currentScope;
    
    
    public VarWorker(
            ParseTree tree, 
            Map<String, Jiffle.ImageRole> imageParams) {
        
        this.imageParams = imageParams;
        this.globalScope = new GlobalScope();
        this.currentScope = globalScope;
        
        addImageVarsToGlobal(imageParams);
        walkTree(tree);
    }
    
    private void addImageVarsToGlobal(Map<String, Jiffle.ImageRole> imageParams) {
        for (Map.Entry<String, Jiffle.ImageRole> e : imageParams.entrySet()) {
            String name = e.getKey();
            
            Symbol.Type type = e.getValue() == Jiffle.ImageRole.SOURCE ?
                    Symbol.Type.SOURCE_IMAGE : Symbol.Type.DEST_IMAGE;
            
            globalScope.add(new Symbol(name, type));
        }
    }

    @Override
    public void enterScript(ScriptContext ctx) {
        set(ctx, globalScope);
    }
    
    @Override
    public void exitInitBlock(InitBlockContext ctx) {
        List<VarDeclarationContext> vars = ctx.varDeclaration();
        for (VarDeclarationContext var : vars) {
            Token tok = var.ID().getSymbol();
            String name = tok.getText();
            
            if (isImage(name)) {
                messages.error(tok, Errors.IMAGE_VAR_INIT_BLOCK + ": " + name);
                
            } else if (currentScope.has(name)) {
                messages.error(tok, Errors.DUPLICATE_VAR_DECL + ": " + name);
                
            } else {
                currentScope.add(new Symbol(name, Symbol.Type.SCALAR));
            }
        }
    }

    @Override
    public void enterBlock(BlockContext ctx) {
        // push a new scope level
        SymbolScope scope = new LocalScope("block", currentScope);
        set(ctx, scope);
        currentScope = scope;
    }

    @Override
    public void exitBlock(BlockContext ctx) {
        // pop the scope level
        currentScope = currentScope.getEnclosingScope();
    }
    
    @Override
    public void exitVarID(VarIDContext ctx) {
        
    }

    
    
    /*
     * Helper methods
     */
    private boolean isImage(String name) { 
        return imageParams.containsKey(name); 
    }
    
    private boolean isSourceImage(String name) { 
        return isImage(name) && imageParams.get(name) == Jiffle.ImageRole.SOURCE; 
    }
    
    private boolean isDestImage(String name) { 
        return isImage(name) && imageParams.get(name) == Jiffle.ImageRole.DEST; 
    }
    
    private Function mkfn(String name) {
        try {
            return new Function(name);
        } catch(UndefinedFunctionException ex) {
            // we should never be here
            throw new IllegalArgumentException(ex);
        }
    }
    
}
