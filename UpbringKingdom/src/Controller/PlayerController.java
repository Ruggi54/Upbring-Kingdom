/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Actor;
import Model.Actor.ACTOR_STATE;
import Model.DIRECTION;
import Model.World;
import Model.WorldObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import ui.DialogueBox;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author simon
 */
public class PlayerController extends InputAdapter{
    
    //istanziamo il personaggio
    private Actor player;
    
    private DialogueBox dialogueBox;
    
    private World world;
    
    //istanziamo le variabili che serviranno per sapere ad ogni frame se e quale tasto si sta premendo
    private boolean up, down, right, left;
    
    //instanziamo le variabili utili a sapere per quanto tempo è pigiato un pulsante e se è pigiato
    private boolean[] buttonPress;
    private float[] buttonTimer;
    
    //soglia massima di tempo per il reface
    private float WALK_REFACE_THRESHOLD = 0.07f;
    
    private String dialogueAnziana = "Dei ragazzini stamattina hanno lasciato dell'immondizia vicino al parco, mi aiuteresti a toglierla?\nRicordati che ogni cassonetto ha la sua categoria di spazzatura!";
    private String dialogueBottiglia= "Hai raccolto la bottiglia di plastica!";
    private String dialogueMela= "Hai raccolto un torsolo di mela!";
    private String dialogueCucchiaio= "Hai raccolto un cucchiaio sporco!";
    private String dialogueFoglio= "Hai raccolto un foglio di carta!";
    private String dialogueCestino= "Hai messo la spazzatura nel cestino corretto, bravo!";
    private String dialogueTrashFailed = "Hai già un oggetto in mano, non puoi portarne due contemporaneamente";
    private String dialogueCestinoFailed = "Non hai niente in mano da buttare!";
    private String dialogueEsc = "Hai salvato correttamente il gioco";
    
    public PlayerController(Actor player, DialogueBox dialogueBox, World world) {
        //inizializziamo la variabile con il player a cui si deve riferire
        this.player = player;
        
        this.dialogueBox = dialogueBox;
        
        this.world = world;
        
        //inizializziamo le variabili buttonPress e buttonTimer per ogni direzione
        buttonPress = new boolean [DIRECTION.values().length];
        buttonPress[DIRECTION.NORTH.ordinal()] = false;
        buttonPress[DIRECTION.SOUTH.ordinal()] = false;
        buttonPress[DIRECTION.EAST.ordinal()] = false;
        buttonPress[DIRECTION.WEST.ordinal()] = false;
        buttonTimer = new float[DIRECTION.values().length];
        buttonTimer[DIRECTION.NORTH.ordinal()] = 0f;
        buttonTimer[DIRECTION.SOUTH.ordinal()] = 0f;
        buttonTimer[DIRECTION.EAST.ordinal()] = 0f;
        buttonTimer[DIRECTION.WEST.ordinal()] = 0f;
    }
    
    
    //funzione che modifica le varibili di direzione boolean in base alla key premuta
    @Override
    public boolean keyDown(int keycode) {
        if(!dialogueBox.active){
            if (keycode == Keys.W) {
                buttonPress[DIRECTION.NORTH.ordinal()] = true;
            }
            if (keycode == Keys.S) {
                buttonPress[DIRECTION.SOUTH.ordinal()] = true;
            }
            if (keycode == Keys.A) {
                buttonPress[DIRECTION.WEST.ordinal()] = true;
            }
            if (keycode == Keys.D) {
                buttonPress[DIRECTION.EAST.ordinal()] = true;
            }
        }
        //controllo se quando premiamo la lettera F devo mostrare o togliere la finestra di dialogo
        if(keycode == Keys.F){
            //controlliamo la direzione del personaggio e annulliamo il continuo del movimento precedentemente imposto
            if(player.facing == DIRECTION.NORTH){
                
                try {
                    //chiamata alla funzione per gestire il diaogo e l'interazione con l'oggetto o la persona di fronte
                    ObjectInteraction(DIRECTION.NORTH, player.getX(), player.getY()+1);
                } catch (IOException ex) {
                    Logger.getLogger(PlayerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //controlliamo la direzione del personaggio e annulliamo il continuo del movimento precedentemente imposto
            if(player.facing == DIRECTION.SOUTH){
                try {
                    ObjectInteraction(DIRECTION.SOUTH, player.getX(), player.getY()-1);
                } catch (IOException ex) {
                    Logger.getLogger(PlayerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //controlliamo la direzione del personaggio e annulliamo il continuo del movimento precedentemente imposto
            if(player.facing == DIRECTION.WEST){
                try {
                    ObjectInteraction(DIRECTION.WEST, player.getX()-1, player.getY());
                } catch (IOException ex) {
                    Logger.getLogger(PlayerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //controlliamo la direzione del personaggio e annulliamo il continuo del movimento precedentemente imposto
            if(player.facing == DIRECTION.EAST){
                try {
                    ObjectInteraction(DIRECTION.EAST, player.getX()+1, player.getY());
                } catch (IOException ex) {
                    Logger.getLogger(PlayerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        //controllo quano viene premuto il tasto ESC se devo mostrare la casella di dialogo o toglierla
        if(keycode == Keys.ESCAPE){
            considerActiveDialogue(dialogueEsc);
            try {
                //SaveDataActor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    //funzione che modifica le varibili di direzione boolean in base alla key rilasciata
    @Override
    public boolean keyUp(int keycode) {
        if(!dialogueBox.active){
            if(keycode == Keys.W){
                releaseDirection(DIRECTION.NORTH);
            }
            if(keycode == Keys.S){
                releaseDirection(DIRECTION.SOUTH);
            }
            if(keycode == Keys.A){
                releaseDirection(DIRECTION.WEST);
            }
            if(keycode == Keys.D){
                releaseDirection(DIRECTION.EAST);
            }
            
        }
        return false;
        
    }
    
    //funzione chiamata ad ogni frame che permette il controllo sul movimento, rendendolo continuo anche per più tile, inoltre aggiorniamo la direzione anche con il semplice tap
    public void update(float delta){
        if ( buttonPress[DIRECTION.NORTH.ordinal()]) {
            updateDirection(DIRECTION.NORTH, delta); //chiamiamo la funzione per cambiare la direzione
            return;
        }
        if ( buttonPress[DIRECTION.SOUTH.ordinal()]) {
            updateDirection(DIRECTION.SOUTH, delta); //chiamiamo la funzione per cambiare la direzione
            return;
        }
        if ( buttonPress[DIRECTION.WEST.ordinal()]) {
            updateDirection(DIRECTION.WEST, delta); //chiamiamo la funzione per cambiare la direzione
            return;
        }
        if ( buttonPress[DIRECTION.EAST.ordinal()]) {
            updateDirection(DIRECTION.EAST, delta); //chiamiamo la funzione per cambiare la direzione
            return;
        }
    }

    //funzione che aggiorna ad ogni frame il tempo passato con il tasto premuto e ogni volta chiama la funzione di controllo del movimento
    private void updateDirection(DIRECTION dir, float delta) {
        buttonTimer[dir.ordinal()] += delta;
        considerMove(dir);
    }

    //funzione che quando si lascia il pulsante, chiama la funzione di controllo per il refacing e refresha i campi
    private void releaseDirection(DIRECTION dir) {
        buttonPress[dir.ordinal()] = false;
        considerReface(dir);
        buttonTimer[dir.ordinal()] = 0f;
    }

    //funzione che controlla se il tempo passato con il tasto premuto è maggiore del tempo prefissato solo per il refacing, se lo supera inizia il movimento
    private void considerMove(DIRECTION dir) {
        if (buttonTimer[dir.ordinal()] > WALK_REFACE_THRESHOLD){
            player.move(dir);
        }
    }

    //funzione che controlla se il tempo per cui si è premuto il pulsante è minore del tempo prefissato di refacing, e se è così fa solo il reface del personaggio e non lo muove
    private void considerReface(DIRECTION dir) {
        if (buttonTimer[dir.ordinal()] < WALK_REFACE_THRESHOLD){
            player.reface(dir);
        }
    }

    //funzione per settare il testo del dialogo
    private void setDialogue(String dialogue) {
        dialogueBox.animateText(dialogue);
        dialogueBox.isFinished(); //feedback per sapere quando è terminata l'animazione
    }

    //funzione che permette di controllare se c'è già una finestra di dialogo aperta e reindirizza alla funzione per caricare il testo
    private void considerActiveDialogue(String dialogue) {
        if (!dialogueBox.active) {
            dialogueBox.active = true;
            setDialogue(dialogue);
        } else {
            dialogueBox.active = false;
            //controlliamo che il testo cisualizzato sia il testo di fine programma, così da chiuderlo appena si clicca un tasto tra ESC e F
            if(dialogueBox.targetText == dialogueEsc){
                Gdx.app.exit();
            }
        }
    }

    //funzione che permette di controllare, in base ai dati passati come parametri, la casella da controllare per vedere se è presente un oggetto con cui interagire
    private void ObjectInteraction(DIRECTION direction, int x, int y) throws UnsupportedEncodingException, IOException {
        
        if(world.getMap().getTile(x, y).getObject() != null){
            String NameObj = world.getMap().getTile(x, y).getObject().getNome();
            WorldObject Obj = world.getMap().getTile(x, y).getObject();
            switch (NameObj) {
                case "carta_trash":
                    releaseDirection(direction);
                    if(player.getTrash().equals("null")){
                        considerActiveDialogue(dialogueFoglio);
                        Obj.setActive(false);
                        //SaveDataTrash();
                        player.setTrash(Obj.getNome());
                    }
                    else{
                        considerActiveDialogue(dialogueTrashFailed);
                    }
                    break;
                case "bottiglietta_trash":
                    releaseDirection(direction);
                    if(player.getTrash().equals("null")){
                        considerActiveDialogue(dialogueBottiglia);
                        Obj.setActive(false);
                        //SaveDataTrash();
                        player.setTrash(Obj.getNome());
                    }
                    else{
                        considerActiveDialogue(dialogueTrashFailed);
                    }
                    break;    
                case "cucchiaio_trash":
                    releaseDirection(direction);
                    if(player.getTrash().equals("null")){
                        considerActiveDialogue(dialogueCucchiaio);
                        Obj.setActive(false);
                        //SaveDataTrash();
                        player.setTrash(Obj.getNome());
                    }
                    else{
                        considerActiveDialogue(dialogueTrashFailed);
                    }
                    break;
                case "mela_trash":
                    releaseDirection(direction);
                    if(player.getTrash().equals("null")){
                        considerActiveDialogue(dialogueMela);
                        Obj.setActive(false);
                        //SaveDataTrash();
                        player.setTrash(Obj.getNome());
                    }
                    else{
                        considerActiveDialogue(dialogueTrashFailed);
                    }
                    break;
                case "Cestino_Grigio":
                    releaseDirection(direction);
                    if(!player.getTrash().equals("null")){
                        if(player.getTrash().equals("mela_trash")){
                            considerActiveDialogue(dialogueCestino);
                            player.setTrash("null");
                        }
                        else{
                            considerActiveDialogue("Non e' il cestino corretto");
                        }
                    }
                    else{
                        considerActiveDialogue(dialogueCestinoFailed);
                    }
                    break;
                case "Cestino_Giallo":
                    releaseDirection(direction);
                    if(!player.getTrash().equals("null")){
                        if(player.getTrash().equals("bottiglietta_trash")){
                            considerActiveDialogue(dialogueCestino);
                            player.setTrash("null");
                        }
                        else{
                            considerActiveDialogue("Non e' il cestino corretto");
                        }
                    }
                    else{
                        considerActiveDialogue(dialogueCestinoFailed);
                    }
                    break;
                case "Cestino_Blu":
                    releaseDirection(direction);
                    if(!player.getTrash().equals("null")){
                        if(player.getTrash().equals("carta_trash")){
                            considerActiveDialogue(dialogueCestino);
                            player.setTrash("null");
                        }
                        else{
                            considerActiveDialogue("Non e' il cestino corretto");
                        }
                    }
                    else{
                        considerActiveDialogue(dialogueCestinoFailed);
                    }
                    break;
                case "Cestino_Verde":
                    releaseDirection(direction);
                    if(!player.getTrash().equals("null")){
                        if(player.getTrash().equals("cucchiaio_trash")){
                            considerActiveDialogue(dialogueCestino);
                                    player.setTrash("null");
                        }
                        else{
                            considerActiveDialogue("Non e' il cestino corretto");
                        }
                    }
                    else{
                        considerActiveDialogue(dialogueCestinoFailed);
                    }
                    break; 
                
            }
        }
        else{
           if(world.getMap().getTile(x, y).getActor() != null){
               String nameActor=world.getMap().getTile(x, y).getActor().getName();
               switch (nameActor){
                   case "elderly_south":
                    releaseDirection(direction);
                    considerActiveDialogue(dialogueAnziana);
                    break;
               }
           }
        }
    }

    private void SaveDataActor() throws MalformedURLException, UnsupportedEncodingException, IOException {
        URL url = new URL("http://localhost:44372/api/actor");
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("id", player.getId());
        params.put("name", player.getName());
        params.put("coordX", player.getX());
        params.put("coordY", player.getY());
        params.put("trash", player.getTrash());
        
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) {
                postData.append('&');
            }
            postData.append(URLEncoder.encode((String) param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        
    }

    private void SaveDataTrash() throws MalformedURLException, UnsupportedEncodingException, IOException {
        URL url = new URL("http://localhost:44372/api/trash");
        for (WorldObject obj : world.getObjects()){
            if(obj.getNome().equals("bottiglietta_trash") || obj.getNome().equals("cucchiaio_trash") || obj.getNome().equals("carta_trash") || obj.getNome().equals("mela_trash")){
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("id", obj.getId());
                params.put("name", obj.getNome());
                params.put("coordX", obj.getX());
                params.put("coordY", obj.getY());
                params.put("active", obj.isActive());

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) {
                        postData.append('&');
                    }
                    postData.append(URLEncoder.encode((String) param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postDataBytes);
                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            }
            
        }
        
    }
    
    
    
    
}
