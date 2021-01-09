package battleships.logic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class FullGameTest {
    
    GameMain gm;
    
    @Before
    public void setUp() {
        gm = new GameMain("fulltest.db");
    }

/**
 * Makes computer play against itself multiple times. The intention is to see
 * that it cannot get stuck at any point and that the code runs efficiently enough.
 */
    @Test
    public void fullGameTest() {
        int loops = 1000;
        
        long beginTime = System.nanoTime();
        for (int i = 0; i < loops; i++) {
            gm.initGame();
            gm.getComputerLogic().fillGrid(gm.getPlayerGrid(), gm.getSettings());
            while(gm.checkResult() == Result.UNFINISHED) {
                gm.getComputerLogic().shoot(gm.getComputerGrid());
                gm.getComputerLogic().shoot(gm.getPlayerGrid());
            }
        }
        long endTime = System.nanoTime();
        double time = (double) (endTime-beginTime) / 1000000000L;
        System.out.println("Time: " + time + " s");
        assertTrue(time < 200.0);
    }

    
}
