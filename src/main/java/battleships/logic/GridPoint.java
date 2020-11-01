
package battleships.logic;


public class GridPoint {
    
    private GridState state;
    private Ship ship;
    
    public GridPoint() {
        this.state = GridState.UNKNOWN;
        this.ship = null;
    }

    public GridState getState() {
        return state;
    }

    public void setState(GridState state) {
        this.state = state;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }
    
    
    
}
