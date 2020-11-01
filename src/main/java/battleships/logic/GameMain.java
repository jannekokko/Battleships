
package battleships.logic;


public class GameMain {
    
    Settings settings;
    Grid playerGrid;
    
    public GameMain() {
        settings = new Settings();
        playerGrid = new Grid(settings.getGridSize());
        initShips();
    }
    
    
    public final void initShips() {
        settings.addShipType(4, 1);
        settings.addShipType(3, 2);
        settings.addShipType(2, 3);
        
    }

    public Settings getSettings() {
        return settings;
    }

    public Grid getPlayerGrid() {
        return playerGrid;
    }
    
    
    
    
}
