package com.Game.model.order;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.Game.model.Player;
import com.Game.model.HumanPlayer;
import com.Game.model.Territory;

/**
 * Test class for the AdvanceOrder classes (AdvanceMove and AdvanceAttack).
 * Tests the Command pattern implementation for advance orders.
 */
public class AdvanceOrderTest {

	private Player d_attacker;
	private Player d_defender;
	private Territory d_sourceTerritory;
	private Territory d_friendlyTargetTerritory;
	private Territory d_enemyTargetTerritory;
	private AdvanceMove d_advanceMove;
	private AdvanceAttack d_advanceAttack;
	private final int ARMY_COUNT = 3;

	/**
	 * Sets up the test environment before each test.
	 */
	@Before
	public void setUp() {
		// Create players
		d_attacker = new HumanPlayer("Attacker", 10,"human1");
		d_defender = new HumanPlayer("Defender", 10,"human2");

		// Create territories
		d_sourceTerritory = new Territory("SourceTerritory", "TestContinent", 2);
		d_sourceTerritory.setNumOfArmies(5); // 5 armies in source
		d_sourceTerritory.setOwner(d_attacker);

		d_friendlyTargetTerritory = new Territory("FriendlyTarget", "TestContinent", 2);
		d_friendlyTargetTerritory.setNumOfArmies(1); // 1 army in friendly target
		d_friendlyTargetTerritory.setOwner(d_attacker);

		d_enemyTargetTerritory = new Territory("EnemyTarget", "TestContinent", 2);
		d_enemyTargetTerritory.setNumOfArmies(2); // 2 armies in enemy target
		d_enemyTargetTerritory.setOwner(d_defender);

		// Set up neighbors
		d_sourceTerritory.addNeighbor(d_friendlyTargetTerritory);
		d_sourceTerritory.addNeighbor(d_enemyTargetTerritory);
		d_friendlyTargetTerritory.addNeighbor(d_sourceTerritory);
		d_enemyTargetTerritory.addNeighbor(d_sourceTerritory);

		// Add territories to players
		d_attacker.addTerritory(d_sourceTerritory);
		d_attacker.addTerritory(d_friendlyTargetTerritory);
		d_defender.addTerritory(d_enemyTargetTerritory);

		// Create orders
		d_advanceMove = new AdvanceMove(d_attacker, d_sourceTerritory, d_friendlyTargetTerritory, ARMY_COUNT);
		d_advanceAttack = new AdvanceAttack(d_attacker, d_sourceTerritory, d_enemyTargetTerritory, ARMY_COUNT);
	}

	/**
	 * Tests the creation of an advance move order.
	 */
	@Test
	public void testCreateAdvanceMove() {
		assertNotNull("AdvanceMove should be created", d_advanceMove);
		assertEquals("Player should be set correctly", d_attacker, d_advanceMove.getIssuer());
		assertEquals("Source territory should be set correctly", d_sourceTerritory, d_advanceMove.getD_territoryFrom());
		assertEquals("Target territory should be set correctly", d_friendlyTargetTerritory,
				d_advanceMove.getD_territoryTo());
		assertEquals("Army count should be set correctly", ARMY_COUNT, d_advanceMove.getD_numberOfArmies());
	}

	/**
	 * Tests the creation of an advance attack order.
	 */
	@Test
	public void testCreateAdvanceAttack() {
		assertNotNull("AdvanceAttack should be created", d_advanceAttack);
		assertEquals("Player should be set correctly", d_attacker, d_advanceAttack.getIssuer());
		assertEquals("Source territory should be set correctly", d_sourceTerritory,
				d_advanceAttack.getD_territoryFrom());
		assertEquals("Target territory should be set correctly", d_enemyTargetTerritory,
				d_advanceAttack.getD_territoryTo());
		assertEquals("Army count should be set correctly", ARMY_COUNT, d_advanceAttack.getD_numberOfArmies());
	}

	/**
	 * Tests the execution of an advance move order.
	 */
	@Test
	public void testExecuteAdvanceMove() {
		int sourceInitialArmies = d_sourceTerritory.getNumOfArmies();
		int targetInitialArmies = d_friendlyTargetTerritory.getNumOfArmies();

		// Execute the move
		d_advanceMove.execute();

		// Target should gain armies
		assertEquals("Target territory should have increased armies after advance move",
				targetInitialArmies + ARMY_COUNT, d_friendlyTargetTerritory.getNumOfArmies());
	}

	/**
	 * Tests the execution of an advance attack order with a successful conquest.
	 * This test simulates a successful attack by directly manipulating the target
	 * territory.
	 */
	@Test
	public void testSuccessfulAdvanceAttack() {
		// Set up a scenario where the attack will be successful (enemy has 0 armies)
		d_enemyTargetTerritory.setNumOfArmies(0);

		// Record initial owner
		Player initialOwner = d_enemyTargetTerritory.getOwner();

		// Execute the attack
		d_advanceAttack.execute();

		// Check if territory was captured
		assertNotEquals("Territory owner should have changed", initialOwner, d_enemyTargetTerritory.getOwner());
		assertEquals("Territory should be owned by attacker", d_attacker, d_enemyTargetTerritory.getOwner());
	}

	/**
	 * Tests the validation of advance orders in the player's issueOrder method.
	 * This covers different validation scenarios for advance orders.
	 */
	@Test
	public void testAdvanceOrderValidation() {
		// Set up player's territory list for validation
		List<Territory> attackerTerritories = new ArrayList<>();
		attackerTerritories.add(d_sourceTerritory);
		attackerTerritories.add(d_friendlyTargetTerritory);
		d_attacker.setOwnedTerritories(attackerTerritories);

		// Create a test map with territories for command validation
		com.Game.model.Map testMap = new com.Game.model.Map();
		testMap.addTerritory(d_sourceTerritory);
		testMap.addTerritory(d_friendlyTargetTerritory);
		testMap.addTerritory(d_enemyTargetTerritory);

		// Test issuing an advance to a non-adjacent territory (should fail)
		Territory nonAdjacentTerritory = new Territory("NonAdjacent", "TestContinent", 2);
		testMap.addTerritory(nonAdjacentTerritory);

		List<Player> players = new ArrayList<>();
		players.add(d_attacker);
		players.add(d_defender);

		// This scenario would need a more complex test setup with the actual string
		// command parsing logic
		// or would need to be tested via integration tests
	}

	/**
	 * Tests the interaction between AdvanceMove and territory ownership.
	 */
	@Test
	public void testAdvanceMoveWithNeutralTerritory() {
		// Create a neutral territory (no owner)
		Territory neutralTerritory = new Territory("NeutralTerritory", "TestContinent", 2);
		neutralTerritory.setNumOfArmies(0);
		neutralTerritory.setOwner(null);

		// Set up neighbors
		d_sourceTerritory.addNeighbor(neutralTerritory);
		neutralTerritory.addNeighbor(d_sourceTerritory);

		// Create advance move to neutral territory
		AdvanceMove advanceToNeutral = new AdvanceMove(d_attacker, d_sourceTerritory, neutralTerritory, ARMY_COUNT);

		// Execute the move
		advanceToNeutral.execute();

		// Check if the neutral territory was conquered
		assertEquals("Neutral territory should now be owned by attacker", d_attacker, neutralTerritory.getOwner());
		assertEquals("Neutral territory should have the advanced armies", ARMY_COUNT,
				neutralTerritory.getNumOfArmies());
	}
}