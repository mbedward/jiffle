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

package org.jaitools.jiffle.runtime;

import java.awt.image.WritableRenderedImage;
import java.util.Map;

import org.jaitools.CollectionFactory;
import org.jaitools.imageutils.ImageUtils;
import org.jaitools.jiffle.Jiffle;

import org.junit.Test;

/**
 * Tests for setting the value of image-scope variables at run-time.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class InjectTest extends RuntimeTestBase {
    
    @Test
    public void varWithDefault() throws Exception {
        System.out.println("   inject value for var with default");
        String script = 
                  "init { n = 0; } \n"
                + "dest = n;" ;

        testScriptWithValue(script, 42.0);
    }
    
    @Test
    public void varWithNoDefault() throws Exception {
        System.out.println("   inject value for var with no default");
        String script = 
                  "init { n; } \n"
                + "dest = n;" ;

        testScriptWithValue(script, 42.0);
    }

    @Test(expected=JiffleRuntimeException.class)
    public void neglectVarWithNoDefault() throws Exception {
        System.out.println("   unset var with no default gives exception");
        String script = 
                  "init { n; } \n"
                + "dest = n;" ;

        testScriptWithValue(script, null);
    }
    
    @Test
    public void injectThenDefault() throws Exception {
        System.out.println("   run with injected value then default value");
        String script = 
                  "init { n = 42; } \n"
                + "dest = n;" ;
        
        JiffleDirectRuntime runtime = getRuntime(script);
        WritableRenderedImage destImg = ImageUtils.createConstantImage(IMG_WIDTH, IMG_WIDTH, 0d);
        runtime.setDestinationImage("dest", destImg);

        runtime.setVar("n", -1.0);
        runtime.evaluateAll(null);
        assertImage(null, destImg, new Evaluator() {
            public double eval(double val) {
                return -1.0;
            }
        });
        
        // set var back to default value (42)
        runtime.setVar("n", null);
        runtime.evaluateAll(null);
        assertImage(null, destImg, new Evaluator() {
            public double eval(double val) {
                return 42.0;
            }
        });
        
    }
    
    @Test
    public void repeatedSetting() throws Exception {
        System.out.println("   repeated setting of var");
        String script = 
                  "init { n; } \n"
                + "dest = n;" ;
        
        JiffleDirectRuntime runtime = getRuntime(script);
        for (int i = -5; i <= 5; i++) {
            testInject(runtime, Double.valueOf(i));
        }
    }
    
    
    private void testScriptWithValue(String script, final Double value) throws Exception {
        JiffleDirectRuntime runtime = getRuntime(script);
        testInject(runtime, value);
    }
            

    private JiffleDirectRuntime getRuntime(String script) throws Exception {
        Jiffle jiffle = new Jiffle();
        jiffle.setScript(script);
        
        Map<String, Jiffle.ImageRole> params = CollectionFactory.map();
        params.put("dest", Jiffle.ImageRole.DEST);
        jiffle.setImageParams(params);
        jiffle.compile();
        
        return jiffle.getRuntimeInstance();
    }
    
    private void testInject(JiffleDirectRuntime runtime, final Double value) throws Exception {
        WritableRenderedImage destImg = ImageUtils.createConstantImage(IMG_WIDTH, IMG_WIDTH, 0d);
        runtime.setDestinationImage("dest", destImg);
        if (value != null) {
            runtime.setVar("n", value);
        }
        runtime.evaluateAll(null);
        
        Evaluator e = new Evaluator() {
            public double eval(double val) {
                return value;
            }
        };
        
        assertImage(null, destImg, e);
    }
    
    
}