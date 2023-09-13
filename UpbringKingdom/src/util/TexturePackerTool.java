package util;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerTool {
    
    public static void main(String[] args) {
        //Va a creare un'immagine con tutte le immagini del personaggio.
        //Il primo elemento è la cartella di input, il secondo è la cartella di output, mentre il terzo è il nome dell'atlas
        TexturePacker.process("res/unpacked/",
                "res/packed/",
                "textures");
    }
}
