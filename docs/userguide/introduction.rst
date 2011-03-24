Introduction
============

What (and why) is Jiffle ?
--------------------------

Jiffle is a simple scripting language to work with raster images. Its main aim is to let you get more done with less
code. 

To illustrate what we mean by that, let's compare a Java method for drawing a simple 3-D mathematical function with an
equivalent Jiffle script.  The function we'll use is this:

.. math:: z_{x y} = sin( 8 \pi D_{x y} ) 

..

    where: :math:`D_{x y}` is the distance between pixel position :math:`(x, y)` and the image center.

It produces concentric sinusoidal waves or, if you prefer more poetic descriptions, ripples on the sunlit surface of a
still pond...

.. image:: ripples-image.png
   :align: center

Here is the Java code which uses a JAI [#f1]_ iterator to set pixel values (we'll omit the import statements and just
concentrate on the method)::

    public void createRipplesImage(WritableRenderedImage image) {

        // image dimensions
        final int width = image.getWidth();
        final int height = image.getHeight();

        // first pixel coordinates
        int x = image.getMinX();
        int y = image.getMinY();

        // center pixel coordinates
        final int xc = x + image.getWidth() / 2;
        final int yc = y + image.getHeight() / 2;

        // constant term
        double C = Math.PI * 8;

        WritableRectIter iter = RectIterFactory.createWritable(image, null);
        do {
            double dy = ((double) (y - yc)) / yc;
            do {
                double dx = ((double) (x - xc)) / xc;
                double d = Math.sqrt(dx * dx + dy * dy);
                iter.setSample(Math.sin(d * C));
                x++ ;
            } while (!iter.nextPixelDone());

            x = image.getMinX();
            y++;
            iter.startPixels();

        } while (!iter.nextLineDone());
    }
  
Now here is the equivalent Jiffle script::

    init {
      // image center coordinates
      xc = xmin() + width() / 2;
      yc = ymin() + height() / 2;

      // constant term
      C = M_PI * 8;
    }

    dx = (x() - xc) / xc;
    dy = (y() - yc) / yc;
    d = sqrt(dx*dx + dy*dy);

    image = sin(C * d);

Compared to the Java method, the Jiffle script:

  * is a lot shorter
  * is easier to read because the algorithm isn't obscured by lots of boiler-plate code
  * uses **no** loops !

The last feature is what enables Jiffle scripts to be so concise. With Jiffle you don't write code to iterate over your
source and destination images. Instead, the script specifies how to calculate the value of a *single pixel* and the
Jiffle runtime system then applies that over the whole image.


What Jiffle can do
------------------

Blah blah blah

What Jiffle can't do
--------------------

Jiffle doesn't try to do everything that you can do when working with Java AWT and JAI [#f1]_ directly.

.. [#f1] Java Advanced Imaging http://java.sun.com/javase/technologies/desktop/media/jai/

