package com.UnitTests;
import com.Game.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link DeployOrder} class.
 * This class tests the execution of deploy orders and the functionality of the copy constructor.
 */
public class DeployOrderTest {

    /**
     * Tests the {@code execute} method of {@link DeployOrder}.
     * <ul>
     *   <li>Creates a player and a territory owned by that player.</li>
     *   <li>Sets an initial number of armies in the territory.</li>
     *   <li>Creates a deploy order to add 5 armies to the territory.</li>
     *   <li>Executes the deploy order and verifies that the army count is updated correctly.</li>
     * </ul>
     */
    @Test
    public void testExecute() {
        // Create a player.
        Player l_testPlayer = new Player("TestPlayer");
        
        // Create a territory, assign it to the player, and add it to the player's territory list.
        Territory l_testTerritory = new Territory("TestTerritory", "TestContinent", 5);
        l_testTerritory.setOwner(l_testPlayer);
        l_testPlayer.addTerritory(l_testTerritory);
        
        // Set an initial number of armies.
        l_testTerritory.setNumOfArmies(10);
        
        // Create a deploy order that deploys 5 armies.
        DeployOrder l_deployOrder = new DeployOrder(l_testPlayer, l_testTerritory, 5);
        
        // Execute the order.
        l_deployOrder.execute();
        
        // Verify the territory's army count increased correctly (10 + 5 = 15).
        assertEquals(15, l_testTerritory.getNumOfArmies());
    }

    /**
     * Tests the copy constructor of {@link DeployOrder}.
     * <ul>
     *   <li>Creates an original deploy order and a copy of it using the copy constructor.</li>
     *   <li>Executes both orders and verifies that the army count is updated correctly each time.</li>
     * </ul>
     */
    @Test
    public void testCopyConstructor() {
        // Create a player.
        Player l_testPlayer = new Player("TestPlayer");
        
        // Create a territory, assign it to the player, and add it to the player's territory list.
        Territory l_testTerritory = new Territory("TestTerritory", "TestContinent", 5);
        l_testTerritory.setOwner(l_testPlayer);
        l_testPlayer.addTerritory(l_testTerritory);
        
        // Set an initial number of armies.
        l_testTerritory.setNumOfArmies(20);
        
        // Create an original deploy order that deploys 10 armies.
        DeployOrder l_originalOrder = new DeployOrder(l_testPlayer, l_testTerritory, 10);
        
        // Create a copy using the copy constructor.
        DeployOrder l_copyOrder = new DeployOrder(l_originalOrder);
        
        // Execute the original order.
        l_originalOrder.execute();
        // After executing the original order, territory armies should be 20 + 10 = 30.
        assertEquals(30, l_testTerritory.getNumOfArmies());
        
        // Execute the copy order.
        l_copyOrder.execute();
        // After executing the copy order, territory armies should be 30 + 10 = 40.
        assertEquals(40, l_testTerritory.getNumOfArmies());
    }
}
