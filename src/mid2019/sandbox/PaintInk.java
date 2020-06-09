package mid2019.sandbox;

import mid2019.UC;
import mid2019.graphicsLib.Window;
import mid2019.graphicsLib.G;
import mid2019.reaction.Shape;
import mid2019.reaction.Ink;

import java.awt.*;
import java.awt.event.MouseEvent;


public class PaintInk extends Window{

  public static Ink.List inkList = new Ink.List();
  public static Shape.Prototype.List pList= new Shape.Prototype.List();
//  static { inkList.add(new Ink()); } //STUB

  public PaintInk() {
    super("PaintInk", UC.mainWindowWidth, UC.mainWindowHeight);
  }

  @Override
  protected void paintComponent(Graphics g) {
    G.fillBackground(g, Color.white);
    g.setColor(Color.RED); Ink.BUFFER.show(g);
    g.setColor(Color.BLACK); inkList.show(g);
    g.drawString("point:" + Ink.BUFFER.n, 600, 30);
    if(inkList.size() > 1) {
      int last = inkList.size() - 1;
      int d = inkList.get(last).norm.distance(inkList.get(last - 1).norm);
      g.setColor(d > UC.noMatchDistance ? Color.RED : Color.BLACK);
      g.drawString("distance: " + d, 600, 60);
    }
    pList.show(g);
  }


  @Override
  public void mousePressed(MouseEvent me) { Ink.BUFFER.dn(me.getX(), me.getY()); repaint();}

  @Override
  public void mouseDragged(MouseEvent me) { Ink.BUFFER.drag(me.getX(), me.getY()); repaint();}

  @Override
  public void mouseReleased(MouseEvent me) {
//    Ink.BUFFER.up(me.getX(), me.getY());
//    inkList.add(new Ink());
    Ink ink = new Ink();
    inkList.add(ink);
    Shape.Prototype proto;
    if(pList.bestDistance(ink.norm) < UC.noMatchDistance) {
      proto = Shape.Prototype.List.bestMatch;
      proto.blend(ink.norm);
    } else {
      proto = new Shape.Prototype();
      pList.add(proto);
    }

    ink.norm = proto; // proto is a Norm
    repaint();
  }
}
