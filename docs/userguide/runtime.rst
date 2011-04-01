The Jiffle run-time system
==========================

Once you know how to *write* a Jiffle script, the next thing you'll want to do is *run* it. This section describes
various ways to do that but all of them follow the same basic steps:

#. Compile your script into a run-time object.
#. Provide the run-time object with the source and/or destination images that your script needs.
#. Execute the object.
#. Retrieve the results (images and/or summary values).

You do all of these steps from within Java (or within Groovy, or JRuby, or whatever your favourite JVM language is).

Compiling and running scripts with JiffleBuilder
------------------------------------------------

Using JiffleBuilder is the easiest way to get started with Jiffle. Here we will work though an example, starting with a
Jiffle script, and then going through the Java code required to compile and run it.  First the script:

.. literalinclude:: /../src/main/resources/jaitools/jiffle/docs/MeanFilter3x3.jfl

This script implements a MAX filter: a 3x3 kernel is placed over each pixel in the input image, represented by the
variable *src*, and the maximum value found is written to the output image, represented by *dest* [*]_. 

Now let's look at a Java method which takes the script (in the form of a file) and an input image, and uses
JiffleBuilder to run the script, returning the resulting image to the caller.

.. literalinclude:: /../src/main/java/com/googlecode/jaitools/jiffle/docs/FirstJiffleBuilderExample.java
   :language: java
   :start-after: // docs start method
   :end-before: // docs end method


Working with Jiffle objects directly
------------------------------------


Running scripts with JiffleExecutor
-----------------------------------


JiffleOpImage
-------------



.. [*] The variable names are arbitrary. We could have called them *foo* and *bar* if we had wanted to.


