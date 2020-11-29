
package battleships.ui.gfxUI;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

/**
 * Class for showing objects at the top of the main view.
 * @author Janne
 */
public class TopView implements ContainsView {
    BorderPane topView;
    
    public TopView(int topHeight) {
        topView = new BorderPane();
        topView.setPrefHeight(topHeight);
    }

    @Override
    public Parent getView() {
        return topView;
    }
    
    public void setCenter(Parent center) {
        topView.setCenter(center);
    }
    public void setLeft(Parent left) {
        BorderPane.setAlignment(left,Pos.CENTER);
        topView.setLeft(left);
    }
    public void setRight(Parent right) {
        BorderPane.setAlignment(right,Pos.CENTER);
        topView.setRight(right);  
    }
    
    public void resetLeftRight() {
        topView.setLeft(new BorderPane());
        topView.setRight(new BorderPane());
    }
    
}
