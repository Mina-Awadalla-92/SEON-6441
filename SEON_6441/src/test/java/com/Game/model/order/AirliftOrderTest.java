package com.Game.model.order;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.Game.model.CardType;
import com.Game.model.Map; // Adapt if your Map class is in a different package
import com.Game.model.Player;
import com.Game.model.HumanPlayer;
import com.Game.model.Territory;

/**
 * Demonstrates tests for Airlift orders (move & attack) covering: 1) Order
 * validation, 2) Conquering a territory, 3) Moving armies post-conquest, 4)
 * End-of-game scenario when all territories are conquered.
 */
public class AirliftOrderTest {

	private Player attacker;
	private Player defender;
	private Player thirdPlayer; // Used in the end-of-game scenario
	private Territory fromTerritory;
	private Territory enemyTerritory;
	private Territory neutralTerritory;
	private Territory thirdPlayerTerritory;
	private Map testMap;

	@Before
	public void setUp() {
		// Create a small map for testing
		testMap = new Map();

		// Create Players
		attacker = new HumanPlayer("Attacker");
		defender = new HumanPlayer("Defender");
		thirdPlayer = new HumanPlayer("ThirdPlayer");

		// Create a territory owned by Attacker
		fromTerritory = new Territory("FromLand", "ContinentA", 2);
		fromTerritory.setOwner(attacker);
		fromTerritory.setNumOfArmies(10);
		attacker.addTerritory(fromTerritory);

		// Create an enemy territory owned by Defender
		enemyTerritory = new Territory("EnemyLand", "ContinentA", 2);
		enemyTerritory.setOwner(defender);
		enemyTerritory.setNumOfArmies(5);
		defender.addTerritory(enemyTerritory);

		// Create a neutral territory (no owner)
		neutralTerritory = new Territory("NeutralLand", "ContinentA", 2);
		neutralTerritory.setOwner(null);
		neutralTerritory.setNumOfArmies(0);

		// Create a territory for a third player (to test end-of-game scenario)
		thirdPlayerTerritory = new Territory("ThirdLand", "ContinentB", 2);
		thirdPlayerTerritory.setOwner(thirdPlayer);
		thirdPlayerTerritory.setNumOfArmies(2);
		thirdPlayer.addTerritory(thirdPlayerTerritory);

		// Add all territories to the map
		testMap.addTerritory(fromTerritory);
		testMap.addTerritory(enemyTerritory);
		testMap.addTerritory(neutralTerritory);
		testMap.addTerritory(thirdPlayerTerritory);
	}

	/**
	 * 1) Test order validation: Attempt to airlift from a territory the player does
	 * NOT own -> should fail validation.
	 */
	@Test
	public void testAirliftOrderValidation_FailWrongOwner() {
		// "Attacker" tries to airlift from a territory owned by "Defender"
		String invalidCommand = "airlift EnemyLand NeutralLand 3";

		// The attacker calls issueOrder; we expect it to fail validation
		boolean result = attacker.issueOrder(invalidCommand, testMap, Arrays.asList(attacker, defender, thirdPlayer));
		assertFalse("Order validation should fail when source territory isn't owned by the issuer", result);
	}

	/**
	 * 2) Test conquering a country: Attacker uses AirliftAttack on Defender's
	 * territory with enough armies to ensure conquest. We expect the territory to
	 * be owned by the Attacker afterwards.
	 */
	@Test
	public void testAirliftAttack_ConquerEnemyTerritory() {
		// We'll reduce defender's armies to 1 to guarantee conquest
		attacker.addCard(CardType.AIRLIFT);
		enemyTerritory.setNumOfArmies(1);

		// Attacker issues a valid airlift attack
		String conquerCommand = "airlift FromLand EnemyLand 3";
		boolean result = attacker.issueOrder(conquerCommand, testMap, Arrays.asList(attacker, defender, thirdPlayer));
		assertTrue("Order validation should succeed for a valid airlift attack", result);

		// Get the created order and execute it
		Order airliftOrder = attacker.nextOrder();
		assertNotNull(airliftOrder);
		airliftOrder.execute();

		// Now, "EnemyLand" should be conquered by Attacker
		assertEquals("EnemyLand should be owned by Attacker after a successful conquest", attacker,
				enemyTerritory.getOwner());
		// The territory's army count should reflect the surviving attacking armies (>=
		// 0).
		assertTrue("EnemyLand should have some attacking armies placed after conquest",
				enemyTerritory.getNumOfArmies() > 0);
	}

	/**
	 * 3) Test moving armies in a conquered country: - Use AirliftMove to a neutral
	 * territory -> it becomes conquered. - Then check that the newly conquered
	 * territory has the moved armies.
	 */
	@Test
	public void testAirliftMove_ConquerNeutralAndMoveArmies() {
		// "Attacker" tries to airlift 4 armies from "FromLand" to the neutral territory
		attacker.addCard(CardType.AIRLIFT);
		String moveCommand = "airlift FromLand NeutralLand 4";
		boolean result = attacker.issueOrder(moveCommand, testMap, Arrays.asList(attacker, defender, thirdPlayer));
		assertTrue("Order validation should succeed for a valid airlift move", result);

		// Execute the order
		Order airliftOrder = attacker.nextOrder();
		assertNotNull(airliftOrder);
		airliftOrder.execute();

		// The neutral territory should now be owned by Attacker and have 4 armies
		assertEquals("NeutralLand should be owned by Attacker after being airlifted to", attacker,
				neutralTerritory.getOwner());
		assertEquals("NeutralLand should have 4 armies after the move", 4, neutralTerritory.getNumOfArmies());
	}

	/**
	 * 4) Test end-of-game scenario: - If the attacker conquers the last territory
	 * not owned by them, the game ends. - We'll simulate that the attacker has
	 * already conquered "EnemyLand" and "NeutralLand" so only "ThirdLand" remains.
	 * Then we do one more conquest to end the game.
	 */
	@Test
	public void testAirliftAttack_EndOfGame() {
		// Assume attacker already owns "EnemyLand" and "NeutralLand" (simulate a
		// mid-game scenario)
		attacker.addCard(CardType.AIRLIFT);
		enemyTerritory.setOwner(attacker);
		neutralTerritory.setOwner(attacker);

		// So the only territory not owned by Attacker is "ThirdLand" (owned by
		// thirdPlayer).
		// We'll reduce thirdPlayer's armies to 1 so Attacker easily conquers it.
		thirdPlayerTerritory.setNumOfArmies(1);

		// Attacker tries to airlift 5 armies to "ThirdLand" from "FromLand"
		String finalConquest = "airlift FromLand ThirdLand 5";
		boolean result = attacker.issueOrder(finalConquest, testMap, Arrays.asList(attacker, defender, thirdPlayer));
		assertTrue("Order validation should succeed for final conquest", result);

		// Execute the final conquest
		Order airliftOrder = attacker.nextOrder();
		airliftOrder.execute();

		// Now check if Attacker owns ALL territories => end of game
		List<Territory> allTerritories = testMap.getTerritoryList();
		boolean allConquered = allTerritories.stream().allMatch(t -> t.getOwner() == attacker);

		assertTrue("Attacker should now own all territories, indicating end of game", allConquered);
	}
}
