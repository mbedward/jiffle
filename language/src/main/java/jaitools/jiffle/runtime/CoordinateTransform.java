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

import java.awt.Point;

/**
 * A transform used by Jiffle to convert from world (processing area) coordinates
 * to image (pixel) coordinates.
 *
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public interface CoordinateTransform {
    
    /**
     * Converts from world to image coordinates. If {@code p} is not {@code null}
     * it will be set to the image coordinates, otherwise a new destination object
     * will be created. In both cases, the resulting point is returned.
     * 
     * @param x world X ordinate
     * @param y world Y ordinate
     * @param p object to receive image coordinates (may be {@code null})
     * 
     * @return image coordinates
     */
    Point worldToImage(double x, double y, Point p);
}
