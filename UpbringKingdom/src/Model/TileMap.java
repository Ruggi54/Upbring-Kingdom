package Model;

public class TileMap {
    
    //istanziamo la larghezza e l'altezza della mappa
    private int width, height;
    
    //istanziamo una matrice di tipo Tile
    private Tile[][] tiles;
    
    //costruttore della classe che va a settare la mappa completa
    public TileMap(int width, int height) {
        this.width = width; //settiamo la larghezza
        this.height = height; //settiamo l'altezza
        tiles = new Tile[width][height];//definizione della grandezza della matrice
        //scorriamo la matrice per caricare i tipi di terreno in ogni cella 
       for(int x=0; x<width; x++){
            for(int y=0; y<height; y++){
                //creiamo un random per decidere quale dei due tipi di erba utilizziamo
                if(Math.random()*10 > 5){
                    tiles[x][y]=new Tile(Terrain.Grass1); //carichiamo il primo tipo di erba
                }
                else{
                    tiles[x][y]=new Tile(Terrain.Grass2); //carichiamo il secondo tipo di erba
                }
                
            }
        }
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    
    
    
}
