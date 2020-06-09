# MusicNotationSystem
#### This is a music notation helper using simple input gestures to draw complicated music notes to the screen. The purpose of this project is to simplify the process of composing. [Instruction page](http://depts.washington.edu/cprogs/BCS/Books/BCS_MidJava.html). Below are usage and some details about all the files.
### Usage:
1. global gesture: ```N-N```(an upward vertical line): undo
2. on a page: 
  - ```E-W```(a rightward and then leftward horizontal line): adding a staff to the page
  - ```E-E```(a rightward horizontal line): adding a system to the page
3. on a staff:
  - ```S-S```(a downward vertical line): draw a bar line across the system if there are multiple staffs in the system, else just in the current staff.
  - ```SW-SW```(a -125 degree directed line): draw a head at that position on the staff
  - ```E-S```(a rightward and then downward line): add an eighth rest 
  - ```W-S```(a rightward and then downward line): add a quarter rest 
  - ```DOT```: if the dot is near a repeating line, assign a direction to the repeating line; if near a stem, add a dot right after the stem, it there are already three dots, set to zero dots(just toggle between 0, 1, 2, 3 dots). Same if the dot is near a rest.
4. on a rest:
  - ```E-E```(a rightward horizontal line): increment the rest by one step, for example, eighth to sixteenth(range: whole - 64th)
  - ```W-W```(a leftward horizontal line): decrement the rest by one step, for example, quarter to eighth(range: whole - 64th)
  - ```DOT```: if the dot is near a rest, add a dot right after the rest, it there are already three dots, set to zero dots(just toggle between 0, 1, 2, 3 dots)
5. on a stem: (similar gestures with rest)
  - ```E-E```(a rightward horizontal line): if the line crosses two stems, add a beam to them. If crosses one stem, increment the stem by one step
  - ```W-W```: similar to rest
  - ```DOT```: similar to rest
6. on a barline:
  - ```S-S```(a downward vertical line): If there's already a single barline, make it a double barline. If there's a double barline, make it a repeating line(no directions initially).
  - ```DOT```: if the dot is near a barline(single or double) or a repeating line, make is a directed repeating line. If the dot is to the left, make the repeating line open toward left, vice versa.
### files:
 - ```I.java```: a nested interface, containing all the sub-interfaces such as React, Hit and Draw
 - ```UC.java```: contains all public static final constants
 - ```Window.java```:  Window is both a JPanel which contains the paint proc and it is an adaptor for the two types of mouse listeners and the key listener. You can see this as a simple version of Swing. To build a simple windows app you extend Window and override paintComponent() and any of the listening behaviors like mouseClicked() or keyTyped()
 - ```G.java```: contains all of out graphical shapes like polylines, vectors and moving coordinate, also the vector transforming routines: first translate the original coordinates to the origin, then scale, then translate the origin up to the new coordinates.
 - ```Ink.java```: keeps a list of ink objects that we can show out to the screen and has a sub-class Ink. Buffer to hold the mouse trace as the user draws.
 - ```Shape.java```: shape trainer and comparator, takes input lines, transform to a standard scale, compare with or add to the database.
 - ```Reaction.java```: wrappers for lists of reactions and for the Map that will take us from a given shape to all the reactions that are looking for that particular shape. The Reaction class will eventually have a static method, best, that will give you back the winning reaction, if there was one, that was looking for that particular shape which won the bid.
 - ```Mass.java```: Mass is pretty simple. Most of the work will be done in subclasses that actually implement the show routine and that adds all the Reactions that this mass will need.
 - ```Layer.java```: has show routine to draw elements to the screen
 - ```Gesture.java```: gives us the mouse behavior that we will want for gestures (dn, drag, up)
 - ```Glyph.java```: take care of musical fonts, need to install [sinfonia.ttf](http://depts.washington.edu/cprogs/BCS/) first
 - ```AaMusic.java```: a driver of the whole system, creating layers and listen from users for mouse events
 - ```Time.java```: x value objects to deal with chord(shared x value notes) so that heads will have time property instead of x
 - ```Duration.java```: shared by both Stems and Rests. We would like both of those classes to extend Duration, to inherit its incFlag(), decFlag() and cycleDot()
 - ```Head.java```, ```Stem.java```, ```Flag.java```, ```Rest.java```, ```Staff.java```, ```Page.java```, ```Sys.java```, ```Beam.java```, ```Bar.java```:   
     They all extend Mass and represent corresponding music notations as their names. In each of those classes, I added different reactions to achieve different functions. Details are shown in usage.