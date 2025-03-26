package com.Game.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.Game.model.order.DeployOrder;
import com.Game.model.order.Order;

/**
 * Unit tests for the Player class.
 * This test class verifies the behavior of the Player methods, including player initialization,
 * territory management, order creation, and card management.
 */
public class PlayerTest {

    /**
     * Tests the constructor with only the player's name.
     */
    @Test
    void testConstructor() {
        Player player = new Player("Alice");
        assertEquals("Alice", player.getName());
        assertEquals(0, player.getNbrOfReinforcementArmies());
        assertNotNull(player.getOwnedTerritories());
        assertTrue(player.getOwnedTerritories().isEmpty());
        assertNotNull(player.getOrders());
        assertTrue(player.getOrders().isEmpty());
    }

    /**
     * Tests the constructor with the player's name and reinforcement armies.
     */
    @Test
    void testConstructorWithArmies() {
        Player player = new Player("Bob", 10);
        assertEquals("Bob", player.getName());
        assertEquals(10, player.getNbrOfReinforcementArmies());
        assertTrue(player.getOwnedTerritories().isEmpty());
        assertTrue(player.getOrders().isEmpty());
    }

    /**
     * Tests adding and removing a territory.
     */
    @Test
    void testAddAndRemoveTerritory() {
        Player player = new Player("Charlie");
        Territory territory = new Territory("Territory1", "Continent1", 5);
        player.addTerritory(territory);
        assertEquals(1, player.getOwnedTerritories().size());
        assertSame(territory, player.getOwnedTerritories().get(0));

        player.removeTerritory(territory);
        assertTrue(player.getOwnedTerritories().isEmpty());
    }

    /**
     * Tests adding a null territory.
     */
    @Test
    void testAddNullTerritory() {
        Player player = new Player("Bob");
        assertThrows(NullPointerException.class, () -> player.addTerritory(null));
    }

    /**
     * Tests adding a duplicate territory.
     */
    @Test
    void testAddDuplicateTerritory() {
        Player player = new Player("Dave");
        Territory territory = new Territory("Territory1", "Continent1", 5);
        player.addTerritory(territory);
        player.addTerritory(territory);
        assertEquals(1, player.getOwnedTerritories().size());
    }

    /**
     * Tests finding a territory by name.
     */
    @Test
    void testFindTerritoryByName() {
        Player player = new Player("Dave");
        Territory t1 = new Territory("Alpha", "ContinentA", 3);
        Territory t2 = new Territory("Beta", "ContinentB", 2);
        player.addTerritory(t1);
        player.addTerritory(t2);
        assertEquals(t1, player.findTerritoryByName("Alpha"));
        assertEquals(t2, player.findTerritoryByName("Beta"));
        assertNull(player.findTerritoryByName("Gamma"));
    }

    /**
     * Tests creating a deploy order with an invalid territory.
     */
    @Test
    void testCreateDeployOrder_InvalidTerritory() {
        Player player = new Player("Eve", 10);
        boolean result = player.createDeployOrder("NonExistent", 5);
        assertFalse(result);
        assertTrue(player.getOrders().isEmpty());
        assertEquals(10, player.getNbrOfReinforcementArmies());
    }

    /**
     * Tests creating a deploy order with insufficient armies.
     */
    @Test
    void testCreateDeployOrder_NotEnoughArmies() {
        Player player = new Player("Frank", 5);
        Territory territory = new Territory("Gamma", "ContinentG", 4);
        player.addTerritory(territory);
        boolean result = player.createDeployOrder("Gamma", 6);
        assertFalse(result);
        assertTrue(player.getOrders().isEmpty());
        assertEquals(5, player.getNbrOfReinforcementArmies());
    }

    /**
     * Tests successfully creating a deploy order.
     */
    @Test
    void testCreateDeployOrder_Success() {
        Player player = new Player("Grace", 10);
        Territory territory = new Territory("Delta", "ContinentD", 7);
        player.addTerritory(territory);
        boolean result = player.createDeployOrder("Delta", 4);
        assertTrue(result);
        List<Order> orders = player.getOrders();
        assertEquals(1, orders.size());
        assertTrue(orders.get(0) instanceof DeployOrder);
        assertEquals(6, player.getNbrOfReinforcementArmies());
    }

    /**
     * Tests retrieving the next order.
     */
    @Test
    void testNextOrder() {
        Player player = new Player("Henry", 10);
        Territory territory = new Territory("Epsilon", "ContinentE", 3);
        player.addTerritory(territory);
        player.createDeployOrder("Epsilon", 3);
        Order order = player.nextOrder();
        assertNotNull(order);
        assertTrue(player.getOrders().isEmpty());
        assertNull(player.nextOrder());
    }

    /**
     * Tests clearing all orders.
     */
    @Test
    void testClearOrders() {
        Player player = new Player("Ivy", 10);
        Territory territory = new Territory("Zeta", "ContinentZ", 2);
        player.addTerritory(territory);
        player.createDeployOrder("Zeta", 3);
        player.createDeployOrder("Zeta", 2);
        assertEquals(2, player.getOrders().size());
        player.clearOrders();
        assertTrue(player.getOrders().isEmpty());
    }

    /**
     * Tests clearing orders when no orders exist.
     */
    @Test
    void testClearOrders_WhenNoOrdersExist() {
        Player player = new Player("Charlie");
        player.clearOrders();
        assertTrue(player.getOrders().isEmpty());
    }

    /**
     * Tests the toString method.
     */
    @Test
    void testToString() {
        Player player = new Player("Jack", 15);
        String str = player.toString();
        assertTrue(str.contains("Jack"));
        assertTrue(str.contains("15"));
    }

    /**
     * Tests adding cards to the player.
     */
    @Test
    void testAddCard() {
        Player player = new Player("Alice");
        player.addCard(CardType.BOMB);
        player.addCard(CardType.AIRLIFT);
        player.addCard(CardType.BOMB);
        HashMap<CardType, Integer> cards = player.getCards();
        assertEquals(2, cards.get(CardType.BOMB));
        assertEquals(1, cards.get(CardType.AIRLIFT));
    }

    /**
     * Tests adding a null card.
     */
    @Test
    void testAddNullCard() {
        Player player = new Player("Alice");
        assertThrows(NullPointerException.class, () -> player.addCard(null));
    }

    /**
     * Tests removing a card successfully.
     */
    @Test
    void testRemoveCard_Success() {
        Player player = new Player("Bob");
        player.addCard(CardType.BOMB);
        player.addCard(CardType.BOMB);
        boolean removed = player.removeCard(CardType.BOMB);
        assertTrue(removed);
        assertEquals(1, player.getCards().get(CardType.BOMB));
    }

    /**
     * Tests failing to remove a card that doesn't exist.
     */
    @Test
    void testRemoveCard_Failure() {
        Player player = new Player("Charlie");
        player.addCard(CardType.AIRLIFT);
        boolean removed = player.removeCard(CardType.BOMB);
        assertFalse(removed);
    }

    /**
     * Tests getting formatted cards.
     */
    @Test
    void testGetFormattedCards() {
        Player player = new Player("Dave");
        player.addCard(CardType.BOMB);
        player.addCard(CardType.AIRLIFT);
        player.addCard(CardType.BOMB);
        String formattedCards = player.getFormattedCards();
        assertTrue(formattedCards.contains("BOMB: 2"));
        assertTrue(formattedCards.contains("AIRLIFT: 1"));
    }

    /**
     * Tests incrementing conquered territories per turn.
     */
    @Test
    void testIncrementConqueredTerritoriesPerTurn() {
        Player player = new Player("Eve");
        player.incrementConqueredTerritoriesPerTurn();
        player.incrementConqueredTerritoriesPerTurn();
        assertEquals(2, player.getConqueredTerritoriesPerTurn());
    }

    /**
     * Tests resetting conquered territories per turn.
     */
    @Test
    void testResetConqueredTerritoriesPerTurn() {
        Player player = new Player("Frank");
        player.incrementConqueredTerritoriesPerTurn();
        player.resetConqueredTerritoriesPerTurn();
        assertEquals(0, player.getConqueredTerritoriesPerTurn());
    }

    /**
     * Tests setting and resetting negotiated players.
     */
    @Test
    void testSetAndResetNegotiatedPlayers() {
        Player player1 = new Player("Grace");
        Player player2 = new Player("Henry");
        Player player3 = new Player("Ivy");
        List<Player> negotiatedPlayers = new ArrayList<>();
        negotiatedPlayers.add(player2);
        negotiatedPlayers.add(player3);
        player1.setNegociatedPlayersPerTurn(negotiatedPlayers);
        assertEquals(2, player1.getNegociatedPlayersPerTurn().size());
        player1.resetNegociatedPlayersPerTurn();
        assertTrue(player1.getNegociatedPlayersPerTurn().isEmpty());
    }

    /**
     * Tests setting and getting the hasConqueredThisTurn flag.
     */
    @Test
    void testSetAndGetHasConqueredThisTurn() {
        Player player = new Player("Jack");
        player.setHasConqueredThisTurn(true);
        assertTrue(player.getHasConqueredThisTurn());
        player.setHasConqueredThisTurn(false);
        assertFalse(player.getHasConqueredThisTurn());
    }
}
