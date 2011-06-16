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
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.media.jai.iterator.RectIter;
import javax.media.jai.iterator.RectIterFactory;

import org.jaitools.CollectionFactory;
import org.jaitools.imageutils.ImageUtils;
import org.jaitools.jiffle.Jiffle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;


/**
 * Tests running a basic task with the executor. Can be run multiple times with
 * {@code JiffleExecutorTestRunner} to check for concurrency problems.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 *
 */
@RunWith(ExecutorTestRunner.class)
public class ExecutorSimpleTaskTest {
    private static final int WIDTH = 100;
    private static final double TOL = 1.0e-8;
    
    private JiffleExecutor executor;
    private final JiffleProgressListener nullListener = new NullProgressListener();
    

    @Before
    public void setup() {
        executor = new JiffleExecutor();
    }
    
    @After
    public void cleanup() {
        executor.shutdownAndWait(1, TimeUnit.SECONDS);
    }
    
    @Test
    public void simpleTask() throws Exception {
        Map<String, Jiffle.ImageRole> imageParams;
        imageParams = CollectionFactory.map();
        imageParams.put("dest", Jiffle.ImageRole.DEST);
        
        Jiffle jiffle = new Jiffle("dest = x() + y();", imageParams);
        JiffleDirectRuntime runtime = jiffle.getRuntimeInstance();
        
        WritableRenderedImage destImage = ImageUtils.createConstantImage(WIDTH, WIDTH, 0d);
        runtime.setDestinationImage("dest", destImage);
        
        WaitingListener listener = new WaitingListener();
        executor.addEventListener(listener);
        
        listener.setNumTasks(1);

        int jobID = executor.submit(runtime, nullListener);
        
        if (!listener.await(2, TimeUnit.SECONDS)) {
            fail("Listener time-out period elapsed");
        }
        
        JiffleExecutorResult result = listener.getResults().get(0);
        assertNotNull(result);
        
        RenderedImage dest = result.getImages().get("dest");
        assertNotNull(dest);
        
        RectIter iter = RectIterFactory.create(dest, null);
        for (int y = 0; y < WIDTH; y++) {
            for (int x = 0; x < WIDTH; x++) {
                assertEquals((double)x + y, iter.getSampleDouble(), TOL);
                iter.nextPixel();
            }
            iter.startPixels();
            iter.nextLine();
        }
    }
    
}
