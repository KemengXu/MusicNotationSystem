package mid2019.reaction;

import mid2019.I;
import mid2019.UC;
import mid2019.graphicsLib.G;

import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.PanelUI;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;


public class Ink implements I.Show, Serializable{
  public static Buffer BUFFER = new Buffer();
  public static final int normSampleSize = UC.normSampleSize;
  public static G.VS nVS = new G.VS(100,100,200,200);

  public Norm norm;
  public G.VS vs;


  public Ink() {
    norm = new Norm();
    vs = BUFFER.bBox.getVS();
  }

  @Override
  public void show(Graphics g) {norm.drawAt(g, vs); }


  //--------------------Norm-----------------
  public static class Norm extends G.PL implements Serializable{
    public static final int N = UC.normSampleSize, MAX = UC.normCoordMax;
    public static final G.VS NCS = new G.VS(0,0, MAX, MAX); // Normalized coordinate system
    public Norm() {
      super(N);
      BUFFER.subSample(this); // copy ink buffer and copy only N points into this
      G.V.T.set(BUFFER.bBox.getVS(), NCS);
      transform();
    }

    public void blend(Norm n, int nBlend) {
      for(int i = 0; i < N; i++) {
        points[i].blend(n.points[i], nBlend);
      }
    }

    public void drawAt(Graphics g, G.VS vs) {
      G.V.T.set(NCS, vs);
      for(int i = 1; i < N; i++) {g.drawLine(points[i-1].tx(), points[i-1].ty(), points[i].tx(), points[i].ty());}
    }
    public int distance(Norm n) {
      int res = 0;
      for(int i = 0; i < N; i++) {
        int dx = points[i].x - n.points[i].x, dy = points[i].y - n.points[i].y;
        res += dx * dx + dy * dy;
      }
      return res;
    }
  }

  //--------------------Buffer----------------
  public static class Buffer extends G.PL implements I.Show, I.Area, Serializable{
    public int n;
    public G.BBox bBox = new G.BBox();
    public static int MAX = UC.inkBufferMax;
    private Buffer() {super(MAX);}  //make the constructor private to achieve singleton

    public void add(int x, int y) { if (n < MAX) {points[n++].set(x,y); bBox.add(x, y);} }
    public void clear() {n = 0;}

    public void subSample(G.PL pl) {
      for(int i = 0; i < UC.normSampleSize; i++) {
        pl.points[i].set(this.points[i * (n - 1)/(UC.normSampleSize - 1)]);
      }
    }

    public void show(Graphics g) {drawN(g, n); /*bBox.draw(g);*/}
    public boolean hit(int x, int y) {return true;}
    public void dn(int x, int y) {clear(); bBox.set(x, y); add(x, y);}
    public void drag(int x, int y) {add(x, y);}
    public void up(int x, int y) {}
  }


  //-------------List-----------------
  public static class List extends ArrayList<Ink> implements I.Show, Serializable {

    @Override
    public void show(Graphics g) {
      for(Ink ink : this) { ink.show(g);}
    }
  }
}
