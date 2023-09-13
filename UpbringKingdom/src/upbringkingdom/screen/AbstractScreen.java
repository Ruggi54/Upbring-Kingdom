/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upbringkingdom.screen;

import com.badlogic.gdx.Screen;
import upbringkingdom.UpbringKingdom;

/**
 *
 * @author simon
 */
public abstract class AbstractScreen implements Screen{

    private UpbringKingdom app;
    
    //costruttore che permette il collegamento con la classe principale UpbringKingdom
    public AbstractScreen (UpbringKingdom app){
        this.app = app;
    }
    
    @Override
    public abstract void show();

    @Override
    public abstract void render(float delta);

    @Override
    public abstract void resize(int weight, int height);

    @Override
    public abstract void pause();

    @Override
    public abstract void resume();

    @Override
    public abstract void hide();

    @Override
    public abstract void dispose();

    public UpbringKingdom getApp() {
        return app;
    }
    
    
    
}
