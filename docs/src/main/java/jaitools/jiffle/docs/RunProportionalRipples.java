package jaitools.jiffle.docs;

import java.awt.Rectangle;
import java.awt.image.WritableRenderedImage;

import jaitools.jiffle.JiffleBuilder;
import jaitools.jiffle.JiffleException;
import jaitools.jiffle.runtime.CoordinateTransform;
import jaitools.jiffle.runtime.CoordinateTransforms;


public class RunProportionalRipples {

    public void runScript(String script, String destVar, WritableRenderedImage destImage) 
            throws JiffleException {
        
        Rectangle imageBounds = new Rectangle(
                destImage.getMinX(), destImage.getMinY(),
                destImage.getWidth(), destImage.getHeight());
        
        CoordinateTransform transform = CoordinateTransforms.unitInterval(imageBounds);
        
        JiffleBuilder builder = new JiffleBuilder();
        builder.script(script).dest(destVar, destImage, transform).run();
    }
}
