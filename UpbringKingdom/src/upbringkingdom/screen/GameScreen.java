/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upbringkingdom.screen;

import Controller.PlayerController;
import static Helpers.DataBaseHelper.InitConnection;
import Model.Actor;
import Model.Camera;
import Model.Terrain;
import Model.TileMap;
import Model.World;
import Model.WorldObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import static com.badlogic.gdx.graphics.g3d.particles.ParticleChannels.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Integer.min;
import java.net.URL;
import static java.sql.JDBCType.NULL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ui.DialogueBox;
import upbringkingdom.Settings;
import upbringkingdom.UpbringKingdom;
import util.AnimationSet;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.HttpClient.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author simon
 */
public class GameScreen extends AbstractScreen{
    
    //creazione variabile che simulerà il nostro controller
    private PlayerController controller;
    
    private World world;
    
    //creazione variabile che simulerà il nostro giocatore
    private Actor player;
    
    private Actor npc;
    
    //Creazione della variabile che simula la camera
    private Camera camera;
    
    //aiuta a renderizzare più velocemente gli sprite che manualmente
    private SpriteBatch batch;
    
    //creazione variabile dello schermo del gioco, indipendente dallo schermo della casella di testo
    private Viewport gameViewport;
    
    //Creazione della variabile di appoggio per il rendering del mondo
    private WorldRenderer worldRenderer;
    
    //variabili per l'uilizzo della finestra di dialogo
    private Stage uiStage;
    private Table root;
    private DialogueBox dialogueBox;
    
    //variabile di scala per la grandezza del dialogueBox
    private int uiScale=2;
    
    //creiamo la variabile per ospitare l'audio in background
    private Music music;
    
    //creiamo e istanziamo la variabile che prenderà la canzone in formato file
    private FileHandle file = new FileHandle("music/OstAOT.mp3");
    
    
    public GameScreen(UpbringKingdom app) throws IOException {
        super(app);
            gameViewport = new ScreenViewport();
            //instanziamento della batch
            batch = new SpriteBatch();
            
            //inizializziamo la musica con il file da caricare
            music = Gdx.audio.newMusic(file);
            //settiamo la musica in loop
            music.setLooping(true);
            //settiamo il volume della canzone
            music.setVolume(0.1f);
            //facciamo partire la canzone
            music.play();
            
            //creiamo la variabile atlas che contiene il file atlas che viene utilizzato per renderizzare gli sprite (contiene dimensione, posizione...)
            TextureAtlas atlas = app.getAssetManager().get("res/packed/textures.atlas", TextureAtlas.class);
            
            //creiamo il set di animazioni e inseriamo tutte quelle del movimento del personaggio
            AnimationSet animationsBoy = new AnimationSet(
                    new Animation(0.3f/2f, atlas.findRegions("boy_walk_north"), PlayMode.LOOP_PINGPONG),
                    new Animation(0.3f/2f, atlas.findRegions("boy_walk_south"), PlayMode.LOOP_PINGPONG),
                    new Animation(0.3f/2f, atlas.findRegions("boy_walk_east"), PlayMode.LOOP_PINGPONG),
                    new Animation(0.3f/2f, atlas.findRegions("boy_walk_west"), PlayMode.LOOP_PINGPONG),
                    atlas.findRegion("boy_stand_north"),
                    atlas.findRegion("boy_stand_south"),
                    atlas.findRegion("boy_stand_east"),
                    atlas.findRegion("boy_stand_west")
            );
            //creiamo il set di animazione per l'npc dell'anziana
            AnimationSet animationsElderly = new AnimationSet(
                    atlas.findRegion("elderly_south")
            );
            
            //istanziamento della camera
            camera = new Camera();
            
            world = new World(31,28);
            
            
            player = new Actor(1, world.getMap(), 15, 11, animationsBoy, "player", "null");
            //LoadActor(animationsBoy);
            
            //instanziamento del giocatore con la sua posizione di partenza
            
            npc = new Actor(2, world.getMap(), 16, 19, animationsElderly, "elderly_south", "null");
            
            addHouse(4, 4, "house_legno");
            addHouse(22, 4, "house_red");
            addHouse(4, 12, "house_red");
            addHouse(22, 12, "house_legno");
            
            addFountain(14, 20);
            
            addPanchine();
            
            addInsegne();
            
            CreateTree();
            
            addStaccionate();
            
            addFiori();
            
            addCestino(14, 26, "Cestino_Grigio");
            addCestino(13, 26, "Cestino_Blu");
            addCestino(16, 26, "Cestino_Giallo");
            addCestino(17, 26, "Cestino_Verde");
            
            //LoadObjects();
            addSpazzatura(1, "cucchiaio_trash", -1, -1);
            addSpazzatura(1, "carta_trash", -1, -1);
            addSpazzatura(1, "bottiglietta_trash", -1, -1);
            addSpazzatura(1, "mela_trash", -1, -1);
            
            world.addActor(player);
            world.addActor(npc);
            //InitConnection();
            initUI();
            
            //instanziamento del controller collegato al player
            controller = new PlayerController(player, dialogueBox, world);
               
            worldRenderer = new WorldRenderer(getApp().getAssetManager(), world);
        /*catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        
    }
    
    //funzione che è perennemente eseguita quando lo schermo è mostrato
    @Override
    public void show() {
        //settiamo all'interno del gdx il controller come input principale
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        //chiamata alla funzione che per(float delta) {
        //chiamata alla funzione chemette di avere un movimento continuo
        controller.update(delta);
        //Aggiorniamo la camera ogni volta che renderizziamo lo schermo inserendo come coordinate il centro della piastrella su cui è il personaggio (0.5f).
        //Si sposta ad ogni frame insieme al personaggio in modo smooth
        camera.update(player.getWorldX()+0.5f, player.getWorldY()+0.5f);
        world.update(delta);
        //chiamata alla funzione act che renderizza il testo animandolo con le proprietà settate nella funzione animateText()
        uiStage.act(delta);
        gameViewport.apply();
        batch.begin();
        
        //Rendering del mondo ad ogni frame
        worldRenderer.render(batch, camera);
        
        batch.end();
        
        //controllo se visualizzare la finestra di dialogo quando è settato come attivo
        if(dialogueBox.active){
            uiStage.draw();
        }
        
    }

    @Override
    public void resize(int width, int height) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        uiStage.getViewport().update(width/uiScale, height/uiScale, true);
        gameViewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
    
    //funzione di creazione di una struttura
    private void addHouse(int xObj, int yObj, String houseName) {
        //creazione della lista delle caselle che non possono essere calpestate
        List<GridPoint2> unwalkable = new ArrayList<GridPoint2>();
        GridPoint2 element = new GridPoint2(xObj, yObj);
        //Larghezza della struttura
        int sizeX = 5;
        //altezza della struttura
        int sizeY = 4;
        //cicli per settare le caselle non calpestabilu all'interno della lista
        for(int i=0; i<sizeY-1; i++){
            for(int j=0; j<sizeX; j++){
                GridPoint2 element1 = new GridPoint2(xObj+j, yObj+i);
                //aggiungiamo l'elemento alla lista delle caselle non calpestabili
                unwalkable.add(element1);
            }
        }
        //prendiamo la texture della struttura facendo il collegamento con il file 'textures.atlas'
        TextureAtlas atlas = getApp().getAssetManager().get("res/packed/textures.atlas", TextureAtlas.class);
        TextureRegion houseRegion = atlas.findRegion(houseName);
        
        //Creiamo l'oggetto con tutte le caratteristiche definite prima
        WorldObject house = new WorldObject(xObj, yObj, houseRegion, sizeX, sizeY, unwalkable, houseName, false, true);
        //aggiungiamo l'oggetto al mondo
        world.addObject(house);
    }

    //funzione di creazione dell'oggetto albero
    private void addTree(int xObj, int yObj, String nomeAlbero) {
        List<GridPoint2> unwalkable = new ArrayList<GridPoint2>();
        GridPoint2 element = new GridPoint2(xObj, yObj);
        int sizeX = 1;
        int sizeY = 2;
        for(int i=0; i<sizeY-1; i++){
            for(int j=0; j<sizeX; j++){
                GridPoint2 element1 = new GridPoint2(xObj+j, yObj+i);
                unwalkable.add(element1);
            }
        }
        TextureAtlas atlas = getApp().getAssetManager().get("res/packed/textures.atlas", TextureAtlas.class);
        TextureRegion treeRegion = atlas.findRegion(nomeAlbero);
        WorldObject tree = new WorldObject(xObj, yObj, treeRegion, sizeX, sizeY, unwalkable, nomeAlbero, false, true);
        world.addObject(tree);
    }

    //funzione di creazione del bordo della mappa con gli alberi
    private void CreateTree() {
        int x=0;
        CreateEdgeY(x);
        x=30;
        CreateEdgeY(x);
        int y=0;
        CreateEdgeX(y);
        y=27;
        CreateEdgeX(y);
        addTree(3, 9, "tree_tall_spigoli");
        addTree(11, 5, "tree_tall_spigoli");
        addTree(19, 8, "tree_tall_spigoli");
        addTree(13, 12, "tree_tall_spigoli");
        addTree(28, 6, "tree_tall_spigoli");
        addTree(24, 20, "tree_tall_spigoli");
        addTree(18, 24, "tree_tall_spigoli");
        addTree(12, 18, "tree_tall_spigoli");
        addTree(12, 24, "tree_tall_spigoli");
        addTree(18, 18, "tree_tall_spigoli");
        
        addTree(1, 17, "tree_tall_spigoli");
        addTree(1, 18, "tree_tall_spigoli");
        addTree(1, 19, "tree_tall_spigoli");
        addTree(1, 20, "tree_tall_spigoli");
        addTree(1, 21, "tree_tall_spigoli");
        addTree(1, 22, "tree_tall_spigoli");
        addTree(1, 23, "tree_tall_spigoli");
        addTree(1, 24, "tree_tall_spigoli");
        addTree(1, 25, "tree_tall_spigoli");
        addTree(1, 26, "tree_tall_spigoli");
        addTree(2, 17, "tree_tall_spigoli");
        addTree(2, 18, "tree_tall_spigoli");
        addTree(2, 19, "tree_tall_spigoli");
        addTree(2, 20, "tree_tall_spigoli");
        addTree(2, 24, "tree_tall_spigoli");
        addTree(2, 25, "tree_tall_spigoli");
        addTree(2, 26, "tree_tall_spigoli");
        addTree(3, 18, "tree_tall_spigoli");
        addTree(3, 19, "tree_tall_spigoli");
        addTree(3, 20, "tree_tall_spigoli");
        addTree(3, 25, "tree_tall_spigoli");
        addTree(3, 26, "tree_tall_spigoli");
        addTree(4, 19, "tree_tall_spigoli");
        addTree(4, 20, "tree_tall_spigoli");
        addTree(4, 25, "tree_tall_spigoli");
        addTree(4, 26, "tree_tall_spigoli");
        addTree(5, 19, "tree_tall_spigoli");
        addTree(5, 20, "tree_tall_spigoli");
        addTree(5, 25, "tree_tall_spigoli");
        addTree(5, 26, "tree_tall_spigoli");
        addTree(6, 19, "tree_tall_spigoli");
        addTree(6, 20, "tree_tall_spigoli");
        addTree(6, 24, "tree_tall_spigoli");
        addTree(6, 25, "tree_tall_spigoli");
        addTree(6, 26, "tree_tall_spigoli");
        addTree(7, 19, "tree_tall_spigoli");
        addTree(7, 20, "tree_tall_spigoli");
        addTree(7, 21, "tree_tall_spigoli");
        addTree(7, 22, "tree_tall_spigoli");
        addTree(7, 23, "tree_tall_spigoli");
        addTree(7, 24, "tree_tall_spigoli");
        addTree(7, 25, "tree_tall_spigoli");
        addTree(7, 26, "tree_tall_spigoli");
        addTree(8, 20, "tree_tall_spigoli");
        addTree(8, 21, "tree_tall_spigoli");
        addTree(8, 22, "tree_tall_spigoli");
        addTree(8, 23, "tree_tall_spigoli");
        addTree(8, 24, "tree_tall_spigoli");
        addTree(8, 25, "tree_tall_spigoli");
        addTree(8, 26, "tree_tall_spigoli");
        addTree(9, 23, "tree_tall_spigoli");
        addTree(9, 24, "tree_tall_spigoli");
        addTree(9, 25, "tree_tall_spigoli");
        addTree(9, 26, "tree_tall_spigoli");
        addTree(10, 25, "tree_tall_spigoli");
        addTree(10, 26, "tree_tall_spigoli");
        
        addTree(29, 17, "tree_tall_spigoli");
        addTree(29, 18, "tree_tall_spigoli");
        addTree(29, 19, "tree_tall_spigoli");
        addTree(29, 20, "tree_tall_spigoli");
        addTree(29, 21, "tree_tall_spigoli");
        addTree(29, 22, "tree_tall_spigoli");
        addTree(29, 23, "tree_tall_spigoli");
        addTree(29, 24, "tree_tall_spigoli");
        addTree(29, 25, "tree_tall_spigoli");
        addTree(29, 26, "tree_tall_spigoli");
        addTree(28, 17, "tree_tall_spigoli");
        addTree(28, 18, "tree_tall_spigoli");
        addTree(28, 19, "tree_tall_spigoli");
        addTree(28, 20, "tree_tall_spigoli");
        addTree(28, 24, "tree_tall_spigoli");
        addTree(28, 25, "tree_tall_spigoli");
        addTree(28, 26, "tree_tall_spigoli");
        addTree(27, 18, "tree_tall_spigoli");
        addTree(27, 19, "tree_tall_spigoli");
        addTree(27, 20, "tree_tall_spigoli");
        addTree(27, 25, "tree_tall_spigoli");
        addTree(27, 26, "tree_tall_spigoli");
        addTree(26, 19, "tree_tall_spigoli");
        addTree(26, 20, "tree_tall_spigoli");
        addTree(26, 25, "tree_tall_spigoli");
        addTree(26, 26, "tree_tall_spigoli");
        addTree(25, 19, "tree_tall_spigoli");
        addTree(25, 20, "tree_tall_spigoli");
        addTree(25, 25, "tree_tall_spigoli");
        addTree(25, 26, "tree_tall_spigoli");
        addTree(24, 19, "tree_tall_spigoli");
        addTree(24, 20, "tree_tall_spigoli");
        addTree(24, 24, "tree_tall_spigoli");
        addTree(24, 25, "tree_tall_spigoli");
        addTree(24, 26, "tree_tall_spigoli");
        addTree(23, 19, "tree_tall_spigoli");
        addTree(23, 20, "tree_tall_spigoli");
        addTree(23, 21, "tree_tall_spigoli");
        addTree(23, 22, "tree_tall_spigoli");
        addTree(23, 23, "tree_tall_spigoli");
        addTree(23, 24, "tree_tall_spigoli");
        addTree(23, 25, "tree_tall_spigoli");
        addTree(23, 26, "tree_tall_spigoli");
        addTree(22, 20, "tree_tall_spigoli");
        addTree(22, 21, "tree_tall_spigoli");
        addTree(22, 22, "tree_tall_spigoli");
        addTree(22, 23, "tree_tall_spigoli");
        addTree(22, 24, "tree_tall_spigoli");
        addTree(22, 25, "tree_tall_spigoli");
        addTree(22, 26, "tree_tall_spigoli");
        addTree(21, 23, "tree_tall_spigoli");
        addTree(21, 24, "tree_tall_spigoli");
        addTree(21, 25, "tree_tall_spigoli");
        addTree(21, 26, "tree_tall_spigoli");
        addTree(20, 25, "tree_tall_spigoli");
        addTree(20, 26, "tree_tall_spigoli");
    }
    
    //funzione di creazione di una riga di alberi in verticale
    private void CreateEdgeY(int x) {
        for (int y=0; y<28; y++){
            addTree (x, y, "tree_tall");
        }
    }
//funzione di creazione di una riga di alberi in orizzontale
    private void CreateEdgeX(int y) {
        for (int x=1; x<30; x++){
            addTree (x, y, "tree_tall");
        }
    }

    private void addFountain(int xObj, int yObj) {
        List<GridPoint2> unwalkable = new ArrayList<GridPoint2>();
        GridPoint2 element = new GridPoint2(xObj, yObj);
        int sizeX = 3;
        int sizeY = 3;
        for(int i=0; i<sizeY; i++){
            for(int j=0; j<sizeX; j++){
                GridPoint2 element1 = new GridPoint2(xObj+j, yObj+i);
                unwalkable.add(element1);
            }
        }
        TextureAtlas atlas = getApp().getAssetManager().get("res/packed/textures.atlas", TextureAtlas.class);
        TextureRegion fountainRegion = atlas.findRegion("fontana");
        WorldObject fountain = new WorldObject(xObj, yObj, fountainRegion, sizeX, sizeY, unwalkable, "fontana", false, true);
        world.addObject(fountain);
    }

    private void addPanchine() {
        addPanchinaVerticale(12, 20, "panchina_verticale");
        addPanchinaVerticale(18, 20, "panchina_verticale");
        addPanchinaOrizzontale(14, 18, "panchina_orizzontale");
        addPanchinaOrizzontale(14, 24, "panchina_orizzontale");
    }
    
    private void addPanchinaVerticale(int xObj, int yObj, String nomePanchina) {
        List<GridPoint2> unwalkable = new ArrayList<GridPoint2>();
        GridPoint2 element = new GridPoint2(xObj, yObj);
        int sizeX = 1;
        int sizeY = 3;
        for(int i=1; i<sizeY-1; i++){
            for(int j=0; j<sizeX; j++){
                GridPoint2 element1 = new GridPoint2(xObj+j, yObj+i);
                unwalkable.add(element1);
            }
        }
        TextureAtlas atlas = getApp().getAssetManager().get("res/packed/textures.atlas", TextureAtlas.class);
        TextureRegion panchinaRegion = atlas.findRegion(nomePanchina);
        WorldObject panchina = new WorldObject(xObj, yObj, panchinaRegion, sizeX, sizeY, unwalkable, nomePanchina, false, true);
        world.addObject(panchina);
    }
    
    private void addPanchinaOrizzontale(int xObj, int yObj, String nomePanchina) {
        List<GridPoint2> unwalkable = new ArrayList<GridPoint2>();
        GridPoint2 element = new GridPoint2(xObj, yObj);
        int sizeX = 3;
        int sizeY = 1;
        for(int i=0; i<sizeY; i++){
            for(int j=1; j<sizeX-1; j++){
                GridPoint2 element1 = new GridPoint2(xObj+j, yObj+i);
                unwalkable.add(element1);
            }
        }
        TextureAtlas atlas = getApp().getAssetManager().get("res/packed/textures.atlas", TextureAtlas.class);
        TextureRegion panchinaRegion = atlas.findRegion(nomePanchina);
        WorldObject panchina = new WorldObject(xObj, yObj, panchinaRegion, sizeX, sizeY, unwalkable, nomePanchina, false, true);
        world.addObject(panchina);
    }

    private void addInsegne() {
        addInsegna(9, 4, "insegna_casa");
        addInsegna(9, 12, "insegna_casa");
        addInsegna(21, 4, "insegna_casa");
        addInsegna(21, 12, "insegna_casa");
    }
    
    private void addInsegna(int xObj, int yObj, String nomeInsegna) {
        List<GridPoint2> unwalkable = new ArrayList<GridPoint2>();
        GridPoint2 element = new GridPoint2(xObj, yObj);
        int sizeX = 1;
        int sizeY = 1;
        for(int i=0; i<sizeY; i++){
            for(int j=0; j<sizeX; j++){
                GridPoint2 element1 = new GridPoint2(xObj+j, yObj+i);
                unwalkable.add(element1);
            }
        }
        TextureAtlas atlas = getApp().getAssetManager().get("res/packed/textures.atlas", TextureAtlas.class);
        TextureRegion insegnaRegion = atlas.findRegion(nomeInsegna);
        WorldObject insegna = new WorldObject(xObj, yObj, insegnaRegion, sizeX, sizeY, unwalkable, nomeInsegna, false, true);
        world.addObject(insegna);
    }

    private void addStaccionate() {
        addStaccionata(10, 12, "staccionata_basso_centro");
        addStaccionata(11, 12, "staccionata_basso_centro");
        addStaccionata(12, 12, "staccionata_basso_destra");
        addStaccionata(12, 13, "staccionata_centro_destra");
        addStaccionata(12, 14, "staccionata_centro_destra");
        addStaccionata(12, 15, "staccionata_alto_destra");
        addStaccionata(11, 15, "staccionata_alto_centro");
        addStaccionata(10, 15, "staccionata_alto_centro");
        addStaccionata(9, 15, "staccionata_alto_centro");
        addStaccionata(8, 15, "staccionata_alto_sinistra");
        
        addStaccionata(3, 6, "staccionata_basso_sinistra");
        addStaccionata(3, 7, "staccionata_centro_sinistra");
        addStaccionata(3, 8, "staccionata_alto_sinistra");
        addStaccionata(4, 8, "staccionata_alto_centro");
        addStaccionata(5, 8, "staccionata_alto_centro");
        addStaccionata(6, 8, "staccionata_alto_centro");
        addStaccionata(7, 8, "staccionata_alto_centro");
        addStaccionata(8, 8, "staccionata_alto_centro");
        addStaccionata(9, 8, "staccionata_alto_destra");
        addStaccionata(9, 7, "staccionata_centro_destra");
        addStaccionata(9, 6, "staccionata_basso_destra");
        
        addStaccionata(21, 6, "staccionata_basso_centro");
        addStaccionata(20, 6, "staccionata_basso_sinistra");
        addStaccionata(20, 7, "staccionata_centro_sinistra");
        addStaccionata(20, 8, "staccionata_centro_sinistra");
        addStaccionata(20, 9, "staccionata_alto_sinistra");
        addStaccionata(21, 9, "staccionata_alto_centro");
        addStaccionata(22, 9, "staccionata_alto_centro");
        addStaccionata(23, 9, "staccionata_alto_centro");
        addStaccionata(24, 9, "staccionata_alto_destra");
        addStaccionata(24, 8, "staccionata_centro_destra");
        addStaccionata(24, 7, "staccionata_centro_destra");
        
        addStaccionata(27, 13, "staccionata_basso_centro");
        addStaccionata(28, 13, "staccionata_basso_destra");
        addStaccionata(28, 14, "staccionata_centro_destra");
        addStaccionata(28, 15, "staccionata_centro_destra");
        addStaccionata(28, 16, "staccionata_alto_destra");
        addStaccionata(27, 16, "staccionata_alto_centro");
        addStaccionata(26, 16, "staccionata_alto_centro");
        addStaccionata(25, 16, "staccionata_alto_centro");
        addStaccionata(24, 16, "staccionata_alto_centro");
        addStaccionata(23, 16, "staccionata_alto_sinistra");
        addStaccionata(23, 15, "staccionata_centro_sinistra");
        
    }

    private void addStaccionata(int xObj, int yObj, String nomeStaccionata) {
        List<GridPoint2> unwalkable = new ArrayList<GridPoint2>();
        GridPoint2 element = new GridPoint2(xObj, yObj);
        int sizeX = 1;
        int sizeY = 1;
        for(int i=0; i<sizeY; i++){
            for(int j=0; j<sizeX; j++){
                GridPoint2 element1 = new GridPoint2(xObj+j, yObj+i);
                unwalkable.add(element1);
            }
        }
        TextureAtlas atlas = getApp().getAssetManager().get("res/packed/textures.atlas", TextureAtlas.class);
        TextureRegion staccionataRegion = atlas.findRegion(nomeStaccionata);
        WorldObject staccionata = new WorldObject(xObj, yObj, staccionataRegion, sizeX, sizeY, unwalkable, nomeStaccionata, false, true);
        world.addObject(staccionata);
    }
    private void addFiori() {
        addFiore(4, 7, "fiori_blu");
        addFiore(5, 7, "fiori_blu");
        addFiore(6, 7, "fiori_blu");
        addFiore(7, 7, "fiori_blu");
        addFiore(8, 7, "fiori_blu");
        
        addFiore(9, 13, "fiori_rosa");
        addFiore(9, 14, "fiori_rosa");
        addFiore(10, 13, "fiori_rosa");
        addFiore(10, 14, "fiori_rosa");
        addFiore(11, 13, "fiori_rosa");
        addFiore(11, 14, "fiori_rosa");
        
        addFiore(21, 7, "fiori_rosa");
        addFiore(21, 8, "fiori_rosa");
        addFiore(22, 7, "fiori_rosa");
        addFiore(22, 8, "fiori_rosa");
        addFiore(23, 7, "fiori_rosa");
        addFiore(23, 8, "fiori_rosa");
        
        addFiore(24, 15, "fiori_blu");
        addFiore(25, 15, "fiori_blu");
        addFiore(26, 15, "fiori_blu");
        addFiore(27, 15, "fiori_blu");
        addFiore(27, 14, "fiori_blu");
        
        addFiore(2, 21, "fiori_blu");
        addFiore(2, 22, "fiori_blu");
        addFiore(2, 23, "fiori_blu");
        addFiore(3, 21, "fiori_blu");
        addFiore(3, 22, "fiori_blu");
        addFiore(3, 23, "fiori_blu");
        addFiore(3, 24, "fiori_blu");
        addFiore(4, 21, "fiori_blu");
        addFiore(4, 22, "fiori_blu");
        addFiore(4, 23, "fiori_blu");
        addFiore(4, 24, "fiori_blu");
        addFiore(5, 21, "fiori_blu");
        addFiore(5, 22, "fiori_blu");
        addFiore(5, 23, "fiori_blu");
        addFiore(5, 24, "fiori_blu");
        addFiore(6, 21, "fiori_blu");
        addFiore(6, 22, "fiori_blu");
        addFiore(6, 23, "fiori_blu");
        
        addFiore(28, 21, "fiori_rosa");
        addFiore(28, 22, "fiori_rosa");
        addFiore(28, 23, "fiori_rosa");
        addFiore(27, 21, "fiori_rosa");
        addFiore(27, 22, "fiori_rosa");
        addFiore(27, 23, "fiori_rosa");
        addFiore(27, 24, "fiori_rosa");
        addFiore(26, 21, "fiori_rosa");
        addFiore(26, 22, "fiori_rosa");
        addFiore(26, 23, "fiori_rosa");
        addFiore(26, 24, "fiori_rosa");
        addFiore(25, 21, "fiori_rosa");
        addFiore(25, 22, "fiori_rosa");
        addFiore(25, 23, "fiori_rosa");
        addFiore(25, 24, "fiori_rosa");
        addFiore(24, 21, "fiori_rosa");
        addFiore(24, 22, "fiori_rosa");
        addFiore(24, 23, "fiori_rosa");
    }
    private void addFiore(int xObj, int yObj, String nomeFiore) {
        List<GridPoint2> unwalkable = new ArrayList<GridPoint2>();
        GridPoint2 element = new GridPoint2(xObj, yObj);
        int sizeX = 1;
        int sizeY = 1;
        for(int i=0; i<sizeY; i++){
            for(int j=0; j<sizeX; j++){
                GridPoint2 element1 = new GridPoint2(xObj+j, yObj+i);
                unwalkable.add(element1);
            }
        }
        TextureAtlas atlas = getApp().getAssetManager().get("res/packed/textures.atlas", TextureAtlas.class);
        TextureRegion fioreRegion = atlas.findRegion(nomeFiore);
        WorldObject fiore = new WorldObject(xObj, yObj, fioreRegion, sizeX, sizeY, unwalkable, nomeFiore, false, true);
        world.addObject(fiore);
    }
    
    private void addCestino(int xObj, int yObj, String nomeCestino) {
        List<GridPoint2> unwalkable = new ArrayList<GridPoint2>();
        GridPoint2 element = new GridPoint2(xObj, yObj);
        int sizeX = 1;
        int sizeY = 1;
        for(int i=0; i<sizeY; i++){
            for(int j=0; j<sizeX; j++){
                GridPoint2 element1 = new GridPoint2(xObj+j, yObj+i);
                unwalkable.add(element1);
            }
        }
        TextureAtlas atlas = getApp().getAssetManager().get("res/packed/textures.atlas", TextureAtlas.class);
        TextureRegion CestinoRegion = atlas.findRegion(nomeCestino);
        WorldObject fiore = new WorldObject(xObj, yObj, CestinoRegion, sizeX, sizeY, unwalkable, nomeCestino, false, true);
        world.addObject(fiore); 
    }
    
    private void addSpazzatura(int id, String nomeSpazzatura, int coordX, int coordY) {
        
        int xObj, yObj;
        if(coordX == -1 && coordY ==-1){
            Random r = new Random();
            int xMax = 23, xMin = 8, yMax = 24, yMin = 17; 
            xObj = r.nextInt((xMax - xMin) + 1) + xMin;
            yObj = r.nextInt((yMax - yMin) + 1) + yMin;
            while(world.getMap().getTile(xObj, yObj).getObject()!=null || world.getMap().getTile(xObj, yObj).getActor()!=null){
                xObj = r.nextInt((xMax - xMin) + 1) + xMin;
                yObj = r.nextInt((yMax - yMin) + 1) + yMin;

            }
        }
        else{
            xObj = coordX;
            yObj = coordY;
        }
        //WorldObject sas = world.getMap().getTile(4, 15).getObject();
        List<GridPoint2> unwalkable = new ArrayList<GridPoint2>();
        GridPoint2 element = new GridPoint2(xObj, yObj);
        int sizeX = 1;
        int sizeY = 1;
        for(int i=0; i<sizeY; i++){
            for(int j=0; j<sizeX; j++){
                GridPoint2 element1 = new GridPoint2(xObj+j, yObj+i);
                unwalkable.add(element1);
            }
        }
        TextureAtlas atlas = getApp().getAssetManager().get("res/packed/textures.atlas", TextureAtlas.class);
        TextureRegion CestinoRegion = atlas.findRegion(nomeSpazzatura);
        WorldObject fiore = new WorldObject(id, xObj, yObj, CestinoRegion, sizeX, sizeY, unwalkable, nomeSpazzatura, false, true);
        world.addObject(fiore);
    }
    
    
    private void initUI() {
        //creiamo una schermata indipendente dalla schermata del mondo e che può avere risoluzione diversa
        uiStage = new Stage(new ScreenViewport());
        //settiamo la dimensione della schermata che ospita il testo (Viewport)
        uiStage.getViewport().update(Gdx.graphics.getWidth()/uiScale, Gdx.graphics.getHeight()/uiScale, true);
        
        //settiamo ciò che ospiterà il nostro dialogueBox
        root = new Table();
        root.setFillParent(true);
        //aggiungiamo la root come attore dello uiStage per visualizzarlo
        uiStage.addActor(root);
        
        //inizializiamo la variabile che conterrà il testo da visualizzare
        dialogueBox = new DialogueBox(getApp().getSkin());
        dialogueBox.animateText("");
        
        root.add(dialogueBox).expand().align(Align.bottom).pad(8f);
        
        
    }

    //funzione che permette di connettersi con la webApp e di reperire informazioni dal database tramita appunto la webApplication
    private void LoadActor (AnimationSet animationsBoy) throws NoSuchAlgorithmException, KeyManagementException{
        URL url;
            HttpURLConnection con = null;
        try {
            validate();
            url = new URL("http://localhost:44372/api/actor");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            int status = con.getResponseCode();
            //qui il controllo degli errori nella risposta (status code)
//            Reader streamReader = null;
//            if (status > 299) {
//                streamReader = new InputStreamReader(con.getErrorStream());
//            } else {
//                streamReader = new InputStreamReader(con.getInputStream());
//            }
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            JSONObject jsonObject = new JSONObject(content.toString());
            String nameActor = jsonObject.getString("name");
            int xActor = jsonObject.getInt("coordX");
            int yActor = jsonObject.getInt("coordY");
            String trashActor = jsonObject.getString("trash");
            int idActor = jsonObject.getInt("id");
            player = new Actor(idActor, world.getMap(), xActor, yActor, animationsBoy, nameActor, trashActor);
        } catch (MalformedURLException ex) {
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            con.disconnect();
        }
    }

    

    private void LoadObjects() throws NoSuchAlgorithmException, KeyManagementException {
        URL url;
            HttpURLConnection con = null;
        try {
            validate();
            url = new URL("http://localhost:44372/api/trash");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            int status = con.getResponseCode();
            //qui il controllo degli errori nella risposta (status code)
//            Reader streamReader = null;
//            if (status > 299) {
//                streamReader = new InputStreamReader(con.getErrorStream());
//            } else {
//                streamReader = new InputStreamReader(con.getInputStream());
//            }
            BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            
            
            JSONArray array = new JSONArray(content.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                int idTrash = jsonObject.getInt("id");
                String nameTrash = jsonObject.getString("name");
                int xTrash = jsonObject.getInt("coordX");
                int yTrash = jsonObject.getInt("coordY");
                boolean activeTrash = jsonObject.getBoolean("active");
                if(activeTrash){
                    addSpazzatura(idTrash, nameTrash, xTrash, yTrash);
                }
            }
            
            
            in.close();
            
            
            
            
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            con.disconnect();
        }
    }
    
    //funzione che permette di bypassare il controlle del certificato ssl in modo da permettere la connessione all'applicazione web
    public static void validate() throws NoSuchAlgorithmException, KeyManagementException{
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };
 
        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
 
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
 
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
    
}
