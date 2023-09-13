package Model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import java.util.HashSet;
import java.util.Set;
import util.AnimationSet;

public class Actor implements YSortable{
    
    //istanziamo la mappa
    private TileMap map;
    
    //istanziamo le variabili corrispondenti alle coordinate del personaggio
    private int x;
    private int y;
    
    //creiamo la variabile che indica verso quale direzione è rivolto il personaggio
    public DIRECTION facing;
    
    //variabili di posizione attuale del personaggio ad ogni frame. Quando lo muoviamo in modo smooth si trova tra una piastrella e l'altra
    private float worldX, worldY;
    
    //variabili che identificano la posizione di inizio del movimento
    private int srcX, srcY;
    //variabili che identificano la posizione di fine del movimento
    private int destX, destY;
    //variabile che tiene conto del tempo passato dall'inizio del movimento fino alla fine
    private float animTimer;
    //variabile che identifica il tempo totale del movimento
    public float WALK_TIME_PER_TILE = 0.22f;
    //variabile che identifica il tempo di reface
    private float REFACE_TIME = 0.1f;
    //variabile di tipo ACTOR_STATE che identifica lo stato attuale del personaggio (camminando o fermo)
    public ACTOR_STATE state;
    
    //variabile che definisce il timer dell'animazione di camminata prolungata e si azzera solo dopo aver finito tutti i movimenti
    private float walkTimer;
    //variabile che definisce se in quel frame l'utente vuole continuare a camminare e si azzera ad ogni piastrella
    private boolean moveRequestThisFrame;
    
    //variabile che definisce il set di immagini che vanno a comporre l'animazione della camminata in base alla direzione
    private AnimationSet animations;
    
    private String name;
    
    public String trash;
    
    public int id;

    @Override
    public float getSizeX() {
        return 1f;
    }

    @Override
    public float getSizeY() {
        return 1.5f;
    }
    
    //Classe interna che possiede tutti i valori che possono rappresentare lo stato del personaggio nel gioco
    public enum ACTOR_STATE{
        WALKING,
        STANDING,
        REFACING,
        ;
    }
    
    public Actor(int id, TileMap map, int x, int y, AnimationSet animations, String name, String trash) {
        this.map = map; //set della mappa
        this.x = x; //set della x del personaggio (in pixel)
        this.y = y; //set della y del personaggio (in pixel)
        this.worldX = x; //set della x dello sprite
        this.worldY = y; //set della y dello sprite
        this.animations = animations; //settiamo l'insieme delle animazioni per tutte le direzioni
        map.getTile(x, y).setActor(this); //set del personaggio sulla casella specifica per sapere che quella casella è occupata
        this.state = ACTOR_STATE.STANDING; //set dello stato del personaggio (all'inizio è fermo)
        this.facing = DIRECTION.SOUTH;
        this.name=name;
        this.trash = trash;
        this.id = id;
    }
    
    
    
    //funzione che permette l'aggiornamento della posizione del personaggio ogni frame. delta è il tempo che intercorre tra un frame e l'altro
    public void update(float delta){
        //controllo se il personaggio si sta muovento per far sì che quando si sta muovendo non sia possibile dargli un altro input di movimento
        if (state == ACTOR_STATE.WALKING){
            animTimer+=delta; //il tempo del movimento aumenta e si aggiorna ogni frame
            walkTimer+=delta; //il tempo dell'animazione di camminata totale aumenta e si aggiorna ad ogni frame
            worldX = Interpolation.linear.apply(srcX, destX, animTimer/WALK_TIME_PER_TILE); //la posizione dello sprite cambia ogni frame
            worldY = Interpolation.linear.apply(srcY, destY, animTimer/WALK_TIME_PER_TILE); //la posizione dello sprite cambia ogni frame
            //controlliamo quando il timer raggiunge il tempo totale di animazione. quando lo fa termina il movimento
            if(animTimer > WALK_TIME_PER_TILE){
                /*calcoliamo il tempo che è avanzato nel movimento e lo togliamo al tempo dell'animazione del movimento
                lo facciamo perchè così affrettiamo l'animazione e la facciamo finire in tempo
                altrimenti quando il personaggio dovrebbe essere sulla piastrella fermo, continuerebbe a camminare senza muoversi*/
                float leftOverTime = animTimer-WALK_TIME_PER_TILE;
                finishMove(); //chiamata della funzione di fine movimento
                /*se l'utente vuole continuare a camminare, per non interrompere l'animazione controlliamo se c'è ancora una richiesta 
                e se c'è continuiamo a far muovere il personaggio */
                if(moveRequestThisFrame){
                    if(move(facing)){
                        animTimer+=leftOverTime;
                        worldX = Interpolation.linear.apply(srcX, destX, animTimer / WALK_TIME_PER_TILE); //la posizione dello sprite cambia ogni frame
                        worldY = Interpolation.linear.apply(srcY, destY, animTimer / WALK_TIME_PER_TILE); //la posizione dello sprite cambia ogni frame
                    }
                }else{
                    walkTimer = 0f; //resettiamo il walkTimer quando il personaggio si deve fermare e non deve più continuare l'animazione
                }
            }
        }
        //controllo se il personaggio si sta girando e se lo sta facendo, tengo aggiornato lo stato del personaggio per tutto il tempo dell'animazione
        if(state == ACTOR_STATE.REFACING){
            animTimer += delta;
            if(animTimer > REFACE_TIME){
                //quando il timer supera il tempo di rifacing, allora cambia lo stato del personaggio
                state = ACTOR_STATE.STANDING;
            }
        }
        moveRequestThisFrame=false; //a ogni frame resettiamo la move request per quel frame
    }
    
    //funzione che serve per girare il personaggio senza che si muova in un altra piastrella    
    public void reface (DIRECTION dir) {
        //controllo se non è fermo, perchè se non lo è ovviamente non lo faccio girare mentre si sta muovendo
        if (state != ACTOR_STATE.STANDING){
            return;
        }
        //controllo se il personaggio non è già girato da quella parte, se lo è non c'è bisogno di girarlo
        if(facing == dir) {
            return;
        }
        //cambio il facing come la direzione nuova, senza farlo muovere
        facing = dir;
        //cambio lo stato del personaggio in refacing
        state = ACTOR_STATE.REFACING;
        //refresho il timer dell'animazione
        animTimer = 0f;
    }
    
    //funzione che modifica le coordinate del personaggio per farlo muovere nella mappa
    public boolean move(DIRECTION dir){
        //controlliamo se il personaggio sta ancora camminando
        if(state == ACTOR_STATE.WALKING){
            //controlliamo se, quando sta camminando continua ad esserci l'input del movimento nella stessa direzione in cui si sta muovendo.
            //se l'input è ancora presente settiamo a true la variabile booleana che ci permette di far continuare l'animazione
            if(facing == dir){
                moveRequestThisFrame = true;
            }
            
            return false;   
        }
        //controllo se il personaggio sta cercando di uscire dai limiti della mappa
        if(x+dir.getDx() >= map.getWidth() || x+dir.getDx() < 0 || y+dir.getDy() >= map.getHeight() || y+dir.getDy() < 0){
            reface(dir);
            return false; //restituisce false quindi non modifica le coordinate del personaggio impedendogli di uscire dalla mappa
        }
        
        //controllo se la casella dove deve andare il personaggio è una casella occupata da un oggetto del mondo
        if (map.getTile(x + dir.getDx(), y + dir.getDy()).getObject() != null) {
            //prendo l'oggetto presente sulla casella dove si dirige il personaggio
            WorldObject o = map.getTile(x + dir.getDx(), y + dir.getDy()).getObject();
            //controllo se è un oggetto su cui posso camminare o no (una casa non è camminabile, mentre una strada è camminabile)
            if (!o.isWalkable()) {
                reface(dir);
                //se non è calpestabile esce dalla funzione, mentre se lo è procede con il movimento del porsonaggio
                return false;
            }
        }
        
        //controllo se la casella dove il personaggio deve andare è occupata da un'altro personaggio
        if(map.getTile(x+dir.getDx(), y+dir.getDy()).getActor() != null){
            reface(dir);
            return false;
        }
        initializeMove(dir); //chiamata alla procedura di inizio movimento
        map.getTile(x, y).setActor(null); //settiamo la casella precedente al movimento non più occupata
        x += dir.getDx(); //cambio della x
        y += dir.getDy(); //cambio della y
        map.getTile(x, y).setActor(this); //settiamo la casella di arrivo dopo il movimento occupata
        return true;
    }
    
    //procedura che definisce tutte le proprietà del personaggio all'inizio di un movimento
    private void initializeMove(DIRECTION dir){
        this.facing = dir;
        this.srcX=x;
        this.srcY=y;
        this.destX=x + dir.getDx();
        this.destY=y + dir.getDy();
        this.worldX=x;
        this.worldY=y;
        animTimer=0f; //all'inizio del movimento il tempo del movimento è 0
        state= ACTOR_STATE.WALKING; //all'inizio del movimento settiamo lo stato su walking
    }
    
    //Procedura che definisce tutte le proprietà del personaggio alla fine di un movimento
    private void finishMove(){
        state= ACTOR_STATE.STANDING; //alla fine del movimento lo stato torna ad essere standing
        //facciamo un clear di tutte le variabili utilizzate per evitare problemi 
        this.worldX=destX;
        this.worldY=destY;
        this.srcX=0;
        this.srcY=0;
        this.destX=0;
        this.destY=0;
        
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float getWorldX() {
        return worldX;
    }

    public float getWorldY() {
        return worldY;
    }

    public String getName() {
        return name;
    }

    public String getTrash() {
        return trash;
    }

    public void setTrash(String trash) {
        this.trash = trash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    
    //funzione che restituisce l'immagine da mostrare nel frame in cui ci si trova
    public TextureRegion getSprite(){
        //controlliamo se il personaggio sta camminando o è fermo o se si sta girando
        if(state == ACTOR_STATE.WALKING){
            return animations.getWalking(facing).getKeyFrame(walkTimer); //se cammina restituiamo lo sprite presente in quel frame in base alla direzione; teniamo conto del tempo dell'animazione
        } else if(state == ACTOR_STATE.STANDING){
            return animations.getStanding(facing); //se è feermo, restituiamo lo sprite del personaggio rivolto in quella direzione
        } else if(state == ACTOR_STATE.REFACING){
            return animations.getWalking(facing).getKeyFrames()[0];
        }
        //per evitare errori, se ci dovesse essere un problema con lo stato del personaggio impostiamo di base lo standing rivolto verso sud
        return animations.getStanding(DIRECTION.SOUTH); 
    }
    
    
}
