package mid2019.sandbox;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import mid2019.I.TimeOut;
import mid2019.graphicsLib.G;
import mid2019.graphicsLib.G.MC;
import mid2019.graphicsLib.G.XY;
import mid2019.graphicsLib.Window;
import mid2019.sandbox.ShuntYard.BT;
import mid2019.sandbox.ShuntYard.BT.View;
import mid2019.sandbox.ShuntYard.Token;
import mid2019.sandbox.ShuntYard;

public class ShuntYardWindow extends Window implements ActionListener {
  public Timer timer;
  public static BT bt;
  public static BT.View view;
  public static int nTick = 0;
  public static String expr = "( 3 + 4 ) * 5";
  public static Token.List pfList= Token.lex(expr).toPostFix();
  public ShuntYardWindow() {
    super("ShuntYardWindow", 1300, 900);
//    BT bt3 = new BT(Token.getToken("3"));
//    BT bt4 = new BT(Token.getToken("4"));
//    BT bt5 = new BT(Token.getToken("5"));
//    BT btTimes = new BT(Token.getToken("*"), bt4, bt5);
//    bt = new BT(Token.getToken("+"), bt3, btTimes);
    bt = pfList.toBT();
    System.out.println(pfList.eval());
    view = View.getView(bt);

    view.setXInfix(400, 0);
    view.setY(200, 0, 0, 0);

    view.setXInfix(400, 100);
    view.setY(200, 100, 0, 100);

    view.loc.setTimeOut(new TimeOut() {
      @Override
      public void act() {
        view.setXPostfix(400, 100);
        view.setY(200, 100, 0, 100);
      }
    });

    timer = new Timer(300, this);
    timer.start();
  }
  public void paintComponent(Graphics g){
    G.fillBackground(g, Color.PINK);
    view.show(g);
    g.drawString("" + nTick, 100, 100);
    pfList.showUpTo(g, Math.min(nTick, pfList.size()-1));
  }
  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    MC.movers.move();
    nTick ++;
    repaint();
  }
}
