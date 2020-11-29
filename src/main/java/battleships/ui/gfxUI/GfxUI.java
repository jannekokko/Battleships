package battleships.ui.gfxUI;

import battleships.logic.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Main class for graphical UI
 * 
 * @author Janne
 */
public class GfxUI extends Application {
    
    private GameMain gameMain;
    
    public static void main(String[] args) {
        launch(GfxUI.class);
    }
    


    @Override
    public void start(Stage stage) {
        stage.setTitle("Battleships");
        this.gameMain = new GameMain();
        SceneControl control = new SceneControl(stage, gameMain);
        control.start();
    }
    
    
    
}
