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
import jaitools.jiffle.JiffleException;
import java.awt.geom.Point2D;

import org.junit.Test;

/**
 * Unit tests for scripts using world coordinate systems.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class WorldCoordsTest extends RuntimeTestBase {
    
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
        
        double xres = worldBounds.getWidth() / IMG_WIDTH;
        double yres = worldBounds.getHeight() / IMG_WIDTH;
        runtime.setWorldByResolution(worldBounds, xres, yres);
        
        WritableRenderedImage destImg = ImageUtils.createConstantImage(IMG_WIDTH, IMG_WIDTH, 0d);
        runtime.setDestinationImage("dest", destImg, tr);
        runtime.evaluateAll(null);
        
        Evaluator e = new Evaluator() {
            public double eval(double val) {
                int right = x >= 5 ? 1 : 0;
                int top = y >= 5 ? 1 : 0;
                double z = right + 2 * top;
                
                move();
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
        
        runtime.setWorldByNumPixels(worldBounds, IMG_WIDTH, IMG_WIDTH);
        
        // Get the transform from world to image coordinates
        // CoordinateTransform tr = CoordinateTransforms.unitBounds(
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
        System.out.println("   using world position and pixel dimensions in script");
        
        final double XO = 750000;
        final double YO = 6550000;
        final double W = 10000;
        
        Rectangle2D worldBounds = new Rectangle2D.Double(XO, YO, W, W);
        
        String script = "images { dest=write; } dest = xres() + yres() + x() + y();" ;
        JiffleDirectRuntime runtime = getRuntime(script);
        
        WritableRenderedImage destImg = ImageUtils.createConstantImage(IMG_WIDTH, IMG_WIDTH, 0d);
        Rectangle imageBounds = new Rectangle(0, 0, IMG_WIDTH, IMG_WIDTH); 
        
        final double RES = W / IMG_WIDTH;
        runtime.setWorldByResolution(worldBounds, RES, RES);
        
        CoordinateTransform tr = CoordinateTransforms.getTransform(worldBounds, imageBounds);
        runtime.setDestinationImage("dest", destImg, tr);
        runtime.evaluateAll(null);
        
        Evaluator e = new Evaluator() {
            Point2D.Double pos = new Point2D.Double(XO, YO);
            
            public double eval(double val) {
                double z = RES + RES + pos.x + pos.y;
                
                move();
                pos.x += RES;
                if (x == 0) {
                    pos.x = XO;
                    pos.y += RES;
                }
                return z;
            }
        };
        
        assertImage(null, destImg, e);
    }
    
    @Test
    public void setDefaultTransform() throws Exception {
        System.out.println("   set default transform");
        
        String script = 
                  "images { src=read; dest=write; } \n"
                + "dest = con(x() > 0.5 && y() < 0.5, src, 0);" ;
        
        JiffleDirectRuntime runtime = getRuntime(script);
        
        RenderedImage srcImg = createRowValueImage();
        WritableRenderedImage destImg = ImageUtils.createConstantImage(IMG_WIDTH, IMG_WIDTH, 0d);
        
        Rectangle worldBounds = new Rectangle(0, 0, 1, 1);
        runtime.setWorldByNumPixels(worldBounds, IMG_WIDTH, IMG_WIDTH);

        Rectangle imageBounds = new Rectangle(0, 0, IMG_WIDTH, IMG_WIDTH);
        CoordinateTransform tr = CoordinateTransforms.unitBounds(imageBounds);
        runtime.setDefaultTransform(tr);
        
        runtime.setSourceImage("src", srcImg);
        runtime.setDestinationImage("dest", destImg);
        runtime.evaluateAll(null);
        
        Evaluator e = new Evaluator() {
            public double eval(double val) {
                double z = 0;
                if (x > IMG_WIDTH / 2 && y < IMG_WIDTH / 2) {
                    z = val;
                }
                move();
                return z;
            }
        };
        
        assertImage(srcImg, destImg, e);
    }
    
    /**
     * Uses a coordinate transform to relate two non-overlapping images
     */
    @Test
    public void nonOverlappingSourceAndDestImages() throws Exception {
        System.out.println("   non-overlapping images");

        final int XO = -10;
        final int YO = 10;
        
        RenderedImage srcImg = ImageUtils.createConstantImage(XO, YO, IMG_WIDTH, IMG_WIDTH, 0);
        WritableRenderedImage destImg = ImageUtils.createConstantImage(IMG_WIDTH, IMG_WIDTH, 0d);
        
        Rectangle srcBounds = new Rectangle(XO, YO, IMG_WIDTH, IMG_WIDTH);
        
        // The world bounds are the bounds of the destination image
        Rectangle worldBounds = new Rectangle(0, 0, IMG_WIDTH, IMG_WIDTH);
        
        String script = "images { src=read; dest=write; } dest = src;" ;
        JiffleDirectRuntime runtime = getRuntime(script);
        
        runtime.setWorldByResolution(worldBounds, 1, 1);
        
        CoordinateTransform srcTr = CoordinateTransforms.getTransform(worldBounds, srcBounds);
        runtime.setSourceImage("src", srcImg, srcTr);
        runtime.setDestinationImage("dest", destImg);
        
        runtime.evaluateAll(null);
        
        Evaluator e = new Evaluator() {
            @Override
            public double eval(double val) {
                return val;
            }
        };
        
        assertImage(srcImg, destImg, e);
    }
    
    /**
     * Setting an image with a transform without having previously set the world
     * bounds should throw a JiffleException.
     */
    @Test(expected=JiffleException.class)
    public void forgetTheWorldBeforeImageWithTransform() throws Exception {
        System.out.println("   forget to set the world before setting image with transform");
        
        JiffleDirectRuntime runtime = getRuntime("images {dest=write;} dest = 42;");
        WritableRenderedImage destImage = ImageUtils.createConstantImage(IMG_WIDTH, IMG_WIDTH, 0d);
        CoordinateTransform tr = CoordinateTransforms.translation(10, 10);
        runtime.setDestinationImage("dest", destImage, tr);
    }
    
    /**
     * Setting a default transform without having previously set the world
     * bounds should throw a JiffleException.
     */
    @Test(expected=JiffleException.class)
    public void forgetTheWorldBeforeDefaultTransform() throws Exception {
        System.out.println("   forget to set the world before setting default transform");
        
        JiffleDirectRuntime runtime = getRuntime("images {dest=write;} dest = 42;");
        CoordinateTransform tr = CoordinateTransforms.translation(10, 10);
        runtime.setDefaultTransform(tr);
    }
    
    
    private JiffleDirectRuntime getRuntime(String script) throws Exception {
        Jiffle jiffle = new Jiffle();
        jiffle.setScript(script);
        jiffle.compile();
        return jiffle.getRuntimeInstance();
    }
}
