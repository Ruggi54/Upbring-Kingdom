package Model;

//classe che identifica la singola mattonella
public class Tile {
    
    //istanziamo il terreno
    private Terrain terrain;
    
    //istanziamo il personaggio
    private Actor actor;
    
    //istanziamo un oggetto sulla piastrella
    private WorldObject object;
    
    //costruttore della classe
    public Tile(Terrain terrain) {
        this.terrain = terrain; //settiamo il terreno
    }
    
    public Terrain getTerrain() {
        return terrain;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public WorldObject getObject() {
        return object;
    }

    public void setObject(WorldObject object) {
        this.object = object;
    }
    
    public void deleteObject() {
        this.object = null;
    }
    
    
}
