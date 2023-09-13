package Model;

//classe che contiene tutte le possibili direzioni di un personaggio
public enum DIRECTION {
    
    //creazione direzioni sottoforma di zoordinate
    NORTH(0,1),
    EAST(1,0),
    SOUTH(0,-1),
    WEST(-1,0),
    ;
    
    //creazione valori di direzione x e y
    private int dx, dy;
    
    //costruttore della classe
    private DIRECTION(int dx, int dy){
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }
    
    
}
