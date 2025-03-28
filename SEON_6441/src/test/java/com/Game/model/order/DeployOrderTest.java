package com.Game.model.order;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.Game.model.Player;
import com.Game.model.Territory;
import java.util.ArrayList;

/**
 * Test class for the DeployOrder class.
 * Tests the Command pattern implementation for deploy orders.
 */
public class DeployOrderTest {
    
    private Player d_player;
    private Territory d_territory;
    private DeployOrder d_deployOrder;
    private final int ARMY_COUNT = 5;
    
    /**
     * Sets up the test environment before each test.
     */
    @Before
    public void setUp() {
        d_player = new Player("TestPlayer", 10); // Player with 10 reinforcement armies
        d_territory = new Territory("TestTerritory", "TestContinent", 2);
        d_territory.setOwner(d_player);
        d_player.addTerritory(d_territory);
        d_deployOrder = new DeployOrder(d_player, d_territory, ARMY_COUNT);
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
        // Add territory to player's owned territories to satisfy the findTerritoryByName check
        ArrayList<Territory> territories = new ArrayList<>();
        territories.add(d_territory);
        d_player.setOwnedTerritories(territories);
        
        boolean success = d_player.createDeployOrder(d_territory.getName(), 3);
        assertTrue("Player should successfully create a deploy order", success);
        assertEquals("Player should have 7 reinforcement armies left", 7, d_player.getNbrOfReinforcementArmies());
        assertEquals("Player should have 1 order in the list", 1, d_player.getOrders().size());
    }
    
    /**
     * Tests validation of deploy orders with invalid parameters.
     */
    @Test
    public void testInvalidDeployOrder() {
        // Add territory to player's owned territories
        ArrayList<Territory> territories = new ArrayList<>();
        territories.add(d_territory);
        d_player.setOwnedTerritories(territories);
        
        // Try to deploy more armies than available
        boolean success = d_player.createDeployOrder(d_territory.getName(), 15);
        assertFalse("Deploy order with too many armies should fail", success);
        
        // Try to deploy to a non-existent territory
        success = d_player.createDeployOrder("NonExistentTerritory", 3);
        assertFalse("Deploy order to non-existent territory should fail", success);
        
        // Reinforcement armies should not have changed
        assertEquals("Player should still have 10 reinforcement armies", 10, d_player.getNbrOfReinforcementArmies());
    }
    
    /**
     * Tests the nextOrder method of Player.
     */
    @Test
    public void testNextOrder() {
        // Add territory to player's owned territories
        ArrayList<Territory> territories = new ArrayList<>();
        territories.add(d_territory);
        d_player.setOwnedTerritories(territories);
        
        d_player.createDeployOrder(d_territory.getName(), 3);
        assertNotNull("Player should have an order", d_player.nextOrder());
        assertNull("Player should have no more orders", d_player.nextOrder());
    }
}