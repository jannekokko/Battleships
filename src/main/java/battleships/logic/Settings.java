
package battleships.logic;

import java.util.HashMap;
import java.util.Map;


public class Settings {
    
    private int gridSize;
    private Map<Integer,Integer> fleet;
    
    
    public Settings() {
        gridSize = 10;
        this.fleet = new HashMap<>();
  
    }

    public int getGridSize() {
        return gridSize;
    }
    
    public Map<Integer,Integer> getFleet() {
        return fleet;
    }
    
    public void addShipType(int length, int count) {
        if (length < 2 || length > 5) return;
        if (count < 1) return;
        
        fleet.put(length, count);
    }
    
}
