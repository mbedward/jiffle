/*
 * Copyright 2009-2011 Michael Bedward
 * 
 * This file is part of jai-tools.
 *
 * jai-tools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * jai-tools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with jai-tools.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package jaitools.jiffle.parser;

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
