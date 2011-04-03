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
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Wraps an AffineTransform object for use as a Jiffle {@link CoordinateTransform}.
 *
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class AffineCoordinateTransform implements CoordinateTransform {
    private final AffineTransform affine;
    private Point2D p2D;

    /**
     * Creates a new instance to wrap the given {@code AffineTransform}. If
     * {@code tr} is {@code null} an identity transform will be used. The
     * input transform is copied.
     * 
     * @param affine the transform
     */
    public AffineCoordinateTransform(AffineTransform affine) {
        this.affine = affine == null ? new AffineTransform() : new AffineTransform(affine);
        p2D = new Point2D.Double();
    }

    /**
     * {@inheritDoc}
     */
    public Point worldToImage(double x, double y, Point p) {
        p2D.setLocation(x, y);
        affine.transform(p2D, p2D);
        
        if (p != null) {
            p.x = (int) Math.round(p2D.getX());
            p.y = (int) Math.round(p2D.getY());
        } else {
            p = new Point((int) Math.round(p2D.getX()), (int) Math.round(p2D.getY()));
        }
        
        return p;
    }
    
}
