package com.Game.model;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CheaterPlayerTest {

    private Map map;
    private CheaterPlayer cheater;
    private Territory c_t1;
    private Territory c_t2;
    private Territory enemy1;
    private Territory enemy2;
    private HumanPlayer enemyPlayer;

    @Before
    public void setUp() {
        // ===== Setup =====
        map = new Map();
        map.addContinent("Continent1", 5);
        // Create two territories for the cheater.
        c_t1 = new Territory("C_T1", "Continent1", 5);
        c_t2 = new Territory("C_T2", "Continent1", 5);
        map.addTerritory(c_t1);
        map.addTerritory(c_t2);
        cheater = new CheaterPlayer("Cheater","Cheater");
        c_t1.setOwner(cheater);
        c_t2.setOwner(cheater);
        cheater.addTerritory(c_t1);
        cheater.addTerritory(c_t2);
        // Set initial armies.
        c_t1.setNumOfArmies(5);
        c_t2.setNumOfArmies(7);
        // Create enemy territories adjacent to the cheater's territories.
        enemy1 = new Territory("EnemyC1", "Continent1", 5);
        enemy2 = new Territory("EnemyC2", "Continent1", 5);
        enemyPlayer = new HumanPlayer("EnemyC", "Human");
        enemy1.setOwner(enemyPlayer);
        enemy2.setOwner(enemyPlayer);
        enemyPlayer.addTerritory(enemy1);
        enemyPlayer.addTerritory(enemy2);
        map.addTerritory(enemy1);
        map.addTerritory(enemy2);
        // Establish neighbor relationships.
        map.addNeighbor("C_T1", "EnemyC1");
        map.addNeighbor("C_T2", "EnemyC2");
    }

    @After
    public void tearDown() {
        map = null;
        cheater = null;
        c_t1 = null;
        c_t2 = null;
        enemy1 = null;
        enemy2 = null;
        enemyPlayer = null;
    }

    @Test
    public void testCheaterIssueOrder() {
        // ===== Execution =====
        boolean result = cheater.issueOrder("", map, new ArrayList<Player>());
        // ===== After =====
        assertTrue("CheaterPlayer's issueOrder should return true", result);
        // Verify that armies on each cheater territory are doubled.
        Territory check_t1 = map.getTerritoryByName("C_T1");
        Territory check_t2 = map.getTerritoryByName("C_T2");
        assertEquals("C_T1's armies should be doubled to 10", 10, check_t1.getNumOfArmies());
        assertEquals("C_T2's armies should be doubled to 14", 14, check_t2.getNumOfArmies());
        // Verify that enemy territories have been captured.
        Territory check_enemy1 = map.getTerritoryByName("EnemyC1");
        Territory check_enemy2 = map.getTerritoryByName("EnemyC2");
        assertEquals("EnemyC1 should be captured by the cheater", cheater, check_enemy1.getOwner());
        assertEquals("EnemyC2 should be captured by the cheater", cheater, check_enemy2.getOwner());
        // Verify that the captured territories are now in the cheater's owned list.
        assertTrue("Cheater should now own EnemyC1", cheater.getOwnedTerritories().contains(check_enemy1));
        assertTrue("Cheater should now own EnemyC2", cheater.getOwnedTerritories().contains(check_enemy2));
    }
}
