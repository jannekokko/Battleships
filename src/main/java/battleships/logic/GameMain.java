
package battleships.logic;

import battleships.db.PlayerData;
import battleships.db.PlayerDatabase;
import java.sql.SQLException;
import java.util.List;

/**
 * Main class of game's logic. Contains functions for generating settings,
 * grids, database connection and delivering them to UI.
 * 
 * @author Janne
 */
public class GameMain {
    
    private Settings settings;
    private Grid playerGrid;
    private Grid computerGrid;
    private ComputerLogic logic;
    private Result result;
    private PlayerDatabase playerDB;
    private int playerId;
    private String playerName;
    private int computerId;
    
    public GameMain() {
        this.logic = new ComputerLogic();
        this.settings = new Settings(6);
        this.result = Result.UNFINISHED;
        try {
            this.playerDB = new PlayerDatabase();
        } catch (SQLException e) {
            System.out.println("Database error!");
        }
        this.playerId = -1;
        this.computerId = -1;
        setDefaultIds();
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
    
    /**
     * Checks if end conditions of the game are met, and updates database if so
     * @return Result as enum
     */
    public Result checkResult() {
        Result oldResult = result;
        if(playerGrid.shipsLeft() <= 0 && computerGrid.shipsLeft() <= 0) {
            result = Result.DRAW;
            if (oldResult == Result.UNFINISHED) {
                playerDB.addDraw(playerId);
                playerDB.addDraw(computerId);
            }
        } else if(playerGrid.shipsLeft() <= 0) {
            result = Result.COMPUTERWON;
            if (oldResult == Result.UNFINISHED) {
                playerDB.addLoss(playerId);
                playerDB.addWin(computerId);
            }
        } else if(computerGrid.shipsLeft() <= 0) {
            result = Result.PLAYERWON;
            if (oldResult == Result.UNFINISHED) {
                playerDB.addWin(playerId);
                playerDB.addLoss(computerId);
            }
        } else {
            result = Result.UNFINISHED;
        }
        return result;
    }
    
    /**
     * Sets default id of player and computer. For player it is the anonymous
     * account which cannot be removed from database.
     */
    private void setDefaultIds() {
        List<PlayerData> playerList = playerDB.getPlayers();
        
        for (PlayerData pd: playerList) {
            if (pd.isComputer()) computerId = pd.getId();
            if (!pd.isComputer() && pd.isIrremovable()) playerId = pd.getId();
        }
        getPlayerNameFromDB();
    }
    
    /**
     * Gets the name matching playerId from database.
     */
    public void getPlayerNameFromDB() {
        playerName = playerDB.getName(playerId);
    }
    
    public String getPlayerName() {
        if (playerName == null || playerName.isEmpty()) getPlayerNameFromDB();
        return playerName;
    }

    public PlayerDatabase getPlayerDB() {
        return playerDB;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getComputerId() {
        return computerId;
    }
}
