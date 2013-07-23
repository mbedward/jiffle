/* 
 *  Copyright (c) 2013, Michael Bedward. All rights reserved. 
 *   
 *  Redistribution and use in source and binary forms, with or without modification, 
 *  are permitted provided that the following conditions are met: 
 *   
 *  - Redistributions of source code must retain the above copyright notice, this  
 *    list of conditions and the following disclaimer. 
 *   
 *  - Redistributions in binary form must reproduce the above copyright notice, this 
 *    list of conditions and the following disclaimer in the documentation and/or 
 *    other materials provided with the distribution.   
 *   
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR 
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */  

package org.jaitools.jiffle.parser;

import java.util.Map;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.jaitools.jiffle.Jiffle;
import org.jaitools.jiffle.Jiffle.ImageRole;
import org.jaitools.jiffle.parser.JiffleParser.AssignmentContext;
import org.jaitools.jiffle.parser.JiffleParser.BlockContext;
import org.jaitools.jiffle.parser.JiffleParser.ExpressionContext;
import org.jaitools.jiffle.parser.JiffleParser.ForeachStmtContext;
import org.jaitools.jiffle.parser.JiffleParser.InitBlockContext;
import org.jaitools.jiffle.parser.JiffleParser.ScriptContext;
import org.jaitools.jiffle.parser.JiffleParser.VarDeclarationContext;

/**
 * Decorates the parse tree to identify variable types.
 * 
 * @author michael
 */
public class VarWorker extends BaseWorker {
    private final SymbolScopeStack scope;
    private final Map<String, ImageRole> imageVarParams;
    private final ParseTreeProperty<NodeDecoration> decorations;

    public VarWorker(Map<String, Jiffle.ImageRole> imageVarParams) {
        this.imageVarParams = imageVarParams;
        this.scope = new SymbolScopeStack();
        this.decorations = new ParseTreeProperty<NodeDecoration>();
    }

    @Override
    public void enterScript(ScriptContext ctx) {
        scope.addLevel("top");
    }

    @Override
    public void exitInitBlock(InitBlockContext ctx) {
        for (VarDeclarationContext varDecl : ctx.varDeclaration()) {
            String name = varDecl.ID().getText();
            
            if (imageVarParams.containsKey(name)) {
                messages.error(varDecl.ID().getSymbol(),
                        "A value cannot be assigned to an image var in the init block");
            } else {
                scope.addSymbol(name, SymbolType.SCALAR, ScopeType.IMAGE);
                decorations.put(varDecl, new VarDeclDecoration(ScopeType.IMAGE));
            }
        }
    }

    @Override
    public void enterBlock(BlockContext ctx) {
        scope.addLevel("block");
    }

    @Override
    public void exitBlock(BlockContext ctx) {
        scope.dropLevel();
    }

    @Override
    public void enterForeachStmt(ForeachStmtContext ctx) {
        scope.addLevel("foreach");
    }

    @Override
    public void exitForeachStmt(ForeachStmtContext ctx) {
        scope.dropLevel();
        
        String varName = ctx.ID().getText();
        scope.addSymbol(varName, SymbolType.LOOP_VAR, ScopeType.PIXEL);
    }

    @Override
    public void enterAssignment(AssignmentContext ctx) {
    }

    @Override
    public void exitAssignment(AssignmentContext ctx) {
        String name = ctx.ID().getText();
        if (scope.isDefined(name)) {
            
        } else {
            
        }
    }
    
    
    
}
