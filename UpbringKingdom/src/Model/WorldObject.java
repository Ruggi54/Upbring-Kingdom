package Model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import java.util.ArrayList;
import java.util.List;

public class WorldObject implements YSortable{
    
    //creazione variabili che definiscono le coordinate dell'oggetto
    private int x, y;
    
    //creazione della variabile che definisce la texture dell'oggetto
    private TextureRegion texture;
    //creazione variabili che definiscono la grandezza dell'oggetto nei 2 piani
    private float sizeX, sizeY;
    
    //creazione della lista delle piastrelle non calpestabili
    private List<GridPoint2> tiles;
    //creazione della variabile che definisce se le piastrelle non calpestabili sono effettivamente calpestabili o no
    private boolean walkable;
    
    private String nome;
    
    private boolean active;
    
    private int id;

    //Costruttore della classe
    public WorldObject(int x, int y, TextureRegion texture, float sizeX, float sizeY, List<GridPoint2> tiles, String nome) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.tiles = tiles;
        this.nome=nome;
        
    }
    
    /*public WorldObject(int x, int y, TextureRegion texture, float sizeX, float sizeY, List<GridPoint2> tiles) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.tiles = new ArrayList<GridPoint2>();
        for (GridPoint2 p : tiles){
            this.tiles.add(p);
        }
        this.walkable = true;
    }*/
    
    public WorldObject(int x, int y, TextureRegion texture, float sizeX, float sizeY, List<GridPoint2> tiles, String nome, boolean walkable, boolean active) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.tiles = new ArrayList<GridPoint2>();
        for (GridPoint2 p: tiles){
            this.tiles.add(p);
        }
        this.nome=nome;
        this.walkable = walkable;
        this.active = active;
    }
    
    public WorldObject(int id, int x, int y, TextureRegion texture, float sizeX, float sizeY, List<GridPoint2> tiles, String nome, boolean walkable, boolean active) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.tiles = new ArrayList<GridPoint2>();
        for (GridPoint2 p: tiles){
            this.tiles.add(p);
        }
        this.nome=nome;
        this.walkable = walkable;
        this.active = active;
        this.id = id;
    }
    
    public void update(float delta){
        
    }
    
    @Override
    public float getWorldX() {
        return x;
    }

    @Override
    public float getWorldY() {
        return y;
    }

    @Override
    public TextureRegion getSprite() {
        return texture;
    }

    @Override
    public float getSizeX() {
        return sizeX;
    }

    @Override
    public float getSizeY() {
        return sizeY;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public List<GridPoint2> getTiles() {
        return tiles;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
        
    //funzione che restituisce se è calpestabile oppure no
    public boolean isWalkable(){
        return walkable;
    }
}
