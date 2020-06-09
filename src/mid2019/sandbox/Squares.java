package mid2019.sandbox;

import mid2019.graphicsLib.G;
import mid2019.graphicsLib.Window;
import mid2019.UC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;



public class Squares extends Window implements ActionListener{
  public static Timer timer;
  public static G.V pressedLoc = new G.V(0,0);
  public static final int WIDTH = UC.mainWindowHeight;
  public static final int HEIGHT = UC.mainWindowHeight;

  public Squares() {
    super("Squares", WIDTH, HEIGHT);
    timer = new Timer(30, this); // this means Square is the action listener
    timer.setInitialDelay(3000);
    timer.start();
  }

//  public static G.VS theVS = new G.VS(100, 100, 200, 300);
//  public static Color theColor = G.rndColor();
//  public static Square.List theList = new Square.List();

  public static Square theSquare;
  public static boolean dragging;
  public static G.V mouseDelta = new G.V(0, 0);

  @Override
  protected void paintComponent(Graphics g) {
//    theVS.fill(g, theColor);
//    theList.show(g);
    Square.ALL.show(g);
  }

  @Override
  public void mousePressed(MouseEvent me) {
    this.theSquare = Square.ALL.hit(me.getX(), me.getY());
    dragging = (this.theSquare != null) ? true : false;

    if(dragging) {
      mouseDelta.set(me.getX() - theSquare.loc.x, me.getY() - theSquare.loc.y);
      pressedLoc.set(me.getX(), me.getY());
    } else {
      // when calling the Square constructor, will add the Square to the ALL list
      theSquare = new Square(me.getX(), me.getY());
    }

    repaint(); // call paintComponent() routine
  }

  @Override
  public void mouseDragged(MouseEvent me) {
    if(dragging) {
      theSquare.loc.set(me.getX() - mouseDelta.x, me.getY() - mouseDelta.y);
    } else {
      theSquare.resize(me.getX(), me.getY());
    }

    repaint(); // call paintComponent() routine
  }

  @Override
  public void mouseReleased(MouseEvent me) {
    theSquare.dv.set(me.getX() - pressedLoc.x, me.getY() - pressedLoc.y);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    repaint();
  }

  // Square内定义List内部类
  // If want to add an object to the list whenever you create it, consider using this design structure
  public static class Square extends G.VS {
    public Color color = G.rndColor();
    public G.V dv = new G.V(G.rnd(20) - 10, G.rnd(20) - 10);
    public static List ALL = new List();

    public Square(int x, int y) {
      super(x, y, 100, 100);
      ALL.add(this);
    }

    public void resize(int x, int y){
      this.size.x = x - loc.x < 0 ? 0 : x - loc.x;
      this.size.y = y - loc.y < 0 ? 0 : y - loc.y;
    }

    // Square show itself
    public void show(Graphics g) {
      this.fill(g, color);
//      loc.add(dv);
      moveAndBounce();
    }

    public void moveAndBounce() {
      loc.add(dv);
      if(xH() > 1000 && dv.x > 0) { dv.x = - dv.x;}
      if(xL() < 0 && dv.x < 0) {dv.x = - dv.x;}
      if(yH() > 1000 && dv.y > 0) {dv.y = - dv.y;}
      if(yL() < 0 && dv.y < 0) {dv.y = - dv.y;}
    }

    public static class List extends ArrayList<Square> {
      public void show(Graphics g) {
        for (Square s : this) {
          s.show(g);
        }
      }

      public Square hit(int x, int y) {
        // loop from the last one, so we can hit the newest created one.
        for (int i = this.size() - 1; i >= 0; i--) {
          if (get(i).hit(x, y)) {
            return get(i);
          }
        }
        return null;
      }
    }
  }
}
