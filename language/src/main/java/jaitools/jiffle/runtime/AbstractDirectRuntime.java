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
import java.awt.image.RenderedImage;
import java.awt.image.WritableRenderedImage;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import javax.media.jai.iterator.WritableRandomIter;

/**
 * The default abstract base class for runtime classes that implement
 * direct evaluation.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public abstract class AbstractDirectRuntime extends AbstractJiffleRuntime implements JiffleDirectRuntime {

    private static final double EPS = 1.0e-10d;
    
    /* 
     * Note: not using generics here because they are not
     * supported by the Janino compiler.
     */
    
    /** 
     * Maps image variable names ({@link String}) to images
     * ({@link RenderedImage}).
     * 
     */
    protected Map images = new HashMap();
    
    /** 
     * Maps source image variable names ({@link String}) to image
     * iterators ({@link RandomIter}).
     */
    protected Map readers = new LinkedHashMap();
    
    /**
     * Maps destination image variable names ({@link String} to
     * image iterators ({@link WritableRandomIter}).
     */
    protected Map writers = new LinkedHashMap();

    /**
     * Creates a new instance and initializes script-option variables.
     */
    public AbstractDirectRuntime() {
        initOptionVars();
    }

    /**
     * {@inheritDoc}
     */
    public void setDestinationImage(String varName, WritableRenderedImage image) {
        setDestinationImage(varName, image, null);
    }
    
    /**
     * {@inheritDoc}
     */
    public void setDestinationImage(String varName, WritableRenderedImage image, 
            CoordinateTransform tr) {
        
        images.put(varName, image);
        writers.put(varName, RandomIterFactory.createWritable(image, null));
        setTransform(varName, tr);
    }

    /**
     * {@inheritDoc}
     */
    public void setSourceImage(String varName, RenderedImage image) {
        setSourceImage(varName, image, null);
    }
    
    /**
     * {@inheritDoc}
     */
    public void setSourceImage(String varName, RenderedImage image, CoordinateTransform tr) {
        images.put(varName, image);
        readers.put(varName, RandomIterFactory.create(image, null));
        setTransform(varName, tr);
    }

    /**
     * {@inheritDoc}
     */
    public void evaluateAll(JiffleProgressListener pl) {
        JiffleProgressListener listener = pl == null ? new NullProgressListener() : pl;
        
        if (!isWorldSet()) {
            setDefaultBounds();
        }

        final long numPixels = getNumPixels();
        listener.setTaskSize(numPixels);
        
        long count = 0;
        long sinceLastUpdate = 0;
        final long updateInterval = listener.getUpdateInterval();
        
        final double minX = getMinX();
        final double maxX = getMaxX();
        final double stepX = getXStep();
        
        final double minY = getMinY();
        final double maxY = getMaxY();
        final double stepY = getYStep();
        
        listener.start();
        for (double y = minY; y < maxY - EPS; y += stepY) {
            for (double x = minX; x < maxX - EPS; x += stepX) {
                evaluate(x, y);
                
                count++ ;
                sinceLastUpdate++;
                if (sinceLastUpdate >= updateInterval) {
                    listener.update( count );
                    sinceLastUpdate = 0;
                }
            }
        }
        listener.finish();
    }
    
    /**
     * {@inheritDoc}
     */
    public double readFromImage(String srcImageName, double x, double y, int band) {
        boolean inside = true;
        RenderedImage img = (RenderedImage) images.get(srcImageName);
        CoordinateTransform tr = getTransform(srcImageName);
        
        Point imgPos = tr.worldToImage(x, y, null);
        
        int xx = imgPos.x - img.getMinX();
        if (xx < 0 || xx >= img.getWidth()) {
            inside = false;
        } else {
            int yy = imgPos.y - img.getMinY();
            if (yy < 0 || yy >= img.getHeight()) {
                inside = false;
            }
        }
        
        if (!inside) {
            if (_outsideValueSet) {
                return _outsideValue;
            } else {
                throw new JiffleRuntimeException( String.format(
                        "Position %.4f %.4f is outside bounds of image: %s", 
                        x, y, srcImageName));
            }
        }
        
        RandomIter iter = (RandomIter) readers.get(srcImageName);
        return iter.getSampleDouble(imgPos.x, imgPos.y, band);
    }
    
    /**
     * {@inheritDoc}
     */
    public void writeToImage(String destImageName, double x, double y, int band, double value) {
        WritableRandomIter iter = (WritableRandomIter) writers.get(destImageName);
        CoordinateTransform tr = getTransform(destImageName);
        Point imgPos = tr.worldToImage(x, y, null);
        iter.setSample(imgPos.x, imgPos.y, band, value);
    }

    /**
     * {@inheritDoc}
     */
    public void setDefaultBounds() {
        RenderedImage refImage = null;
        String imageName = null;
        
        if (!writers.isEmpty()) {
            imageName = (String) writers.keySet().iterator().next();
            refImage = (RenderedImage) images.get(imageName);
        } else {
            imageName = (String) readers.keySet().iterator().next();
            refImage = (RenderedImage) images.get(imageName);
        }
        
        Rectangle rect = new Rectangle(
                refImage.getMinX(), refImage.getMinY(), 
                refImage.getWidth(), refImage.getHeight());
        
        setWorldByStepDistance(rect, 1, 1);
    }

    /**
     * Returns the images set for this runtime object as a {@code Map} with
     * variable name as key and iamge as value. The returned {@code Map} is
     * a copy of the one held by this object, so it can be safely modified
     * by the caller.
     * 
     * @return images keyed by variable name
     */
    public Map getImages() {
        Map copy = new HashMap();
        copy.putAll(images);
        return copy;
    }

}
