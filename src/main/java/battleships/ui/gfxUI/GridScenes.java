
package battleships.ui.gfxUI;

import battleships.logic.GameMain;
import battleships.logic.Grid;
import battleships.logic.GridState;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 * Class for generating both player and computer grids.
 * @author Janne
 */
public class GridScenes {
    private GameMain gameMain;
    private UIDataStore uiData;
    private int width;
    private int height;
    private boolean shipsVisible;
    Grid grid;

    
    public GridScenes(GameMain gameMain, UIDataStore currentView) {
        this.gameMain = gameMain;
        this.uiData = currentView;
    }
    
    public Parent getPlayerGrid() {
        grid = gameMain.getPlayerGrid();
        shipsVisible = true;
        return getGrid();
    }
    
    public Parent getComputerGrid() {
        grid = gameMain.getComputerGrid();
        shipsVisible = false;
        return getGrid();
    }
    
    /**
     * Draws grid base and details of each grid point.
     * @return grid
     */
    public Parent getGrid() {        
        GridPane gp = new GridPane();
        
        int size = grid.getSize();
        width = uiData.getGridWidth() / size;
        height = uiData.getGridHeight() / size;
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                gp.add(drawRect(),i*width,j*height);
                gp.add(drawGridPoint(i,j), i*width,j*height);
            }
        }
        return gp;
    }
    

    /**
     * Draws gridpoint. Depending on boolean shipsVisible, either shows or does not
     * show ships that have not yet been sunk.
     * @param x x-coordinate of grid point
     * @param y y-coordinate of grid point
     * @return Parent object of grid points view
     */
    private Parent drawGridPoint(int x, int y) {
        StackPane sp = new StackPane();
        sp.getChildren().add(drawRect());
        if (grid.getShip(x, y) != null
                && (shipsVisible || grid.getGridState(x, y) == GridState.SUNK)) {
            sp.getChildren().add(drawShip());
            if(grid.getGridState(x, y) == GridState.SUNK) {
                sp.getChildren().add(drawHit());
            }
        }
        if (grid.getGridState(x, y) == GridState.HIT) {
            sp.getChildren().add(drawHit());
        }
        if (grid.getGridState(x, y) == GridState.MISS) {
            sp.getChildren().add(drawMiss());
        }
        
        return sp;
    }
    
    /**
     * Draws a rectangle that represent ship.
     * @return a rectangle
     */
    private Parent drawShip() {
        Rectangle r = new Rectangle(width, height,Color.GRAY);
        Group g = new Group(r);
        return g;
    }
    
    /**
     * Draws a line that represents missed shot.
     * @return Parent object of missed shot visual
     */
    private Parent drawMiss() {
        Line l = new Line();
        l.setStartX(0.9*width);
        l.setStartY(0.1*height);
        l.setEndX(0.1*width);
        l.setEndY(0.9*height);
        l.setFill(Color.BLACK);
        l.setStrokeWidth(0.05*width);
        Group g = new Group(l);
        return g;
    }
    
    /**
     * Draws X that represents a hit.
     * @return Parent object of hit visual
     */
    private Parent drawHit() {
        Parent firstStroke = drawMiss();
        Parent secondStroke = drawMiss();
        secondStroke.setRotate(90);
        Group g = new Group(firstStroke);
        g.getChildren().add(secondStroke);
        return g;
        
    }
    
    /**
     * Draws a rectangle that represents water.
     * @return Parent object of water visual
     */
    private Parent drawRect() {
        Rectangle r = new Rectangle(width, height);
        Rectangle rIn = new Rectangle(5,5,width-10,height-10);
        rIn.setFill(Color.LIGHTBLUE);
        
        Group g = new Group();
        g.getChildren().add(r);
        g.getChildren().add(rIn);
        
        return g;
    }
    
}
