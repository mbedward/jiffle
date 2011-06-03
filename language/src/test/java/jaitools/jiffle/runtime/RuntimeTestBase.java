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
import java.util.Map;

import javax.media.jai.TiledImage;
import javax.media.jai.iterator.RectIter;
import javax.media.jai.iterator.RectIterFactory;

import jaitools.CollectionFactory;
import jaitools.imageutils.ImageUtils;
import jaitools.jiffle.Jiffle;

import static org.junit.Assert.*;

/**
 * Base class for unit tests of runtime methods.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public abstract class RuntimeTestBase {

    protected static final int IMG_WIDTH = 10;
    protected static final int NUM_PIXELS = IMG_WIDTH * IMG_WIDTH;
    protected static final double TOL = 1.0e-8;
    
    private final JiffleProgressListener nullListener = new NullProgressListener();
    
    protected Map<String, Jiffle.ImageRole> imageParams;
    protected JiffleDirectRuntime runtimeInstance;

    public abstract class Evaluator {
        int x = 0;
        int y = 0;
        
        public void move() {
            if (++x >= IMG_WIDTH) {
                x = 0;
                y++ ;
            }
        }
        
        public abstract double eval(double val);
    }
    
    protected RenderedImage createSequenceImage() {
        TiledImage img = ImageUtils.createConstantImage(IMG_WIDTH, IMG_WIDTH, 0.0);
        int k = 0;
        for (int y = 0; y < IMG_WIDTH; y++) {
            for (int x = 0; x < IMG_WIDTH; x++) {
                img.setSample(x, y, 0, k++);
            }
        }
        return img;
    }
    
    protected RenderedImage createRowValueImage() {
        TiledImage img = ImageUtils.createConstantImage(IMG_WIDTH, IMG_WIDTH, 0.0);
        for (int y = 0; y < IMG_WIDTH; y++) {
            for (int x = 0; x < IMG_WIDTH; x++) {
                img.setSample(x, y, 0, y);
            }
        }
        return img;
    }

    protected void testScript(String script, Evaluator evaluator) throws Exception {
        RenderedImage srcImg = createSequenceImage();
        testScript(script, srcImg, evaluator);
    }

    protected void testScript(String script, RenderedImage srcImg, Evaluator evaluator) throws Exception {
        imageParams = CollectionFactory.map();
        imageParams.put("dest", Jiffle.ImageRole.DEST);
        imageParams.put("src", Jiffle.ImageRole.SOURCE);

        Jiffle jiffle = new Jiffle(script, imageParams);
        runtimeInstance = (JiffleDirectRuntime) jiffle.getRuntimeInstance();

        testRuntime(srcImg, runtimeInstance, evaluator);
    }

    protected void testRuntime(RenderedImage srcImg, JiffleDirectRuntime runtime, Evaluator evaluator) {
        runtime.setSourceImage("src", srcImg);

        TiledImage destImg = ImageUtils.createConstantImage(
                srcImg.getMinX(), srcImg.getMinY(), srcImg.getWidth(), srcImg.getHeight(), 0.0);
        runtime.setDestinationImage("dest", destImg);

        runtime.evaluateAll(nullListener);
        assertImage(srcImg, destImg, evaluator);
    }

    protected void assertImage(RenderedImage srcImg, RenderedImage destImg, Evaluator evaluator) {
        RectIter destIter = RectIterFactory.create(destImg, null);
        
        if (srcImg != null) {
            RectIter srcIter = RectIterFactory.create(srcImg, null);
            
            do {
                do {
                    assertEquals(evaluator.eval(srcIter.getSampleDouble()), destIter.getSampleDouble(), TOL);
                    destIter.nextPixelDone();
                } while (!srcIter.nextPixelDone());
                
                srcIter.startPixels();
                destIter.startPixels();
                destIter.nextLineDone();
                
            } while (!srcIter.nextLineDone());
            
        } else {
            do {
                do {
                    assertEquals(evaluator.eval(0), destIter.getSampleDouble(), TOL);
                } while (!destIter.nextPixelDone());
                
                destIter.startPixels();
                
            } while (!destIter.nextLineDone());
        }
    }

}

