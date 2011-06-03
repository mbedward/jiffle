/* 
 *  Copyright (c) 2011, Michael Bedward. All rights reserved. 
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

package jaitools.jiffle.parser;

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit tests for FunctionLookup.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class FunctionLookupTest {

    @Test
    public void proxyFunctions() throws Exception {
        System.out.println("   proxy functions");
        String[] names = { "x", "y", "width", "height", "size" };
        
        for (String name : names) {
            FunctionInfo info = FunctionLookup.getInfo(name, null);
            assertNotNull(info);
            assertTrue(info.isProxy());
        }
    }
    
    @Test
    public void oneAndTwoArgFunctions() throws Exception {
        System.out.println("   alternate one or two arg functions");
        String[] names = { "round", "log" };
        List<String> oneArg = Arrays.asList("D");
        List<String> twoArg = Arrays.asList("D", "D");
        
        for (String name : names) {
            assertNotNull(FunctionLookup.getInfo(name, oneArg));
            assertNotNull(FunctionLookup.getInfo(name, twoArg));
        }
    }
    
    @Test
    public void minAndMax() throws Exception {
        System.out.println("   alternate min and max functions");
        String[] names = { "min", "max" };
        List<String> oneArg = Arrays.asList("List");
        List<String> twoArg = Arrays.asList("D", "D");
        
        for (String name : names) {
            FunctionInfo info = FunctionLookup.getInfo(name, oneArg);
            assertNotNull(info);
            assertTrue(info.getRuntimeExpr().contains("_FN."));
            
            info = FunctionLookup.getInfo(name, twoArg);
            assertNotNull(info);
            assertTrue(info.getRuntimeExpr().contains("Math."));
        }
    }
}
