
package battleships.ui;

import battleships.logic.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


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
    
    public void start() {
        putShips();
    }
    
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
                    System.out.println("Incorrect position!");
                }
            }
        } 
        drawPlayerGrid(playerGrid);
    }
    
    public int[] queryPosition(int length) {
        int[] position = {-1,-1,-1};
        System.out.println("Ship length: " + length);
        System.out.println("Give top left coords (x, y) and direction, horizontal = 0, vertical = 1 (example: 2 4 1)");
        String s = reader.nextLine();
        String[] parts = s.split(" ");
        for (int i = 0; i < 3; i++) {
            position[i] = Integer.valueOf(parts[i]);
        }
        
        return position;
        
    }
    
    public void drawPlayerGrid(Grid playerGrid) {
        
        int size = playerGrid.getSize();
        GridPoint[][] gridPoints = playerGrid.getGridMap();
        
        System.out.print(" ");
        for (int i = 0; i < size; i++) System.out.print("  "+ i);
        System.out.println("");
        
        for (int i = 0; i < size; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < size; j++) {
                if (gridPoints[j][i].getShip() == null) System.out.print(" ~ ");
                else System.out.print(" O ");
            }
            System.out.println("");
        }
        
        
    }
    
    
}
