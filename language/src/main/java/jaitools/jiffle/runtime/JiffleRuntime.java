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

import jaitools.jiffle.Jiffle.ImageRole;
import jaitools.jiffle.JiffleException;
import java.awt.geom.Rectangle2D;
import java.util.Map;

/**
 * The root interface for Jiffle runtime classes.
 *
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public interface JiffleRuntime {

    /**
     * Sets the world (processing area) bounds and resolution (pixel dimensions).
     * 
     * @param bounds outer bounds of the processing area
     * @param xres pixel width in world units
     * @param yres pixel height in world units
     * 
     * @throws IllegalArgumentException if {@code bounds} is {@code null} or empty
     */
    void setWorldByResolution(Rectangle2D bounds, double xres, double yres);
    
    /**
     * Sets the world (processing area) bounds and the number of pixels in the
     * X and Y directions.
     * 
     * @param bounds outer bounds of the processing area
     * @param numX number of pixels in the X direction
     * @param numY number of pixels in the Y direction
     * 
     * @throws IllegalArgumentException if {@code bounds} is {@code null} or empty
     */
    void setWorldByNumPixels(Rectangle2D bounds, int numX, int numY);
    
    /**
     * Sets a coordinate transform to be used by any source and destination images
     * submitted to the runtime object without their own transforms. This 
     * includes any images submitted prior to calling this method. If {@code tr}
     * is {@code null} the system default transform ({@link IdentityCoordinateTransform})
     * will be used.
     * 
     * @param tr the coordinate transform to use as the default; or {@code null}
     *        for the system default
     * 
     * @throws JiffleException if the world bounds and resolution have not
     *         been set previously
     */
    void setDefaultTransform(CoordinateTransform tr) throws JiffleException;
    
    /**
     * Gets the min X ordinate of the processing area.
     * 
     * @return min X ordinate in world units
     */
    double getMinX();

    /**
     * Gets the max X ordinate of the processing area.
     * 
     * @return max X ordinate in world units
     */
    double getMaxX();

    /**
     * Gets the min Y ordinate of the processing area.
     * 
     * @return min Y ordinate in world units
     */
    double getMinY();

    /**
     * Gets the max Y ordinate of the processing area.
     * 
     * @return max Y ordinate in world units
     */
    double getMaxY();
    
    /**
     * Gets the width of the processing area.
     * 
     * @return the width in world units
     */
    double getWidth();
    
    /**
     * Gets the height of the processing area.
     * 
     * @return the height in world units
     */
    double getHeight();
    
    /**
     * Gets the pixel width (resolution in X direction) in world units.
     * 
     * @return pixel width
     */
    double getXRes();
    
    /**
     * Gets the pixel height (resolution in Y direction) in world units.
     * 
     * @return pixel height
     */
    double getYRes();
    
    /**
     * Gets the total number of pixels in the processing area.
     * 
     * @return number of pixels
     * @throws IllegalStateException if the processing area has not been set
     */
    long getNumPixels();
    
    /**
     * Checks whether the world bounds and pixel dimensions have been set.
     * 
     * @return {@code true} if set; {@code false} otherwise
     */
    boolean isWorldSet();
    
    /**
     * Returns the names of image scope variables that can be used with
     * the {@link #getVar(String)} and {@link #setVar(String, Double)}
     * methods.
     * 
     * @return array of variable names; may be empty but not {@code null}
     */
    String[] getVarNames();
    
    /**
     * Returns the value of a variable that was declared in the
     * script's <i>init</i> block.
     *
     * @param varName variable name
     *
     * @return the values or {@code null} if the variable name is
     *         not found
     */
    Double getVar(String varName);

    /**
     * Sets the value of a variable that was declared in the script's
     * <i>init</i> block, overriding the default value in the script
     * if present. Setting {@code value} to {@code null} results in the
     * default script value being used.
     * 
     * @param varName variable name
     * @param value the new value
     * 
     * @throws JiffleRuntimeException if the variable name is not found
     */
    void setVar(String varName, Double value) throws JiffleRuntimeException;

    /**
     * Supplies the runtime object with the names and roles if image variables
     * used in the script. Although this is a public method it is not intended
     * for general use. It is called by the {@link jaitools.jiffle.Jiffle} 
     * instance that is creating the runtime object so that clients can use 
     * the {@link #getSourceVarNames()} and {@link #getDestinationVarNames()}
     * methods.
     * 
     * @param imageParams a {@code Map} of variable names (key) and roles (value)
     */
    void setImageParams(Map<String, ImageRole> imageParams);
    
    /**
     * Gets the variable names associated with source images.
     * 
     * @return an array of names; may be empty but not {@code null}
     */
    String[] getSourceVarNames();
    
    /**
     * Gets the variable names associated with destination images.
     * 
     * @return an array of names; may be empty but not {@code null}
     */
    String[] getDestinationVarNames();
    
}
