package util;

import Model.DIRECTION;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import java.util.Map;

//classe contenente tutti i metodi e le mappe per l'animazine di movimento del personaggio tramite le texture
public class AnimationSet {
    //creiamo una mappa che, in base alla direzione restituisce l'insieme delle immagini che compongono l'animazione
    private Map<DIRECTION, Animation> walking;
    //creiamo una mappa che, in base alla direzione restituisce l'immagine del peronaggio rivolto verso quella direzione
    private Map<DIRECTION, TextureRegion> standing;
    
    //costruttore della classe che prende in input il set di tutte quante le immagini delle animazioni dal file texture packed
    public AnimationSet(Animation walkNorth,
            Animation walkSouth,
            Animation walkEast,
            Animation walkWest,
            TextureRegion standNorth,
            TextureRegion standSouth,
            TextureRegion standEast,
            TextureRegion standWest) {
        walking = new HashMap<DIRECTION, Animation>(); //istanzia la mappa walking
        walking.put(DIRECTION.NORTH, walkNorth); //inserisce l'animazione di camminata verso nord
        walking.put(DIRECTION.SOUTH, walkSouth); //inserisce l'animazione di camminata verso sud
        walking.put(DIRECTION.EAST, walkEast); //inserisce l'animazione di camminata verso est
        walking.put(DIRECTION.WEST, walkWest); //inserisce l'animazione di camminata verso ovest
        standing = new HashMap<DIRECTION, TextureRegion>(); //istanzia la mappa standing
        standing.put(DIRECTION.NORTH, standNorth); //inserisce l'animazione di standing verso nord
        standing.put(DIRECTION.SOUTH, standSouth); //inserisce l'animazione di standing verso sud
        standing.put(DIRECTION.EAST, standEast); //inserisce l'animazione di standing verso est
        standing.put(DIRECTION.WEST, standWest); //inserisce l'animazione di standing verso ovest
        
    }
    public AnimationSet(TextureRegion standSouth){
        standing = new HashMap<DIRECTION, TextureRegion>(); //istanzia la mappa standing
        standing.put(DIRECTION.SOUTH, standSouth); //inserisce l'animazione di standing verso sud
    }
    
    public Animation getWalking(DIRECTION dir){
        return walking.get(dir);
    }
    
    public TextureRegion getStanding(DIRECTION dir){
        return standing.get(dir);
    }
}
