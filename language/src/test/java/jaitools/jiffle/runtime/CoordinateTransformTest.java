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
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit tests for CoordinateTransform class and the CoordinateTransforms helper
 * class.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class CoordinateTransformTest {
    
    @Test
    public void identity_direct() throws Exception {
        System.out.println("   identity transform created directly");
        
        CoordinateTransform tr = new IdentityCoordinateTransform();
        Point pt = tr.worldToImage(10.4, 10.6, null);
        assertPoint(10, 11, pt);
    }
    
    @Test
    public void identity_fromHelper() throws Exception {
        System.out.println("   identity transform from helper");
        
        CoordinateTransform tr = CoordinateTransforms.identity();
        Point pt = tr.worldToImage(10.4, 10.6, null);
        assertPoint(10, 11, pt);
    }
    
    @Test
    public void translation() throws Exception {
        System.out.println("   translation transform");
        
        CoordinateTransform tr = CoordinateTransforms.translation(10, -10);
        Point pt = tr.worldToImage(100, 100, null);
        assertPoint(110, 90, pt);
    }
    
    @Test
    public void scale() throws Exception {
        System.out.println("   scaling transform");
        
        CoordinateTransform tr = CoordinateTransforms.scale(0.1, 0.2);
        Point pt = tr.worldToImage(100, 100, null);
        assertPoint(10, 20, pt);
    }

    @Test
    public void unitBounds() throws Exception {
        System.out.println("   unit bounds transform");
        
        Rectangle r = new Rectangle(-100, 100, 1000, 2000);
        CoordinateTransform tr = CoordinateTransforms.unitBounds(r);
        
        assertPoint(r.x, r.y, tr.worldToImage(0, 0, null));
        assertPoint(r.x + r.width, r.y + r.height, tr.worldToImage(1, 1, null));
    }
    
    @Test
    public void getTransform() throws Exception {
        System.out.println("   getTransform method");
        
        Rectangle world = new Rectangle(750000, 6550000, 10000, 10000);
        Rectangle image = new Rectangle(10, -10, 100, 100);
        CoordinateTransform tr = CoordinateTransforms.getTransform(world, image);
        
        assertPoint(image.x, image.y, tr.worldToImage(world.x, world.y, null));
        
        assertPoint(image.x + image.width, image.y + image.height, 
                tr.worldToImage(world.x + world.width, world.y + world.height, null));
    }
    
    @Test
    public void affineRotation() throws Exception {
        System.out.println("   affine rotation");

        // 90 degrees anti-clockwise rotation
        AffineTransform affine = AffineTransform.getRotateInstance(Math.PI/2, 50, 50);
        CoordinateTransform tr = new AffineCoordinateTransform(affine);
        
        assertPoint(100, 0, tr.worldToImage(0, 0, null));
        assertPoint(0, 100, tr.worldToImage(100, 100, null));
    }
    
    private void assertPoint(int expectedX, int expectedY, Point pt) {
        assertEquals(expectedX, pt.x);
        assertEquals(expectedY, pt.y);
    }
}
