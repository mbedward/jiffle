/* 
 *  Copyright (c) 2009-2011, Michael Bedward. All rights reserved. 
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

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTreeNodeStream;

import org.junit.Test;

/**
 * Tests for parsing list expressions.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class ListParsingTest extends ParserTestBase {

    @Test
    public void createEmptyList() throws Exception {
        System.out.println("   create empty list");
        
        String script = "foo = [];" ;
        CommonTreeNodeStream ast = getAST(script);
        
        int[] expected = {
            JiffleParser.EQ,
            Token.DOWN,
            JiffleParser.ID,
            JiffleParser.DECLARED_LIST,
            Token.DOWN,
            JiffleParser.EXPR_LIST,
            Token.UP,
            Token.UP
        };
        
        assertAST(ast, expected);
    }
    
    @Test
    public void createListWithElements() throws Exception {
        String script = " foo = [1, 2.0, bar, null];";
        
        CommonTreeNodeStream ast = getAST(script);

        int[] expected = {
            JiffleParser.EQ,
            Token.DOWN,
            JiffleParser.ID,
            JiffleParser.DECLARED_LIST,
            Token.DOWN,
            JiffleParser.EXPR_LIST,
            Token.DOWN,
            JiffleParser.INT_LITERAL,
            JiffleParser.FLOAT_LITERAL,
            JiffleParser.ID,
            JiffleParser.NULL,
            Token.UP,
            Token.UP,
            Token.UP
        };
        
        assertAST(ast, expected);
    }
    
    @Test
    public void appendWithOperator() throws Exception {
        String script = "foo = []; foo << 1; foo << bar;" ;

        CommonTreeNodeStream ast = getAST(script);
        
        int[] expected = {
            JiffleParser.EQ,
            Token.DOWN,
            JiffleParser.ID,
            JiffleParser.DECLARED_LIST,
            Token.DOWN,
            JiffleParser.EXPR_LIST,
            Token.UP,
            Token.UP,
            
            JiffleParser.APPEND,
            Token.DOWN,
            JiffleParser.ID,
            JiffleParser.INT_LITERAL,
            Token.UP,
            
            JiffleParser.APPEND,
            Token.DOWN,
            JiffleParser.ID,
            JiffleParser.ID,
            Token.UP
        };
        
        assertAST(ast, expected);
    }

}
