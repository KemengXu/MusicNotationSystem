package mid2019.reaction;

import mid2019.I;
import mid2019.graphicsLib.G;

import java.util.ArrayList;

public class Gesture {
    public Shape shape;
    public G.VS vs;
    public static List UNDO = new List();

    private Gesture(Shape shape, G.VS vs){  //Ink:norm+box, gesture:shape+box, difference is the shape in gesture doesn't care about order
        this.shape = shape;
        this.vs = vs;
    }
    public static Gesture getNew(Ink ink){
        Shape s = Shape.recognize(ink); //could fail
        return (s == null)? null: new Gesture(s, ink.vs);
    }

    public void redoGesture(){
        Reaction r = Reaction.best(this);
        if(r != null){ r.act(this); }
    }

    public void doGesture(){
        Reaction r = Reaction.best(this);
        if(r != null){ UNDO.add(this); r.act(this); }
    }
    public static void undo(){
        if(UNDO.size()>0){
            UNDO.remove(UNDO.size() - 1);
            Layer.nuke();
            Reaction.nuke();
            UNDO.redo();
        }
    }
    public static I.Area AREA = new I.Area(){
        public boolean hit(int x, int y){return true;}
        public void dn(int x, int y){Ink.BUFFER.dn(x, y);}
        public void drag(int x, int y){Ink.BUFFER.drag(x,y);}
        public void up(int x, int y){
            Ink.BUFFER.add(x, y);
            Ink ink = new Ink();
            Ink.BUFFER.clear();
            Gesture g = getNew(ink);
            if (g != null){
                if(g.shape.name.equals("N-N")){
                    undo();
                }else{
                    g.doGesture();
                }
            }
        }
    };
    public static class List extends ArrayList<Gesture>{
        public void redo(){
            for (Gesture g : this){ g.redoGesture(); }
        }
    }
}
