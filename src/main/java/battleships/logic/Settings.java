
package battleships.logic;

import java.util.HashMap;
import java.util.Map;

/**
 * Settings class stores and modifies game settings.
 * 
 * @author Janne
 */
public class Settings {
    
    private int gridSize;
    private Map<Integer,Integer> fleet;
    
    
    public Settings(int gridSize) {
        this.gridSize = gridSize;
        this.fleet = new HashMap<>();
  
    }

    public int getGridSize() {
        return gridSize;
    }
    
    public Map<Integer,Integer> getFleet() {
        return fleet;
    }
    
    /**
     * Adds new ship length and count to fleet.
     * @param length ship length
     * @param count count of ships of this type
     */
    public void addShipType(int length, int count) {
        if (length < 2 || length > 5) return;
        if (count < 1) return;
        
        fleet.put(length, count);
    }
    
}
