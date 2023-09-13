package upbringkingdom;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import upbringkingdom.screen.GameScreen;
import util.SkinGenerator;

public class UpbringKingdom extends Game{

    //creazione variabile di appoggio per lo schermo
    private GameScreen screen;
    
    //crazione variabile di tipo AssetManager
    private AssetManager assetManager;
    
    private Skin skin;
    
    //override del metodo create che usiamo per settare lo screen con i parametri che vogliamo
    @Override
    public void create() {
        //istanziamo la variabile assetManager
        assetManager = new AssetManager();
        //carichiamo la variabile assetManager con l'atlas del file con le texture del personaggio
        //grazie a quello riesce a decifrare l'immagine e a trovare tramite la posizione in pixel tutte le texture
        assetManager.load("res/packed/textures.atlas", TextureAtlas.class); 
        assetManager.load("res/packed/uipack.atlas", TextureAtlas.class); 
        assetManager.load("res/font/small_letters_font.fnt", BitmapFont.class);
        assetManager.finishLoading();
        skin = SkinGenerator.generateSkin(assetManager);
        try {
            screen = new GameScreen(this);
        } catch (IOException ex) {
            Logger.getLogger(UpbringKingdom.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setScreen(screen);
    }

    //override del metodo render che usiamo per settare il colore base dello schermo
    @Override
    public void render() {
        //Pulizia dello schermo ad ogni frame per evitare eventuali bug tra frame che si sovrappongono
        //definizione colore dello schermo in esadecimale
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        
        //caricamento del colore settato nello schermo
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public Skin getSkin() {
		return skin;
    }
    
    
    
}
