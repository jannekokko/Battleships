package battleships.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Contains methods for completing enemy's (computer) tasks. They include filling
 * grid and firing at the enemy.
 * @author Janne
 */
public class ComputerLogic {
    
    /**
     * Fills computer's Grid with Ships.
     * @param grid Computer's Grid object
     * @param settings Settings object
     */
    public void fillGrid(Grid grid, Settings settings) {
        Random r = new Random();
        Map<Integer,Integer> fleet = settings.getFleet();
        int size = grid.getSize();
        List<Integer> Ships = new ArrayList<>(fleet.keySet());
        Collections.sort(Ships, Collections.reverseOrder());
        int count = 0;
        int maxCount = 10000;   // In case we end up with impossible arrangement

        for(int length: Ships) {
            int left = fleet.get(length);
            while (left > 0 && count < maxCount) {
                count++;
                Direction dir = Direction.values()[r.nextInt(2)];
                int x;
                int y;
                if (dir.equals(Direction.HORIZONTAL)) {
                    x = r.nextInt(size-length);
                    y = r.nextInt(size);
                } else {
                    x = r.nextInt(size);
                    y = r.nextInt(size-length);
                }
                if(grid.add(x, y, dir, length)) {
                    left--;
//                    System.out.println("x: " + x + " y: " + y + " dir: " + dir.toString() + " length: " + length);
                }
                
            }
            
            if (count >= maxCount) {
                grid.clearGrid();
                fillGrid(grid,settings);
            }
        }
    }
    
    /**
     * Shoots at random location of enemy grid
     * @param grid  Enemy's grid object
     */
    public GridState shoot(Grid grid) {
        Random r = new Random();
        int size = grid.getSize();
        
        int target = r.nextInt(grid.getUnknownPoints());
        
        GridState state = GridState.UNKNOWN;
        
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (grid.getGridState(x, y) == GridState.UNKNOWN) {
                    if (target == 0) {
                        state = grid.shootAt(x, y);
                    }
                    target--;
                }
            }
        }
        if (state == GridState.UNKNOWN) {
            state = shoot(grid);
        }
        return state;
    }
    
    
}
