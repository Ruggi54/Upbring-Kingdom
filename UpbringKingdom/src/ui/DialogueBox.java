/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

/**
 *
 * @author simon
 */
public class DialogueBox extends Table{
    public String targetText = "";
    private float animTimer = 0f;
    private float animationTotalTime = 0f;
    private float TIME_PER_CHARACTER = 0.02f;
    public STATE state = STATE.IDLE;
    
    //variabile per controllare quando il dialogo deve essere considerato attivo e quindi da visualizzare
    public boolean active = false;

    private Label textLabel;

    //classe enum che gestisce gli stati del testo
    private enum STATE {
        ANIMATING,
        IDLE,;
    }

    //Costruttore della classe
    public DialogueBox(Skin skin) {
        super(skin); //restituisce la skin da mettere al dialoguebox
        this.setBackground("dialoguebox"); //mette il background della dialoguebox, ovvero la skin
        textLabel = new Label("\n", skin); //variabile che contiene il testo da visualizzare applicando la skin
        this.add(textLabel).expand().align(Align.left).pad(5f); //aggiunge il testo al dialoguebox con tutte le proprietà (pad(5f) è la grandezza del font)
    }

    //funzione per inizializzare le variabili per l'animazione e la scrittura del testo
    public void animateText(String text) {
        targetText = text;
        animationTotalTime = text.length() * TIME_PER_CHARACTER;
        state = STATE.ANIMATING;
        animTimer = 0f;
    }

    public boolean isFinished() {
        if (state == STATE.IDLE) {
            return true;
        } else {
            return false;
        }
    }

    private void setText(String text) {
        if (!text.contains("\n")) {
            text += "\n";
        }
        this.textLabel.setText(text);
    }

    @Override
    public void act(float delta) {
        if (state == STATE.ANIMATING) {
            animTimer += delta;
            if (animTimer > animationTotalTime) {
                state = STATE.IDLE;
                animTimer = animationTotalTime;
            }
            String actuallyDisplayedText = "";
            int charactersToDisplay = (int) ((animTimer / animationTotalTime) * targetText.length());
            for (int i = 0; i < charactersToDisplay; i++) {
                actuallyDisplayedText += targetText.charAt(i);
            }
            if (!actuallyDisplayedText.equals(textLabel.getText().toString())) {
                setText(actuallyDisplayedText);
            }
        }
    }

    @Override
    public float getPrefWidth() {
        return 200f;
    }
}
