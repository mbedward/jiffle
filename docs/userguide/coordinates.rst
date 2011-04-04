Working with coordinate systems
===============================

Up until now, the scripts we've been looking at have worked directly with image coordinates, ie. pixel positions. This
chapter introduces the concept of the *processing area*, also referred to as the *world* which allows you to write your
scripts in terms of other coordinate systems such as proportional or geographic distance. You can also use it to deal
with source and destination images that have non-overlapping bounds or different resolutions.

To get the flavour of this, let's look again at the *ripples* script which we saw in the :doc:`introduction`:

.. literalinclude:: /../src/main/resources/jaitools/jiffle/docs/Ripples.jfl

The variables *dx* and *dy* are the proportional X and Y distances of the current pixel from the image centre *(xc,
yc)*. We use these to calculate the scalar proportional distance *d* which is then fed to the trigonometric function.
You might have noticed that the way this script is written assumes that the image origin is at pixel position *(0, 0)*.
It could be generalized by using the coordinate functions *xmin()*, *ymin()*, *width()* and *height()*.

If we re-write the script to work in proportional coordinates directly, rather than image coordinates, it becomes a lot
simpler:

.. literalinclude:: /../src/main/resources/jaitools/jiffle/docs/RipplesProportionalCoords.jfl

We no longer need the variables *xc* and *yc* because the proportional X and Y distances from the image centre are now
simply *x() - 0.5* and *y() - 0.5* respectively. Also, unlike the previous script, this one will work equally well with
a destination image having a non-zero origin. So working in propoortional coordinates has made the script both simpler
and more general.


How Jiffle relates image and world coordinates
----------------------------------------------

.. figure:: coordsystem.png
   :align: right

When a Jiffle script is executed, any calls to coordinate functions such as *x()* (which returns the X ordinate of the
current processing position) or *width()* (which returns the width of the processing area) return values in *world
units*. When reading a value from a source image, or writing a value to a destination image, Jiffle converts the
position from *world coordinates* to a pixel lcoation using the **CoordinateTransform** associated with the image. So,
to run the ripples script (above) written in terms of proportional coordinates, we would associate a transform 
with the destination image when submitting it to the Jiffle run-time object.

Note that CoordinateTransforms are run-time objects. You don't need to worry about the transforms in the script itself.
Instead, you write the script using whatever coordinate system is most appropriate for the application, then provide the
necessary transforms at run-time to convert between world and image coordinates.

When working only with image coordinates, or with a script that doesn't require any coordinate references, you don't
need to worry about supplying transforms. Jiffle will create a default identity transform for each image which simply
converts double-precision world coordinates into integer image coordinates by rounding.


Defining the processing area
----------------------------

To execute a script, the Jiffle runtime system needs to know the bounds of the processing area and the pixel dimensions,
both of which are expressed in world units. Before going into any more detail, let's look at an example where we run the
proportional coordinate ripples script (above):

.. literalinclude:: /../src/main/java/jaitools/jiffle/docs/RunProportionalRipples.java


Creating coordinate transforms
------------------------------


