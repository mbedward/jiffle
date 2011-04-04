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

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRenderedImage;

import jaitools.imageutils.ImageUtils;
import jaitools.jiffle.Jiffle;
import javax.media.jai.TiledImage;

import org.junit.Test;

/**
 * Unit tests for scripts using world coordinate systems.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class WorldCoordsTest extends StatementsTestBase {
    
    /**
     * Working with UTM-style bounding coordinates
     */
    @Test
    public void utmBoundsDest() throws Exception {
        System.out.println("   UTM type bounds on destination image");
        
        Rectangle2D worldBounds = new Rectangle(750000, 6550000, 1000, 1000);
        Rectangle imageBounds = new Rectangle(0, 0, 10, 10);
        CoordinateTransform tr = CoordinateTransforms.getTransform(worldBounds, imageBounds);
                
        String script = 
                  "images {dest=write;} \n"
                + "right = x() >= 750500; \n"
                + "top = y() >= 6550500; \n"
                + "dest = right + 2 * top;" ;
        JiffleDirectRuntime runtime = getRuntime(script);
        
        double xstep = worldBounds.getWidth() / IMG_WIDTH;
        double ystep = worldBounds.getHeight() / IMG_WIDTH;
        runtime.setWorldByStepDistance(worldBounds, xstep, ystep);
        
        WritableRenderedImage destImg = ImageUtils.createConstantImage(IMG_WIDTH, IMG_WIDTH, 0d);
        runtime.setDestinationImage("dest", destImg, tr);
        runtime.evaluateAll(null);
        
        Evaluator e = new Evaluator() {
            int x = 0;
            int y = 0;
            public double eval(double val) {
                int right = x >= 5 ? 1 : 0;
                int top = y >= 5 ? 1 : 0;
                double z = right + 2 * top;
                
                if (++x == IMG_WIDTH) {
                    x = 0;
                    y++ ;
                }
                
                return z;
            }
        };
        
        assertImage(null, destImg, e);
    }


    /**
     * Unit bounds for both source and destination images.
     */
    @Test
    public void unitIntervalCopySourceToDest() throws Exception {
        System.out.println("   world coordinates on the unit interval");
        
        String script = "images {src=read; dest=write;} dest = src;" ;
        JiffleDirectRuntime runtime = getRuntime(script);
        
        Rectangle2D worldBounds = new Rectangle(0, 0, 1, 1);
        Rectangle imageBounds = new Rectangle(0, 0, IMG_WIDTH, IMG_WIDTH);
        
        runtime.setWorldByNumSteps(worldBounds, IMG_WIDTH, IMG_WIDTH);
        
        // Get the transform from world to image coordinates
        // CoordinateTransform tr = CoordinateTransforms.unitInterval(
        //        new Rectangle(0, 0, IMG_WIDTH, IMG_WIDTH));
        CoordinateTransform tr = CoordinateTransforms.getTransform(worldBounds, imageBounds);
        
        RenderedImage srcImg = createSequenceImage();
        runtime.setSourceImage("src", srcImg, tr);
        WritableRenderedImage destImg = ImageUtils.createConstantImage(IMG_WIDTH, IMG_WIDTH, 0d);
        runtime.setDestinationImage("dest", destImg, tr);
        runtime.evaluateAll(null);
        
        Evaluator e = new Evaluator() {
            public double eval(double val) {
                return val;
            }
        };
        
        assertImage(srcImg, destImg, e);
    }
    
    @Test
    public void worldDistancesInScript() throws Exception {
        System.out.println("   using world position and step distances in script");
        
        final double XO = 750000;
        final double YO = 6550000;
        final double W = 10000;
        
        Rectangle2D worldBounds = new Rectangle2D.Double(XO, YO, W, W);
        
        String script = "images { dest=write; } dest = xstep() + ystep() + x() + y();" ;
        JiffleDirectRuntime runtime = getRuntime(script);
        
        WritableRenderedImage destImg = ImageUtils.createConstantImage(IMG_WIDTH, IMG_WIDTH, 0d);
        Rectangle imageBounds = new Rectangle(0, 0, IMG_WIDTH, IMG_WIDTH); 
        
        final double STEP = W / IMG_WIDTH;
        runtime.setWorldByStepDistance(worldBounds, STEP, STEP);
        
        CoordinateTransform tr = CoordinateTransforms.getTransform(worldBounds, imageBounds);
        runtime.setDestinationImage("dest", destImg, tr);
        runtime.evaluateAll(null);
        
        Evaluator e = new Evaluator() {
            int ix = 0;
            int iy = 0;
            double x = XO;
            double y = YO;
            
            public double eval(double val) {
                double z = STEP + STEP + x + y;
                
                ix++;
                x += STEP;
                if (ix == IMG_WIDTH) {
                    ix = 0;
                    x = XO;
                    iy++ ;
                    y += STEP;
                }
                return z;
            }
        };
        
        assertImage(null, destImg, e);
    }
    
    private JiffleDirectRuntime getRuntime(String script) throws Exception {
        Jiffle jiffle = new Jiffle();
        jiffle.setScript(script);
        jiffle.compile();
        return jiffle.getRuntimeInstance();
    }
}
