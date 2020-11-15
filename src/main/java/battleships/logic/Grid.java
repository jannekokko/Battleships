
package battleships.logic;

import java.util.ArrayList;


/**
 * Class for storing and modifying grid data.
 * 
 * @author Janne
 */
public class Grid {
    
    private GridPoint[][] gridMap;
    private int size;
    private int unknownPoints;
    
    public Grid(int size) {
        this.size = size;
        clearGrid();
    }
    
    /**
     * Resets GridPoints from Grid
     */
    public void clearGrid() {
        this.gridMap = new GridPoint[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                gridMap[i][j] = new GridPoint();
            }
        }
        
        this.unknownPoints = size * size;
    }
    
    /**
     * Attemps to add ship to Grid
     * @param x Top left x-coordinate
     * @param y Top left y-coordinate
     * @param direction Direction (horizontal or vertical)
     * @param length Ship length
     * @return true if successful, otherwise false
     */
    public boolean add(int x, int y, Direction direction, int length) {
        int xdir = 0;
        int ydir = 0;
        if (direction == Direction.HORIZONTAL) xdir = 1;
        else ydir = 1;
        
        if(x < 0 || y < 0 || x+xdir*(length-1) >= size || y+ydir*(length-1) >= size) {
//            System.out.println("out of bounds error");
            return false;
        }
        
        for (int i = 0; i < length; i++) {
            if (shipNear(x+i*xdir,y+i*ydir)) {
//                System.out.println("other ship too near");
                return false;
            }
        }
        
        Ship ship = new Ship(length);
        
        for (int i = 0; i < length; i++) {
            gridMap[x+i*xdir][y+i*ydir].setShip(ship);
        }
        
        return true;
    }
    
    /**
     * Attempt to shoot at coordinates
     * @param x x coordinate of target
     * @param y y coordinate of target
     * @return GridState after shot has been fired. Returns GridState.UNKNOWN
     * if the target coordinates were invalid
     */
    public GridState shootAt(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) return GridState.UNKNOWN;
        GridState state = gridMap[x][y].getState();
        if (state != GridState.UNKNOWN) return GridState.UNKNOWN;
        
        Ship ship = gridMap[x][y].getShip();
        
        if (ship == null) {
            state = GridState.MISS;
        } else {
            ship.addHit();
            updateSunkenShips();
            state = ship.sunk() ? GridState.SUNK : GridState.HIT;
        }
        unknownPoints--;
        
        gridMap[x][y].setState(state);

        return state;
    }
    
    /**
     * Goes through Grid to find out if there are any ships that are completely
     * sunk and sets the state of those gridpoints to GridState.SUNK
     */
    public void updateSunkenShips() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Ship ship = gridMap[x][y].getShip();
                if (ship != null && ship.sunk()) gridMap[x][y].setState(GridState.SUNK);                
            }
        }
    }
    
    /**
     * Returns number of ships left on Grid.
     * @return Number of ships left on Grid
     */
    public int shipsLeft() {
        ArrayList<Ship> ships = new ArrayList<>();
        int sunken = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Ship ship = gridMap[x][y].getShip();
                if(ship != null && !ships.contains(ship)) {
                    ships.add(ship);
                    if (ship.sunk()) sunken++;
                }
            }
        }
        return ships.size() - sunken;
    }
    
    /**
     * Checks if there is a ship close to x,y
     * @param x x coordinate
     * @param y y coordinate
     * @return True of there is a ship near, otherwise false
     */
    public boolean shipNear(int x, int y) {
        for (int i = x-1; i <= x+1; i++) {
            for (int j = y-1; j <= y+1; j++) {
                if (i < 0 || i >= size || j < 0 || j >= size) continue;
                if (gridMap[i][j].getShip() != null) return true;
            }
        }
        return false;
    }

    public GridPoint[][] getGridMap() {
        return gridMap;
    }
    
    public GridState getGridState(int x, int y) {
        return gridMap[x][y].getState();
    }

    public int getSize() {
        return size;
    }
    
    public int getUnknownPoints() {
        return unknownPoints;
    }
    
    
}
