/*
 * Copyright 2011 Michael Bedward
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

package jaitools.jiffle.runtime;

import java.awt.image.RenderedImage;
import java.awt.image.WritableRenderedImage;

import jaitools.imageutils.ImageUtils;
import jaitools.jiffle.Jiffle;
import jaitools.jiffle.JiffleException;

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
