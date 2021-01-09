
package battleships.logic;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Janne
 */
public class GridTest {
    

    
    GameMain gm;
    
    @Before
    public void setUp() {
        gm = new GameMain("test.db");
        gm.initGame();
    }
    
    @Test
    public void testAddShipSuccessful() {
        Grid g = gm.getPlayerGrid();
        assertTrue(g.add(0, 0, Direction.VERTICAL, 3));
    }
    
    @Test
    public void testAddOverlappingShip() {
        Grid g = gm.getPlayerGrid();
        g.add(0, 0, Direction.HORIZONTAL, 4);
        assertFalse(g.add(1, 0, Direction.VERTICAL, 3));
    }
    
    @Test
    public void testAddOutsideTheGrid() {
        Grid g = gm.getPlayerGrid();
        int size = g.getSize();
        assertFalse(g.add(size+1, 0, Direction.VERTICAL, 2));
    }


}

