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

import org.jaitools.jiffle.JiffleException;


/**
 * Defines methods implemented by runtime classes adopting the direct evaluation
 * model. In this model, the runtime object writes values to the destination
 * image(s) directly within its {@link #evaluate(double, double)} method. It also
 * provides an {@link #evaluateAll(JiffleProgressListener)} method.
 *
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public interface JiffleDirectRuntime extends JiffleRuntime {
    
    /**
     * Associates a variable name with a destination image and coordinate transform.
     * The transform defines how to convert from processing area coordinates
     * to image (pixel) coordinates. If {@code tr} is {@code null} the default
     * identify transform will be used.
     * <p> 
     * Note that Jiffle uses rounding to reduce the transformed coordinates to 
     * integers.
     * 
     * @param varName script variable representing the destination image
     * @param image writable image
     * @param tr transform for processing area to image coordinates
     * 
     * @throws JiffleException if the world bounds and resolution have not
     *         been set previously
     */
    void setDestinationImage(String varName, WritableRenderedImage image, CoordinateTransform tr)
            throws JiffleException;
    
    /**
     * Associates a variable name with a destination image. Equivalent to:
     * <pre><code>
     * setDestinationImage(varName, image, null)
     * </code></pre>
     * This convenience method is defined in the interface because it will be
     * commonly when working directly with image coordinates.
     * 
     * @param varName script variable representing the destination image
     * @param image writable image
     */
    void setDestinationImage(String varName, WritableRenderedImage image);

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
     * @param image writable image
     * @param tr transform for processing area to image coordinates
     * 
     * @throws JiffleException if the world bounds and resolution have not
     *         been set previously
     */
    void setSourceImage(String varName, RenderedImage image, CoordinateTransform tr)
            throws JiffleException;

    /**
     * Associates a variable name with a source image. Equivalent to:
     * <pre><code>
     * setSourceImage(varName, image, null)
     * </code></pre>
     * This convenience method is defined in the interface because it will be
     * commonly when working directly with image coordinates.
     * 
     * @param varName script variable representing the source image
     * @param image writable image
     */
    void setSourceImage(String varName, RenderedImage image);
    
    /**
     * Sets default bounds for the processing area. These are the bounds of 
     * the first destination image or, if there are no destination images,
     * the bounds of the first source image, where first means first added
     * to the run-time object's list of images.
     */
    void setDefaultBounds();
    
    /**
     * Evaluates the script for the given world position.
     * 
     * @param x world position X ordinate
     * @param y world position Y ordinate
     */
    void evaluate(double x, double y);

    /**
     * Evaluates the script for all pixel locations within the world bounds.
     * 
     * @param pl an optional progress listener (may be {@code null}
     */
    void evaluateAll(JiffleProgressListener pl);
    
    /**
     * Gets a value from a source image for a given world position and
     * image band.
     * 
     * @param srcImageName the source image
     * @param x source X ordinate in world units
     * @param y source Y ordinate in world units
     * @param band source band
     * 
     * @return image value
     */
    double readFromImage(String srcImageName, double x, double y, int band);
    
    /**
     * Writes a value to a destination image for a given world position and
     * image band.
     * 
     * @param destImageName
     * @param x destination X ordinate in world units
     * @param y destination Y ordinate in world units
     * @param band destination band
     * 
     * @param value the value to write
     */
    void writeToImage(String destImageName, double x, double y, int band, double value);

    /**
     * Gets the images used by this object and returns them as a {@code Map}
     * with variable names as keys and images as values.
     * 
     * @return images keyed by variable name
     */
    Map<String, RenderedImage> getImages();
}
