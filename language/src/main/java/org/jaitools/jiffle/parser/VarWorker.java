package org.jaitools.jiffle.parser;

import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jaitools.jiffle.Jiffle;
import org.jaitools.jiffle.parser.JiffleParser.AssignmentContext;
import org.jaitools.jiffle.parser.JiffleParser.BlockContext;
import org.jaitools.jiffle.parser.JiffleParser.ForeachStmtContext;
import org.jaitools.jiffle.parser.JiffleParser.InitBlockContext;
import org.jaitools.jiffle.parser.JiffleParser.ScriptContext;
import org.jaitools.jiffle.parser.JiffleParser.VarDeclarationContext;
import org.jaitools.jiffle.parser.JiffleParser.VarIDContext;

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

    final GlobalScope globalScope;
    private SymbolScope currentScope;
    
    
    public VarWorker(
            ParseTree tree, 
            Map<String, Jiffle.ImageRole> imageParams) {
        
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
                error(tok, Errors.IMAGE_VAR_INIT_BLOCK, name);
                
            } else if (currentScope.has(name)) {
                error(tok, Errors.DUPLICATE_VAR_DECL, name);
                
            } else {
                currentScope.add(new Symbol(name, Symbol.Type.SCALAR));
            }
        }
    }

    @Override
    public void enterBlock(BlockContext ctx) {
        pushScope("block");
    }

    @Override
    public void exitBlock(BlockContext ctx) {
        popScope();
    }

    @Override
    public void enterForeachStmt(ForeachStmtContext ctx) {
        // The foreach statement creates its own scope within
        // which the loop variable is defined.
        pushScope("foreach");
        
        // Loop var
        Token tok = ctx.ID().getSymbol();
        String name = tok.getText();

        // Loop var is allowed to shadow any vars of the same name
        // in enclosing scopes
        currentScope.add(new Symbol(name, Symbol.Type.LOOP_VAR));
    }
    
    @Override
    public void exitForeachStmt(ForeachStmtContext ctx) {
        popScope();
    }
    
    @Override
    public void exitAssignment(AssignmentContext ctx) {
        Token tok = ctx.ID().getSymbol();

        if (isValidAssignment(ctx)) {
            String name = tok.getText();

            // Add the symbol if this is its first appearance
            if (!currentScope.has(name)) {
                currentScope.add(new Symbol(name, Symbol.Type.SCALAR));
            }
        }
    }
    
    @Override
    public void exitVarID(VarIDContext ctx) {
        /*
         * We are in an expression (but not LHS) to get here.
         */
        Token tok = ctx.ID().getSymbol();
        String name = tok.getText();
        
        if (!currentScope.has(name)) {
            error(tok, Errors.VAR_UNDEFINED, name);
            
        } else if (isDestImage(name)) {
            error(tok, Errors.READING_FROM_DEST_IMAGE, name);
        }
        
    }

    
    private boolean isValidAssignment(AssignmentContext ctx) {
        Token tok = ctx.ID().getSymbol();
        String name = tok.getText();
        
        // Short-cut: scalar that is already defined is OK
        if (currentScope.has(name) 
                && currentScope.get(name).getType() == Symbol.Type.SCALAR) {
            return true;
        }
    
        if (isLoopVar(name)) {
            // Trying to assign to a loop var within loop scope
            error(tok, Errors.ASSIGNMENT_TO_LOOP_VAR, name);
            
        } else if (isSourceImage(name)) {
            // Trying to write to a source image
            error(tok, Errors.WRITING_TO_SOURCE_IMAGE, name);
            return false;
            
        } else if (isDestImage(name) && ctx.ASSIGN() == null) {
            // Using operator other than simple assignment (=) with 
            // destination image
            error(tok, Errors.INVALID_ASSIGNMENT_OP_WITH_DEST_IMAGE, name);
            return false;
        
        } else if (ConstantLookup.isDefined(name)) {
            // Trying to write to a built-in constant
            error(tok, Errors.ASSIGNMENT_TO_CONSTANT, name);
            return false;
        }
        
        return true;
    }
    

    private void pushScope(String newScopeLabel) {
        SymbolScope newScope = new LocalScope(newScopeLabel, currentScope);
        currentScope = newScope;
    }
    
    private void popScope() {
        currentScope = currentScope.getEnclosingScope();
    }
    
    private void error(Token tok, Errors error, String varName) {
        messages.error(tok, error + ": " + varName);
    }
    
    private boolean isImage(String name) { 
        return isSourceImage(name) || isDestImage(name);
    }
    
    private boolean isSourceImage(String name) { 
        if (globalScope.has(name)) {
            return globalScope.get(name).getType() == Symbol.Type.SOURCE_IMAGE;
        }
        return false;
    }
    
    private boolean isDestImage(String name) { 
        if (globalScope.has(name)) {
            return globalScope.get(name).getType() == Symbol.Type.DEST_IMAGE;
        }
        return false;
    }
    
    private boolean isLoopVar(String name) {
        if (currentScope.has(name)) {
            return currentScope.get(name).getType() == Symbol.Type.LOOP_VAR;
        }
        return false;
    }
    
    private boolean isFunction(String name) {
        return FunctionLookup.isDefined(name);
    }
    
}
