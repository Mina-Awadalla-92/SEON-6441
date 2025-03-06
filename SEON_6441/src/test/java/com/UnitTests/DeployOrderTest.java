package com.UnitTests;
import com.Game.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DeployOrderTest {

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
