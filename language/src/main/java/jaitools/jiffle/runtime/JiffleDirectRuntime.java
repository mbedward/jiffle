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
import java.util.Map;

/**
 * Defines methods implemented by runtime classes adopting the direct evaluation
 * model. In this model, the runtime object writes values to the destination
 * image(s) directly within its {@link #evaluate(int, int)} method. It also
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
     * Note that Jiffle uses truncation rather than rounding to reduce the
     * transformed coordinates to integers, so this should be taken into account
     * when defining the transform.
     * 
     * @param varName script variable representing the destination image
     * @param image writable image
     * @param tr transform for processing area to image coordinates
     */
    void setDestinationImage(String varName, WritableRenderedImage image, CoordinateTransform tr);
    
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
     * Note that Jiffle uses truncation rather than rounding to reduce the
     * transformed coordinates to integers, so this should be taken into account
     * when defining the transform.
     * 
     * @param varName script variable representing the source image
     * @param image writable image
     * @param tr transform for processing area to image coordinates
     */
    void setSourceImage(String varName, RenderedImage image, CoordinateTransform tr);

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
