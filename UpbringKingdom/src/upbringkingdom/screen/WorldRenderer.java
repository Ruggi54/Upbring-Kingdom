package upbringkingdom.screen;

import Model.Actor;
import Model.Camera;
import Model.Terrain;
import Model.World;
import Model.WorldObject;
import Model.YSortable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import upbringkingdom.Settings;

public class WorldRenderer {
    private AssetManager assetManager;
    //creiamo la variabile di tipo world
    private World world;
    
    //creiamo le due variabili per l'erba
    private Texture grass1;
    private Texture grass2;
    
    private Texture strada_centro;
    private Texture strada_centro_destra;
    private Texture strada_centro_sinistra;
    private Texture strada_bassa_centro;
    private Texture strada_bassa_sinistra;
    private Texture strada_bassa_destra;
    private Texture strada_alto_centro;
    private Texture strada_alto_sinistra;
    private Texture strada_alto_destra;
    
    private Texture strada_bassa_sinistra_inters;
    private Texture strada_bassa_destra_inters;
    private Texture strada_alto_sinistra_inters;
    private Texture strada_alto_destra_inters;
    
    
    
    
    //creiamo una lista con l'ashcode degli oggetti del mondo in modo da non ridisegnarli ogni volta, ma solo una
    private List<Integer> renderedObjects = new ArrayList<Integer>();
    //creiamo una lista di Ysortable che ci permette di salvarci tutte le caratteristiche degli oggetti e dei personaggi presentinel mondo
    private List<YSortable> forRendering = new ArrayList<YSortable>();

    //Costruttore del WorldRenderer
    public WorldRenderer(AssetManager assetManager, World world) {
        this.assetManager = assetManager; //set dell'assetManager
        this.world = world; //Set del mondo
        
        //set delle texture dell'erba
        grass1 = new Texture ("res/grass1.png");
        grass2 = new Texture ("res/grass2.png");
        strada_centro = new Texture ("res/strada_centro.png");
        strada_centro_destra = new Texture ("res/strada_centro_destra.png");
        strada_centro_sinistra = new Texture ("res/strada_centro_sinistra.png");
        strada_bassa_centro = new Texture ("res/strada_bassa_centro.png");
        strada_bassa_sinistra = new Texture ("res/strada_bassa_sinistra.png");
        strada_bassa_destra = new Texture ("res/strada_bassa_destra.png");
        strada_alto_centro = new Texture ("res/strada_alto_centro.png");
        strada_alto_sinistra = new Texture ("res/strada_alto_sinistra.png");
        strada_alto_destra = new Texture ("res/strada_alto_destra.png");
        strada_bassa_sinistra_inters = new Texture ("res/strada_bassa_sinistra_inters.png");
        strada_bassa_destra_inters = new Texture ("res/strada_bassa_destra_inters.png");
        strada_alto_sinistra_inters = new Texture ("res/strada_alto_sinistra_inters.png");
        strada_alto_destra_inters = new Texture ("res/strada_alto_destra_inters.png");
        
        
        
    }
    
    //funzione di render del mondo che viene chiamata ad ogni frame
    public void render(SpriteBatch batch, Camera camera){
        //Creiamo e istanziamo le coordinate del mondo in base alla posizione del personaggio e della camera. Il personaggio è fisso al centro e la mappa si muove
        float worldStartX = Gdx.graphics.getWidth()/2 - camera.getCameraX()*Settings.SCALED_TILE_SIZE;
        float worldStartY = Gdx.graphics.getHeight()/2 - camera.getCameraY()*Settings.SCALED_TILE_SIZE;
        
        //renderizziamo la mappa
        for(int x=0; x< world.getMap().getWidth(); x++){
            for(int y=0; y<world.getMap().getHeight(); y++){
                Texture render; //variabile d'appoggio che ottiene la texture giusta da caricare nella posizione
                //settiamo il tipo di terreno in base al tipo di terreno che è in quella posizione nella matrice
                if(world.getMap().getTile(x, y).getTerrain() == Terrain.Grass1){
                    render = grass1; //assegnamento grass1 come texture
                }else{
                    render = grass2; //assegnamento grass1 come texture
                }
                batch.draw(render, //set della texture della piastrella
                        worldStartX+x*Settings.SCALED_TILE_SIZE, //set della x moltiplicato per la grandezza dinamica della casella (in pixel) e aggiunto al punto di partenza del mondo
                        worldStartY+y*Settings.SCALED_TILE_SIZE, //set della y moltiplicato per la grandezza dinamica della casella (in pixel) e aggiunto al punto di partenza del mondo
                        Settings.SCALED_TILE_SIZE, //settiamo la larghezza della casella
                        Settings.SCALED_TILE_SIZE //settiamo l'altezza della casella
                        );
            }
        }
        for (int x=1; x<= 30; x++){
            for(int y=1; y<17; y++){
                Texture render = null;
                switch(y){
                    case 2: if(x>=3 && x<=27) render = strada_bassa_centro;
                            if(x==2) render = strada_bassa_sinistra;
                            if(x==28) render = strada_bassa_destra;
                            break;
                    case 3: if(x>=3 && x<=27) render = strada_alto_centro;
                            if(x==2) render = strada_alto_sinistra;
                            if(x==28) render = strada_alto_destra;
                            break;
                    case 10:if(x>=3 && x<=27) render = strada_bassa_centro;
                            if(x==2) render = strada_bassa_sinistra;
                            if(x==28) render = strada_bassa_destra;
                            break;
                    case 11:if(x>=3 && x<=27) render = strada_alto_centro;
                            if(x==2) render = strada_alto_sinistra;
                            if(x==28) render = strada_alto_destra;
                            break;        
                            
                }
                if(x>=14 && x<=16){
                    if(y==1){
                        
                    }
                    else{
                        if (y == 16) {
                            if(x==14) render = strada_alto_sinistra;
                            if(x==15) render = strada_alto_centro;
                            if(x==16) render = strada_alto_destra;

                        }
                        else{
                            if(x==14) render = strada_centro_sinistra;
                            if(x==15) render = strada_centro;
                            if(x==16) render = strada_centro_destra;
                        }

                    }
                    if(y==3){
                        if(x==14) render = strada_alto_sinistra_inters;
                        if(x==16) render = strada_alto_destra_inters;
                    }
                    if(y==2)render = strada_bassa_centro;
                    
                    if(y==10){
                        if(x==14) render = strada_bassa_sinistra_inters;
                        if(x==16) render = strada_bassa_destra_inters;
                    }
                    if(y==11){
                        if(x==14) render = strada_alto_sinistra_inters;
                        if(x==16) render = strada_alto_destra_inters;
                    }
                    
                }
                if(render==null) continue;
                batch.draw(render, //set della texture della piastrella
                            worldStartX+x*Settings.SCALED_TILE_SIZE, //set della x moltiplicato per la grandezza dinamica della casella (in pixel) e aggiunto al punto di partenza del mondo
                            worldStartY+y*Settings.SCALED_TILE_SIZE, //set della y moltiplicato per la grandezza dinamica della casella (in pixel) e aggiunto al punto di partenza del mondo
                            Settings.SCALED_TILE_SIZE, //settiamo la larghezza della casella
                            Settings.SCALED_TILE_SIZE //settiamo l'altezza della casella
                            );
            }
        }
        
        
        //set degli oggetti e dei personaggi all'interno del mondo
        for (int x = 0; x < world.getMap().getWidth(); x++) {
            for (int y = 0; y < world.getMap().getHeight(); y++) {
                if (world.getMap().getTile(x, y).getObject() != null) {
                    WorldObject object = world.getMap().getTile(x, y).getObject();
                    if (renderedObjects.contains(object.hashCode())) { //andiamo ad iserire nella lista un codice unico per ogni oggetto, cosi che, visto che un oggetto può
                                                                       //occupare più caselle, non viene ridisegnato anche nelle altre caselle, ma solo una volta
                        //se l'oggetto è già stato disegnato, non lo ridisegna e va al ciclo successivo
                        if(!object.isActive()){
                            renderedObjects.remove(object);
                        }
                        else{
                            continue;
                        }
                        
                    }
                    
                    /*if(object.getSprite().getTexture().equals("com.badlogic.gdx.graphics.Texture@7d8f71c3")){
                        continue;
                    }*/
                    if(object.isActive()){
                        //aggiunge l'oggetto nella lista degli oggetti da renderizzare
                        forRendering.add(object);
                        //aggiunge l'ashcode dell'oggetto nella lista renderedObjects
                        renderedObjects.add(object.hashCode());
                    }
                    else{
                        forRendering.remove(object);
                        world.deleteObject(object);
                    }
                }
                //controllo se la casella è occupata da un personaggio
                if (world.getMap().getTile(x, y).getActor() != null) {
                    //prendo il personaggio di quella casella
                    Actor actor = world.getMap().getTile(x, y).getActor();
                    //lo aggiungo nella lista degli oggetti da renderizzare
                    forRendering.add(actor);
                }
            }
        }
        
        
        //ordina gli oggetti in base alla loro posizione nel mondo
        Collections.sort(forRendering, new WorldObjectYComparator());
        //inverte l'ordinamento in modo che un oggetto che si trova sotto un'altro gli sarà davanti
        Collections.reverse(forRendering);
        
        //render degli oggetti all'interno del mondo
        for(YSortable loc: forRendering){
            //disegna l'oggetto
            batch.draw(loc.getSprite(),
                    worldStartX+loc.getWorldX()*Settings.SCALED_TILE_SIZE,
                    worldStartY+loc.getWorldY()*Settings.SCALED_TILE_SIZE,
                    Settings.SCALED_TILE_SIZE*loc.getSizeX(),
                    Settings.SCALED_TILE_SIZE*loc.getSizeY());
        }
        
        
    }
    
}
