
package battleships.ui.gfxUI;

import battleships.logic.GameMain;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Class for creation of player selection and modification view. In this view
 * current player can be changed, and players can be created and deleted.
 * @author Janne
 */
public class PlayerSelectionView implements ContainsView {
private final GameMain gameMain;
private final UIDataStore uiData;
private final TopView topView;
private String selectedName;
private final ObservableList<String> names;
    
    public PlayerSelectionView(GameMain gameMain, UIDataStore uiData, TopView topView) {
        this.gameMain = gameMain;
        this.uiData = uiData;
        this.topView = topView;
        this.selectedName = "";
        this.names = FXCollections.observableArrayList();
        updateNameList();
    }
    
    @Override
    public Parent getView() {
        BorderPane placement = new BorderPane();

        ListView<String> nameList = new ListView<>(names);

        nameList.setOnMouseClicked((MouseEvent event) -> {
            selectedName = nameList.getSelectionModel().getSelectedItem();
        });
        
        placement.setLeft(nameList);
        
        VBox midPane = new VBox(20);        
        
        HBox nameCreation = new HBox();
        TextField nameField = new TextField();
        Label addFeedback = new Label();
        Button addNameButton = new Button("Add name");
        addNameButton.setOnAction((event) -> {
            if (gameMain.getPlayerDB().addPlayer(nameField.getText())) {
                addFeedback.setText("Name added to database!");
                updateNameList();
                setPlayer(nameField.getText());
            } else {
                addFeedback.setText("Adding name failed! Name must be unique and be 3-20 characters long");
            }
        });
        nameCreation.getChildren().addAll(nameField, addNameButton, addFeedback);
        
        Button selectButton = new Button("Select player");
        selectButton.setOnAction((event) -> {
            setPlayer(selectedName);
        });
        
        Button deleteButton = new Button("Delete player");
        deleteButton.setOnAction((event) -> {
            delete(selectedName);
        });
        
        Button backButton = new Button("Back");
        backButton.setOnAction((event) -> {
            uiData.setView(Views.MAINMENU);
        });
        
        midPane.getChildren().addAll(nameCreation,selectButton,deleteButton,backButton);
        
        placement.setCenter(midPane);
        
        return placement;
    }
    
    private void updateNameList() {
        names.clear();
        names.addAll(gameMain.getPlayerDB().getNames());
    }
    
    private void delete(String name) {
        if(!gameMain.getPlayerDB().isDeletable(name)) {
            Alert a = new Alert(AlertType.ERROR);
            a.setContentText("Unable to delete player!");
            a.show();
        } else {
            Alert a = new Alert(AlertType.CONFIRMATION);
            a.setContentText("Are you sure you want to delete " + name + "?");
            a.getButtonTypes().add(ButtonType.YES);
            a.getButtonTypes().removeAll(ButtonType.OK);
            Optional<ButtonType> action = a.showAndWait();
            if(action.get() == ButtonType.YES) {
                gameMain.getPlayerDB().deletePlayer(name);
                updateNameList();
                if(gameMain.getPlayerName().equals(name)) {
                    gameMain.setDefaultIds();
                    topView.resetLeftRight();
                }
            }
        }
    }
    
    private void setPlayer(String name) {
        gameMain.setPlayerName(name);
        topView.resetLeftRight();
    }
    
}
