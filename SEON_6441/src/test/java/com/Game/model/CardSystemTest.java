package com.Game.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the card system implementation. Tests the card functionality
 * in the Player class.
 */
public class CardSystemTest {

	private HumanPlayer d_player;

	/**
	 * Sets up the test environment before each test.
	 */
	@Before
	public void setUp() {
		d_player = new HumanPlayer("TestPlayer", 10);
	}

	/**
	 * Tests adding a card to a player.
	 */
	@Test
	public void testAddCard() {
		// Player should start with no cards
		assertTrue("Player should start with no cards", d_player.getCards().isEmpty());

		// Add a bomb card
		d_player.addCard(CardType.BOMB);

		// Check if the card was added correctly
		assertFalse("Player should now have cards", d_player.getCards().isEmpty());
		assertTrue("Player should have a bomb card", d_player.getCards().containsKey(CardType.BOMB));
		assertEquals("Player should have 1 bomb card", 1, (int) d_player.getCards().get(CardType.BOMB));
	}

	/**
	 * Tests adding multiple cards of the same type.
	 */
	@Test
	public void testAddMultipleCards() {
		// Add multiple bomb cards
		d_player.addCard(CardType.BOMB);
		d_player.addCard(CardType.BOMB);
		d_player.addCard(CardType.BOMB);

		// Check if the count was incremented correctly
		assertEquals("Player should have 3 bomb cards", 3, (int) d_player.getCards().get(CardType.BOMB));
	}

	/**
	 * Tests adding different types of cards.
	 */
	@Test
	public void testAddDifferentCards() {
		d_player.addCard(CardType.BOMB);
		d_player.addCard(CardType.BLOCKADE);
		d_player.addCard(CardType.AIRLIFT);
		d_player.addCard(CardType.NEGOTIATE);

		// Check if all cards were added correctly
		assertEquals("Player should have 1 bomb card", 1, (int) d_player.getCards().get(CardType.BOMB));
		assertEquals("Player should have 1 blockade card", 1, (int) d_player.getCards().get(CardType.BLOCKADE));
		assertEquals("Player should have 1 airlift card", 1, (int) d_player.getCards().get(CardType.AIRLIFT));
		assertEquals("Player should have 1 negotiate card", 1, (int) d_player.getCards().get(CardType.NEGOTIATE));
	}

	/**
	 * Tests removing a card from a player.
	 */
	@Test
	public void testRemoveCard() {
		// Add a card first
		d_player.addCard(CardType.BOMB);

		// Remove the card
		boolean result = d_player.removeCard(CardType.BOMB);

		// Check if the card was removed correctly
		assertTrue("Card removal should be successful", result);
		assertTrue("Player should have no cards after removal", d_player.getCards().isEmpty());
	}

	/**
	 * Tests removing a card type that the player doesn't have.
	 */
	@Test
	public void testRemoveNonExistentCard() {
		// Try to remove a card the player doesn't have
		boolean result = d_player.removeCard(CardType.BOMB);

		// Check that the removal failed
		assertFalse("Card removal should fail for non-existent card", result);
	}

	/**
	 * Tests removing one card when the player has multiple of that type.
	 */
	@Test
	public void testRemoveOneOfMultipleCards() {
		// Add multiple cards
		d_player.addCard(CardType.BOMB);
		d_player.addCard(CardType.BOMB);

		// Remove one card
		boolean result = d_player.removeCard(CardType.BOMB);

		// Check that one card was removed but the player still has one left
		assertTrue("Card removal should be successful", result);
		assertTrue("Player should still have bomb cards", d_player.getCards().containsKey(CardType.BOMB));
		assertEquals("Player should have 1 bomb card left", 1, (int) d_player.getCards().get(CardType.BOMB));
	}

	/**
	 * Tests the getFormattedCards method.
	 */
	@Test
	public void testGetFormattedCards() {
		// Add various cards
		d_player.addCard(CardType.BOMB);
		d_player.addCard(CardType.BLOCKADE);
		d_player.addCard(CardType.BOMB);

		// Get the formatted cards string
		String formattedCards = d_player.getFormattedCards();

		// The exact format might vary based on your implementation, but it should
		// contain card info
		assertTrue("Formatted cards should include BOMB", formattedCards.contains("BOMB"));
		assertTrue("Formatted cards should include BLOCKADE", formattedCards.contains("BLOCKADE"));
		assertTrue("Formatted cards should include the count of 2 for BOMB", formattedCards.contains("2"));
		assertTrue("Formatted cards should include the count of 1 for BLOCKADE", formattedCards.contains("1"));
	}

	/**
	 * Tests the hasCard method.
	 */
	@Test
	public void testHasCard() {
		// Check initially
		assertFalse("Player should not have any cards initially", d_player.hasCard(CardType.BOMB));

		// Add a card
		d_player.addCard(CardType.BOMB);

		// Check again
		assertTrue("Player should now have a bomb card", d_player.hasCard(CardType.BOMB));
		assertFalse("Player should not have a blockade card", d_player.hasCard(CardType.BLOCKADE));
	}
}