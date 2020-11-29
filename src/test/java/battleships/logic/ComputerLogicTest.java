package battleships.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Janne
 */
public class ComputerLogicTest {
 
    
    GameMain gm;
    
    @Before
    public void setUp() {
        gm = new GameMain();
        gm.initGame();
    }
    
    @Test
    public void testFillGrid() {
        Grid g = gm.getComputerGrid();
        ArrayList<Ship> ships = new ArrayList<>();
        for (int x = 0; x < g.getSize(); x++) {
            for(int y = 0; y < g.getSize(); y++) {
                if(g.getShip(x, y) != null) {
                    if(!ships.contains(g.getShip(x, y))) ships.add(g.getShip(x, y));
                }
            }
        }
        Map<Integer,Integer> fleet = gm.getSettings().getFleet();
        List<Integer> Ships = new ArrayList<>(gm.getSettings().getFleet().keySet());

        int total = 0;
        for(int length: Ships) {
            total += fleet.get(length);
        }
        assertEquals(ships.size(),total);
    }
    
    @Test
    public void testShoot() {
        Grid g = gm.getComputerGrid();
        ComputerLogic l = gm.getComputerLogic();
        HashSet<GridState> hits = new HashSet<>();
        for (int i = 0; i < 0.9*g.getSize()*g.getSize(); i++) hits.add(l.shoot(g));
        
        assertEquals(hits.size(),3);
    }


}