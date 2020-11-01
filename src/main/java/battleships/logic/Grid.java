package battleships.logic;


public class Grid {
    
    private GridPoint[][] gridMap;
    private int size;
    
    public Grid(int size) {
        this.gridMap = new GridPoint[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                gridMap[i][j] = new GridPoint();
            }
        }
        this.size = size;
    }
    
    public boolean add(int x, int y, Direction direction, int length) {
        
        int xdir = 0;
        int ydir = 0;
        if (direction == Direction.HORIZONTAL) xdir = 1;
        else ydir = 1;
        
        if(x < 0 || y < 0 || x+xdir*length >= size || y+ydir*length >= size) return false;
        
        for (int i = 0; i < length; i++) {
            if (shipNear(x+i*xdir,y+i*ydir)) {
                return false;
            }
        }
        
        Ship ship = new Ship(length);
        
        for (int i = 0; i < length; i++) {
            gridMap[x+i*xdir][y+i*ydir].setShip(ship);
        }
        
        return true;
    }
    
    public boolean shipNear(int x, int y) {
        for (int i = x-1; i < x+1; i++) {
            for (int j = y-1; j < y+1; j++) {
                if (i < 0 || j >= size || j < 0 || j >= size) continue;
                if (gridMap[i][j].getShip() != null) return true;
            }
        }
        return false;
    }

    public GridPoint[][] getGridMap() {
        return gridMap;
    }

    public int getSize() {
        return size;
    }
    
    
}
