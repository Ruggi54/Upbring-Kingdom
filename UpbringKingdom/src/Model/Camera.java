package Model;

//Classe che identifica la camera, ovvero il punto di vista dell'utente
public class Camera {
    
    //istanziamo le coordinate x e y della camera
    private float cameraX = 0f;
    private float cameraY = 0f;
    
    //funzione di aggiornamento della camera
    public void update(float newCamX, float newCamY){
        this.cameraX = newCamX; //set della nuova posizione x della camera
        this.cameraY = newCamY; //set della nuova posizione y della camera
    }

    public float getCameraX() {
        return cameraX;
    }

    public float getCameraY() {
        return cameraY;
    }
    
    
    
}
