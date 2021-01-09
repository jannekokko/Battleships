package battleships.ui.gfxUI;

import battleships.db.PlayerData;
import battleships.logic.GameMain;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Class for generation of statistics view.
 * @author Janne
 */
public class StatisticsView implements ContainsView {
    private final GameMain gameMain;
    private final UIDataStore uiData;
    private final TopView topView; 
    private final int nameWidth;
    private final int numberWidth;
    
    
    public StatisticsView(GameMain gameMain, UIDataStore uiData, TopView topView) {
        this.gameMain = gameMain;
        this.uiData = uiData;
        this.topView = topView;
        nameWidth = 300;
        numberWidth = 80;
    }
    
    /**
     * Generates and returns statistics view.
     * @return Parent object of statistics view
     */
    @Override
    public Parent getView() {
        Button b = new Button("Back");
        b.setOnAction((event) -> {
            topView.resetLeftRight();
            uiData.setView(Views.MAINMENU);
        });
        topView.setLeft(b);
        
        List<PlayerData> players = gameMain.getPlayerDB().getPlayers();
        TableView table = new TableView();
        
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(nameWidth);
        TableColumn gamesCol = new TableColumn("Games");
        gamesCol.setMinWidth(numberWidth);
        TableColumn winsCol = new TableColumn("Wins");
        winsCol.setMinWidth(numberWidth);
        TableColumn lossesCol = new TableColumn("Losses");
        lossesCol.setMinWidth(numberWidth);
        TableColumn drawsCol = new TableColumn("Draws");
        drawsCol.setMinWidth(numberWidth);

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        gamesCol.setCellValueFactory(new PropertyValueFactory<>("games"));
        winsCol.setCellValueFactory(new PropertyValueFactory<>("wins"));
        lossesCol.setCellValueFactory(new PropertyValueFactory<>("losses"));
        drawsCol.setCellValueFactory(new PropertyValueFactory<>("draws"));
        
        ObservableList<PlayerData> data = FXCollections.<PlayerData>observableArrayList();
        data.addAll(players);
        table.setItems(data);
        table.getColumns().addAll(nameCol, gamesCol, winsCol, lossesCol, drawsCol);
        table.getSortOrder().add(nameCol);
        
        return table;
    }
    
}
