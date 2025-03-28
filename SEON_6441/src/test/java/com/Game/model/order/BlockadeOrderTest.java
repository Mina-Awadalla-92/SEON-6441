package com.Game.model.order;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.Game.model.Player;
import com.Game.model.Territory;

/**
 * Unit tests for BlockadeOrder.
 * 
 * Tests include:
 * 1. Order validation failure when no Blockade card is available.
 * 2. Successful execution: the territory becomes neutral and its armies are tripled.
 */
public class BlockadeOrderTest {

    private Player player;
    private Territory territory;

    @Before
    public void setUp() {
        // Create a player with a name (reinforcement armies not relevant for blockade tests)
        player = new Player("PlayerOne");

        // Create a territory owned by the player
        territory = new Territory("TestLand", "TestContinent", 2);
        territory.setNumOfArmies(4);  // Set initial armies to 4
        territory.setOwner(player);
        player.addTerritory(territory);
    }

    /**
     * Test that the blockade order fails validation if the player does not have a Blockade card.
     */
    @Test
    public void testBlockadeOrderValidationFailsWithoutCard() {
        // Attempt to issue a blockade order without adding a Blockade card
        // Command format: "blockade TestLand"
        boolean result = player.issueOrder("blockade TestLand", null, null);
        // Since validateBlockade calls removeCard(CardType.BLOCKADE) and there is no such card,
        // we expect the order to be rejected.
        assertFalse("Blockade order should fail when no Blockade card is available.", result);
    }

    /**
     * Test that a valid blockade order correctly converts the target territory to neutral and triples its armies.
     */
    @Test
    public void testBlockadeOrderExecution() {
        // First, add a Blockade card to the player's card collection so the order can be validated.
        player.addCard(com.Game.model.CardType.BLOCKADE);

        // Issue a blockade order on "TestLand".
        // Format: "blockade TestLand"
        boolean result = player.issueOrder("blockade TestLand", null, null);
        assertTrue("Blockade order should be issued successfully when a Blockade card is available.", result);

        // Retrieve the created order from the player's order list.
        Order blockadeOrder = player.nextOrder();
        assertNotNull("A BlockadeOrder should have been created.", blockadeOrder);
        assertTrue("Order should be an instance of BlockadeOrder.", blockadeOrder instanceof BlockadeOrder);

        // Execute the blockade order.
        blockadeOrder.execute();

        // After execution, the territory should become neutral (owner = null)
        assertNull("After blockade, the territory should have no owner (neutral).", territory.getOwner());

        // The number of armies should be tripled.
        // With an initial count of 4, we expect 4 * 3 = 12 armies.
        assertEquals("The territory's army count should be tripled after blockade.", 12, territory.getNumOfArmies());
    }
}
