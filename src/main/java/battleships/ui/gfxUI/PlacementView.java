package battleships.ui.gfxUI;

import battleships.logic.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Class for generating view and handling events in ship placement phase of the game.
 * @author Janne
 */
public class PlacementView implements ContainsView {
    private final GameMain gameMain;
    private final UIDataStore uiData;
    private Parent nextShip;
    private Parent grid;
    private Pane leftSide;
    private final Map<Integer,Integer> fleet;
    private Grid playerGrid;
    private PriorityQueue<Integer> ships;
    private final int width;
    private final int height;
    private final int size;
    Direction dir = Direction.HORIZONTAL;
    
    public PlacementView(GameMain gameMain, UIDataStore uiData) {
        this.gameMain = gameMain;
        this.uiData = uiData;
        
        size = gameMain.getSettings().getGridSize();
        width = uiData.getGridWidth() / size;
        height = uiData.getGridHeight() / size;
        
        fleet = gameMain.getSettings().getFleet();        
        
    }
    
    /**
     * Generates the parent object of placement view.
     * @return Parent object of the placement view
     */
    @Override
    public Parent getView() {
        gameMain.initGame();
        playerGrid = gameMain.getPlayerGrid();
        createShipList();
        
        BorderPane placement = new BorderPane();
        
        leftSide = new Pane();
        leftSide.setPrefWidth(uiData.getGridWidth());
        Parent rightSide = getRightSide();

        grid = new GridScenes(gameMain,uiData).getPlayerGrid();
        leftSide.getChildren().add(grid);
        
        nextShip = drawNextShip();
        leftSide.getChildren().add(nextShip);
        EventHandler<javafx.scene.input.MouseEvent> eventHandler = 
        new EventHandler<javafx.scene.input.MouseEvent>() { 
            @Override 
            public void handle(javafx.scene.input.MouseEvent e) { 
                if(e.getEventType() == javafx.scene.input.MouseEvent.MOUSE_MOVED) {
                    nextShip.toFront();
                    nextShip.relocate((int) e.getX()-width/2,(int) e.getY()-height/2);
                }
                if(e.getEventType() == javafx.scene.input.MouseEvent.MOUSE_CLICKED) {
                    if (e.getButton() == MouseButton.SECONDARY) {
                        dir = dir == Direction.HORIZONTAL ? Direction.VERTICAL : Direction.HORIZONTAL;
                        leftSide.getChildren().remove(nextShip);
                        nextShip = drawNextShip();
                        leftSide.getChildren().add(nextShip);
                        nextShip.relocate((int) e.getX()-width/2,(int) e.getY()-height/2);
                    }
                    if (e.getButton() == MouseButton.PRIMARY) {
                        if(putShip((int) e.getX(),(int) e.getY())) {
                            leftSide.getChildren().remove(grid);
                            grid = new GridScenes(gameMain,uiData).getPlayerGrid();
                            leftSide.getChildren().add(grid);
                            leftSide.getChildren().remove(nextShip);
                            if (nextShipLength() != 0) {
                                nextShip = drawNextShip();
                                nextShip.relocate((int) e.getX()-width/2,(int) e.getY()-height/2);
                                leftSide.getChildren().add(nextShip);
                            }
                        }
                    }
                }
            } 
        };
        leftSide.addEventHandler(javafx.scene.input.MouseEvent.ANY, eventHandler);
        
        placement.setLeft(leftSide);
        placement.setRight(rightSide);

        return placement;
    }
    
    /**
     * Generates right view that contains reset button and instructions.
     * @return Parent object of the right side view
     */
    private Parent getRightSide() {
        BorderPane bp = new BorderPane();
        VBox rightSide = new VBox();
        rightSide.setPrefWidth(uiData.getGridWidth());
        rightSide.setAlignment(Pos.CENTER);
        Button b = new Button("Reset");
        String s = "Place your ships!\n\nRight-click to rotate";
        Label instructions = new Label(s);
        rightSide.getChildren().add(b);
        rightSide.getChildren().add(instructions);
        
        b.setOnAction((event) -> {
            playerGrid.clearGrid();
            createShipList();
            grid = new GridScenes(gameMain,uiData).getPlayerGrid();
            leftSide.getChildren().remove(grid);
            leftSide.getChildren().add(grid);
            leftSide.getChildren().remove(nextShip);
            nextShip = drawNextShip();
            leftSide.getChildren().add(nextShip);
        });
        
        bp.setCenter(rightSide);
        return bp;
        
    }
    
    /**
     * Draws shape of the next ship that is to be put to grid
     * @return rectangular shape of the next ship
     */
    private Parent drawNextShip() {
        Rectangle r;
        int length = 1;
        if(!ships.isEmpty()) length = nextShipLength();
        if (dir == Direction.HORIZONTAL) r = new Rectangle(length*width, height);
        else r = new Rectangle(width, length*height);
        r.setFill(Color.DARKGRAY);
        r.setOpacity(0.8);
        Group g = new Group(r);
        return g;
    }
    
    
    /**
     * Creates list of ships that should be put to grid in length order from
     * longest to shortest.
     */
    private void createShipList() {
        List<Integer> shipSet = new ArrayList<>(fleet.keySet());
        ships = new PriorityQueue<>(Collections.reverseOrder());
        
        for(int length: shipSet) {
            int shipsOfLength = fleet.get(length);
            for (int i = 0; i < shipsOfLength; i++) {
                ships.add(length);
            }
        }
    }
    
    /**
     * Return the length of next ship that will be put to grid
     * @return ship length
     */
    private int nextShipLength() {
        if (!ships.isEmpty()) {
            return ships.peek();
        }
        return 0;
    }
    
    /**
     * Attempt to put ship to x,y-coords.
     * @param x x-coordinate of the top-left corner of the ship
     * @param y y-coordinate of the top-left corner of the ship
     * @return true if successful, otherwise false
     */
    private boolean putShip(int x, int y) {
        int gridX = x / width;
        int gridY = y / height;
        
        if(!ships.isEmpty() && playerGrid.add(gridX, gridY, dir, nextShipLength())){
            ships.poll();
            if (ships.isEmpty()) uiData.setView(Views.GAMEVIEW);
            return true;
        }
        return false;
    }
    
    
}
