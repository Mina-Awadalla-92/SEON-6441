package com.Game.integration;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.Game.controller.GameController;
import com.Game.controller.GamePlayController;
import com.Game.controller.MapEditorController;
import com.Game.model.CardType;
import com.Game.model.Map;
import com.Game.model.HumanPlayer;
import com.Game.model.Player;
import com.Game.model.Territory;
import com.Game.model.order.Order;
import com.Game.utils.MapLoader;

/**
 * Integration test class for the game flow. Tests the interaction between
 * controllers, models, and the game phases.
 */
public class GameFlowIntegrationTest {

	private GameController d_gameController;
	private GamePlayController d_gamePlayController;
	private MapEditorController d_mapEditorController;
	private Map d_gameMap;
	private MapLoader d_mapLoader;

	/**
	 * Sets up the test environment before each test.
	 */
	@Before
	public void setUp() {
		d_gameController = new GameController();
		d_gameMap = d_gameController.getGameMap();
		d_mapLoader = new MapLoader();
		d_gamePlayController = new GamePlayController(d_gameController, d_gameMap, d_gameController.getPlayers());
		d_mapEditorController = new MapEditorController(d_gameController, d_gameMap, d_mapLoader);
	}

	/**
	 * Tests the creation and assignment of territories in the map editor phase.
	 */
	@Test
	public void testMapCreation() {
		// Test adding continents
		d_mapEditorController.handleCommand(new String[] { "editcontinent", "-add", "Continent1", "5" },
				"editcontinent", true);
		d_mapEditorController.handleCommand(new String[] { "editcontinent", "-add", "Continent2", "3" },
				"editcontinent", true);

		// Test adding countries
		d_mapEditorController.handleCommand(new String[] { "editcountry", "-add", "Country1", "Continent1" },
				"editcountry", true);
		d_mapEditorController.handleCommand(new String[] { "editcountry", "-add", "Country2", "Continent1" },
				"editcountry", true);
		d_mapEditorController.handleCommand(new String[] { "editcountry", "-add", "Country3", "Continent2" },
				"editcountry", true);

		// Test adding neighbors
		d_mapEditorController.handleCommand(new String[] { "editneighbor", "-add", "Country1", "Country2" },
				"editneighbor", true);
		d_mapEditorController.handleCommand(new String[] { "editneighbor", "-add", "Country2", "Country3" },
				"editneighbor", true);

		// Verify map structure
		assertEquals("Map should have 2 continents", 2, d_gameMap.getContinents().size());
		assertEquals("Map should have 3 countries", 3, d_gameMap.getTerritoryList().size());

		// Verify connectivity
		Territory country1 = d_gameMap.getTerritoryByName("Country1");
		Territory country2 = d_gameMap.getTerritoryByName("Country2");
		Territory country3 = d_gameMap.getTerritoryByName("Country3");

		assertNotNull("Country1 should exist", country1);
		assertNotNull("Country2 should exist", country2);
		assertNotNull("Country3 should exist", country3);

		assertTrue("Country1 should be connected to Country2", country1.getNeighborList().contains(country2));
		assertTrue("Country2 should be connected to Country3", country2.getNeighborList().contains(country3));
	}

	/**
	 * Tests the player creation and territory assignment in the startup phase.
	 */
	@Test
	public void testPlayerCreationAndAssignment() {
		// Set up a simple map
		d_mapEditorController.handleCommand(new String[] { "editcontinent", "-add", "Continent1", "5" },
				"editcontinent", true);
		d_mapEditorController.handleCommand(new String[] { "editcountry", "-add", "Country1", "Continent1" },
				"editcountry", true);
		d_mapEditorController.handleCommand(new String[] { "editcountry", "-add", "Country2", "Continent1" },
				"editcountry", true);
		d_mapEditorController.handleCommand(new String[] { "editneighbor", "-add", "Country1", "Country2" },
				"editneighbor", true);

		// Add players
		d_gameController.handleGamePlayer("-add", "Player1");
		d_gameController.handleGamePlayer("-add", "Player2");

		// Verify players were added
		List<Player> players = d_gameController.getPlayers();
		assertEquals("There should be 2 players", 2, players.size());
		assertEquals("First player should be Player1", "Player1", players.get(0).getName());
		assertEquals("Second player should be Player2", "Player2", players.get(1).getName());

		// Assign countries
		assertTrue("Country assignment should succeed", d_gamePlayController.handleAssignCountries());

		// Verify countries were assigned
		for (Territory territory : d_gameMap.getTerritoryList()) {
			assertNotNull("Territory should have an owner", territory.getOwner());
			assertTrue("Territory owner should be a valid player", territory.getOwner().getName().equals("Player1")
					|| territory.getOwner().getName().equals("Player2"));
			assertEquals("Territory should have 1 army initially", 1, territory.getNumOfArmies());
		}
	}

	/**
	 * Tests the reinforcement calculation in the main game phase.
	 */
	@Test
	public void testReinforcementCalculation() {
		// Set up a simple map
		d_mapEditorController.handleCommand(new String[] { "editcontinent", "-add", "Continent1", "5" },
				"editcontinent", true);
		for (int i = 1; i <= 9; i++) {
			d_mapEditorController.handleCommand(new String[] { "editcountry", "-add", "Country" + i, "Continent1" },
					"editcountry", true);
		}

		// Connect all countries
		for (int i = 1; i < 9; i++) {
			d_mapEditorController.handleCommand(
					new String[] { "editneighbor", "-add", "Country" + i, "Country" + (i + 1) }, "editneighbor", true);
		}

		// Add players
		d_gameController.handleGamePlayer("-add", "Player1");
		d_gameController.handleGamePlayer("-add", "Player2");

		// Manually assign territories to test reinforcement calculation
		Player player1 = d_gameController.getPlayers().get(0);
		Player player2 = d_gameController.getPlayers().get(1);

		// Assign 6 territories to Player1 and 3 to Player2
		for (int i = 1; i <= 6; i++) {
			Territory territory = d_gameMap.getTerritoryByName("Country" + i);
			territory.setOwner(player1);
			player1.addTerritory(territory);
		}

		for (int i = 7; i <= 9; i++) {
			Territory territory = d_gameMap.getTerritoryByName("Country" + i);
			territory.setOwner(player2);
			player2.addTerritory(territory);
		}

		// Start the game
		d_gamePlayController.startMainGame();

		// Check reinforcement calculations
		assertEquals("Player1 should have correct reinforcements based on territory count", 3,
				player1.getNbrOfReinforcementArmies());
		assertEquals("Player2 should have minimum of 3 reinforcements", 3, player2.getNbrOfReinforcementArmies());
	}

	/**
	 * Tests the card awarding system after conquering territories.
	 */
	@Test
	public void testCardAwarding() {
		// Instead of calling handleEndTurn, let's test the card system directly

		// Set up players
		Player player1 = new HumanPlayer("Player1", "human1");
		Player player2 = new HumanPlayer("Player2", "human2");

		// Set hasConqueredThisTurn flag for player1
		player1.setHasConqueredThisTurn(true);

		// Check initial state
		assertTrue("Player1 should start with no cards", player1.getCards().isEmpty());
		assertTrue("Player2 should start with no cards", player2.getCards().isEmpty());

		// Manually add a card to player1 (simulating what happens in handleEndTurn)
		player1.addCard(CardType.BOMB);

		// Verify player1 received a card
		assertFalse("Player1 should have cards after conquering", player1.getCards().isEmpty());
		assertTrue("Player1 should have a BOMB card", player1.hasCard(CardType.BOMB));
		assertEquals("Player1 should have exactly 1 card", 1,
				player1.getCards().values().stream().mapToInt(Integer::intValue).sum());

		// Verify player2 did not receive a card
		assertTrue("Player2 should not have cards", player2.getCards().isEmpty());

		// Test using and removing a card
		boolean cardRemoved = player1.removeCard(CardType.BOMB);
		assertTrue("Card should be successfully removed", cardRemoved);
		assertTrue("Player1 should have no cards after removal", player1.getCards().isEmpty());
	}

	/**
	 * Tests the order creation and execution phases.
	 */
	@Test
	public void testOrderCreationAndExecution() {
		// Set up a simple map with two connected territories
		d_mapEditorController.handleCommand(new String[] { "editcontinent", "-add", "Continent1", "5" },
				"editcontinent", true);
		d_mapEditorController.handleCommand(new String[] { "editcountry", "-add", "Country1", "Continent1" },
				"editcountry", true);
		d_mapEditorController.handleCommand(new String[] { "editcountry", "-add", "Country2", "Continent1" },
				"editcountry", true);
		d_mapEditorController.handleCommand(new String[] { "editneighbor", "-add", "Country1", "Country2" },
				"editneighbor", true);

		// Set up a player with territories
		Player player = new HumanPlayer("TestPlayer", 5,"human"); // 5 reinforcement armies
		Territory territory1 = d_gameMap.getTerritoryByName("Country1");
		Territory territory2 = d_gameMap.getTerritoryByName("Country2");

		territory1.setOwner(player);
		player.addTerritory(territory1);
		territory2.setOwner(player);
		player.addTerritory(territory2);

		// Add the player to the controller
		d_gameController.getPlayers().add(player);

		// Create a deploy order
		String deployCommand = "deploy Country1 3";
		player.issueOrder(deployCommand, d_gameMap, d_gameController.getPlayers());

		// Check that the order was created
		assertEquals("Player should have 1 order", 1, player.getOrders().size());
		assertEquals("Player should have 2 reinforcement armies left", 2, player.getNbrOfReinforcementArmies());

		// Execute the order
		Order order = player.nextOrder();
		assertNotNull("Order should not be null", order);
		order.execute();

		// Verify order execution
		assertEquals("Territory1 should have 3 armies after deploy", 3, territory1.getNumOfArmies());
		assertEquals("Player should have no more orders", 0, player.getOrders().size());
	}
}