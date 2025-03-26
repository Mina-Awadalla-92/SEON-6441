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
 * This test suite covers:
 * Construction with and without reinforcement armies
 * Adding and removing territories and finding them by name
 * Creating deploy orders with both invalid and valid parameters
 * Retrieving and clearing orders
 * The nextOrder() behavior and the string representation via toString()
 */

public class PlayerTest {

    @Test
    void testConstructor() {
        // Test constructor with name only
        Player player = new Player("Alice");
        assertEquals("Alice", player.getName());
        assertEquals(0, player.getNbrOfReinforcementArmies());
        assertNotNull(player.getOwnedTerritories());
        assertTrue(player.getOwnedTerritories().isEmpty());
        assertNotNull(player.getOrders());
        assertTrue(player.getOrders().isEmpty());
    }

    @Test
    void testConstructorWithArmies() {
        // Test constructor with name and reinforcement armies
        Player player = new Player("Bob", 10);
        assertEquals("Bob", player.getName());
        assertEquals(10, player.getNbrOfReinforcementArmies());
        assertTrue(player.getOwnedTerritories().isEmpty());
        assertTrue(player.getOrders().isEmpty());
    }

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

    @Test
    void testAddNullTerritory() {
        Player player = new Player("Bob");
        assertThrows(NullPointerException.class, () -> player.addTerritory(null), "Adding a null territory should throw an exception.");
    }

    @Test
    void testAddDuplicateTerritory() {
        Player player = new Player("Dave");
        Territory territory = new Territory("Territory1", "Continent1", 5);
        player.addTerritory(territory);
        player.addTerritory(territory);
        assertEquals(1, player.getOwnedTerritories().size(), "Adding the same territory twice should not create duplicates.");
    }

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

    @Test
    void testCreateDeployOrder_InvalidTerritory() {
        Player player = new Player("Eve", 10);
        // No territory added, so findTerritoryByName returns null
        boolean result = player.createDeployOrder("NonExistent", 5);
        assertFalse(result);
        assertTrue(player.getOrders().isEmpty());
        assertEquals(10, player.getNbrOfReinforcementArmies());
    }

    @Test
    void testCreateDeployOrder_NotEnoughArmies() {
        Player player = new Player("Frank", 5);
        Territory territory = new Territory("Gamma", "ContinentG", 4);
        player.addTerritory(territory);
        // Request more armies than available
        boolean result = player.createDeployOrder("Gamma", 6);
        assertFalse(result);
        assertTrue(player.getOrders().isEmpty());
        assertEquals(5, player.getNbrOfReinforcementArmies());
    }

    @Test
    void testCreateDeployOrder_Success() {
        Player player = new Player("Grace", 10);
        Territory territory = new Territory("Delta", "ContinentD", 7);
        player.addTerritory(territory);
        boolean result = player.createDeployOrder("Delta", 4);
        assertTrue(result);
        List<Order> orders = player.getOrders();
        assertEquals(1, orders.size());
        Order order = orders.get(0);
        assertTrue(order instanceof DeployOrder, "Expected a DeployOrder instance.");
        // Reinforcement armies reduced accordingly
        assertEquals(6, player.getNbrOfReinforcementArmies());
    }

    @Test
    void testNextOrder() {
        Player player = new Player("Henry", 10);
        Territory territory = new Territory("Epsilon", "ContinentE", 3);
        player.addTerritory(territory);
        boolean created = player.createDeployOrder("Epsilon", 3);
        assertTrue(created);
        Order order = player.nextOrder();
        assertNotNull(order);
        // After removing the order, orders list should be empty
        assertTrue(player.getOrders().isEmpty());
        // Subsequent call returns null
        assertNull(player.nextOrder());
    }

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

    @Test
    void testClearOrders_WhenNoOrdersExist() {
        Player player = new Player("Charlie");
        player.clearOrders();
        assertTrue(player.getOrders().isEmpty(), "Clearing orders when no orders exist should not cause errors.");
    }

    @Test
    void testToString() {
        Player player = new Player("Jack", 15);
        String str = player.toString();
        assertTrue(str.contains("Jack"), "toString() should contain player's name.");
        assertTrue(str.contains("15"), "toString() should contain the number of reinforcement armies.");
    }

    @Test
    void testAddCard() {
        Player player = new Player("Alice");
        player.addCard(CardType.BOMB);
        player.addCard(CardType.AIRLIFT);
        player.addCard(CardType.BOMB);

        HashMap<CardType, Integer> cards = player.getCards();
        assertEquals(2, cards.get(CardType.BOMB));
        assertEquals(1, cards.get(CardType.AIRLIFT));
        assertEquals(2, cards.size());
    }

    @Test
    void testAddNullCard() {
        Player player = new Player("Alice");
        assertThrows(NullPointerException.class, () -> player.addCard(null), "Adding a null card should throw an exception.");
    }

    @Test
    void testRemoveCard_Success() {
        Player player = new Player("Bob");
        player.addCard(CardType.BOMB);
        player.addCard(CardType.BOMB);

        boolean removed = player.removeCard(CardType.BOMB);
        assertTrue(removed);
        assertEquals(1, player.getCards().get(CardType.BOMB));
    }

    @Test
    void testRemoveCard_Failure() {
        Player player = new Player("Charlie");
        player.addCard(CardType.AIRLIFT);

        boolean removed = player.removeCard(CardType.BOMB);
        assertFalse(removed);
        assertEquals(1, player.getCards().size());
    }

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

    @Test
    void testIncrementConqueredTerritoriesPerTurn() {
        Player player = new Player("Eve");
        player.incrementConqueredTerritoriesPerTurn();
        player.incrementConqueredTerritoriesPerTurn();

        assertEquals(2, player.getConqueredTerritoriesPerTurn());
    }

    @Test
    void testResetConqueredTerritoriesPerTurn() {
        Player player = new Player("Frank");
        player.incrementConqueredTerritoriesPerTurn();
        player.incrementConqueredTerritoriesPerTurn();
        player.resetConqueredTerritoriesPerTurn();

        assertEquals(0, player.getConqueredTerritoriesPerTurn());
    }

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

    @Test
    void testSetAndGetHasConqueredThisTurn() {
        Player player = new Player("Jack");
        player.setHasConqueredThisTurn(true);
        assertTrue(player.getHasConqueredThisTurn());

        player.setHasConqueredThisTurn(false);
        assertFalse(player.getHasConqueredThisTurn());
    }
}
