
package battleships.logic;


public class Ship {
    
    private int length;
    private int x;
    private int y;
    private Direction direction;
    
    public Ship(int length) {
        this.length = length;
        this.x = -1;
        this.y = -1;
        this.direction = Direction.HORIZONTAL;
    }
    
}
