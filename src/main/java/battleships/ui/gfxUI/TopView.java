
package battleships.ui.gfxUI;

import battleships.logic.GameMain;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

/**
 * Class for showing objects at the top of the main view.
 * @author Janne
 */
public class TopView implements ContainsView {
    private GridPane topView;
    private GameMain gameMain;
    private StackPane left;
    private StackPane center;
    private StackPane right;
    
    public TopView(int topHeight, GameMain gameMain) {
        this.topView = new GridPane();
        this.left = new StackPane();
        this.center = new StackPane();
        this.right = new StackPane();
        this.topView.add(left, 0, 0);
        this.topView.add(center, 1, 0);
        this.topView.add(right, 2, 0);
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(30);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(40);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(30);
        this.topView.getColumnConstraints().addAll(column1, column2, column3);
        RowConstraints row1 = new RowConstraints();
        row1.setPrefHeight(topHeight);
        this.topView.getRowConstraints().add(row1);
        this.gameMain = gameMain;
    }

    @Override
    public Parent getView() {
        return topView;
    }
    
    /**
     * Displays player name on the left side of the top section.
     */
    public void showPlayerName() {
        Label l = new Label("Player: " + gameMain.getPlayerName());
        setLeft(l);
    }
    
    /**
     * Sets center section of the top to show Object given as a parameter.
     * @param center Object to be shown
     */
    public void setCenter(Parent center) {
        this.center.getChildren().clear();
        this.center.getChildren().add(center);
        StackPane.setAlignment(center, Pos.CENTER);
    }
    
    /**
     * Sets left side of the top to show Object given as a parameter.
     * @param left Object to be shown
     */
    public void setLeft(Parent left) {
        this.left.getChildren().clear();
        this.left.getChildren().add(left);
        StackPane.setAlignment(left, Pos.CENTER_LEFT);
    }
    
    /**
     * Sets right side of the top to show Object given as a parameter.
     * @param right Object to be shown
     */
    public void setRight(Parent right) {
        this.right.getChildren().clear();
        this.right.getChildren().add(right);
        StackPane.setAlignment(right, Pos.CENTER_RIGHT);
    }
    
    /**
     * Resets top of the screen to show player name on the left.
     */
    public void resetLeftRight() {
        left.getChildren().clear();
        showPlayerName();
        right.getChildren().clear();
    }
    
}
