package com.Game.model.order;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.Game.model.Player;
import com.Game.model.HumanPlayer;
import com.Game.model.Territory;

/**
 * Unit tests for BombOrder.
 *
 * These tests cover: 1. Successful bombing where the target territory's armies
 * are halved. 2. Cancellation of the bomb order due to active diplomacy between
 * the issuer and the target's owner.
 */
public class BombOrderTest {

	private Player issuer;
	private Player defender;
	private Territory targetTerritory;

	@Before
	public void setUp() {
		// Create the issuer (the player issuing the bomb order)
		issuer = new HumanPlayer("Issuer");

		// Create the defender (the owner of the target territory)
		defender = new HumanPlayer("Defender");

		// Create a territory for the defender with an initial army count.
		targetTerritory = new Territory("TargetLand", "TestContinent", 2);
		targetTerritory.setOwner(defender);
		targetTerritory.setNumOfArmies(20); // For example, start with 20 armies.
		defender.addTerritory(targetTerritory);
	}

	/**
	 * Test successful execution of BombOrder: The territory's army count should be
	 * halved.
	 */
	@Test
	public void testBombOrderExecution_Success() {
		// Create a BombOrder issued by 'issuer' targeting 'targetTerritory'
		BombOrder bombOrder = new BombOrder(issuer, targetTerritory);

		// Execute the bomb order.
		bombOrder.execute();

		// After execution, the target territory's armies should be halved (20 / 2 =
		// 10).
		assertEquals("TargetLand should have half its original armies (20/2 = 10) after bomb execution.", 10,
				targetTerritory.getNumOfArmies());
	}

	/**
	 * Test that the BombOrder is cancelled when there is active diplomacy: If the
	 * target territory's owner is in the issuer's negotiated players list, the bomb
	 * order should be undone and the army count remains unchanged.
	 */
	@Test
	public void testBombOrderExecution_CancelledDueToDiplomacy() {
		// Add the target territory's owner (defender) to the issuer's negotiated
		// players list.
		issuer.getNegociatedPlayersPerTurn().add(defender);

		// Capture the initial number of armies.
		int initialArmies = targetTerritory.getNumOfArmies();

		// Create and execute the BombOrder.
		BombOrder bombOrder = new BombOrder(issuer, targetTerritory);
		bombOrder.execute();

		// Since diplomacy is active, the bomb order should be cancelled and the army
		// count should remain unchanged.
		assertEquals("Bomb order should be cancelled due to diplomacy; the territory's armies remain unchanged.",
				initialArmies, targetTerritory.getNumOfArmies());
	}
}
