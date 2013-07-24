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

import java.util.Arrays;
import java.util.List;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.jaitools.jiffle.parser.JiffleParser.*;

/**
 * Generates Java sources for the runtime class.
 *
 * @author michael
 */
public class RuntimeSourceWorker extends BaseWorker {
    
    public ParseTreeProperty<String> source = new ParseTreeProperty<String>();
    
    /*
     * Sugar to set a node property
     */
    private void set(ParseTree ctx, String node) {
        source.put(ctx, node);
    }
    
    /*
     * Sugar to get a node property
     */
    private String get(ParseTree ctx) {
        return source.get(ctx);
    }
    
    /*
     * Concatenates strings with space delimiters.
     */
    private String cat(String ...strs) {
        return cat(' ', strs);
    }

    /*
     * Concatenates strings.
     */
    private String cat(char sep, String ...strs) {
        int n = strs.length;
        if (n == 0) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        for (String s : strs) {
            sb.append(s);
            if (n-- > 1) {
                sb.append(sep);
            }
        }
        return sb.toString();
    }
    
    /*
     * Replacement for String.split that doesn't return
     * empty tokens.
     */
    private String[] split(String s, String regex) {
        return s.replaceFirst("^" + regex, "").split(regex);
    }
    
    /*
     * Formats a semi-colon terminated source line;
     */
    private String semi(String s) {
        return s + ";\n";
    }
    
    /*
     * Looks up a runtime function name
     */
    private String rt(String fnName, JiffleType ...argTypes) {
        try {
            return FunctionLookup.getRuntimeExpr(fnName, argTypes);
        } catch (UndefinedFunctionException ex) {
            // Must be a problem with the compiler itself - give up
            throw new IllegalArgumentException(ex);
        }
    }
    
    /*
     * Formats a function call
     */
    private String fn(String fnName, String ...args) {
        StringBuilder sb = new StringBuilder();
        sb.append(fnName);
        sb.append('(');
        sb.append(cat(',', args));
        sb.append(')');
        return sb.toString();
    }

    public RuntimeSourceWorker(ParseTree tree) {
        walkTree(tree);
    }

    @Override
    public void exitScript(ScriptContext ctx) {
        StringBuilder sb = new StringBuilder();
        for (StatementContext stmt : ctx.statement()) {
            sb.append(get(stmt));
        }
        
        set(ctx, sb.toString());
    }

    @Override
    public void exitExprStmt(ExprStmtContext ctx) {
        set(ctx, semi(get(ctx.expression())));
    }

    @Override
    public void exitExpressionList(ExpressionListContext ctx) {
        List<ExpressionContext> ecs = ctx.expression();
        String[] ss = new String[ecs.size()];
        int i = 0;
        for (ExpressionContext ec : ecs) {
            ss[i++] = get(ec);
        }
        set(ctx, cat(',', ss));
    }

    @Override
    public void exitAtomExpr(AtomExprContext ctx) {
        set( ctx, get(ctx.atom()) );
    }

    @Override
    public void exitPowExpr(PowExprContext ctx) {
        String left = get(ctx.expression(0));
        String right = get(ctx.expression(1));
        set( ctx, String.format("math.pow(%s, %s)", left, right) );
    }

    @Override
    public void exitPostExpr(PostExprContext ctx) {
        String ex = get(ctx.expression());
        String op = ctx.getChild(1).getText();
        set(ctx, ex + op);
    }

    @Override
    public void exitPreExpr(PreExprContext ctx) {
        String op = ctx.getChild(0).getText();
        String ex = get(ctx.expression());
        set(ctx, op + ex);
    }

    @Override
    public void exitNotExpr(NotExprContext ctx) {
        String fname = rt("NOT", JiffleType.D);
        String arg = get( ctx.expression() );
        set(ctx, fn(fname, arg));
    }

    @Override
    public void exitTimesDivModExpr(TimesDivModExprContext ctx) {
        String left = get(ctx.expression(0));
        String right = get(ctx.expression(1));
        String op = ctx.getChild(1).getText();
        set(ctx, cat(left, op, right));
    }
    
    @Override
    public void exitPlusMinusExpr(PlusMinusExprContext ctx) {
        String left = get(ctx.expression(0));
        String right = get(ctx.expression(1));
        String op = ctx.getChild(1).getText();
        set(ctx, cat(left, op, right));
    }

    @Override
    public void exitCompareExpr(CompareExprContext ctx) {
        String left = get(ctx.expression(0));
        String right = get(ctx.expression(1));
        String op;
        
        switch (ctx.op.getType()) {
            case JiffleParser.LT: op = "LT"; break;
            case JiffleParser.LE: op = "LE"; break;
            case JiffleParser.GE: op = "GE"; break;
            case JiffleParser.GT: op = "GT"; break;
            default: throw new IllegalStateException("Unknown op: " + ctx.op.getText());
        }
        
        String fname = rt(op, JiffleType.D, JiffleType.D);
        set(ctx, fn(fname, left, right));
    }
    
    @Override
    public void exitEqExpr(EqExprContext ctx) {
        String left = get(ctx.expression(0));
        String right = get(ctx.expression(1));
        String op;
        
        switch (ctx.op.getType()) {
            case JiffleParser.EQ: op = "EQ"; break;
            case JiffleParser.NE: op = "NE"; break;
            default: throw new IllegalStateException("Unknown op: " + ctx.op.getText());
        }
        
        String fname = rt(op, JiffleType.D, JiffleType.D);
        set(ctx, fn(fname, left, right));
    }

    @Override
    public void exitAndExpr(AndExprContext ctx) {
        String left = get(ctx.expression(0));
        String right = get(ctx.expression(1));
        String fname = rt("AND", JiffleType.D, JiffleType.D);
        set(ctx, fn(fname, left, right));
    }
    
    @Override
    public void exitOrExpr(OrExprContext ctx) {
        String left = get(ctx.expression(0));
        String right = get(ctx.expression(1));
        String fname = rt("OR", JiffleType.D, JiffleType.D);
        set(ctx, fn(fname, left, right));
    }

    @Override
    public void exitXorExpr(XorExprContext ctx) {
        String left = get(ctx.expression(0));
        String right = get(ctx.expression(1));
        String fname = rt("XOR", JiffleType.D, JiffleType.D);
        set(ctx, fn(fname, left, right));
    }

    @Override
    public void exitTernaryExpr(TernaryExprContext ctx) {
        String x = get(ctx.expression(0));
        String a = get(ctx.expression(1));
        String b = get(ctx.expression(2));
        set(ctx, Sources.conCall(x, a, b));
    }

    @Override
    public void exitAssignExpr(AssignExprContext ctx) {
        set(ctx, get(ctx.assignment()));
    }

    @Override
    public void exitAssignment(AssignmentContext ctx) {
        String left = ctx.ID().getText();
        String right = get(ctx.expression());
        String op = ctx.getChild(1).getText();
        set(ctx, cat(left, op, right));
    }
    
    @Override
    public void exitAtom(AtomContext ctx) {
        set( ctx, get(ctx.getChild(0)) );
    }

    @Override
    public void exitConCall(ConCallContext ctx) {
        // retrieve arg list string and split into tokens
        String argList = get( ctx.argumentList() );
        String[] args = split(argList, "[(), ]+");
        
        set( ctx, Sources.conCall(args) );
    }

    @Override
    public void exitParenExpression(ParenExpressionContext ctx) {
        set( ctx, cat("(", get(ctx.expression()), ")") );
    }

    @Override
    public void exitArgumentList(ArgumentListContext ctx) {
        set(ctx, "(" + get(ctx.expressionList()) + ")");
    }

    @Override
    public void exitImageCall(ImageCallContext ctx) {
        String name = ctx.ID().getText();
        String bandPixel = get(ctx.imagePos());
        set(ctx, String.format("readImageValue(%s, %s)", name, bandPixel));
    }

    @Override
    public void exitImagePos(ImagePosContext ctx) {
        BandSpecifierContext bandCtx = ctx.bandSpecifier();
        String band = bandCtx == null ? "0" : get(bandCtx);
        
        PixelSpecifierContext pixelCtx = ctx.pixelSpecifier();
        String pixel = pixelCtx == null ? "_x, _y" : get(pixelCtx);
        
        set(ctx, pixel + ", " + band);
    }

    @Override
    public void exitBandSpecifier(BandSpecifierContext ctx) {
        set( ctx, get(ctx.expression()) );
    }

    @Override
    public void exitPixelSpecifier(PixelSpecifierContext ctx) {
        String x = get(ctx.pixelPos(0));
        String y = get(ctx.pixelPos(1));
        
        if (x.startsWith("+")) {
            x = "_x" + x;
        }
        if (y.startsWith("+")) {
            y = "_y" + y;
        }
        
        set(ctx, x + ", " + y);
    }

    @Override
    public void exitAbsolutePixel(AbsolutePixelContext ctx) {
        set( ctx, get(ctx.expression()) );
    }
    
    @Override
    public void exitRelativePixel(RelativePixelContext ctx) {
        set( ctx, "+" + get(ctx.expression()) );
    }

    @Override
    public void exitVarID(VarIDContext ctx) {
        String name = ctx.ID().getText();
        set(ctx, name);
    }

    @Override
    public void exitLiteral(LiteralContext ctx) {
        Token tok = ctx.getStart();
        switch (tok.getType()) {
            case JiffleParser.INT_LITERAL:
            case JiffleParser.FLOAT_LITERAL:
                set(ctx, tok.getText());
                break;
                
            case JiffleParser.TRUE:
                set(ctx, Sources.trueValue());
                break;
                
            case JiffleParser.FALSE:
                set(ctx, Sources.falseValue());
                break;
                
            case JiffleParser.NULL:
                set(ctx, Sources.nanValue());
                break;
                
            default:
                throw new JiffleParserException("Unrecognized literal type: " + tok.getText());
        }
    }
    
    
}
