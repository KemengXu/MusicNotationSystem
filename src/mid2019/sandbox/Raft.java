package mid2019.sandbox;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.security.PublicKey;
import java.util.ArrayList;
import javax.swing.Timer;
import mid2019.I.TimeOut;
import mid2019.graphicsLib.G;
import mid2019.graphicsLib.G.MBox;
import mid2019.graphicsLib.G.MC;
import mid2019.graphicsLib.G.VBox;
import mid2019.graphicsLib.G.XY;
import mid2019.graphicsLib.Window;
import sandbox.Animation;

public class Raft extends Window implements ActionListener {
  public Timer timer;
  public static MBox msg = new MBox(50, 500, 1050, 550);
  public static MBox cont = new MBox(700, 550, 1000, 580);
  public static String currMsg = "";
  public static int frameNumber = 0;
  public static Dot.List servers = new Dot.List();
  public static Dot.List clients = new Dot.List();
  public static Dot.List messages = new Dot.List();
  public static Color SERVER_COLOR = new Color(91, 223, 235);
  public static Color CLIENT_COLOR = new Color(103, 194, 89);
  public static Color[] SERVER_COLORS = new Color[]{SERVER_COLOR, Color.RED, Color.BLACK};
  public static final int FOLLOWER = 0, CANDIDATE = 1, LEADER = 2;
  static {
    servers.fillList(Dot.SERVER, 5, SERVER_COLOR);
    clients.fillList(Dot.SERVER, 2, CLIENT_COLOR);
    messages.fillList(Dot.MSG, 8, Color.BLACK);
  }
  public Raft() {
    super("Raft", 1300, 900);
    timer = new Timer(30, this);
//        timer.setDelay(1000);
    timer.start();
  }
  public void showMessage(Graphics g, String message){
    g.setColor(Color.BLACK);
    msg.drawText(g, message);
    g.setColor(Color.pink);
    cont.drawText(g, "continue");
  }
  public void paintComponent(Graphics g){
    G.fillBackground(g, Color.white);
    showMessage(g, currMsg);
    messages.show(g);
    servers.show(g);
    clients.show(g);
  }
  public static int zoomTo = 28;
  public void mousePressed(MouseEvent me){
    for (int i = 0; i < zoomTo; i ++){script(); frameNumber++; }
    zoomTo = -1;
    script();
    frameNumber++;
    repaint();
  }

  public void script(){
    switch (frameNumber) {
      case 1: servers.visCount(0); currMsg = "Raft; Understandable Distributed Consensus";
      break; case 2: currMsg = "So What is Distributed Consensus?; Let's start with an example...";
      break; case 3:
        servers.visCount(1); servers.get(0).moveTo(500, 300, 10).radius.set(25, 30);
        currMsg = "Let's say we have a single node system";
      break; case 4: servers.get(0).radius.set(200, 30);currMsg = "You can think of our node as a server that stores a single value.";
      break; case 5: servers.get(0).text = "X";
      break;case 6:
        servers.get(0).moveTo(600, 300, 10).radius.set(Dot.SERVER, 30);
        clients.visCount(1);
        clients.get(0).moveTo(400, 300, 10);
        currMsg = "We also have a client that can send a value to the server.";
      break;case 7:
        clients.get(0).text = "8";
        messages.visCount(1);
        messages.get(0).moveTo(400, 300, 0).moveTo(600, 300, 60);
        TimeOut timeOut = new TimeOut() {
          @Override
          public void act() {
            servers.get(0).text = "8";
          }
        };
        messages.get(0).setTimeOut(timeOut);

      break;case 8: currMsg = "Coming to agreement, or consensus, on that value is easy with one node.";
      break;case 9:
        currMsg = "But how do we come to consensus if we have multiple nodes?";
        servers.visCount(3);
        servers.get(1).moveTo(600, 400, 10);
        servers.get(2).moveTo(800, 400, 10);
        servers.get(0).moveTo(700, 200, 10);
        messages.visCount(0);

      break;case 10: currMsg = "That's the problem of distributed consensus.";
      break;case 11:
        currMsg = "Raft is a protocol for implementing distributed consensus.";
        servers.moveAll(-100, 0, 10);
        clients.visCount(0);
        servers.get(0).text = "";
      break; case 12:
          currMsg = "Let's look at a high level overview of how it works.";
      break; case 13:
          currMsg = "A node can be in 1 of 3 states:";
          servers.visCount(1);
          servers.get(0).radius.set(200, 10);
      break; case 14:
          currMsg = "The follower state";
      break; case 15:
          currMsg = "The candidate state";
          servers.get(0).state = CANDIDATE;
      break; case 16:
          currMsg = "The leader state";
          servers.get(0).state = LEADER;
      break; case 17:
          currMsg = "All our nodes start in the follower state.";
          servers.get(0).state = FOLLOWER;
          servers.visCount(3);
          servers.get(0).radius.set(Dot.SERVER, 10);
      break; case 18:
          currMsg = "If followers don't hear from a leader then they can become a candidate.";
          servers.get(0).state = CANDIDATE;
      break; case 19:
          currMsg = "The candidate then requests votes from other nodes.";
          messages.visCount(2);
          servers.msgFromListToServer(0, 0, 1, 30);
          servers.msgFromListToServer(1, 0, 2, 30);
      break; case 20:
          currMsg = "Nodes will reply with their vote.";
          servers.msgFromListToServer(0, 1, 0, 30);
          servers.msgFromListToServer(1, 2, 0, 30);
          timeOut = new TimeOut() {
            @Override
            public void act() {
              servers.get(0).state = LEADER;
            }
          };
          messages.get(0).setTimeOut(timeOut);
      break; case 21:
          currMsg = "The candidate becomes the leader if it gets votes from a majority of nodes.";
      break; case 22:
          currMsg = "This process is called Leader Election.";
      break; case 23:
          currMsg = "All changes to the system now go through the leader.";
      break; case 24:
          currMsg = "All changes to the system now go through the leader.";
          clients.visCount(1);
          servers.moveAll(100, 0, 10);
          clients.get(0).text = "5";
          messages.visCount(1);
          clients.msgFromListToServer(0, 0, 0, 20);
          servers.get(0).sideText = "SET 5";
      break; case 25:
          currMsg = "Each change is added as an entry in the node's log.";
      break; case 26:
          currMsg = "This log entry is currently uncommitted so it won't update the node's value.";
      break; case 27:
          currMsg = "To commit the entry the node first replicates it to the follower nodes...";
          messages.visCount(2);
          servers.msgFromListToServer(0, 0, 1, 20);
          servers.msgFromListToServer(1, 0, 2, 20);
          timeOut = new
              TimeOut() {
                @Override
                public void act() {
                  servers.get(1).sideText = "SET 5";
                  servers.get(2).sideText = "SET 5";
                }
              };
          messages.get(0).setTimeOut(timeOut);

      break; case 28:
          currMsg = "then the leader waits until a majority of nodes have written the entry.";
          servers.msgFromListToServer(0, 1, 0, 20);
          servers.msgFromListToServer(1, 2, 0, 20);
          timeOut = new
              TimeOut() {
                @Override
                public void act() {
                  s(0).committed = true;
                  s(0).sideText = "SET 5";
                }
              };
          messages.get(0).setTimeOut(timeOut);
      break; case 29:
          currMsg = "The entry is now committed on the leader node and the node state is \"5\".";
      break; case 30:
          currMsg = "The leader always notifies the followers of His commitment level.";
          servers.msgFromListToServer(0, 0, 1, 20);
          servers.msgFromListToServer(1, 0, 2, 20);
          timeOut = new TimeOut() {
            @Override
            public void act() {
              s(1).committed = true;
              s(1).text = "5";
              s(2).committed = true;
            }
          };
          messages.get(0).setTimeOut(timeOut);
      break;default: frameNumber = 0;break;
    }
  }
  public static Dot s(int n){return servers.get(n);}
  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    MC.movers.move();
//    if(G.MC.allStopped()){ newLocation(); }
    repaint();
  }
  public static class Dot{
    public XY loc = new XY(0, 0);
    public MC radius = new G.MC(0);
    public Color color;
    public boolean visible = true;
    public String text = "";
    public String sideText = "";
    public int state = 0; // 0:follower state, 1: candidate state, 2: leader state
    public static final int SERVER = 50, MSG = 10;
    public boolean committed = false;

    public Dot(int x, int y, int radius) {
      loc.moveTo(x, y ,0);
      this.radius.set(radius, 0);
    }

    public void setTimeOut(TimeOut to){loc.setTimeOut(to);}
    public void show(Graphics g){
      if(visible) {
        if(color == SERVER_COLOR){
          g.setColor(SERVER_COLORS[state]);
          G.fillCircle(g, loc.x.val, loc.y.val, radius.val);
          g.setColor(SERVER_COLOR);
          G.fillCircle(g, loc.x.val, loc.y.val, radius.val-3);
        } else {
          g.setColor(color);
          G.fillCircle(g, loc.x.val, loc.y.val, radius.val);
        }
        g.setColor(Color.WHITE);
        g.drawString(text, loc.x.val, loc.y.val);
        if(sideText != ""){
          g.setColor(committed? Color.BLACK: Color.RED);
          int xText = loc.x.val + 50;
          int yText = loc.y.val + 50;
          g.drawString(sideText, xText, yText);
          g.drawRect(xText - 10, yText - 15, 50, 20);
        }
      }
    }
    public Dot moveTo(int x, int y, int t){loc.moveTo(x, y, t); return this;}
    //---------------------------------------Dot.List------------------------------------------
    public static class List extends ArrayList<Dot>{
      public void fillList(int size, int nDots, Color color){
        for(int i = 0; i < nDots; i++){
          Dot d = new Dot(100+G.rnd(800), 100 + G.rnd(500), size);
          d.color = color;
          this.add(d);
        }
        this.visCount(0);
      }
      public void visCount(int count){
        for(int i = 0; i < this.size(); i ++){ this.get(i).visible = i<count; }
      }
      public void show(Graphics g){
        for(Dot d : this){ d.show(g); }
      }
      public void moveAll(int x, int y, int t) {
        for (Dot d: this) { d.moveTo(d.loc.x.val + x, d.loc.y.val + y, t); }
      }
      public void msgFromListToServer(int iMsg, int iFrom, int iTo, int time){
        Dot m = messages.get(iMsg);
        Dot f = this.get(iFrom);
        Dot t = servers.get(iTo);
        m.moveTo(f.loc.x.stop, f.loc.y.stop, 0);
        m.moveTo(t.loc.x.stop, t.loc.y.stop, time);
      }
    }

  }
}
