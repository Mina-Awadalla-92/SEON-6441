package com.Game.model;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BenevolentPlayerTest {

    private Map map;
    private BenevolentPlayer benevolent;
    private Territory weak;
    private Territory strong;
    private Territory enemy;
    private HumanPlayer enemyPlayer;

    @Before
    public void setUp() {
        // ===== Setup =====
        map = new Map();
        map.addContinent("Continent1", 5);
        // Create two territories for the benevolent player.
        weak = new Territory("B_T1", "Continent1", 5);
        strong = new Territory("B_T2", "Continent1", 5);
        map.addTerritory(weak);
        map.addTerritory(strong);
        benevolent = new BenevolentPlayer("Benevolent", 8);
        weak.setOwner(benevolent);
        strong.setOwner(benevolent);
        benevolent.addTerritory(weak);
        benevolent.addTerritory(strong);
        // Set army counts so that weak is vulnerable.
        weak.setNumOfArmies(2);
        strong.setNumOfArmies(6);
        // Create an enemy territory adjacent to 'strong'.
        enemy = new Territory("EnemyB", "Continent1", 5);
        enemyPlayer = new HumanPlayer("EnemyB");
        enemy.setOwner(enemyPlayer);
        enemyPlayer.addTerritory(enemy);
        map.addTerritory(enemy);
        // Establish neighbor relationship via the Map's method.
        map.addNeighbor("B_T2", "EnemyB");
    }

    @After
    public void tearDown() {
        map = null;
        benevolent = null;
        weak = null;
        strong = null;
        enemy = null;
        enemyPlayer = null;
    }

    @Test
    public void testBenevolentIssueOrder() {
        // ===== Execution =====
        boolean result = benevolent.issueOrder("", map, new ArrayList<Player>());
        // ===== After =====
        assertTrue("BenevolentPlayer should issue orders based on its defensive strategy", result);
        assertFalse("BenevolentPlayer should have orders (deploy or reallocation) in its list", benevolent.getOrders().isEmpty());
    }
}
