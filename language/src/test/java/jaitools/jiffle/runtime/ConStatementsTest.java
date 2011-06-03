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

package jaitools.jiffle.runtime;

import java.awt.image.RenderedImage;

import jaitools.imageutils.ImageUtils;
import jaitools.jiffle.JiffleBuilder;

import org.junit.Test;

/**
 * Unit tests for {@code con} functions.
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class ConStatementsTest extends RuntimeTestBase {

    @Test
    public void con1Arg() throws Exception {
        String script = "dest = con(src > 10);";
        System.out.println("   " + script);
        
        testScript(script, new Evaluator() {

            public double eval(double val) {
                return val > 10 ? 1 : 0;
            }
        });
    }

    @Test
    public void con2Arg() throws Exception {
        String script = "dest = con(src > 10, 10);";
        System.out.println("   " + script);
        
        testScript(script, new Evaluator() {

            public double eval(double val) {
                return val > 10 ? 10 : 0;
            }
        });
    }

    @Test
    public void con3Arg() throws Exception {
        String script = "dest = con(src > 10, src, 10);";
        System.out.println("   " + script);
        
        testScript(script, new Evaluator() {

            public double eval(double val) {
                return val > 10 ? val : 10;
            }
        });
    }
    
    @Test
    public void con4Arg() throws Exception {
        String script = "dest = con(src - 10, src, 10, 0);";
        System.out.println("   " + script);
        
        testScript(script, new Evaluator() {

            public double eval(double val) {
                double comp = val - 10;
                if (comp > 0) {
                    return val;
                } else if (comp == 0) {
                    return 10;
                } else {
                    return 0;
                }
            }
        });
    }
    
    @Test
    public void nestedCon() throws Exception {
        String script = "dest = con(src1, con(src1 > src2, 1, null), null);" ;
        
        System.out.println("   " + script);
        
        final double threshold = IMG_WIDTH * IMG_WIDTH / 2;
        RenderedImage src1 = createSequenceImage();
        RenderedImage src2 = ImageUtils.createConstantImage(IMG_WIDTH, IMG_WIDTH, threshold);
        
        Evaluator e = new Evaluator() {
            public double eval(double val) {
                return val > threshold ? 1 : Double.NaN;
            }
        };
                
        JiffleBuilder builder = new JiffleBuilder();
        builder.script(script).source("src1", src1).source("src2", src2);
        builder.dest("dest", IMG_WIDTH, IMG_WIDTH);
        RenderedImage dest = builder.run().getImage("dest");
        
        assertImage(src1, dest, e);
    }
    
}
