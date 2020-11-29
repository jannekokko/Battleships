package battleships.ui.gfxUI;

import battleships.logic.GameMain;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Main class for controlling the graphical UI.
 * @author Janne
 */
public class SceneControl {
    private Stage stage;
    private GameMain gameMain;
    private UIDataStore uiData;
    private int topHeight;
    
    public SceneControl(Stage stage, GameMain gameMain) {
        this.stage = stage;
        this.gameMain = gameMain;
        this.uiData = new UIDataStore(500,500);
        this.topHeight = 100;
    }
    
    /**
     * Initiates UI views and the game.
     */
    public void start() {
        stage.setResizable(false);
        gameMain.initGame();
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 2*uiData.getGridWidth(), topHeight + uiData.getGridHeight());
        TopView topView = new TopView(topHeight);
        MainMenu mainMenu = new MainMenu(gameMain, uiData);
        PlacementView placementView = new PlacementView(gameMain, uiData);       
        GameView gameView = new GameView(gameMain, uiData, topView);
        
        Label topLabel = new Label("Battleships");
        topLabel.setFont(Font.font("Arial",30));
        topView.setCenter(topLabel);

        root.setCenter(mainMenu.getView());
        root.setTop(topView.getView());
        stage.setScene(scene);
        
        stage.show();
        
        uiData.viewProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal) {
                 if (uiData.getView() == Views.MAINMENU.ordinal()) {
                     root.setCenter(mainMenu.getView());
                 }
                 if (uiData.getView() == Views.PLACEMENTVIEW.ordinal()) {
                     root.setCenter(placementView.getView());
                 }
                 if (uiData.getView() == Views.GAMEVIEW.ordinal()) {
                     root.setCenter(gameView.getView());
                 }
            }
        });

        
        
        
        
    }

    
}
