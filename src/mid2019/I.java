package mid2019;

import mid2019.reaction.Gesture;

import java.awt.*;

public interface I {
  interface Hit { boolean hit(int x, int y);}
  interface Show{ void show(Graphics g);}
  // Area is a rectangular area on the screen
  interface Area extends Hit{
    void dn(int x, int y);
    void drag(int x, int y);
    void up(int x, int y);
  }
  interface Act{
    void act(Gesture g);           //gesture is a shape and a box
  }
  interface React extends Act{
    int bid(Gesture g);              // things in react bid for the input gesture
  }
  interface Box{
    int left();
    int right();
    int top();
    int bot();
    void drawText(Graphics g, String s);
  }
  interface TimeOut{void act();}
}
