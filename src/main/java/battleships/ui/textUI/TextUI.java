

package battleships.ui.textUI;

import battleships.logic.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Class for text-based user interface. Cointains funtionality for querying user
 * inputs and displaying game grids and results
 * 
 * @author Janne
 */
public class TextUI {
    private GameMain gameMain;
    private Scanner reader;
    
    public TextUI(GameMain gameMain, Scanner reader) {
        this.gameMain = gameMain;
        this.reader = reader;
    }
    
    
    public static void main(String[] args) {  
        GameMain gameMain = new GameMain();
        Scanner reader = new Scanner(System.in);

        TextUI textUI = new TextUI(gameMain, reader);
        
        textUI.start();
        
    }
    
    /**
     * Initializes the game by filling ships to both player and computer grids.
     */
    public void start() {
        gameMain.initGame();
        putShips();
        textGame();
        
    }
    
    /**
     * Main function for filling player's grid with ships.
     */
    public void putShips() {
        Settings settings = gameMain.getSettings();
        Map<Integer,Integer> fleet = settings.getFleet();
        Grid playerGrid = gameMain.getPlayerGrid();
        
        List<Integer> Ships = new ArrayList<>(fleet.keySet());
        Collections.sort(Ships, Collections.reverseOrder());
        
        for(int length: Ships) {
            int shipsLeft = fleet.get(length);
            while (shipsLeft > 0) {
                drawPlayerGrid(playerGrid);
                int[] position = queryPosition(length);
                Direction direction;
                if (position[2] == 0) direction = Direction.HORIZONTAL;
                else direction = Direction.VERTICAL;
                
                if(playerGrid.add(position[0], position[1], direction, length)) {
                    shipsLeft--;
                } else {
                    System.out.println("\nIncorrect position!");
                }
            }
        }
        System.out.println("");

    }
    
    /**
     * Queries position and direction of next ship.
     * @param length length of the ship
     * @return Array with x and y coordinates of the ships top left corner and its direction
     */
    public int[] queryPosition(int length) {

        System.out.println("Ship length: " + length);
        System.out.println("Give top left coords (x, y) and direction, horizontal = 0, vertical = 1 (example: 2 4 1)");
        
        return numberQuery(3);
    }
    
    /**
     * Queries numeric input from user. Parameter n represent total amount of inputs needed.
     * Numbers can be separated with either space or entered one by one
     * @param n Amount of numbers requested
     * @return Array of numeric values
     */
    public int[] numberQuery(int n) {
        int[] numbers = new int[n];
        int i = 0;
        while(i < n) {
            String s = reader.nextLine();
            String[] parts = s.split(" ");
            for (int j = 0; j < parts.length && i < n; j++) {
                try {
                    numbers[i] = Integer.parseInt(parts[j]);
                    i++;
                } catch (NumberFormatException e) {
                    System.out.println("Input is not a number!");
                }
            }
        }
        return numbers;
    }
    
    /**
     * Draws player's grid.
     * @param playerGrid Reference to player's Grid object
     */
    public void drawPlayerGrid(Grid playerGrid) {
        
        String[] strings = gridStrings(playerGrid, true);
        for (String s: strings) System.out.println(s);

    }
    
    /**
     * Generates String array for displaying game grids.
     * @param grid Grid object
     * @param visible true if grid points that haven't been hit yet should show ship, false otherwise
     * @return String-array representation of requested Grid
     */
    public String[] gridStrings(Grid grid, boolean visible) {
        int size = grid.getSize();
        GridPoint[][] gridPoints = grid.getGridMap();
        String[] s = new String[size+1];
        s[0] = " ";
        for (int i = 0; i < size; i++) s[0] += "  " + i;
        s[0] += " ";
        
        for (int i = 0; i < size; i++) {
            s[i+1] = i + " ";
            for (int j = 0; j < size; j++) {
                Ship ship = gridPoints[j][i].getShip();
                GridState state = gridPoints[j][i].getState();
                if (ship == null && state == GridState.UNKNOWN) s[i+1]+= " ~ ";
                else if (ship != null && state == GridState.UNKNOWN && visible) s[i+1] += " O ";
                else if (state == GridState.MISS) s[i+1] += " x ";
                else if (state == GridState.HIT) s[i+1] += " H ";
                else if (state == GridState.SUNK) s[i+1] += " S ";
                else s[i+1] += " ~ ";
            }
        }
        return s;
    }
    
    /**
     * Main function for actual game phase where target coordinates are requested
     * and results of each round are displayed.
     */
    public void textGame() {
        System.out.println("\nGAME BEGINS!\n");
        while(true) {
            drawGrids();
            int[] target = queryTarget();
            GridState playerTargetState = gameMain.getComputerGrid().shootAt(target[0], target[1]);
            if (playerTargetState == GridState.UNKNOWN) {
                System.out.println("INCORRECT TARGET!\n");
                continue;
            }
            GridState computerTargetState = gameMain.getComputerLogic().shoot(gameMain.getPlayerGrid());
            
            System.out.println("\n\nResults of this round:");
            System.out.println("You: " + playerTargetState.toString());
            System.out.println("Enemy: " + computerTargetState.toString() + "\n");
            
            if(gameMain.getPlayerGrid().shipsLeft() <= 0 && gameMain.getComputerGrid().shipsLeft() <= 0) {
                System.out.println("\n\nDRAW!");
                break;
            } else if(gameMain.getPlayerGrid().shipsLeft() <= 0) {
                System.out.println("\n\nYOU LOST!");
                break;
            } else if(gameMain.getComputerGrid().shipsLeft() <= 0) {
                System.out.println("\n\nYOU WON!");
                break;
            }
      
        }
    }
    
    /**
     * Queries target coordinates where player wants to shoot.
     * @return Array with x and y coordinates of target
     */
    public int[] queryTarget() {
        System.out.println("Where do you want to shoot? (x y)");
        return numberQuery(2);
    }
    
    /**
     * Draws grids of player and computer during actual game phase.
     */
    public void drawGrids() {
        String[] playerStrings = gridStrings(gameMain.getPlayerGrid(), true);
        String[] computerStrings = gridStrings(gameMain.getComputerGrid(), false);
        StringBuilder sb = new StringBuilder("   ENEMY GRID");
        for (int i = sb.length(); i < computerStrings[0].length() + 8; i++) sb.append(' ');
        sb.append("PLAYER GRID");
        System.out.println(sb.toString());
        for (int i = 0; i < playerStrings.length; i++) {
            System.out.println(computerStrings[i] + "  |  " + playerStrings[i]);
        }
    } 
}
