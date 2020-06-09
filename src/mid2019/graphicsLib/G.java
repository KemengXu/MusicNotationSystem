package mid2019.graphicsLib;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import mid2019.I;
import mid2019.I.TimeOut;
import mid2019.music.Time;

public class G {
  public static Random RANDOM = new Random();

  public static int rnd(int max) {
    return RANDOM.nextInt(max);
  }

  public static Color rndColor() {
    return new Color(rnd(256), rnd(256), rnd(256));
  }

  public static int fontPixels = 0, fontPoints = 100;
  public static int pixelToPointConverter (Graphics g, int pixel){
    if(fontPixels == 0){
      Font oldFont = g.getFont();
      Font f = new Font("serif", Font.PLAIN, fontPoints);
      g.setFont(f);
      FontMetrics fm = g.getFontMetrics();
      fontPixels = fm.getHeight();
      g.setFont(oldFont);
    }
    return pixel * fontPoints / fontPixels;
  }
  public static void fillBackground(Graphics g, Color c) {
    g.setColor(c);
    g.fillRect(0, 0, 5000, 5000);
  }
  public static void fillCircle(Graphics g, int x, int y, int r){
    g.fillOval(x-r, y-r, 2*r, 2*r);
  }
  public static void drawCircle(Graphics g, XY xy, int r){
    g.setColor(Color.WHITE);
    fillCircle(g, xy.x.val+5, xy.y.val-5, r);
    g.setColor(Color.BLACK);
    g.drawOval(xy.x.val-r+5, xy.y.val-r-5, 2*r, 2*r);
  }
  // Vector
  public static class V implements Serializable{
    public static Transform T = new Transform();
    public int x, y;

    // constructor for V
    public V(int x, int y) {
      this.set(x, y);
    }
    public void add(int x, int y) { this.x += x; this.y += y; }
    public void add(V v) { this.x += v.x; this.y += v.y; }
    public void set(int x, int y) { this.x = x; this.y = y; }
    public void set(V v) {set(v.x, v.y);}

    public int tx() {return x * T.n / T.d + T.dx;}
    public int ty() {return y * T.n / T.d + T.dy;}

    public void blend(V v, int k) {set((k * x + v.x) / (k+1), (k * y + v.y) / (k+1));}


    public void setT(V v) {set(v.tx(), v.ty());}

    public static class Transform {
      public int n, d = 1, dx, dy; //numerator, denominator
      public void set(VS oVS, VS nVS) {
        setScale(oVS.size.x, oVS.size.y, nVS.size.x, nVS.size.y);
        dx = setOff(oVS.loc.x, oVS.size.x, nVS.loc.x, nVS.size.x);
        dy = setOff(oVS.loc.y, oVS.size.y, nVS.loc.y, nVS.size.y);
      }
      public void setScale(int oW, int oH, int nW, int nH) {
        n = (nW > nH) ? nW : nH;
        d = (oW > oH) ? oW : oH;
        d = (d == 0) ? 1 : d;
      }
      public int setOff(int oX, int oW, int nX, int nW) {
        return (-oX - oW/2)* n / d + (nX + nW/2);
      }
    }
  }

  // Vector space
  public static class VS implements Serializable{
    public V loc, size;

    // constructor for VS
    public VS(int x, int y, int w, int h) {
      this.loc = new V(x, y);
      this.size = new V(w, h);
    }

    public void fill(Graphics g, Color c) {
      g.setColor(c);
      g.fillRect(loc.x, loc.y, size.x, size.y);
    }

    // return true if (x, y) is in the VS area
    public boolean hit(int x, int y) {
      return x >= loc.x && x <= (loc.x + size.x) && y >= loc.y && y <= (loc.y + size.y);
    }

    public int xL() {return loc.x;}
    public int xH() {return loc.x + size.x;}
    public int xMid() {return (loc.x + loc.x + size.x) / 2;}
    public int yL() {return loc.y;}
    public int yH() {return loc.y + size.y;}
    public int yMid() {return (loc.y + loc.y + size.y) / 2;}

  }

  public static class LoHi implements Serializable{
    public int lo, hi;
    public LoHi(int lo, int hi) {this.lo = lo; this.hi = hi;}
    public void set(int v) {lo = v; hi = v;}
    public void add(int v) {if (v < lo) {lo = v;} if (v > hi) {hi = v;}}
    public int size() {return hi - lo;}
    //TODO
    public int constrain(int v) {if(v < lo) {return lo;} if(v > hi) {return hi;} return v;}
  }

  // bounding box
  public static class BBox implements Serializable{
    public LoHi h, v;  //horizontal and vertical
    public BBox() { h = new LoHi(0,0); v = new LoHi(0, 0);}
    public void set(int x, int y) {h.set(x); v.set(y);}
    public void add(int x, int y) {h.add(x); v.add(y);}
    public void add(V v) {add(v.x, v.y);}
    public void draw(Graphics g) {g.drawRect(h.lo, v.lo, h.size(), v.size());}
    public G.VS getVS() {return new VS(h.lo, v.lo, h.size(), v.size());}
  }

  //polyline
  public static class PL implements Serializable{
    public V[] points;
    public PL(int n) {
      points = new V[n];
      for(int i = 0; i < n; i++) {points[i] = new V(0,0);}
    }
    public int size(){return points.length;}
    public void drawN(Graphics g, int n){
      for(int i = 1; i < n; i++) {g.drawLine(points[i-1].x, points[i-1].y, points[i].x, points[i].y);}
      drawNDot(g, n);
    }
    public void drawNDot(Graphics g, int n) {
      g.setColor(Color.BLUE);
      for(int i = 0; i < n; i++) {
        g.drawOval(points[i].x - 3, points[i].y - 3, 6,6);
      }
    }
    public void draw(Graphics g) {drawN(g, points.length);}

    public void transform() {
      for(int i = 0; i < points.length; i++) {points[i].setT(points[i]);}
    }
  }

  

  public static class MC {

    public int val, start, stop, dx, ddx = 0, time;
    public static List movers = new List();
    public TimeOut timeOut;

    public MC(int start, int stop, int time) {   // coordinate class
      timeOut = null;
      this.start = start;
      val = start;
      set(stop, time);
      movers.add(this);
    }

    public MC(int x) {     // Moving coordinate
      val = x;
      start = x;
      stop = x;
      time = 0;
      dx = 0;
      movers.add(this);
    }

    public void set(int x, int t) {
      start = val;
      stop = x;
      time = t;
      if (time == 0) {
        start = stop;
        val = stop;
      } else {
        dx = (stop - start) / time;
      }
    }

    public static boolean allStopped() {
      boolean res = true;
      for (MC c : movers) {
        if (c.time != 0)
          return false;
      }
      return res;
    }

    public static boolean moveAll() {
      return movers.move();
    }

    public boolean move() {        // return true if stop
      boolean res = false;
      if (time == 0) {
        val = stop;
        res = true;
      } else {
        dx = (stop - val) / time;
        time--;
//        System.out.println("time = "+time+"; timeout = "+ timeOut);
        val += dx;
        if (time == 0) {
//          System.out.println("time " + time + " timeout " + timeOut);
          res = true;
          if (timeOut != null) {
            val = stop;
            timeOut.act();
          }
        }
      }
      return res;
    }

    public static class List extends ArrayList<MC> {

      public int nActive = 0;

      public boolean move() {
        boolean res = true;
        for (MC c : this) { if (!c.move()) { res &= false; } }
        return res;
      }
    }
  }
  public static class XY{
    public MC x, y;
    public XY(int x, int y){
      this.x = new MC(x);
      this.y = new MC(y);
    }
    public void moveTo(int x, int y, int t){ this.x.set(x, t); this.y.set(y, t); }
    public void setTimeOut(TimeOut to){ this.x.timeOut = to; }
    public void drawLine(Graphics g, XY dest){ g.drawLine(this.x.val+5, this.y.val, dest.x.val, dest.y.val);}
  }
  public static boolean FORCE_FIT = false;  // set true to force text width to fit in box
  public static class MBox implements I.Box{
    public MC left = new MC(100);
    public MC right = new MC(200);
    public MC top = new MC(100);
    public MC bot = new MC(200);

    public MBox(int l, int t, int r, int b){
      this.left.set(l, 0);
      this.right.set(r, 0);
      this.top.set(t, 0);
      this.bot.set(b, 0);
    }
    public int left() { return left.val; }
    public int right() { return right.val; }
    public int top() { return top.val; }
    public int bot() { return bot.val; }
    public void drawText(Graphics g, String s) {
      int y1 = top(), y2 = bot();
      Font oldFont = g.getFont();
      int p = G.pixelToPointConverter(g, y2 - y1);
      Font f = new Font("serif", Font.PLAIN, p);
      g.setFont(f);
      FontMetrics fm = g.getFontMetrics();
      int yb = fm.getAscent() + y1;
      int w = fm.stringWidth(s);
      int x = (right() + left())/2 - w/2;
      while(FORCE_FIT && x < left()){
        f = new Font("serif", Font.PLAIN, --p);
        g.setFont(f);
        fm = g.getFontMetrics();
        yb = fm.getAscent() + y1;
        w = fm.stringWidth(s);
        x = (right() + left())/2 - w/2;
      }

      g.drawString(s, x, yb);
      g.setFont(oldFont);
    }

    public void show(Graphics g, Color color){
      g.setColor(color);
      g.fillRect(left(), top(), right() - left(), bot() - top());
      //System.out.println(left() + " " + top() + " " + right() + " " + bot());
    }
  }
  public static class VBox implements I.Box{
    public I.Box parent;
    public int percent;
    public VBox(I.Box parent, int percent){
      this.parent = parent;
      this.percent = percent;
    }

    public int left() {
      return parent.left();
    }
    public int right() {
      return parent.right();
    }
    public int top() { return percent < 0? (100 + percent) * (parent.bot() - parent.top()) / 100 + parent.top() : parent.top(); }
    public int bot() { return percent < 0? parent.bot() : percent * (parent.bot() - parent.top()) / 100 + parent.top(); }
    public void drawText(Graphics g, String s) {
      int y1 = top(), y2 = bot();
      Font oldFont = g.getFont();
      int p = G.pixelToPointConverter(g, y2 - y1);
      Font f = new Font("serif", Font.PLAIN, p);
      g.setFont(f);
      FontMetrics fm = g.getFontMetrics();
      int yb = fm.getAscent() + y1;
      int w = fm.stringWidth(s);
      int x = (right() + left())/2 - w/2;
      while(FORCE_FIT && x < left()){
        f = new Font("serif", Font.PLAIN, --p);
        g.setFont(f);
        fm = g.getFontMetrics();
        yb = fm.getAscent() + y1;
        w = fm.stringWidth(s);
        x = (right() + left())/2 - w/2;
      }
      g.drawString(s, x, yb);
      g.setFont(oldFont);
    }

    public void show(Graphics g, Color color){
      g.setColor(color);
      g.fillRect(left(), top(), right() - left(), bot() - top());
    }
  }
}

