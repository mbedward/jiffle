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

import org.junit.Test;

/**
 * Tests parsing options, init and images blocks correctly.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class SpecialBlocksTest extends ParserTestBase {
    
    @Test(expected=JiffleParserException.class)
    public void testDuplicateOptionsBlock() throws Exception {
        System.out.println("   duplicate options block throws exception");
        String script = 
                  "options { outside = 0; } \n"
                + "options { outside = 0; } \n"
                + "dest = 42;" ;
                
        getAST(script);
    }
    
    @Test(expected=JiffleParserException.class)
    public void testDuplicateInitBlock() throws Exception {
        System.out.println("   duplicate init block throws exception");
        String script = 
                  "init { foo = 0; } \n"
                + "init { bar = 0; } \n"
                + "dest = 42;" ;
                
        getAST(script);
    }
    
    @Test(expected=JiffleParserException.class)
    public void testDuplicateImagesBlock() throws Exception {
        System.out.println("   duplicate images block throws exception");
        String script = 
                  "images { dest = write; } \n"
                + "images { dest = write; } \n"
                + "dest = 42;" ;
                
        getAST(script);
    }
    
    @Test
    public void validlockPlacement() throws Exception {
        System.out.println("   any order of blocks should be valid");
        
        String[] blocks = {
            "options { outside = 0; }\n",
            "init { foo = 42; }\n",
            "images { src=read; dest=write; }\n"
        };
        
        String body = "dest = src + foo;";
        
        int[][] blockPos = {
            {0, 1, 2},
            {0, 2, 1},
            {1, 0, 2},
            {1, 2, 0},
            {2, 0, 1},
            {2, 1, 0}
        };

        for (int[] pos : blockPos) {
            String script = 
                    blocks[pos[0]] + 
                    blocks[pos[1]] + 
                    blocks[pos[2]] + 
                    body;
            
            getAST(script);
        }
    }
}
