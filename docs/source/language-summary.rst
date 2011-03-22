Language Summary
================

Structure of a Jiffle script
----------------------------

Each script has the following elements:

  **options block**
     Sets values for script options that control Jiffle's compile-time and run-time behaviour.
     It is optional but, if present, must be the first element in the script (other than comments).

  **init block**
     Declares variables that will have *image-scope*, ie. their values will persist between processing
     each destination pixel.
     It is optional but, if present, must precede the script body.

  **body**
     The general script code.

The following example script uses all of the above elements::

  // This script implements a max filter with a 3x3
  // neighbourhood (kernel)

  // Set option to treat locations outside the source image
  // area as null values
  options { outside = null; }
  
  // Declare a variable to record the global max value
  init { 
      hiVal = 0;
  }

  // The body of script is everything below this line

  values = [];
  foreach (dy in -1:1) {
      foreach (dx in -1:1) {
          values << src[dx, dy];
      }
  }

  outVal = max(values);
  hiVal = max(hiVal, outVal);

  // Write the value to the destination image
  dest = outVal;


Variables
---------

Types and variable declaration
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Jiffle supports the following types of variables:

  **Scalar**
    A single value. In Jiffle all scalar values correspond to Java type Double.

  **Array**
    A dynamically sized array of scalar values.

  **Image**
    A variables that stands for a source or destination image in a script.

.. note::
   Support for multi-dimensional arrays is yet to be added.

Jiffle uses lazy declaration for scalar variables. In other words, you can just start using a variable name in the
script body. In this snippet::

  // The variable val required no prior declaration
  val = max(0, image1 - image2);

In contrast, array variables must be declared before use so that Jiffle can distinguish them from scalars::

  // declares an empty array 
  foo = [];

  // declares an array with initial values
  bar = [1, 2, 42];

Unlike languages such as Ruby, it is invalid to change the type type of a variable within a script::

  // Create an array variable
  foo = [1, 2, 3];

  // Now if you try to use it as a scalar you will get a compile-time error
  foo = 42;
  
  // Creating a scalar variable bar, then attempting to do an array operation
  // with it (<< is the append operator) will also make the compiler unhappy
  bar = 42;
  bar << 43; // error


Names
~~~~~

Variable names must begin with a letter, optionally followed by any combination of letters, digits, underscores and
dots. Letters can be upper or lower case. Variable names are case-sensitive.

Scope
~~~~~

All scalar and list variables which first appear in the body of the script have *pixel-scope*: their values are
discarded after each destination pixel is processed. Variables declared in the init block, when present, have
*image-scope*: their values persist between pixels::

  init {
      // An image-scope variable with an initial value
      foo = 0;
  }

  // A variable which first appears in the script body
  // has pixel scope
  bar = 0;


Loops
-----

One of the features of Jiffle that makes for concise scripts is that you don't need to write the code to loop through
source and destination images because the runtime system does that for you. So many of your scripts will not need any
loop statements. However, Jiffle does provide loop constructs which are useful when working with pixel neighbourhoods or
performing iterative calculations.

foreach loop
~~~~~~~~~~~~

Probably most of the times when you need to use a loop in a Jiffle script it will be a foreach loop. The general form
is:

    foreach (*var* in *elements*) *target*

where: 
  *var* is a scalar variable that will be set to each value of *elements* in turn;

  *elements* is an array or sequence (see below);
  
  *target* is a single statement or a block of code delimited by curly brackets.

This example iterates through a 3x3 pixel neighbourhood and counts the number of values that are greater than a
threshold value. It uses **sequence** notation, which has the form **lowValue:highValue**. Each loop variable is set
to -1, 0, 1 in turn. The loop variables are then used to access a *relative pixel position* in the source image
(see :ref:`relative-pixel-position`)::

  // Iterate through pixels in a 3x3 neighbourhood
  n = 0;
  foreach (dy in -1:1) {
      foreach (dx in -1:1) {
          n += srcimage[dx, dy] > someValue;
      }
  }

Here is the same example, but this time using the **array** form of the foreach loop::

  // Iterate through pixels in a 3x3 neighbourhood
  delta = [-1, 0, 1];
  n = 0;
  foreach (dy in delta) {
      foreach (dx in delta) {
          n += srcimage[dx, dy] > someValue;
      }
  }


while loop
~~~~~~~~~~

A conditional loop which executes the target statement or block while its conditional expression is non-zero.  Examples::

  // code example here

until loop
~~~~~~~~~~

A conditional loop which executes the target statement or block until its conditional expression is non-zero.  Examples::

  // code example here

break and breakif statements
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Jiffle provides the **break** statement to unconditionally exit a loop as well as **breakif** for conditional exit::

  // code example here

Specifying source image position
--------------------------------

.. _relative-pixel-position:

Relative pixel position
~~~~~~~~~~~~~~~~~~~~~~~



Absolute pixel position
~~~~~~~~~~~~~~~~~~~~~~~

Specified band 
~~~~~~~~~~~~~~

Functions
---------

General numeric functions
~~~~~~~~~~~~~~~~~~~~~~~~~

===============  ====================   =====================  =====================  ===========================
Name             Description            Arguments              Returns                Notes
===============  ====================   =====================  =====================  ===========================
``abs(x)``       Absolute value         double value           absolute value of x

``acos(x)``      Arc-cosine             value in range [-1,1]  angle in radians

``asin(x)``      Arc-sine               value in range [-1,1]  angle in radians

``atan(x)``      Arc-tangent            value in range [-1,1]  angle in radians

``cos(x)``       Cosine                 angle in radians       cosine [-1, 1]

``degToRad(x)``  Degrees to radians     angle in radians       angle in degrees

``exp(x)``       Exponential            double value           e to the power x       

``floor(x)``     Floor                  double value           integer part of x
                                                               as a double

``isinf(x)``     Is infinite            double value           1 if x is positive
                                                               or negative infinity;
                                                               0 otherwise

``isnan(x)``     Is NaN                 double value           1 if x is equal to     
                                                               Java's Double.NaN;
                                                               0 otherwise

``isnull(x)``    Is null                double value           1 if x is null;        Equivalent to isnan(x)
                                                               0 otherwise

``log(x)``       Natural logarithm      positive value         logarithm to base e

``log(x, b)``    General logarithm      x: positive value;     logarithm to base b
                                        b: base
                                    
``radToDeg(x)``  Radians to degrees     angle in radians       angle in degrees

``rand(x)``      Pseudo-random number   double value           value in range [0, x)  Volatile function

``randInt(x)``   Pseudo-random number   double value           integer part of value  Equivalent to ``floor(rand(x))``
                                                               in range [0, x)
                                                               
``round(x)``     Round                  double value           rounded value     

``round(x, n)``  Round to multiple of   x: double value;       value rounded to       E.g. ``round(44.5, 10)``
                 n                      n: whole number        nearest multiple of n  returns 40
                 
``sin(x)``       Sine                   angle in radians       sine [-1, 1]

``sqrt(x)``      Square-root            non-negative value     square-root of x

``tan(x)``       Tangent                angle in radians       double value
===============  ====================   =====================  =====================  ===========================


Logical functions
~~~~~~~~~~~~~~~~~

===================      ====================   =====================  =====================
Name                     Description            Arguments              Returns             
===================      ====================   =====================  =====================
``con(x)``               Conditional            double value           1 if x is non-zero;
                                                                       0 otherwise

``con(x, a)``            Conditional            double values          a if x is non-zero;
                                                                       0 otherwise

``con(x, a, b)``         Conditional            double values          a if x is non-zero;
                                                                       b otherwise

``con(x, a, b, c)``      Conditional            double values          a if x is positive;
                                                                       b if x is zero;
                                                                       c if x is negative

===================      ====================   =====================  =====================

Statistical functions
~~~~~~~~~~~~~~~~~~~~~

================  ====================   =====================  =========================
Name              Description            Arguments              Returns               
================  ====================   =====================  =========================
``max(x, y)``     Maximum                double values          maximum of x and y

``max(ar)``       Maximum                array                  maximum of array values 

``mean(ar)``      Mean                   array                  mean of array values

``min(x, y)``     Minimum                double values          minimum of x and y

``min(ar)``       Minimum                array                  minimum of array values

``median(ar)``    Median                 array                  median of array values

``mode(ar)``      Mode                   array                  mode of array values

``range(ar)``     Range                  array                  range of array values

``sdev(ar)``      Standard deviation     array                  sample standard deviation
                                                                of array values

``sum(ar)``       Sum                    array                  sum of array values

``variance(ar)``  Variance               array                  sample variance of array
                                                                values

================  ====================   =====================  =========================

Processing area functions
~~~~~~~~~~~~~~~~~~~~~~~~~

===============   ================================================
Name              Returns             
===============   ================================================
``height()``      Height of the processing area (pixels)

``width()``       Width of the processing area (pixels)

``size()``        Total size of the processing area (pixels)

``xmin()``        Minimum X ordinate of the processing area

``ymin()``        Minimum Y ordinate of the processing area

``xmax()``        Maximum X ordinate of the processing area

``ymax()``        Maximum Y ordinate of the processing area

``x()``           X ordinate of the current destination pixel

``y()``           Y ordinate of the current destination pixel

===============   ================================================

