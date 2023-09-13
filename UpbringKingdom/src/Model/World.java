package Model;

import com.badlogic.gdx.math.GridPoint2;
import java.util.ArrayList;
import java.util.List;

public class World {
    
    //creazione della variabile che identifica la mappa
    private TileMap map;
    //creazione della lista dei personaggi presenti nel mondo
    private List<Actor> actors;
    //creazione della lista degli oggetti presenti nel mondo
    private List<WorldObject> objects;
    
    //Costruttore della classe World
    public World(int width, int height){
        //set della grandezza della mappa
        this.map = new TileMap(width, height);
        //inizializzazione delle liste di oggetti e personaggi
        this.actors = new ArrayList<Actor>();
        this.objects = new ArrayList<WorldObject>();
    }
    
    //funzione di aggiunta di un personaggio nel mondo
    public void addActor(Actor a){
        map.getTile(a.getX(), a.getY()).setActor(a);
        actors.add(a);
    }
    
    //funzione di aggiunta di un oggetto nel mondo
    public void addObject(WorldObject o){
        for(GridPoint2 p : o.getTiles()){
            map.getTile(p.x, p.y).setObject(o);
        }
        
        objects.add(o);
    }
    
    public void deleteObject(WorldObject o){
        for(GridPoint2 p : o.getTiles()){
            map.getTile(p.x, p.y).deleteObject();
        }
        
        objects.remove(o);
    }
    
    //funzione che viene chiamata ad ogni frame per aggiornare la posizione degi personaggi e degli oggetti
    public void update(float delta){
        for(Actor a: actors){
            a.update(delta);
        }
        for(WorldObject o: objects){
            o.update(delta);
        }
    }

    public TileMap getMap() {
        return map;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public List<WorldObject> getObjects() {
        return objects;
    }
    
    
}
