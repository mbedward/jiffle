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

import org.antlr.v4.runtime.tree.ParseTree;
import org.jaitools.jiffle.parser.JiffleParser.AndExprContext;
import org.jaitools.jiffle.parser.JiffleParser.AssignExprContext;
import org.jaitools.jiffle.parser.JiffleParser.AssignmentContext;
import org.jaitools.jiffle.parser.JiffleParser.AtomContext;
import org.jaitools.jiffle.parser.JiffleParser.AtomExprContext;
import org.jaitools.jiffle.parser.JiffleParser.CompareExprContext;
import org.jaitools.jiffle.parser.JiffleParser.ConCallContext;
import org.jaitools.jiffle.parser.JiffleParser.EqExprContext;
import org.jaitools.jiffle.parser.JiffleParser.ExpressionContext;
import org.jaitools.jiffle.parser.JiffleParser.FunctionCallContext;
import org.jaitools.jiffle.parser.JiffleParser.ImageCallContext;
import org.jaitools.jiffle.parser.JiffleParser.ListLiteralContext;
import org.jaitools.jiffle.parser.JiffleParser.LiteralContext;
import org.jaitools.jiffle.parser.JiffleParser.NotExprContext;
import org.jaitools.jiffle.parser.JiffleParser.OrExprContext;
import org.jaitools.jiffle.parser.JiffleParser.ParenExpressionContext;
import org.jaitools.jiffle.parser.JiffleParser.PlusMinusExprContext;
import org.jaitools.jiffle.parser.JiffleParser.PostExprContext;
import org.jaitools.jiffle.parser.JiffleParser.PowExprContext;
import org.jaitools.jiffle.parser.JiffleParser.PreExprContext;
import org.jaitools.jiffle.parser.JiffleParser.RangeContext;
import org.jaitools.jiffle.parser.JiffleParser.TernaryExprContext;
import org.jaitools.jiffle.parser.JiffleParser.TimesDivModExprContext;
import org.jaitools.jiffle.parser.JiffleParser.VarIDContext;
import org.jaitools.jiffle.parser.JiffleParser.XorExprContext;

/**
 * Labels expression nodes with their Jiffle types. Expects that
 * the parse tree has been previously annotated by a VarWorker.
 * 
 * @author michael
 */
public class ExpressionWorker extends PropertyWorker<JiffleType> {
    private final TreeNodeProperties<SymbolScope> scopes;
    private SymbolScope currentScope;

    public ExpressionWorker(ParseTree tree, VarWorker vw) {
        super(tree);
        
        this.scopes = vw.getProperties();
        currentScope = scopes.get(tree);
        
        walkTree();
    }

    @Override
    public void exitRange(RangeContext ctx) {
        // both left and right must be scalar
        JiffleType left = get(ctx.expression(0));
        JiffleType right = get(ctx.expression(1));
        if (left != JiffleType.D || right != JiffleType.D) {
            messages.error(ctx.getStart(), Errors.LIST_IN_RANGE);
        }
        set(ctx, JiffleType.LIST);
    }
    
    @Override
    public void exitAtomExpr(AtomExprContext ctx) {
        set(ctx, get(ctx.atom()));
    }

    @Override
    public void exitPowExpr(PowExprContext ctx) {
        JiffleType arg = get(ctx.expression(0));
        JiffleType exponent = get(ctx.expression(1));
        
        // exponent type must be scalar
        if (exponent != JiffleType.D) {
            messages.error(ctx.getStart(), Errors.POW_EXPR_WITH_LIST_EXPONENT);
        }
        
        // result type will be that of the arg
        set(ctx, arg);
    }

    @Override
    public void exitPostExpr(PostExprContext ctx) {
        set(ctx, get(ctx.expression()));
    }
    
    @Override
    public void exitPreExpr(PreExprContext ctx) {
        set(ctx, get(ctx.expression()));
    }
    
    @Override
    public void exitNotExpr(NotExprContext ctx) {
        set(ctx, get(ctx.expression()));
    }
    
    @Override
    public void exitTimesDivModExpr(TimesDivModExprContext ctx) {
        setBinaryExprType(ctx, ctx.expression(0), ctx.expression(1));
    }
    
    @Override
    public void exitPlusMinusExpr(PlusMinusExprContext ctx) {
        setBinaryExprType(ctx, ctx.expression(0), ctx.expression(1));
    }
    
    @Override
    public void exitCompareExpr(CompareExprContext ctx) {
        setBinaryExprType(ctx, ctx.expression(0), ctx.expression(1));
    }

    @Override
    public void exitEqExpr(EqExprContext ctx) {
        setBinaryExprType(ctx, ctx.expression(0), ctx.expression(1));
    }

    @Override
    public void exitAndExpr(AndExprContext ctx) {
        setBinaryExprType(ctx, ctx.expression(0), ctx.expression(1));
    }

    @Override
    public void exitOrExpr(OrExprContext ctx) {
        setBinaryExprType(ctx, ctx.expression(0), ctx.expression(1));
    }

    @Override
    public void exitXorExpr(XorExprContext ctx) {
        setBinaryExprType(ctx, ctx.expression(0), ctx.expression(1));
    }

    @Override
    public void exitTernaryExpr(TernaryExprContext ctx) {
        // first expression (condition) must be scalar (true / false)
        JiffleType condType = get(ctx.expression(0));
        if (condType != JiffleType.D) {
            messages.error(ctx.getStart(), Errors.LIST_AS_TERNARY_CONDITION);
        }
        setBinaryExprType(ctx, ctx.expression(1), ctx.expression(2));
    }

    @Override
    public void exitAssignExpr(AssignExprContext ctx) {
        set(ctx, get(ctx.assignment()));
    }
    
    private void setBinaryExprType(ParseTree ctx, 
            ExpressionContext left, ExpressionContext right) {
        
        JiffleType leftType = get(left);
        JiffleType rightType = get(right);
        
        if (leftType == rightType) {
            // single type expression so result is same type
            set(ctx, leftType);
            
        } else {
            // if different, they must be scalar and list so
            // result is list
            set(ctx, JiffleType.LIST);
        }
    }
    
    

    @Override
    public void exitAssignment(AssignmentContext ctx) {
        JiffleType rhsType = get(ctx.expression());
        set(ctx, rhsType);
        
        // Ensure valid type for LHS variable
        String name = ctx.ID().getText();
        SymbolScope scope = getScope(ctx);
        Symbol symbol = scope.get(name);
        
        if (symbol.getType() == Symbol.Type.UNKNOWN) {
            // Symbol was of unknown type.
            // Replace it with a properly typed one.
            //
            Symbol.Type stype = rhsType == JiffleType.D ? 
                    Symbol.Type.SCALAR : Symbol.Type.LIST;
            
            scope.add(new Symbol(name, stype), true);
            
        } else {
            // Symbol was of known type.
            // Ensure it is compatible with RHS expression type.
            //
            switch (symbol.getType()) {
                case SCALAR:
                case DEST_IMAGE:
                    if (rhsType == JiffleType.LIST) {
                        messages.error(ctx.ID().getSymbol(), Errors.ASSIGNMENT_LIST_TO_SCALAR);
                    }
                    break;
                    
                case LIST:
                    if (rhsType == JiffleType.D) {
                        messages.error(ctx.ID().getSymbol(), Errors.ASSIGNMENT_SCALAR_TO_LIST);
                    }
                    break;
            }
        }
    }

    @Override
    public void exitAtom(AtomContext ctx) {
        set(ctx, get(ctx.getChild(0)));
    }
    
    @Override
    public void exitParenExpression(ParenExpressionContext ctx) {
        set(ctx, get(ctx.expression()));
    }

    @Override
    public void exitConCall(ConCallContext ctx) {
        set(ctx, JiffleType.D);
    }

    @Override
    public void exitVarID(VarIDContext ctx) {
        // We should be processing the RHS of an expression to
        // be here
        String name = ctx.ID().getText();
        SymbolScope scope = getScope(ctx);
        
        // TODO - temp debug
        Symbol symbol = scope.get(name);
        
        switch (symbol.getType()) {
            case LIST:
                set(ctx, JiffleType.LIST);
                break;

            case UNKNOWN:
                set(ctx, JiffleType.UNKNOWN);
                break;
                
            default:
                // assume it is a scalar
                set(ctx, JiffleType.D);
        }
    }
    
    @Override
    public void exitImageCall(ImageCallContext ctx) {
        set(ctx, JiffleType.D);
    }
    
    @Override
    public void exitFunctionCall(FunctionCallContext ctx) {
        String name = ctx.ID().getText();
        try {
            set( ctx, FunctionLookup.getReturnType(name) );
            
        } catch (UndefinedFunctionException ex) {
            // just give up
            throw new JiffleParserException(ex);
        }
    }

    @Override
    public void exitLiteral(LiteralContext ctx) {
        // All plain literals are scalar values
        set(ctx, JiffleType.D);
    }

    @Override
    public void exitListLiteral(ListLiteralContext ctx) {
        set(ctx, JiffleType.LIST);
    }

    /*
     * Looks up the inner-most scope for a rule node.
     */
    private SymbolScope getScope(ParseTree ctx) {
        if (ctx != null) {
            SymbolScope s = scopes.get(ctx);
            return s != null ? s : getScope(ctx.getParent());
            
        } else {
            throw new IllegalStateException(
                    "Compiler error: failed to find symbol scope");
        }
    }

}
