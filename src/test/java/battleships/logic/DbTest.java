/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleships.logic;

import battleships.db.PlayerDatabase;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Janne
 */
public class DbTest {
    private static GameMain gm;
    private static boolean setUpDone = false;
    
    @Before
    public void setUp() {
        if (setUpDone) return;
        gm = new GameMain("dbTest.db");
        gm.initGame();
        setUpDone = true;
    }
    
    @Test
    public void addNameToDBSuccessful() {
        PlayerDatabase db = gm.getPlayerDB();
        db.deletePlayer("Testname");
        assertTrue(db.addPlayer("Testname"));
    }
    
    @Test
    public void nameMustBeUnique() {
        PlayerDatabase db = gm.getPlayerDB();
        db.addPlayer("Uniquename");
        assertFalse(db.addPlayer("Uniquename"));
    }
    
    @Test
    public void testTooShortName() {
        PlayerDatabase db = gm.getPlayerDB();
        assertFalse(db.addPlayer("12"));
    }
    
    @Test
    public void testTooLongName() {
        PlayerDatabase db = gm.getPlayerDB();
        assertFalse(db.addPlayer("123456789012345678901"));
    }
    
    @Test
    public void isDeletableTrue() {
        PlayerDatabase db = gm.getPlayerDB();
        db.addPlayer("Testname");
        assertTrue(db.isDeletable("Testname"));
    }
    
    @Test
    public void isDeletableFalse() {
        PlayerDatabase db = gm.getPlayerDB();
        assertFalse(db.isDeletable("Computer"));
    }
    
    @Test
    public void deleteFailsOnIrremovable() {
        PlayerDatabase db = gm.getPlayerDB();
        db.deletePlayer("Anonymous");
        assertTrue(db.getId("Anonymous") > -1);
    }
    
    @Test
    public void playerListContainsAtLeast2Names() {
        PlayerDatabase db = gm.getPlayerDB();
        assertTrue(db.getNames().size() >= 2);
    }
    
    
}
