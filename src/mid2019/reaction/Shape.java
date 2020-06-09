package mid2019.reaction;

import mid2019.I;
import mid2019.UC;
import mid2019.graphicsLib.G;
import mid2019.graphicsLib.Window;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;


public class Shape implements Serializable{
  public static String dot = "DOT";
  public String name;
  public Prototype.List prototypes;
  public static HashMap<String, Shape> DB = loadShapeDB();
  public static Collection<Shape> LIST = DB.values();
  public static Shape DOT = DB.get(dot);

  public static HashMap<String, Shape> loadShapeDB() {
    HashMap<String, Shape> result = new HashMap<>();
    result.put(dot, new Shape(dot));
    try{
      System.out.println("attempting DB load...");
      ObjectInputStream ois = new ObjectInputStream(new FileInputStream(UC.fileName));
      result = (HashMap<String, Shape>) ois.readObject();
      System.out.println("successful load - " + result.keySet());
      ois.close();
    } catch (Exception e) {
      System.out.println("load fail.");
      System.out.println(e);
    }
    return result;
  }

  public static void saveShapeDB() {
    try {
      ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(UC.fileName));
      oos.writeObject(DB);
      System.out.println("saved DB.");
      oos.close();
    } catch (Exception e) {
      System.out.println("save fail.");
      System.out.println(e);
    }
  }

  public Shape(String name) {
    this.name = name;
    prototypes = new Prototype.List();
  }

  public static Shape recognize(Ink ink) {
    if(ink.vs.size.x < UC.dotThreshold && ink.vs.size.y < UC.dotThreshold) {
      return DOT;
    }
    Shape bestMatch = null;
    int bestSoFar = UC.noMatchDistance;
    for(Shape shape : LIST) {
      int d = shape.prototypes.bestDistance(ink.norm);
      if(d < bestSoFar) { bestMatch = shape; bestSoFar = d; }
    }
    return bestMatch;
  }

  //----------------Prototype-------------
  public static class Prototype extends Ink.Norm implements Serializable{
    int nBlend = 1;

    public void blend(Ink.Norm norm) {blend(norm, nBlend++);}

    //------------List----------------
    public static class List extends ArrayList<Prototype> implements I.Show, Serializable{
      public static Prototype bestMatch;
      public static int m = 10, w = 60;
      public static G.VS showBox = new G.VS(m, m, w, w);

      public int bestDistance(Ink.Norm norm) { // Notice bestMatch can be null
        bestMatch = null;
        int bestSoFar = UC.noMatchDistance;
        for(Prototype p : this) {
          int d = p.distance(norm);
          if(d < bestSoFar) {bestMatch = p; bestSoFar = d;}
        }
        return bestSoFar;
      }

      public void show(Graphics g) {
        g.setColor(Color.ORANGE);
        for(int i = 0; i < size(); i++) {
          Prototype p = get(i);
          int x = m + i * (m + w);
          showBox.loc.set(x, m);
          p.drawAt(g, showBox);
          g.drawString("" + p.nBlend, x, 20);
        }
      }
    }
  }


  //---------------SHAPETRAINER-----------------
  public static class Trainer extends Window {
    public static String UNKNOWN = "<= This name currently unknown";
    public static String ILLEGAL = "<= This name is not a legal shape name";
    public static String KNOWN = "<= This is a known shape";
    public static String name = "";
    public static String state = UNKNOWN;
    public static Prototype.List pList = new Prototype.List();

    public Trainer() {
      super("Shape Trainer", UC.mainWindowWidth, UC.mainWindowHeight);
    }

    public void paintComponent(Graphics g) {
      G.fillBackground(g, Color.white);
      g.setColor(Color.RED);
      //g.fillRect(100,100,100,100);
      g.drawString(name + state, 600, 30);
      if(pList != null) {pList.show(g);}
      Ink.BUFFER.show(g);
    }

    public void mousePressed(MouseEvent e) { Ink.BUFFER.dn(e.getX(), e.getY()); repaint(); }
    public void mouseDragged(MouseEvent e) { Ink.BUFFER.drag(e.getX(), e.getY()); repaint(); }
    public void mouseReleased(MouseEvent e) {
      if(state != ILLEGAL) {
        Ink ink = new Ink();
        Shape.Prototype proto;
        if(pList == null) {
          Shape s = new Shape(name);
          Shape.DB.put(name, s);
          pList = s.prototypes;
        }
        if(pList.bestDistance(ink.norm) < UC.noMatchDistance) {
          proto = Prototype.List.bestMatch;
          proto.blend(ink.norm);
        } else {
          proto = new Shape.Prototype();
          pList.add(proto);
        }
        setState();
      }
    }

    public void keyTyped(KeyEvent e) {
      char c = e.getKeyChar();
      System.out.println(c);
      name = (c == ' ' || c == 0x0D || c == 0x0A) ? "" : name + c;
      if(c == 0x0D || c == 0x0A) {saveShapeDB();}
      setState();
      repaint();
    }

    public void setState() {
      state = (name.equals("") || name.equals("DOT")) ? ILLEGAL : UNKNOWN;
      if(state == UNKNOWN) {
        if(Shape.DB.containsKey(name)) {
          state = KNOWN;
          pList = Shape.DB.get(name).prototypes;
        } else {
          pList = null;
        }
      }
    }
  }
}
