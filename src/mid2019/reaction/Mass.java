package mid2019.reaction;

import mid2019.I;
import mid2019.graphicsLib.G;
import org.w3c.dom.ls.LSOutput;

import java.awt.*;

public abstract class Mass extends Reaction.List implements I.Show{
    public Layer layer;
    private int hash = G.rnd(100000000);
    public boolean equals(Object o){return this == o;}
    public int hashCode(){return hash;}
    public Mass (String layerName){
        this.layer = Layer.byName.get(layerName);
        if(layer != null){
            layer.add(this);
        }else{
            System.out.println("Bad layer name: " + layerName);
        }
    }
    public void deleteMass(){
        clearAll();
        System.out.println(this);
        //System.out.println("Layer now : " + layer);
        layer.remove(this);
        // layer.betterRemove(this);
        //System.out.println("Layer now : " + layer);
    }

    // do nothing
    public void show(Graphics g){}
}
