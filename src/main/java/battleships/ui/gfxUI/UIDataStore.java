/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleships.ui.gfxUI;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Parent;


/**
 * Class for storing some key details of the interface.
 * @author Janne
 */
public class UIDataStore {
    private final SimpleIntegerProperty view;
    private final int gridHeight;
    private final int gridWidth;
    
    public UIDataStore(int gridWidth, int gridHeight) {
        this.view = new SimpleIntegerProperty(Views.MAINMENU.ordinal());
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;

    }
    
    public int getView() {
        return view.get();
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public int getGridWidth() {
        return gridWidth;
    }
    
    public void setView(Views view) {
        this.view.set(view.ordinal());
    }
    
    public IntegerProperty viewProperty() {
        return view;
    }
    
}
