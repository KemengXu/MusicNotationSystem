package mid2019.reaction;

import mid2019.I;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import mid2019.music.Glyph;

public class Layer extends ArrayList<I.Show> implements I.Show{
    public String name;
    public static HashMap<String, Layer> byName = new HashMap<>();
    public static Layer ALL = new Layer("ALL");

    @Override
    public String toString() {
        String res = "Layer( ";
        for(I.Show show : this){
            res += show + ", ";
        }
        return res + ")";
    }

    public Layer (String name) {
        this.name = name;
        if (!name.equals("ALL")){
            ALL.add(this);
            byName.put(name, this);
        }
    }
    public void show(Graphics g){
        for(I.Show item : this){ item.show(g); }
//        Glyph.CLEF_G.showAt(g, 8, 100, 100);
    }

    public static void nuke(){ for (I.Show layer:ALL){((Layer)layer).clear();}}  // nuke is nuklere

    public void betterRemove(I.Show show){
        for(int i = 0; i < size(); i ++){
            if(get(i) == show){remove(i); break;}
        }
    }
}
