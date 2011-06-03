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

import jaitools.jiffle.JiffleException;


/**
 * Defines methods implemented by runtime classes adopting the indirect 
 * evaluation model. In this model, there is only a single destination image
 * and the {@link #evaluate(double, double)} method passes values back to the caller 
 * rather than writing them to the destination image directly.
 *
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public interface JiffleIndirectRuntime extends JiffleRuntime {
    /**
     * Specifies the name of the script variable which represents the destination
     * image and defines the coordinate transform.
     * The transform defines how to convert from processing area coordinates
     * to image (pixel) coordinates. If {@code tr} is {@code null} the default
     * identify transform will be used.
     * <p> 
     * Note that Jiffle uses rounding to reduce the transformed coordinates to 
     * integers.
     * 
     * @param varName script variable representing the destination image
     * @param tr transform for processing area to image coordinates
     * 
     * @throws JiffleException if the world bounds and resolution have not
     *         been set previously
     */
    void setDestinationImage(String varName, CoordinateTransform tr)
            throws JiffleException;
    
    /**
     * Specifies the name of the script variable which represents the destination
     * image. Equivalent to:
     * <pre><code>
     * setDestinationImage(varName, null)
     * </code></pre>
     * This convenience method is defined in the interface because it will be
     * commonly when working directly with image coordinates.
     * 
     * @param varName script variable representing the destination image
     */
    void setDestinationImage(String varName);
    
    /**
     * Associates a variable name with a source image and coordinate transform.
     * The transform defines how to convert from processing area coordinates
     * to image (pixel) coordinates. If {@code tr} is {@code null} the default
     * identify transform will be used.
     * <p> 
     * Note that Jiffle uses rounding to reduce the transformed coordinates to 
     * integers.
     * 
     * @param varName script variable representing the source image
     * @param tr transform for processing area to image coordinates
     * 
     * @throws JiffleException if the world bounds and resolution have not
     *         been set previously
     */
    void setSourceImage(String varName, CoordinateTransform tr)
            throws JiffleException;
    
    /**
     * Associates a variable name with a source image. Equivalent to:
     * <pre><code>
     * setSourceImage(varName, null)
     * </code></pre>
     * This convenience method is defined in the interface because it will be
     * commonly when working directly with image coordinates.
     * 
     * @param varName script variable representing the source image
     */
    void setSourceImage(String varName);

    /**
     * Evaluates the script for the given world position.
     * 
     * @param x world position X ordinate
     * @param y world position Y ordinate
     * 
     * @return the result
     */
    double evaluate(double x, double y);

}
