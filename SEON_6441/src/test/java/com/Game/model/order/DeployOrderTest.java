package com.Game.model.order;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.model.HumanPlayer;
import com.Game.model.Territory;

/**
 * Test class for the DeployOrder class. Tests the Command pattern
 * implementation for deploy orders.
 */
public class DeployOrderTest {

    private Player d_player;
    private Territory d_territory;
    private DeployOrder d_deployOrder;
    private final int ARMY_COUNT = 5;
    private Map map;

    /**
     * Sets up the test environment before each test.
     */
    @Before
    public void setUp() {
        // Create a HumanPlayer with 10 reinforcement armies.
        d_player = new HumanPlayer("TestPlayer", 10,"human");
        // Create a territory and assign it to a continent.
        d_territory = new Territory("TestTerritory", "TestContinent", 2);
        d_territory.setOwner(d_player);
        d_player.addTerritory(d_territory);
        // Create a DeployOrder for testing its internal state and execution.
        d_deployOrder = new DeployOrder(d_player, d_territory, ARMY_COUNT);
        // Create a Map instance, add the continent and the territory.
        map = new Map();
        map.addContinent("TestContinent", 2);
        map.addTerritory(d_territory);
    }

    /**
     * Tests the creation of a deploy order.
     */
    @Test
    public void testCreateDeployOrder() {
        assertNotNull("DeployOrder should be created", d_deployOrder);
        assertEquals("Player should be set correctly", d_player, d_deployOrder.getIssuer());
        assertEquals("Territory should be set correctly", d_territory, d_deployOrder.getTargetTerritory());
        assertEquals("Army count should be set correctly", ARMY_COUNT, d_deployOrder.getNumberOfArmies());
    }

    /**
     * Tests the execution of a deploy order.
     */
    @Test
    public void testExecuteDeployOrder() {
        int initialArmies = d_territory.getNumOfArmies();
        d_deployOrder.execute();
        assertEquals("Territory should have increased armies after deploy execution",
                     initialArmies + ARMY_COUNT, d_territory.getNumOfArmies());
    }

    /**
     * Tests the player's issueOrder method for deploy orders.
     */
    @Test
    public void testPlayerIssueDeployOrder() {
        // ===== Execution =====
        // Issue a deploy order using issueOrder (command format: "deploy <territory> <armies>").
        boolean success = d_player.issueOrder("deploy " + d_territory.getName() + " 3", map, new ArrayList<Player>());
        // ===== After =====
        assertTrue("Player should successfully issue a deploy order", success);
        // 10 - 3 = 7 reinforcement armies should remain.
        assertEquals("Player should have 7 reinforcement armies left", 7, d_player.getNbrOfReinforcementArmies());
        assertEquals("Player should have 1 order in the list", 1, d_player.getOrders().size());
    }

    /**
     * Tests validation of deploy orders with invalid parameters.
     */
    @Test
    public void testInvalidDeployOrder() {
        // ===== Execution =====
        // Try to deploy more armies than available.
        boolean success = d_player.issueOrder("deploy " + d_territory.getName() + " 15", map, new ArrayList<Player>());
        assertFalse("Deploy order with too many armies should fail", success);

        // Try to deploy to a non-existent territory.
        success = d_player.issueOrder("deploy NonExistentTerritory 3", map, new ArrayList<Player>());
        assertFalse("Deploy order to non-existent territory should fail", success);

        // ===== After =====
        // Reinforcement armies should remain unchanged (i.e., still 10).
        assertEquals("Player should still have 10 reinforcement armies", 10, d_player.getNbrOfReinforcementArmies());
    }

    /**
     * Tests the nextOrder method of Player.
     */
    @Test
    public void testNextOrder() {
        // ===== Execution =====
        d_player.issueOrder("deploy " + d_territory.getName() + " 3", map, new ArrayList<Player>());
        // ===== After =====
        assertNotNull("Player should have an order", d_player.nextOrder());
        assertNull("Player should have no more orders", d_player.nextOrder());
    }
}
