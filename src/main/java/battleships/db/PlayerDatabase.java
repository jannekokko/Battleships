package battleships.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class for handling SQLite database connection.
 * @author Janne
 */
public class PlayerDatabase {
    
    Connection db;
    
    public PlayerDatabase() throws SQLException {
        db = DriverManager.getConnection("jdbc:sqlite:playerdb.db");
        
        prepareDatabase();
    }
    
    /**
     * Checks if database file is valid based on queries from Players table.
     * If database is invalid or cannot be opened, this method drops Players
     * table and recreates it with proper keys and default players.
     * @throws SQLException 
     */
    private void prepareDatabase() throws SQLException {
        Statement s = db.createStatement();
        try {
            s.executeQuery("SELECT id, irremovable, computer, wins, losses, draws FROM Players WHERE name='Computer'");
            s.executeQuery("SELECT id, irremovable, computer, wins, losses, draws FROM Players WHERE name='Anonymous'");
        } catch (SQLException e) {
            System.out.println("Corrupted or missing database, reconstructing...");
            s.execute("DROP TABLE IF EXISTS Players");
            s.execute("CREATE TABLE Players (id INTEGER PRIMARY KEY, "
                    + "name TEXT UNIQUE, "
                    + "irremovable INTEGER DEFAULT 0, "
                    + "computer INTEGER DEFAULT 0, "
                    + "wins INTEGER DEFAULT 0, "
                    + "losses INTEGER DEFAULT 0, "
                    + "draws INTEGER DEFAULT 0)");
            s.execute("INSERT INTO Players (name, irremovable, computer) VALUES ('Computer',1,1)");
            s.execute("INSERT INTO Players (name, irremovable) VALUES ('Anonymous',1)");
        }
    }
    
    /**
     * Adds a new player to database.
     * @param name name of the player
     * @return true if successful, otherwise false
     */
    public boolean addPlayer(String name) {
        try {
            PreparedStatement p = db.prepareStatement("INSERT INTO Players (name) VALUES=(?)");
            p.setString(1, name);
            p.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }
    
    /**
     * Attempts to delete a player.
     * @param id id of the player
     * @return true if successful, otherwise false
     */
    public boolean deletePlayer(int id) {
        try {
            PreparedStatement p = db.prepareStatement("DELETE FROM Players WHERE id=? AND irremovable=0");
            p.setInt(1, id);
            p.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }
    
    /**
     * Adds a win to player.
     * @param id id of the player
     */
    public void addWin(int id) {
        try {
            PreparedStatement p = db.prepareStatement("UPDATE Players SET wins=wins+1 WHERE id=?");
            p.setInt(1, id);
            p.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Player not found.");
        }
    }
    
    public void addLoss(int id) {
        try {
            PreparedStatement p = db.prepareStatement("UPDATE Players SET losses=losses+1 WHERE id=?");
            p.setInt(1, id);
            p.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Player not found.");
        }
    }
    
    /**
     * Adds a draw to player.
     * @param id id of the player
     */
    public void addDraw(int id) {
        try {
            PreparedStatement p = db.prepareStatement("UPDATE Players SET draws=draws+1 WHERE id=?");
            p.setInt(1, id);
            p.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Player not found.");
        }
    }
    
    /**
     * Gets the name corresponding to id
     * @param id database id of query
     * @return name
     */
    public String getName(int id) {
        String name = "";
        try {
            PreparedStatement p = db.prepareStatement("SELECT name FROM Players WHERE id=?");
            p.setInt(1, id);
            ResultSet r = p.executeQuery();
            if (!r.next()) {
                System.out.println("No players in database!");
            } else {
                name = r.getString("name");
            }
        } catch (SQLException e) {
            System.out.println("Error reading database!");
        }
        return name;
    }
    
    /**
     * Generates a list of all players in database and their statistics.
     * @return List of PlayerData objects.
     */
    public List<PlayerData> getPlayers() {
        List<PlayerData> players = new ArrayList<>();
        try {
            PreparedStatement p = db.prepareStatement("SELECT id, name, wins, losses, draws, irremovable, computer FROM Players");
            ResultSet r = p.executeQuery();
            if (!r.next()) {
                System.out.println("No players in database!");
            } else {
                boolean next = true;
                while(next) {
                    players.add(new PlayerData(r.getInt("id"),r.getString("name"),r.getInt("wins"),r.getInt("losses"),
                            r.getInt("draws"),r.getBoolean("irremovable"),r.getBoolean("computer")));
                    next = r.next();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error reading database!");
        }
        return players;
    }
}
