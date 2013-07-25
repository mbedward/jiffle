package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.util.Strings;

/**
 *
 * @author michael
 */
public class ImagePos implements Node {
    public static ImagePos DEFAULT = new ImagePos(Band.DEFAULT, Pixel.DEFAULT);

    private final Band band;
    private final Pixel pixel;

    public ImagePos(Band band, Pixel pixel) {
        this.band = band;
        this.pixel = pixel;
    }

    @Override
    public String toString() {
        return Strings.commas(pixel, band);
    }
    
    
}
