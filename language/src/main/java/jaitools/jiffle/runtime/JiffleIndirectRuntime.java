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


/**
 * Defines methods implemented by runtime classes adopting the indirect 
 * evaluation model. In this model, there is only a single destination image
 * and the {@link #evaluate(int, int)} method passes values back to the caller 
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
     * Note that Jiffle uses truncation rather than rounding to reduce the
     * transformed coordinates to integers, so this should be taken into account
     * when defining the transform.
     * 
     * @param varName script variable representing the destination image
     * @param tr transform for processing area to image coordinates
     */
    void setDestinationImage(String varName, CoordinateTransform tr);
    
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
     * Note that Jiffle uses truncation rather than rounding to reduce the
     * transformed coordinates to integers, so this should be taken into account
     * when defining the transform.
     * 
     * @param varName script variable representing the source image
     * @param image writable image
     * @param tr transform for processing area to image coordinates
     */
    void setSourceImage(String varName, CoordinateTransform tr);
    
    /**
     * Associates a variable name with a source image. Equivalent to:
     * <pre><code>
     * setSourceImage(varName, null)
     * </code></pre>
     * This convenience method is defined in the interface because it will be
     * commonly when working directly with image coordinates.
     * 
     * @param varName script variable representing the source image
     * @param image writable image
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
