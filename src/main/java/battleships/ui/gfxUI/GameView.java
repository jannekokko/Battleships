package battleships.ui.gfxUI;

import battleships.logic.*;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * Main game view that shows both grids and handles game events.
 * @author Janne
 */
public class GameView implements ContainsView {
    private GameMain gameMain;
    private UIDataStore uiData;
    private Parent playerGrid;
    private Parent computerGrid;
    private int width;
    private int height;
    private boolean gameInProgress;
    private TopView topView;
    
    public GameView(GameMain gameMain, UIDataStore uiData, TopView topView) {
        this.gameMain = gameMain;
        this.uiData = uiData;
        this.topView = topView;
    }

    /**
     * Returns a view with player and enemy grids.
     * @return a view with player and enemy grids
     */
    @Override
    public Parent getView() {
        int size = gameMain.getSettings().getGridSize();
        width = uiData.getGridWidth() / size;
        height = uiData.getGridHeight() / size;
        gameInProgress = true;
        BorderPane placement = new BorderPane();
        
        
        Pane rightPane = new Pane();
        Pane leftPane = new Pane();
        
        refreshGrids(leftPane, rightPane);
        placement.setLeft(leftPane);
        placement.setRight(rightPane);
        
        EventHandler<javafx.scene.input.MouseEvent> eventHandler = 
        new EventHandler<javafx.scene.input.MouseEvent>() { 
            @Override 
            public void handle(javafx.scene.input.MouseEvent e) { 
                if(e.getEventType() == javafx.scene.input.MouseEvent.MOUSE_CLICKED
                        && gameInProgress) {
                    GridState result = gameMain.getComputerGrid().shootAt((int)e.getX()/width, (int)e.getY()/height);
                    if (result != GridState.UNKNOWN) {
                        gameMain.getComputerLogic().shoot(gameMain.getPlayerGrid());
                        refreshGrids(leftPane, rightPane);
                        checkResult();
                    }
                }
            } 
        };
        leftPane.addEventHandler(javafx.scene.input.MouseEvent.ANY, eventHandler);

        return placement;
    }
    
    /**
     * Rereads grid data and refreshes grid views.
     * @param leftPane parent object of player grid
     * @param rightPane parent object of computer grid
     */
    private void refreshGrids(Pane leftPane, Pane rightPane) {
        playerGrid = new GridScenes(gameMain,uiData).getPlayerGrid();
        rightPane.getChildren().removeIf(node -> node instanceof Parent);
        rightPane.getChildren().add(playerGrid);

        
        computerGrid = new GridScenes(gameMain,uiData).getComputerGrid();
        leftPane.getChildren().removeIf(node -> node instanceof Parent);
        leftPane.getChildren().add(computerGrid);
    }
    
    /**
     * Checks if end condition of the game is met and shows result & return button if yes.
     */
    private void checkResult() {
        Result result = gameMain.checkResult();
        if(result == Result.DRAW) {
            topView.setLeft(new Label("IT'S A DRAW!"));
        } else if(result == Result.COMPUTERWON) {
            topView.setLeft(new Label("YOU LOST!"));
        } else if(result == Result.PLAYERWON) {
            topView.setLeft(new Label("YOU WON!"));
        } else return;
        gameInProgress = false;
        Button b = new Button("Main menu");
        b.setOnAction((event) -> {
            topView.resetLeftRight();
            uiData.setView(Views.MAINMENU);
        });
        topView.setRight(b);
        
    }
    
}
