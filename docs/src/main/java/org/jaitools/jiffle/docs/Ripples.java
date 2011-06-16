package org.jaitools.jiffle.docs;

import java.awt.image.RenderedImage;
import java.awt.image.WritableRenderedImage;
import java.io.File;

import javax.media.jai.TiledImage;
import javax.media.jai.iterator.RectIterFactory;
import javax.media.jai.iterator.WritableRectIter;

import org.jaitools.imageutils.ImageUtils;
import org.jaitools.jiffle.JiffleBuilder;
import org.jaitools.swing.ImageFrame;

public class Ripples {

    public static void main(String[] args) {
        Ripples me = new Ripples();
        TiledImage image = ImageUtils.createConstantImage(300, 300, 0d);
        me.createRipplesImage(image);
        
        ImageFrame frame = new ImageFrame(image, "ripples");
        frame.setSize(550, 550);
        frame.setVisible(true);
    }

    // docs-begin-method
    public void createRipplesImage(WritableRenderedImage destImg) {

        // image dimensions
        final int width = destImg.getWidth();
        final int height = destImg.getHeight();

        // first pixel coordinates
        int x = destImg.getMinX();
        int y = destImg.getMinY();

        // center pixel coordinates
        final int xc = x + destImg.getWidth() / 2;
        final int yc = y + destImg.getHeight() / 2;

        // constant term
        double C = Math.PI * 8;

        WritableRectIter iter = RectIterFactory.createWritable(destImg, null);
        do {
            double dy = ((double) (y - yc)) / yc;
            do {
                double dx = ((double) (x - xc)) / xc;
                double d = Math.sqrt(dx * dx + dy * dy);
                iter.setSample(Math.sin(d * C));
                x++ ;
            } while (!iter.nextPixelDone());

            x = destImg.getMinX();
            y++;
            iter.startPixels();

        } while (!iter.nextLineDone());
    }
    // docs-end-method
    
    public void runScriptWithBuilder(File scriptFile) throws Exception {
        // docs-begin-builder-example
        JiffleBuilder builder = new JiffleBuilder();
        
        // These chained methods read the script from a file,
        // create a new image for the output, and run the script
        builder.script(scriptFile).dest("destImg", 500, 500).run();
        
        RenderedImage result = builder.getImage("destImg");
        // docs-end-builder-example
    }
}
