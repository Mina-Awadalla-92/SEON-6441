package com.Game.model.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.Game.model.Player;
import com.Game.model.Territory;


/**
 * The DeployOrder class is a concrete implementation of Order, so we can test:
 * 1.The inherited methods from Order (e.g., getIssuer, setIssuer).
 * 2.The specific functionality of DeployOrder (e.g., execute, getTargetTerritory, getNumberOfArmies).
*/
class DeployOrderTest {

    private Player player;
    private Territory territory;
    private DeployOrder deployOrder;

    @BeforeEach
    void setUp() {
        // Initialize a Player and a Territory
        player = new Player("Player1");
        territory = new Territory("Territory1", "Continent1", 5);

        // Initialize a DeployOrder
        deployOrder = new DeployOrder(player, territory, 10);
    }

    @Test
    void testConstructorWithParameters() {
        assertEquals(player, deployOrder.getIssuer(), "The issuer should be Player1.");
        assertEquals(territory, deployOrder.getTargetTerritory(), "The target territory should be Territory1.");
        assertEquals(10, deployOrder.getNumberOfArmies(), "The number of armies should be 10.");
    }

    @Test
    void testDefaultConstructor() {
        DeployOrder defaultOrder = new DeployOrder();
        assertNull(defaultOrder.getIssuer(), "The issuer should be null for the default constructor.");
        assertNull(defaultOrder.getTargetTerritory(), "The target territory should be null for the default constructor.");
        assertEquals(0, defaultOrder.getNumberOfArmies(), "The number of armies should be 0 for the default constructor.");
    }

    @Test
    void testCopyConstructor() {
        DeployOrder copiedOrder = new DeployOrder(deployOrder);
        assertEquals(player, copiedOrder.getIssuer(), "The copied order should have the same issuer as the original.");
        assertEquals(territory, copiedOrder.getTargetTerritory(), "The copied order should have the same target territory as the original.");
        assertEquals(10, copiedOrder.getNumberOfArmies(), "The copied order should have the same number of armies as the original.");
    }

    @Test
    void testSetAndGetTargetTerritory() {
        Territory newTerritory = new Territory("Territory2", "Continent2", 3);
        deployOrder.setTargetTerritory(newTerritory);
        assertEquals(newTerritory, deployOrder.getTargetTerritory(), "The target territory should be updated to Territory2.");
    }

    @Test
    void testSetAndGetNumberOfArmies() {
        deployOrder.setNumberOfArmies(20);
        assertEquals(20, deployOrder.getNumberOfArmies(), "The number of armies should be updated to 20.");
    }

    @Test
    void testExecute() {
        // Set initial number of armies in the territory
        territory.setNumOfArmies(5);

        // Execute the deploy order
        deployOrder.execute();

        // Verify the number of armies in the territory after execution
        assertEquals(15, territory.getNumOfArmies(), "The territory should have 15 armies after execution.");
    }
}
