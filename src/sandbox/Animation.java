package sandbox;

import mid2019.I.TimeOut;
import mid2019.graphicsLib.G;
import mid2019.graphicsLib.G.MBox;
import mid2019.graphicsLib.G.MC;
import mid2019.graphicsLib.G.VBox;
import mid2019.graphicsLib.G.XY;
import mid2019.graphicsLib.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Animation extends Window implements ActionListener {
    public static int w = 100, h = 100;
    public static G.MC x;
    public static G.MC y;
    public static G.MC c;
//    public static G.MC xx;
//    public static G.MC yy;
    public Timer timer;
    public Color color = Color.RED;
    public static MBox mBox;
    public static VBox top;
    public static VBox bot;
    public static Dot dotA = new Dot(100, 100, 50);
    public static Dot dotB = new Dot(400, 300, 50);
    public static Dot msg = new Dot(0,0,8);
    public static TimeOut hitA, hitB, goA, goB;


    public Animation() {
        super("Animation", 1300, 900);
        timer = new Timer(30, this);
//        timer.setDelay(1000);
        timer.start();
        msg.loc.moveTo(dotA.loc.x.val, dotA.loc.y.val, 30);
        x = new G.MC(100, 1100, 60);
        y = new G.MC(100, 700, 80);
        c = new G.MC(0, 255, 100);
        mBox = new MBox(100, 100, 200, 200);
        top = new VBox(mBox, 40);
        bot = new VBox(mBox, -40);
        hitA = new TimeOut() {
            public void act() {
                System.out.println("HitA");
                dotA.loc.moveTo(G.rnd(1400) + 50, G.rnd(900) + 50, 30);
                msg.setTimeOut(goB);
                msg.loc.moveTo(dotA.loc.x.stop, dotA.loc.y.stop, 30);
            }
        };
        hitB = new TimeOut() {
            public void act() {
                dotB.loc.moveTo(G.rnd(1400) + 50, G.rnd(900) + 50, 30);
                msg.setTimeOut(goA);
                msg.loc.moveTo(dotB.loc.x.stop, dotB.loc.y.stop, 30);
            }
        };
        goA = new TimeOut() {
            public void act() {
                msg.loc.moveTo(dotA.loc.x.val, dotA.loc.y.val, 30);
                msg.setTimeOut(hitA);
            }
        };
        goB = new TimeOut() {
            public void act() {
                msg.loc.moveTo(dotB.loc.x.val, dotB.loc.y.val, 30);
                msg.setTimeOut(hitB);
            }
        };
        msg.setTimeOut(hitA);
        //xx = new G.MC(100, 1200, 60);
        //yy = new G.MC(100, 700, 80);
    }
    public void paintComponent(Graphics g){
        G.fillBackground(g, Color.white);
        g.setColor(color);
        g.fillRect(x.val, y.val, w, h);
        Color textColor = new Color(0,0,0, c.val);
        g.setColor(textColor);
        g.drawString("Hello", x.val, y.val);
        //g.drawOval(xx.val, yy.val, 20, 20);
        mBox.show(g, color);
        top.show(g, Color.RED);
        bot.show(g, Color.YELLOW);
        g.setColor(Color.BLACK);
        bot.drawText(g, "Dude!");
        g.setColor(Color.BLUE);
        dotA.show(g);
        g.setColor(Color.GREEN);
        dotB.show(g);
        g.setColor(Color.RED);
        msg.show(g);
//        int y1 = 100, m = 20, y2 = y1 + m;
//        g.setColor(Color.BLACK);
//        g.drawLine(0, y1, 1000, y1);
//        g.drawLine(0, y2, 1000, y2);
//        int p = G.pixelToPointConverter(g, m);
//        Font f = new Font("serif", Font.PLAIN, p);
//        g.setFont(f);
//        FontMetrics fm = g.getFontMetrics();
//        int yb = fm.getAscent() + y1;
//        g.drawString("Hello y", 100, yb);
    }
    public static void newLocation(){
        int t = G.rnd(30) + 10;
        x.set(G.rnd(1000) + 50, t);
        y.set(G.rnd(800) + 50, t);
        t = G.rnd(30) + 10;
//        xx.set(G.rnd(1200) + 50, t);
//        yy.set(G.rnd(800) + 50, t);
        mBox.left.set(G.rnd(1000) + 50, t);
        mBox.right.set(G.rnd(200) + mBox.left.stop, t);
        mBox.top.set(G.rnd(800) + 50, t);
        mBox.bot.set(G.rnd(200) + mBox.top.stop, t);
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        MC.movers.move();
        if(G.MC.allStopped()){ color = Color.GREEN; }
        if(G.MC.allStopped()){ newLocation(); }
        repaint();
    }
    public static class Dot{
        public XY loc = new XY(0, 0);
        public int r;
        public Dot(int x, int y, int r) {
            loc.moveTo(x, y ,0);
            this.r = r;
        }
        public void setTimeOut(TimeOut to){loc.setTimeOut(to);}
        public void show(Graphics g){ G.fillCircle(g, loc.x.val, loc.y.val, r); }
    }
}
