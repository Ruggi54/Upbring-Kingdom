package upbringkingdom;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {

    public static void main(String[] args) {
        //creazione dell'istaza della classe che definisce l'applicazione eseguibile
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        
        config.title = "Upbring Kingdom Game";
        config.height = 1080;
        config.width = 1920;
        config.vSyncEnabled = true;
        
        //creazione e visualizzazione dell'applicazione eseguibile caricando il gioco tramite la classe UpbringKingdom
        new LwjglApplication(new UpbringKingdom(), config); 
    }
    
}
