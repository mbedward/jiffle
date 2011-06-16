package org.jaitools.jiffle.docs;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.jaitools.jiffle.runtime.CoordinateTransform;
import org.jaitools.jiffle.runtime.CoordinateTransforms;
import org.jaitools.jiffle.runtime.JiffleDirectRuntime;

public class TreeChange {

    JiffleDirectRuntime runtimeObj = null;
    int imageX, imageY, numCols, numRows;

    // DO NOT RUN - SNIPPET FOR DOCS USE ONLY
    public void runtimeExample() throws Exception {
        // docs start
// World bounds
Rectangle2D worldBounds = new Rectangle2D.Double(
        750000, 6500000, 100000, 50000);

// Common image bounds
Rectangle imageBounds = new Rectangle(0, 0, 4000, 2000);

// Set the bounds (world units) and resolution of the 
// processing area
runtimeObj.setWorldByNumPixels(worldBounds, 4000, 2000);

// Create a new transform that converts from world units to
// pixel positions using the CoordinateTransforms helper class
CoordinateTransform tr = CoordinateTransforms.getTransform(
        worldBounds, imageBounds);

// Set this coordinate transform object to be used with all images
runtimeObj.setDefaultTransform(tr);
        // docs end
    }
}
