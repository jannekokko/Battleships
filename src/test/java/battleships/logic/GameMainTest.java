
package battleships.logic;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class GameMainTest {
    
    GameMain gm;
    
    @Before
    public void setUp() {
        gm = new GameMain();
    }


    @Test
    public void testGetSettings() {
        Settings settings = gm.getSettings();
        assertTrue(settings.getGridSize() > 0);
    }


    @Test
    public void testGetPlayerGrid() {
        Grid pg = gm.getPlayerGrid();
        assertNotNull(pg);
    }
    
}
