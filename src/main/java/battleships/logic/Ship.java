
package battleships.logic;

/**
 * Ship class stores and modifies status of Ship objects. Ship locations are
 * stored in GridPoints, which are accessed through Grid objects.
 * 
 * @author Janne
 */
public class Ship {
    
    private int length;
    private int hits;
    
    public Ship(int length) {
        this.length = length;
        this.hits = 0;
    }
    
    /**
     * Adds +1 to hits until the amount of hits equals to ship length.
     */
    public void addHit() {
        if (hits < length) hits++;
    }
    
    /**
     * Checks if the ship has been sunk.
     * @return true if ship is sunk, otherwise false
     */
    public boolean sunk() {
        return hits >= length;
    }
    
}
