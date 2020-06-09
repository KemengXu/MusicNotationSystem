# MusicNotationSystem
### This is a music notation helper using simple input gestures to draw complicated music notes to the screen. The purpose of this project is to simplify the process of composing. [Instruction page](http://depts.washington.edu/cprogs/BCS/Books/BCS_MidJava.html) Below are some details about all the files.
### files:
 - ```I.java```: a nested interface, containing all the sub-interfaces such as React, Hit and Draw
 - ```UC.java```: contains all public static final constants
 - ```Window.java```:  Window is both a JPanel which contains the paint proc and it is an adaptor for the two types of mouse listeners and the key listener. You can see this as a simple version of Swing. To build a simple windows app you extend Window and override paintComponent() and any of the listening behaviors like mouseClicked() or keyTyped()
 - ```G.java```: contains all of out graphical shapes like polylines, vectors and moving coordinate.
 - ```Ink.java```: keeps a list of ink objects that we can show out to the screen and has a sub-class Ink.Buffer to hold the mouse trace as the user draws.
 - ```Shape.java```: shape trainer and comparator, takes input lines, transform to a standard scale, compare with or add to database.
 - ```Reaction.java```: wrappers for lists of reactions and for the Map that will take us from a given shape to all the reactions that are looking for that particular shape. The Reaction class will eventually have a static method, best, that will give you back the winning reaction, if there was one, that was looking for that particular shape which won the bid.
 - ```Mass.java```: Mass is pretty simple. Most of the work will be done in subclasses that actually implement the show routine and that add all the Reactions that this mass will need.
 - ```Layer.java```: has show routine to draw elements to the screen
 - ```Gesture.java```: gives us the mouse behavior that we will want for gestures (dn, drag, up)
To be continued

