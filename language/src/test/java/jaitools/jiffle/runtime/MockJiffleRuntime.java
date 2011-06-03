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

import java.awt.Rectangle;

/**
 * Mock object for unit tests.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class MockJiffleRuntime extends AbstractDirectRuntime {

    private final long pixelTime;

    /**
     * Creates a new mock object. Its thread will sleep for {@code pixelTime}
     * milliseconds each time its {@link #evaluate(int, int)} method is 
     * called.
     * 
     * @param imageSize image size (number of pixels)
     * 
     * @param pixelTime time to spend pretending to process each pixel
     */
    public MockJiffleRuntime(int imageSize, long pixelTime) {
        this.pixelTime = pixelTime;
        setWorldByResolution(new Rectangle(0, 0, imageSize, 1), 1, 1);
    }

    /**
     * Returns an empty array
     */
    @Override
    public String[] getDestinationVarNames() {
        return new String[0];
    }

    /**
     * Returns an empty array
     */
    @Override
    public String[] getSourceVarNames() {
        return new String[0];
    }

    /**
     * Does nothing.
     */
    @Override
    protected void initImageScopeVars() {
    }

    /**
     * Does nothing.
     */
    @Override
    protected void initOptionVars() {
    }

    /**
     * Pretends to process a pixel (very slowly).
     */
    public void evaluate(double x, double y) {
        try {
            Thread.sleep(pixelTime);

        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected Double getDefaultValue(int index) {
        return null;
    }
}
