
package battleships.logic;

/**
 * Main class of game's logic. Contains functions for generating settings,
 * grids and delivering them to UI.
 * 
 * @author Janne
 */
public class GameMain {
    
    private Settings settings;
    private Grid playerGrid;
    private Grid computerGrid;
    private ComputerLogic logic;
    
    public GameMain() {
        this.logic = new ComputerLogic();
        this.settings = new Settings(6);
        initShips();
    }
    
    /**
     * Initializes the game by generating new Grid objects for player and computer
     * and filling computer's grid.
     */
    public void initGame() {
        playerGrid = new Grid(settings.getGridSize());
        computerGrid = new Grid(settings.getGridSize());
        logic.fillGrid(computerGrid, settings);
    }
    
    /**
     * Generates ship setup for the game.
     */
    public final void initShips() {
        settings.addShipType(4, 1);
        settings.addShipType(3, 1);
        settings.addShipType(2, 2);
        
    }

    public Settings getSettings() {
        return settings;
    }

    public Grid getPlayerGrid() {
        return playerGrid;
    }
    
    public Grid getComputerGrid() {
        return computerGrid;
    }
    
    public ComputerLogic getComputerLogic() {
        return logic;
    }
    
    
    
    
}
