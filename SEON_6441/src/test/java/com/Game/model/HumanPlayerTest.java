package com.Game.model;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HumanPlayerTest {

    private Map map;
    private HumanPlayer human;
    private Territory t1;

    @Before
    public void setUp() {
        // ===== Setup =====
        map = new Map();
        // Add a continent and a territory.
        map.addContinent("Continent1", 5);
        t1 = new Territory("T1", "Continent1", 5);
        map.addTerritory(t1);
        // Create HumanPlayer with 10 reinforcement armies.
        human = new HumanPlayer("Alice", 10,"human1");
        // Assign territory ownership.
        t1.setOwner(human);
        human.addTerritory(t1);
    }

    @After
    public void tearDown() {
        map = null;
        human = null;
        t1 = null;
    }

    @Test
    public void testDeployOrder() {
        // ===== Execution =====
        boolean result = human.issueOrder("deploy T1 5", map, new ArrayList<Player>());
        // ===== After =====
        assertTrue("HumanPlayer should successfully issue a deploy order", result);
        assertEquals("Reinforcement armies should be reduced by 5", 5, human.getNbrOfReinforcementArmies());
        assertFalse("An order should be added to the orders list", human.getOrders().isEmpty());
    }
}
