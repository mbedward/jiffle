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

It produces a set of concentric sinusoidal waves which emanate from the image center or, more poetically: *ripples on
the sunlit surface of a still pond...*

.. image:: ripples-image.png
   :align: center

Here is the Java code to plot the function. It uses a JAI [#f1]_ iterator to set pixel values. We'll omit the import statements and just
concentrate on the method itself:

.. literalinclude:: /../src/main/java/com/googlecode/jaitools/jiffle/docs/Ripples.java
   :language: java
   :start-after: // docs-begin-method
   :end-before: // docs-end-method
  
Now here is the equivalent Jiffle script:

.. literalinclude:: /../src/main/resources/jaitools/jiffle/docs/Ripples.jfl

Compared to the Java method, the Jiffle script:

  * is much shorter
  * is easier to read because the algorithm isn't obscured by lots of boiler-plate code
  * uses **no** loops !

That last feature is what enables Jiffle scripts to be so concise. In Jiffle, you don't write code to iterate over your
source and destination images. Instead, you specify how to calculate the value of an *individual pixel* and the Jiffle
runtime system then applies that calculation over the whole image.

Now, some readers might cry foul at the above comparison because although we presented the Jiffle script, we didn't show
the necessary Java code to actually run it. To be fair, we didn't show the import statements and calling code necessary
for the Java method either, but in the interest of even-handedness, here is one way to run that script within a Java
program:

.. literalinclude:: /../src/main/java/com/googlecode/jaitools/jiffle/docs/Ripples.java
   :start-after: // docs-begin-builder-example
   :end-before: // docs-end-builder-example


What you can do with Jiffle
---------------------------

Coming soon...

What you can't do with Jiffle
-----------------------------

Coming soon...

.. [#f1] Java Advanced Imaging http://java.sun.com/javase/technologies/desktop/media/jai/

