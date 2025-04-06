package com.Game.model;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before; 
import org.junit.Test;

public class RandomPlayerTest {

    private Map map;
    private RandomPlayer random;
    private Territory r_t1;
    private Territory r_t2;
    private Territory enemy;
    private HumanPlayer enemyPlayer;

    @Before
    public void setUp() {
        // ===== Setup =====
        map = new Map();
        
        map.addContinent("Continent1", 5);
        // Create two territories for the random player.
        r_t1 = new Territory("R_T1", "Continent1", 5);
        r_t2 = new Territory("R_T2", "Continent1", 5);
        map.addTerritory(r_t1);
        map.addTerritory(r_t2);
        random = new RandomPlayer("Random", 12, "Random");
        r_t1.setOwner(random);
        r_t2.setOwner(random);
        random.addTerritory(r_t1);
        random.addTerritory(r_t2);
        // Create an enemy territory adjacent to r_t1.
        enemy = new Territory("EnemyR", "Continent1", 5);
        enemyPlayer = new HumanPlayer("EnemyR", "Human");
        enemy.setOwner(enemyPlayer);
        enemyPlayer.addTerritory(enemy);
        map.addTerritory(enemy);
        // Establish neighbor relationship.
        map.addNeighbor("R_T1", "EnemyR");
    }

    @After
    public void tearDown() {
        map = null;
        random = null;
        r_t1 = null;
        r_t2 = null;
        enemy = null;
        enemyPlayer = null;
    }

    @Test
    public void testRandomIssueOrder() {
        // ===== Execution =====
        boolean result = random.issueOrder("", map, new ArrayList<Player>());
        // ===== After =====
        assertTrue("RandomPlayer should issue orders based on its random strategy", result);
        assertFalse("RandomPlayer should have orders in its list", random.getOrders().isEmpty());
    }
}
