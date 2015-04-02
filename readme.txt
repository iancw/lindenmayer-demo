The final project is a 3D implementation of Lindenmayer Systems (L-Systems) for generating tree-like structures with natural characteristics.  These systems are like fractals, but use formal grammars to describe branching patterns.  Those branching patterns are then recursively applied, resulting in similar results as 1/f noise.

Build requirements:
  * A JDK on the system path.  You can download the latest JDK from http://www.oracle.com/technetwork/java/javase/downloads/index.html.

To build:  The jar is built using Java's automated build system "ant."  You will need ant installed on your path to build.  You can obtain and from http://ant.apache.org/bindownload.cgi.  The default target compiles code into a bin2 directory, then puts it all in a jar named 'final.jar.'

To run:
ant

Example 1: 
	java -jar final.jar



Basic Operation:
  Edit settings using the interface, press apply to apply any changed settings and redraw the image.

L-System Control:
To control L-System rendering, use the Recursions, Length, Angle, and L System fields.  Recursion controls the number of times the L System rules are applied.  Errors result when it is 0 or less.  Length defines the beginning length of each segment.  Length is decreased in subsequent recursions.  Angle defines the angle at which elements (+, -, ^, &) modify the angle.

Sample L-Systems:
Paste this text in the L System text box and click apply to try these L-Systems out, or try your own the syntax is:

<start expression>
<string>:replacement string

'[' initiates a branch
']' pops a branch from the stack
'-' turns left
'+' turns right
'^' pitches down
'&' pitches up
'(#####) prefixes a length multiplier to F
'F' moves forward one length (multiplied by any current length multipliers)

Examples:

(from http://en.wikipedia.org/wiki/L-system)
X
X:F-[[X]+X]+F[+FX]-X
F:FF

(from http://www.nbb.cornell.edu/neurobio/land/OldStudentProjects/cs490-94to95/hwchen/)
F
F:[-&F][++&F]F[--&F][+&F]

F
F:[&+F]F[-F][-F][&F]

F
F:[^F]F[^F][F]

F 
F:F[+F]F[-F]F

F 
F:FF-[-F+F+F]+[+F-F-F]

(From http://www.xs4all.nl/~cvdmark/tutor.html)

A
A:[+BFA][-BFA]
B:'(0.7071)

A
A: [+BFA][-BFA][^BFA][&BFA]
B: '(0.7071)

AB
A:[F[+FCA][-FCA]]
B:[F[^FCB][&FCB]]
C:'(0.7071)


Navigation:
Zoom in by decreasing the value of 'h,' zoom out by increasing it.  Clicking and dragging in the canvas causes the camera position to move, holding shift while dragging moves the reference position, and holding alt while dragging moves the light position.  Shift-drag is inverted, and Alt-drag only works on the y axis.  This can be frusturating, but is enough to position items and view significant variation in lighting conditions dynamically.

  Material properties are set using the Ambient, Diffuse, Specular, and Specularity interface elements.  They impact color and illumination.  Each field takes three numbers, representing the values in Red, Green, and Blue channels respectively. 

  Sometimes models show up very small when intially loaded (the scaling isn't consistent between model files).  Decrease h to "zoom."  Often a value of 1 or less is necessary make an object fully fill the screen.
