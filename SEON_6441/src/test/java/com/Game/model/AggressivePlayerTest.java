package com.Game.model;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AggressivePlayerTest {

    private Map map;
    private AggressivePlayer aggressive;
    private Territory a_t1;
    private Territory a_t2;
    private Territory enemy;
    private HumanPlayer enemyPlayer;

    @Before
    public void setUp() {
        // ===== Setup =====
        map = new Map();
        map.addContinent("Continent1", 5);
        // Create two territories for the aggressive player.
        a_t1 = new Territory("A_T1", "Continent1", 5);
        a_t2 = new Territory("A_T2", "Continent1", 5);
        map.addTerritory(a_t1);
        map.addTerritory(a_t2);
        aggressive = new AggressivePlayer("Aggressive", 10,"Aggressive");
        a_t1.setOwner(aggressive);
        a_t2.setOwner(aggressive);
        aggressive.addTerritory(a_t1);
        aggressive.addTerritory(a_t2);
        // Create an enemy territory.
        enemy = new Territory("Enemy", "Continent1", 5);
        enemyPlayer = new HumanPlayer("Enemy", "Human");
        enemy.setOwner(enemyPlayer);
        enemyPlayer.addTerritory(enemy);
        map.addTerritory(enemy);
        // Establish neighbor relationship using the Map's method.
        map.addNeighbor("A_T1", "Enemy");
    }

    @After
    public void tearDown() {
        map = null;
        aggressive = null;
        a_t1 = null;
        a_t2 = null;
        enemy = null;
        enemyPlayer = null;
    }

    @Test
    public void testAggressiveIssueOrder() {
        // ===== Execution =====
        boolean result = aggressive.issueOrder("", map, new ArrayList<Player>());
        // ===== After =====
        assertTrue("AggressivePlayer should issue orders based on its aggressive strategy", result);
        assertFalse("AggressivePlayer should have at least one order in its list", aggressive.getOrders().isEmpty());
    }
}
