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
    private int maxCycles;
    private int maxNeighborChecks;
    private double randomShotChance;
    
    public ComputerLogic() {
        this.maxCycles = 10000;
        this.maxNeighborChecks = 20;
        this.randomShotChance = 0.05;
    }
    
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

        for(int length: Ships) {
            int left = fleet.get(length);
            while (left > 0 && count < maxCycles) {
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
                }
                
            }
            
            if (count >= maxCycles) {
                grid.clearGrid();
                fillGrid(grid,settings);
            }
        }
    }
    
    /**
     * Shoots at random location of enemy grid. This is a backup mechanism
     * where we should not end up usually.
     * @param grid  Enemy's grid object
     * @return Result of the shot
     */
    private GridState shootRandom(Grid grid) {
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
            state = shootRandom(grid);
        }

        return state;
    }
    
    /**
     * Attempts to shoot smartly by looking if there are ships that have been
     * hit but not sunk yet and makes firing decision based on that.
     * @param grid Grid object of the target
     * @return Result of the shot
     */
    private GridState shootSmart(Grid grid) {
        ArrayList<Integer> xHits = new ArrayList<>();
        ArrayList<Integer> yHits = new ArrayList<>();
        int size = grid.getSize();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (grid.getGridState(x, y) == GridState.HIT) {
                    xHits.add(x);
                    yHits.add(y);
                }
            }
        }
        Collections.sort(xHits);
        Collections.sort(yHits);

        GridState state = GridState.UNKNOWN;
        if (xHits.size() > 1) {
            if (yHits.get(0) == yHits.get(yHits.size()-1)) {
                state = shootSubGrid(grid,xHits.get(0)-1,xHits.get(xHits.size()-1)+1,yHits.get(0),yHits.get(0));
            } else {
                state = shootSubGrid(grid,xHits.get(0),xHits.get(0),yHits.get(0)-1,yHits.get(yHits.size()-1)+1);
            }
        } else if (xHits.size() == 1) {
            state = shootSubGrid(grid,xHits.get(0)-1,xHits.get(0)+1,yHits.get(0)-1,yHits.get(0)+1);
        }
        
        // If there are no ships that have been hit but not sunk, shoot either
        // semismartly or randomly.
        if (state == GridState.UNKNOWN) {
            Random r = new Random();
            if (r.nextDouble() < randomShotChance) state = shootRandom(grid);
            else state = shootSubGrid(grid,0,size-1,0,size-1);
        }
        return state;
    }
    
    /**
     * Attempts to shoot at subgrid of grid.
     * @param grid Grid object where to shoot
     * @param x0 Lower limit of x-coordinate
     * @param x1 Upper limit of x-coordinate
     * @param y0 Lower limit of y-coordinate
     * @param y1 Upper limit of y-coordinate
     * @return Result of the shot
     */
    private GridState shootSubGrid(Grid grid, int x0, int x1, int y0, int y1) {
        if (x0 < 0) x0 = 0;
        if (y0 < 0) y0 = 0;
        if (x1 >= grid.getSize()) x1 = grid.getSize()-1;
        if (y1 >= grid.getSize()) y1 = grid.getSize()-1;
        GridState state = GridState.UNKNOWN;
        Random r = new Random();
        int x = x0;
        int y = y0;
        int neighborChecks = 0;
        boolean noNeighborsShot = false;
        boolean hitPossible = false;
        while (neighborChecks < maxNeighborChecks && !noNeighborsShot) {
            hitPossible = false;
            int cycles = 0;
            while (cycles < maxCycles && !hitPossible) {
                if (x0 < x1) x = r.nextInt(x1 - x0 + 1) + x0;
                if (y0 < y1) y = r.nextInt(y1 - y0 + 1) + y0;
                cycles++;
                hitPossible = hitPossible(grid, x, y);
            }
            if (hitPossible) noNeighborsShot = noNeighborsShot(grid, x, y); 

            neighborChecks++;
        }
        if (hitPossible) state = grid.shootAt(x, y);
        return state;
    }
    
    
    /**
     * Checks if shooting at x,y makes sense based on neighboring grid points.
     * @param grid Grid object of the target
     * @param x x-coordinate of the target
     * @param y y-coordinate of the target
     * @return True if shooting at x,y makes sense, false otherwise
     */
    private boolean hitPossible(Grid grid, int x, int y) {
        if (grid.getGridState(x, y) != GridState.UNKNOWN) return false;
        if (grid.getGridState(x-1, y) == GridState.SUNK) return false;
        if (grid.getGridState(x+1, y) == GridState.SUNK) return false;
        if (grid.getGridState(x, y-1) == GridState.SUNK) return false;
        if (grid.getGridState(x, y+1) == GridState.SUNK) return false;
        if (grid.getGridState(x-1, y-1) == GridState.HIT || grid.getGridState(x-1, y-1) == GridState.SUNK)
            return false;
        if (grid.getGridState(x+1, y-1) == GridState.HIT || grid.getGridState(x+1, y-1) == GridState.SUNK)
            return false;
        if (grid.getGridState(x-1, y+1) == GridState.HIT || grid.getGridState(x-1, y+1) == GridState.SUNK)
            return false;
        if (grid.getGridState(x+1, y+1) == GridState.HIT || grid.getGridState(x+1, y+1) == GridState.SUNK)
            return false;
        
        return true;
    }
    
    /**
     * Checks if grid points horizontally or vertically next to (x,y) have been shot at
     * @param Grid object of the target
     * @param x x-coordinate of the target
     * @param y y-coordinate of the target
     * @return True if neighborin points haven't been shot at, otherwise false
     */
    private boolean noNeighborsShot(Grid grid, int x, int y) {
        if (grid.getGridState(x-1, y) != GridState.UNKNOWN) return false;
        if (grid.getGridState(x+1, y) != GridState.UNKNOWN) return false;
        if (grid.getGridState(x, y-1) != GridState.UNKNOWN) return false;
        if (grid.getGridState(x, y+1) != GridState.UNKNOWN) return false;
        
        return true;
    }
    
    /**
     * Tells computer to shoot. At first a smart shot is attempted. If that is
     * unsuccessful, shoots at random coordinates
     * @param grid Grid object of the target
     * @return Result of the shot
     */
    public GridState shoot(Grid grid) {
        GridState state = shootSmart(grid);
        if (state == GridState.UNKNOWN) state = shootRandom(grid);
        return state;
    }
    
}
