package battleships.db;

/**
 * Storage class for statistics of each player (including computer)
 * @author Janne
 */
public class PlayerData {
    private final int id;
    private final String name;
    private final int wins;
    private final int losses;
    private final int draws;
    private final boolean irremovable;
    private final boolean computer;
    
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
