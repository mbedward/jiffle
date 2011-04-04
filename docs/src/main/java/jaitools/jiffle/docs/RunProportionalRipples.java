package jaitools.jiffle.docs;

import jaitools.jiffle.runtime.JiffleDirectRuntime;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.WritableRenderedImage;
import java.util.Map;

import jaitools.CollectionFactory;
import jaitools.imageutils.ImageUtils;
import jaitools.jiffle.Jiffle;
import jaitools.jiffle.JiffleBuilder;
import jaitools.jiffle.JiffleException;
import jaitools.jiffle.runtime.CoordinateTransform;
import jaitools.jiffle.runtime.CoordinateTransforms;
import jaitools.swing.ImageFrame;


public class RunProportionalRipples {
    
    public static void main(String[] args) throws JiffleException {
        RunProportionalRipples self = new RunProportionalRipples();
        
        String script = 
                "init { C = M_PI * 8; }"
                + "dx = 2*(x() - 0.5); \n"
                + "dy = 2*(y() - 0.5); \n"
                + "d = sqrt(dx*dx + dy*dy); \n"
                + "destImg = sin(C * d);" ;
        
        WritableRenderedImage destImage = ImageUtils.createConstantImage(500, 500, 0d);
        self.runScriptWithJiffle(script, "destImg", destImage);
        
        ImageFrame frame = new ImageFrame(destImage, "Ripples");
        frame.setSize(550, 550);
        frame.setVisible(true);
    }

    // docs start jiffle method
    public void runScriptWithJiffle(String script, String destVar, WritableRenderedImage destImage) 
            throws JiffleException {
        
        Jiffle jiffle = new Jiffle();
        jiffle.setScript(script);
        
        Map<String, Jiffle.ImageRole> imageParams = CollectionFactory.map();
        imageParams.put(destVar, Jiffle.ImageRole.DEST);
        jiffle.setImageParams(imageParams);
        
        jiffle.compile();
        JiffleDirectRuntime runtimeObj = jiffle.getRuntimeInstance();
        
        // Image bounds are taken from the destination image
        Rectangle imageBounds = new Rectangle(
                destImage.getMinX(), destImage.getMinY(),
                destImage.getWidth(), destImage.getHeight());
        
        // The world bounds are the unit rectangle
        Rectangle2D worldBounds = new Rectangle2D.Double(0, 0, 1, 1);
        
        // We use the CoordinateTransforms helper class to create a transform that
        // will convert proportional coordinates into pixel positions.
        CoordinateTransform transform = CoordinateTransforms.unitInterval(imageBounds);

        // Set the world bounds and resolution
        runtimeObj.setWorldByNumPixels(worldBounds, destImage.getWidth(), destImage.getHeight());
        
        // Associate the image and its transform with the destination variable 
        // name used in the script
        runtimeObj.setDestinationImage(destVar, destImage, transform);
        
        // Execute the runtime object. This will write results into destImage.
        runtimeObj.evaluateAll(null);
    }
    // docs end jiffle method
    
    // docs start builder method
    public void runScriptWithBuilder(String script, String destVar, WritableRenderedImage destImage) 
            throws JiffleException {
        
        // Image bounds are taken from the destination image
        Rectangle imageBounds = new Rectangle(
                destImage.getMinX(), destImage.getMinY(),
                destImage.getWidth(), destImage.getHeight());
        
        // The world bounds are the unit rectangle
        Rectangle2D worldBounds = new Rectangle2D.Double(0, 0, 1, 1);
        
        CoordinateTransform transform = CoordinateTransforms.unitInterval(imageBounds);
        
        JiffleBuilder builder = new JiffleBuilder();
        
        // Set the processing area (world units)
        builder.worldAndNumPixels(worldBounds, destImage.getWidth(), destImage.getHeight());
        
        // Set the script and the destination image with its transform
        builder.script(script).dest(destVar, destImage, transform);
        
        // This executes the script and writes the results into destImage
        builder.run();
    }
    // docs end builder method
}
