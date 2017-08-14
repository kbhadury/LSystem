# LSystem


![Header][header]

[header]: https://raw.githubusercontent.com/kbhadury/LSystem/master/Screenshots/Header.PNG

This program parses basic L-systems and draws their output using the gpdraw library.  See the ["How to"](https://github.com/kbhadury/LSystem/blob/master/README.md#how-to-use-this-program) section for instructions on how to use it.  See the ["Gallery"](https://github.com/kbhadury/LSystem/blob/master/README.md#gallery) section below for screenshots.

Latest update: now supports stochastic L-systems, in which multiple rules can be associated with one variable and one is chosen at random at each step.  Added extra instructions to change pen attributes.

Check out the 3D version made with OpenGL!  [Project Hilbert](https://github.com/kbhadury/Hilbert)

### What are L-systems?
An L-system (<https://en.wikipedia.org/wiki/L-system>) is a type of language with rules for rewriting itself.  Each system has an "alphabet" with variables and constants, an "axiom" (the starting point of the system), and a set of rules which modify the system.  

For example, consider a system where the variable 'F' means move forward, the constant '+' means turn right, and the constant '-' means turn left.  The axiom is simply F, and the rule is F=F+F−F−F+F.
* Level 1: F
* Level 2: F+F−F−F+F (variable F is replaced in each occurrence of Level 1 according to the rule)
* Level 3: F+F−F−F+F + F+F−F−F+F − F+F−F−F+F − F+F−F−F+F + F+F−F−F+F (F is replaced in each occurrence of Level 2 according to the rule).  
See the Wikipedia page for the associated images

The self-repeating nature of L-systems makes them great for generating fractals.  You can find instructions below on how to make some of the more popular ones.

### How to use this program
The program will prompt you for all the information it needs in order to create the system:
* Variables: every variable must have an associated rule (note that X=X is an acceptable rule but acts like a constant)
* Constants: constants are not associated with rules.  These are instructions like + (turn right) and F (move forward)
  * If no alphabetic characters are used as constants, the variables will automatically cause the pen to move forward
* Start pattern: any string using variables and constants
* Rules: comma-separated rules for each variable.  Rules start with a variable followed by '=' and a string of variables and constants
  * Example: B=BB, A=B[-A]+A
* You can generate stochastic L-systems by providing multiple rules for each variable.  At each step one will be chosen at random.
  * Example: Given variable F, we provide rules F=F[+F]F[-F]F, F=F[+F]F, F=F[-F]F
* Turning angle: used by +/- to turn right/left this many degrees
* Size: determines how far the pen moves (in pixels) on a "forward" instruction.  15 is usually a good number
* Recursion level: how many times to use the rules to replace variables (see the example above).  The program limits itself to 10

These are the constants the program currently accepts:
* + : turn right by turning angle
* - : turn left by turning angle
* F : move forward by length
* M : move forward by length without drawing
* [ : push pen info to the stack
* ] : pop pen info from the stack
* ' : decrease pen width
* " : increase pen width
* ~ : multiply length by 2
* \` : divide length by 2
* \* : change pen color (default is to change every time it moves forward)

### Plans
I might try to make a GUI version of this program so it isn't dependent on the gpdraw library.

### Gallery
Dragon curve:

![Dragon curve](https://github.com/kbhadury/LSystem/blob/master/Screenshots/DragonCurve.PNG)

Sierpinski carpet:

![Sierpinski carpet](https://github.com/kbhadury/LSystem/blob/master/Screenshots/Carpet.PNG)

Snowflake:

![Snowflake](https://github.com/kbhadury/LSystem/blob/master/Screenshots/Snowflake.PNG)

Fractal plants:

![Plant](https://github.com/kbhadury/LSystem/blob/master/Screenshots/Plant1.PNG)

Stochasitc fractal plants:

![Stochastic plant](https://github.com/kbhadury/LSystem/blob/master/Screenshots/Plant2.PNG)

### Sample L-systems
Here are some fun L-systems to try out!
Recommended max recursion level is next to each name

#### Pythagoras tree (7)
* Variables: A B
* Constants: + - [ ]
* Start: A
* Rules: B=BB, A=B[-A]+A
* Angle: 45

#### Sierpinski triangle (hex) (7)
* Variables: A B
* Constants: + -
* Start: A
* Rules: A=+B-A-B+, B=-A+B+A-
* Angle: 60

#### Sierpinski triangle (6)
* Variables: F G
* Constants: + -
* Start: F-G-G
* Rules: F=F-G+F+G-F, G=GG
* Angle: 120

#### Sierpinski carpet (4)
* Variables: F G
* Constants: + -
* Start: F
* Rules: F=F+F-F-F-G+F+F+F-F, G=GGG
* Angle: 90

#### Sierpinski median curve (8)
* Variables: A B F
* Constants: + -
* Start: A--F--A--F
* Rules: A=+B-F-B+, B=-A+F+A-, F=F (force varDraw)
* Angle: 45

#### Dragon curve (10)
* Variables: X Y
* Constants: F + -
* Start: FX
* Rules: X=X+YF+, Y=-FX-Y
* Angle: 90

#### Hilbert curve (5)
* Variables: A B
* Constants: F + -
* Start: A
* Rules: A=+BF-AFA-FB+, B=-AF+BFB+FA-
* Angle: 90

#### Snowflake (4)
* Variables: F
* Constants: + -
* Start: F-F-F-F-F
* Rules: F=F-F++F+F-F-F
* Angle: 72

#### Plant 1 (6)
* Variables: X F
* Constants: + - [ ]
* Start: X
* Rules: X=F-[[X]+X]+F[+FX]-X, F=FF
* Angle: 25

#### Plant 2 (7)
* Variables: X F
* Constants: + - [ ]
* Start: X
* Rules: X=F[+''X]F[-''X]+X, F=FF
* Angle: 20

#### Stochastic plant 1 (4)
* Variables: F
* Constants: + - [ ]
* Start: F
* Rules: F=F[+\`F]F[-\`F]F, F=F[+\`F]F, F=F[-\`F]F
* Angle: 30

#### Stochastic plant 2 (4)
* Variables: X F
* Constants: + - [ ]
* Start: X
* Rules: X=F-[[X]+X]+F[+FX]-X, F=FF, X=F[+X]F[-X]+X
* Angle: 25
