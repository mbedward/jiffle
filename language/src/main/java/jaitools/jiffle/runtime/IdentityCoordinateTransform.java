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
 * An implementation of {@code CoordinateTransform} which simple converts 
 * input coordinates to integers by <strong>rounding<strong>. 
 * This is the default transform used by Jiffle and is slightly faster 
 * than using an identity {@link AffineCoordinateTransform}.
 *
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class IdentityCoordinateTransform implements CoordinateTransform {
    
    /**
     * {@inheritDoc}
     */
    public Point worldToImage(double x, double y, Point p) {
        
        if (p != null) {
            p.x = (int) Math.round(x);
            p.y = (int) Math.round(y);
        } else {
            p = new Point((int) Math.round(x), (int) Math.round(y));
        }
        return p;
    }
}
