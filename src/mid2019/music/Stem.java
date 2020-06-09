package mid2019.music;

import mid2019.UC;
import mid2019.reaction.Gesture;
import mid2019.reaction.Reaction;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Stem extends Duration implements Comparable<Stem>{
    public Staff staff;
    public Head.List heads = new Head.List();
    public boolean isUp = true;
    public int id;
    public static int ID = 3;
    public Beam beam = null;       // default stem has no beam

    public int compareTo(Stem s){return this.x() - s.x();}

    public String toString(){
        String res = "stem: " + id + "-------- ";
        for (Head h : heads){
            res += h.line + " ";
        }
        return res;
    }
    public Stem(Staff staff, Head.List heads, boolean up){
        this.staff = staff;
        // staff.sys.stems.addStem(this);
        this.isUp = up;
        for(Head h : heads){
            h.unstem();
            h.stem = this;
        }
        this.heads = heads;
        this.id = ID++;
        staff.sys.stems.addStem(this);
        setWrongSides();
        addReaction(new Reaction("E-E") {
            @Override
            public int bid(Gesture g) {
                int y = g.vs.yMid(), x1 = g.vs.xL(), x2 = g.vs.xH();
                int xs = Stem.this.heads.get(0).time.x;
                if(x1 > xs || x2 < xs){return UC.nobid;}
                int y1 = Stem.this.yLow(), y2 = Stem.this.yHigh();
                if(y < y1 || y > y2){return  UC.nobid;}
                return Math.abs(y - (y1 + y2)/2) + 60;   // bias because of sys(beam) E-E
            }

            @Override
            public void act(Gesture g) {
                Stem.this.incFlag();
            }
        });
        addReaction(new Reaction("W-W") {
            @Override
            public int bid(Gesture g) {
                int y = g.vs.yMid(), x1 = g.vs.xL(), x2 = g.vs.xH();
                int xs = Stem.this.heads.get(0).time.x;
                if(x1 > xs || x2 < xs){return UC.nobid;}
                int y1 = Stem.this.yLow(), y2 = Stem.this.yHigh();
                if(y < y1 || y > y2){return  UC.nobid;}
                return Math.abs(y - (y1 + y2)/2);
            }

            @Override
            public void act(Gesture g) {
                Stem.this.decFlag();
            }
        });
    }
    public static Stem getStem(Staff staff, Time time, int y1, int y2, boolean isUp){
        Head.List heads = new Head.List();
        for (Head h : time.heads){
            int yH = h.y();
            if(yH > y1 && yH< y2){heads.add(h);}
        }
        if (heads.size() == 0){return null;}
        Beam b = internalStem(staff.sys, time.x, y1, y2);
        Stem res = new Stem(staff, heads, isUp);
        if(b != null){b.addStem(res); res.nFlag = 1;}
        return res;
    }
    private static Beam internalStem(Sys sys, int x, int y1, int y2){
        for(Stem s : sys.stems){
            if(s.beam != null && s.x() < x && s.yLow() < y2 && s.yHigh() > y1){
                int bX = s.beam.first().x(), bY = s.beam.first().yBeamEnd(); // beginning
                int eX = s.beam.last().x(), eY = s.beam.last().yBeamEnd(); // ending
                if(Beam.verticalLineCrossesSegment(x, y1, y2, bX, bY, eX, eY)){return s.beam;}
            }
        }
        return null;
    }
    public void show(Graphics g){
        System.out.println("show stem: " + this);
        if(nFlag >= -1 && heads.size() > 0){
            int x = this.x(), h = staff.H(), yh = yFirstHead(), yb = yBeamEnd();
            g.drawLine(x, yh, x, yb);
            if(nFlag > 0 && beam != null){
                if(nFlag == 1){(isUp? Glyph.FLAG1D: Glyph.FLAG1U).showAt(g, h, x, yb);}
                if(nFlag == 2){(isUp? Glyph.FLAG2D: Glyph.FLAG2U).showAt(g, h, x, yb);}
                if(nFlag == 3){(isUp? Glyph.FLAG3D: Glyph.FLAG3U).showAt(g, h, x, yb);}
                if(nFlag == 4){(isUp? Glyph.FLAG4D: Glyph.FLAG4U).showAt(g, h, x, yb);}
            }
        }
    }

    public void deleteStem(){
        deleteMass();
        staff.sys.stems.remove(this);
        // System.out.println("deleting stem: " + this + " Layers: " + AaMusic.NOTE.size());
    }
    public void setWrongSides(){
        Collections.sort(heads);
        int i, last, next;
        if(isUp){
            i = heads.size() - 1;
            last = 0;
            next = -1;
        }else{
            i = 0;
            last = heads.size() - 1;
            next = 1;
        }
        Head prevH = heads.get(i);
        prevH.wrongSide = false;
        while(i != last){
            i += next;
            Head nextH = heads.get(i);
            nextH.wrongSide = (prevH.staff == nextH.staff && Math.abs(nextH.line - prevH.line) <= 1) && !prevH.wrongSide;
            prevH = nextH;
        }
    }

    public int yFirstHead(){
        Head h = firstHead();
        return h.staff.yLine(h.line);
    }
    public Head firstHead(){ return heads.get(isUp? heads.size()-1 : 0); }
    public Head lastHead(){return heads.get(isUp? 0: heads.size() - 1);}
    public int x(){Head h = firstHead(); return h.time.x + (isUp? h.w():0);}
    public int yBeamEnd(){  // based on music theory requirement
        Head h = lastHead();
        int line = h.line;
        line += isUp? -7 : 7;
        int flagInc = (nFlag > 2)? 2* (nFlag - 2): 0;
        line += isUp? -flagInc : flagInc;
        if((isUp && line > 4) || (!isUp && line < 4)){line = 4; }  // 4 is the center line
        return h.staff.yLine(line);
    }
    public int yLow(){return isUp? yBeamEnd(): yFirstHead();}
    public int yHigh(){return isUp? yFirstHead(): yBeamEnd();}


    // -------------------------LIST--------------------------
    public static class List extends ArrayList<Stem>{
        public int yMin = 1000000, yMax = -1000000;
        public void addStem(Stem s){
            add(s);
            if(s.yLow() < yMin){yMin = s.yLow();}
            if(s.yHigh() > yMax){yMax = s.yHigh();}
        }
        public boolean fastReject(int y1, int y2){
            return (y1 > yMax || y2 < yMin);
        }
        public void sort(){Collections.sort(this);}

        public ArrayList<Stem> allIntersectors(int x1, int y1, int x2, int y2) {
            ArrayList<Stem> res = new ArrayList<>();
            for (Stem s : this){
                int x = s.x(), y = Beam.yOfX(x, x1, y1, x2, y2);
                if(x > x1 && x < x2 && y > s.yLow() && y < s.yHigh()){res.add(s);}
            }
            return res;
        }
    }
}
