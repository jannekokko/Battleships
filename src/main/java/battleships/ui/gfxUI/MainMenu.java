
package battleships.ui.gfxUI;

import battleships.logic.*;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Main menu's class that generates pushable buttons to main menu.
 * @author Janne
 */
public class MainMenu implements ContainsView {
    private GameMain gameMain;
    private UIDataStore uiData;
    
    public MainMenu(GameMain gameMain, UIDataStore currentView) {
        this.gameMain = gameMain;
        this.uiData = currentView;
    }
    
    /**
     * Generates main menu view.
     * @return Parent object of main menu
     */
    @Override
    public Parent getView() {
        BorderPane menuPane = new BorderPane();
        
        VBox buttonLayout = new VBox();
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.setSpacing(20);
        
        menuPane.setCenter(buttonLayout);
        
        createButtons(buttonLayout);
        
        return menuPane;
    }
    
    /**
     * Creates main menu buttons and listeners for them.
     * @param buttonLayout button layout
     */
    public void createButtons(VBox buttonLayout) {
        Button startButton = new Button("Start game");
        Button statisticsButton = new Button("Statistics");
        Button quitButton = new Button("Quit game");
        
        buttonLayout.getChildren().add(startButton);
        buttonLayout.getChildren().add(statisticsButton);
        buttonLayout.getChildren().add(quitButton);
        
        startButton.setOnAction((event) -> {
            uiData.setView(Views.PLACEMENTVIEW);
        });
        statisticsButton.setOnAction((event) -> {
            uiData.setView(Views.STATISTICSVIEW);
        });
        quitButton.setOnAction((event) -> {
            Platform.exit();
        });
    }


    
}
