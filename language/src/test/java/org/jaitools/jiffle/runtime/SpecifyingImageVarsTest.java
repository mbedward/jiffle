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

package org.jaitools.jiffle.runtime;

import java.awt.image.RenderedImage;
import java.awt.image.WritableRenderedImage;

import org.jaitools.imageutils.ImageUtils;
import org.jaitools.jiffle.Jiffle;
import org.jaitools.jiffle.JiffleException;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for use of the images block in scripts.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class SpecifyingImageVarsTest extends RuntimeTestBase {
    
    @Test
    public void destVarInImagesBlock() throws Exception {
        System.out.println("   destination image var name in images block");
        String script = 
                  "images { foo = write; }  foo = 42;";
        
        Evaluator e = new Evaluator() {
            public double eval(double val) {
                return 42;
            }
        };
        
        assertScriptResult(script, e, null, "foo");
    }
    
    @Test
    public void sourceAndDestVarsInImagesBlock() throws Exception {
        System.out.println("   source and destination names in images block");

        String script = 
                "images { inimage = read; outimage = write; } outimage = inimage + 1;" ;
        
        Evaluator e = new Evaluator() {
            public double eval(double val) {
                return val + 1;
            }
        };
        
        assertScriptResult(script, e, "inimage", "outimage");
    }
    
    @Test
    public void noDestImage() throws Exception {
        System.out.println("   destination-less script with images block");

        String script = String.format(
                  "images { inimage = read; } \n"
                + "init { n = 0; } \n"
                + "n += inimage >= %d;",
                NUM_PIXELS - 5);
        
        Jiffle jiffle = new Jiffle();
        jiffle.setScript(script);
        jiffle.compile();
        
        runtimeInstance = (JiffleDirectRuntime) jiffle.getRuntimeInstance();
        runtimeInstance.setSourceImage("inimage", createSequenceImage());
        runtimeInstance.evaluateAll(null);
        
        Double var = runtimeInstance.getVar("n");
        assertNotNull(var);
        assertEquals(5, var.intValue());
    }
    
    @Test(expected=JiffleException.class)
    public void emptyImagesBlock() throws Exception {
        System.out.println("   empty images block and no parameters causes exception");
        
        String script = "images { } dest = 42;" ;
        
        Jiffle jiffle = new Jiffle();
        jiffle.setScript(script);
        jiffle.compile();
    }
    
    private void assertScriptResult(String script, 
            Evaluator e, String srcVarName, String destVarName) throws Exception {
        
        RenderedImage srcImg = null;
        WritableRenderedImage destImg = null;
        
        Jiffle jiffle = new Jiffle();
        jiffle.setScript(script);
        jiffle.compile();
        
        runtimeInstance = (JiffleDirectRuntime) jiffle.getRuntimeInstance();
        
        if (srcVarName != null && srcVarName.length() > 0) {
            srcImg = createSequenceImage();
            runtimeInstance.setSourceImage(srcVarName, srcImg);
        }
        
        if (destVarName != null && destVarName.length() > 0) {
            destImg = ImageUtils.createConstantImage(IMG_WIDTH, IMG_WIDTH, 0d);
            runtimeInstance.setDestinationImage(destVarName, destImg);
        }
        
        runtimeInstance.evaluateAll(null);
        assertImage(srcImg, destImg, e);
    }

}
