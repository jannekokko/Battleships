package battleships.db;

/**
 * Storage class for statistics of each player (including computer)
 * @author Janne
 */
public class PlayerData {
    private int id;
    private String name;
    private int wins;
    private int losses;
    private int draws;
    private boolean irremovable;
    private boolean computer;
    
    public PlayerData(int id, String name, int wins, int losses, int draws, boolean irremovable, boolean computer) {
        this.id = id;
        this.name = name;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.irremovable = irremovable;
        this.computer = computer;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getDraws() {
        return draws;
    }
    
    public int getGames() {
        return wins+losses+draws;
    }

    public boolean isIrremovable() {
        return irremovable;
    }

    public boolean isComputer() {
        return computer;
    }

    
}
