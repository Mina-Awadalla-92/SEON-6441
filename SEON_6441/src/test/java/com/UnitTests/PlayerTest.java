package com.UnitTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


import com.Game.Player;
import com.Game.Order;
import com.Game.Territory;
import com.Game.DeployOrder;

/**
 * The {@code PlayerTest} class contains unit tests for the {@code Player} class.
 * These tests verify the functionality of the methods in the {@code Player} class,
 */
public class PlayerTest {

	/**
	 * The Player object being tested.
	 */
	private Player d_player;

	/**
	 * The Territory object being tested.
	 */
	private Territory d_territory;

	/**
	 * Set up the environment for each test.
	 * Initializes a player with a name, assigns a territory to the player,
	 * and sets the number of reinforcement armies available to the player.
	 */
	@BeforeEach
	void setup() {
		d_player = new Player("Ali");
		d_territory = new Territory("Canada", "America", 5);
		d_territory.setOwner(d_player);
		d_player.getOwnedTerritories().add(d_territory);

		d_player.setNbrOfReinforcementArmies(10);
	}

	/**
	 * Test that verifies if a valid deploy order reduces the player's reinforcement armies
	 * and successfully deploys armies to a territory.
	 */
	@Test
	void testValidDeployOrder() {
		
		String input = "deploy Canada 5\n";
		
		InputStream backupIn = System.in;
		
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			d_player.issueOrder();
		} finally {
			System.setIn(backupIn);
		}
		
	
		
		//Making sure that the number of armies the player has went from 10 to 5
		assertEquals(5, d_player.getNbrOfReinforcementArmies());
		
		//Making sure that the order is created and exists in the order list of the player
		Order order = d_player.nextOrder();
		assertNotNull(order);
		
		//Making sure that its a deploy order and not any type of orders
		assertTrue(order instanceof DeployOrder);
		
		// Making sure that there are no armies in the target territory
        assertEquals(0, d_territory.getNumOfArmies());
		
		order.execute();
		
		//Making sure that the territory armies increased from 0 to 5
        assertEquals(5, d_territory.getNumOfArmies());

		
		
	}

	/**
	 * Test that ensures the player cannot deploy more armies than they have in their reinforcement pool.
	 */
	@Test
	void testNotEnoughReinforcements() {

		String fakeInput = "deploy Canada 20\n";
        InputStream backupIn = System.in;

        try {
            ByteArrayInputStream in = new ByteArrayInputStream(fakeInput.getBytes());
            System.setIn(in);

			d_player.issueOrder();

        } finally {
            System.setIn(backupIn);
        }

        // The deploy should fail because the player only has 10
        assertEquals(10, d_player.getNbrOfReinforcementArmies());
        
        // Making sure that the order was not added
        assertNull(d_player.nextOrder(), "No order should have been created.");
    }

	/**
	 * Test that ensures the player cannot deploy armies to a territory they do not own.
	 */
	@Test
    void testUnknownTerritory() {
        // Provide fake input: "deploy Berlin 3\n" (player does not own "Berlin")
        String fakeInput = "deploy USA 3\n";
        InputStream backupIn = System.in;

        try {
            ByteArrayInputStream in = new ByteArrayInputStream(fakeInput.getBytes());
            System.setIn(in);

			d_player.issueOrder();

        } finally {
            System.setIn(backupIn);
        }

        // The reinforcement pool should remain unchanged
        assertEquals(10, d_player.getNbrOfReinforcementArmies());
        // No order should be created
        assertNull(d_player.nextOrder());
    }

	/**
	 * Test that verifies if multiple deploy orders can be issued sequentially and
	 * correctly execute, updating both the player's reinforcement pool and the territory's army count.
	 */
	@Test
	void testIssueOrder_MultipleOrders() {
	    // Provide multiple lines of input, each line representing one deploy command.
	    // For example, we want to deploy 3 armies, then 2 armies, to "Paris".
	    String fakeInput1 = "deploy Canada 3\n";
	    String fakeInput2 = "deploy Canada 2\n";
	    
	    // Backup the original System.in
	    InputStream backupIn = System.in;

	    try {
	        ByteArrayInputStream in = new ByteArrayInputStream(fakeInput1.getBytes());
	        System.setIn(in);
			d_player.issueOrder();
	        
	        in = new ByteArrayInputStream(fakeInput2.getBytes());
	        System.setIn(in);
			d_player.issueOrder();

	    } finally {
	        System.setIn(backupIn);
	    }



	    // Now we should have two orders
	    // The total reinforcements used: 3 + 2 = 5, so the player’s pool should be 5 left (10 - 5).
	    assertEquals(5, d_player.getNbrOfReinforcementArmies(),
	        "Reinforcement pool should be 5 after deploying 3 and 2 armies.");

	    // Retrieving the first order
	    Order firstOrder = d_player.nextOrder();
	    assertNotNull(firstOrder, "First order should not be null.");
	    firstOrder.execute();
	    
	    // After the first deploy, territory should have 3 armies.
	    assertEquals(3, d_territory.getNumOfArmies(),
	        "Territory should have 3 armies after the first deploy.");
	    
	    // Retrieving the second order
	    Order secondOrder = d_player.nextOrder();
	    assertNotNull(secondOrder, "Second order should not be null.");
	    secondOrder.execute();
	    
	    // After the second deploy, territory should have 5 armies total (3 + 2).
	    assertEquals(5, d_territory.getNumOfArmies(),
	        "Territory should have 5 armies total after both deploy orders.");

	    // There should be no more orders in the queue
	    assertNull(d_player.nextOrder(), "No more orders should remain.");
	}
}
