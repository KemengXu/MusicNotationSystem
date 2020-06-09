package mid2019.music;

import java.util.ArrayList;
import java.util.List;
import mid2019.UC;

public class Time {
  public int x;
  public Head.List heads = new Head.List();
  private Time(Sys sys, int x) {
    this.x = x;
    sys.times.add(this);
  }
//  public void stemHeads (Staff staff, boolean up, int y1, int y2){
//      Stem s = new Stem(staff, up);
//      System.out.println("Layer after add stem: "+ AaMusic.NOTE.size());
//      System.out.println("head size: "+ this.heads.size());
//      for (Head h : heads){
//          int y = h.y();
//          System.out.println("y y1 y2: " + y + y1 + y2);
//          if(y > y1 && y < y2){h.joinStem(s);}
//      }
//      if(s.heads.size() == 0){
//          System.out.println("empty head list after stemming");
//      }else{
//          s.setWrongSides();
//      }
//      s.staff.sys.stems.addStem(s);
//  }
  public void unstemHeads(int y1, int y2){
      System.out.println("unstem head size: " + this.heads.size());
      for (Head h : heads){
          int y = h.y();
          if(y > y1 && y < y2){h.unstem();}
      }
  }


  // System will keep track of list of time
  // --------------- TIME LIST ---------------------
  public static class List extends ArrayList<Time> {
    public Sys sys;
    public List(Sys sys) { this.sys = sys; }

    // get time close to the x coordinate of note
    public Time getTime(int x) {
      if(this.size() == 0) {
        return new Time(this.sys, x);
      }
      Time t = getClosestToTime(x);
      return (Math.abs(x - t.x) < UC.snapTime) ?  t : new Time(this.sys, x);
    }

    public Time getClosestToTime(int x) {
        Time res = get(0);
        int bestSoFar = Math.abs(x - res.x);
        for (Time t : this) {
            int dis = Math.abs(x - t.x);
            if (dis < bestSoFar) {
                res = t;
                bestSoFar = dis;
            }
        }
        return res;
    }
  }

}
