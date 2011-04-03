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
     * Sets the world (processing area) bounds and step distances.
     * 
     * @param bounds outer bounds of the processing area
     * @param xstep distance between pixels in world units in the X direction
     * @param ystep distance between pixels in world units in the Y direction
     * 
     * @throws IllegalArgumentException if {@code bounds} is {@code null} or empty
     */
    void setWorldByStepDistance(Rectangle2D bounds, double xstep, double ystep);
    
    /**
     * Sets the world (processing area) bounds and the number of pixels in the
     * X and Y directions.
     * 
     * @param bounds outer bounds of the processing area
     * @param nx number of pixels in the X direction
     * @param ny number of pixels in the Y direction
     * 
     * @throws IllegalArgumentException if {@code bounds} is {@code null} or empty
     */
    void setWorldByNumSteps(Rectangle2D bounds, int nx, int ny);
    
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
     * Gets the distance between pixels in the X direction, expressed in
     * world units.
     * 
     * @return step distance in world units
     */
    double getXStep();
    
    /**
     * Gets the distance between pixels in the Y direction, expressed in
     * world units.
     * 
     * @return step distance in world units
     */
    double getYStep();
    
    /**
     * Gets the total number of pixels in the processing area, calculated from 
     * the world bounds and step distances.
     * 
     * @return number of pixels
     * @throws IllegalStateException if the processing area has not been set
     */
    long getNumPixels();
    
    /**
     * Checks whether the world bounds and step distances have been set.
     * 
     * @return {@code true} if set; {@code false} otherwise
     */
    boolean isWorldSet();
    
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
     * Supplies the image parameters to the runtime object so that source
     * and destination image variable names can be retrieved by the client
     * prior to the actual images being set.
     * <p>
     * <b>Note:</b> The {@code imageParams} Map is declared here without 
     * type parameters to work with the Janino compiler.
     * 
     * @param imageParams a {@code Map} with image variable names as keys and
     *        {@code Jiffle.ImageRole} constants as values
     * 
     */
    void setImageParams(Map imageParams);
    
    /**
     * Gets the names of script variables which represent source images.
     * 
     * @return array of names (may be empty but not {@code null}
     */
    String[] getSourceVarNames();
    
    /**
     * Gets the names of script variables which represent destination images.
     * 
     * @return array of names (may be empty but not {@code null}
     */
    String[] getDestinationVarNames();
}
