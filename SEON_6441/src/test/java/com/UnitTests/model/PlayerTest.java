package com.UnitTests.model;

import com.Game.model.Player;
import com.Game.model.Territory;
import com.Game.model.order.DeployOrder;
import com.Game.model.order.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link Player} class.
 * This class tests the reinforcement calculation and order issuing functionalities of a player.
 */
public class PlayerTest {

    /**
     * Saves the original System.in to restore it after each test.
     */
    private final InputStream originalSystemIn = System.in;

    /**
     * Restores the original System.in after each test.
     */
    @AfterEach
    public void restoreSystemInput() {
        System.setIn(originalSystemIn);
    }

    /**
     * Tests the reinforcement calculation for a player.
     * <ul>
     *   <li>Creates a player with 10 reinforcement armies.</li>
     *   <li>Simulates a deploy order to place 5 armies on "Quebec".</li>
     *   <li>Verifies that the reinforcement armies are reduced correctly.</li>
     *   <li>Checks that a deploy order is added to the player's orders.</li>
     * </ul>
     */
    @Test
    public void testReinforcementCalculation() {
        // Create a player with 10 reinforcement armies.
        Player l_player = new Player("TestPlayer", 10);
        // Create a territory "Quebec" and add it to the player's owned territories.
        Territory l_territory = new Territory("Quebec", "Canada", 5);
        l_player.addTerritory(l_territory);

        // Simulate valid input: deploy 5 armies to "Quebec", then finish.
        String l_simulatedInput = "deploy Quebec 5\nFINISH\n";
        ByteArrayInputStream l_in = new ByteArrayInputStream(l_simulatedInput.getBytes());
        System.setIn(l_in);

        // Issue orders using the simulated input.
        l_player.issueOrder();

        // After the valid deploy order, reinforcement armies should be 5 (10 - 5).
        assertEquals(5, l_player.getNbrOfReinforcementArmies(),
            "Reinforcement armies should be reduced by the number of armies deployed.");

        // Verify that one order was added.
        List<Order> l_orders = l_player.getOrders();
        assertEquals(1, l_orders.size(), "One deploy order should have been added.");

        // Optionally, check that the order is a DeployOrder.
        Order order = l_orders.get(0);
        assertTrue(order instanceof DeployOrder, "The order should be an instance of DeployOrder.");
    }

    /**
     * Tests that a player cannot deploy more armies than available.
     * <ul>
     *   <li>Creates a player with 10 reinforcement armies.</li>
     *   <li>Simulates an invalid deploy order to place 15 armies on "Quebec".</li>
     *   <li>Verifies that the reinforcement armies remain unchanged.</li>
     *   <li>Checks that no orders are added for invalid deployments.</li>
     * </ul>
     */
    @Test
    public void testCannotDeployMoreThanAvailable() {
        // Create a player with 10 reinforcement armies.
        Player l_player = new Player("TestPlayer", 10);
        // Create a territory "Quebec" and add it to the player's owned territories.
        Territory l_territory = new Territory("Quebec", "Canada", 5);
        l_player.addTerritory(l_territory);

        // Simulate input: attempt to deploy 15 armies (more than available), then finish.
        String l_simulatedInput = "deploy Quebec 15\nFINISH\n";
        ByteArrayInputStream l_in = new ByteArrayInputStream(l_simulatedInput.getBytes());
        System.setIn(l_in);

        // Issue orders using the simulated input.
        l_player.issueOrder();

        // Since the deployment exceeds available armies, reinforcement armies should remain unchanged.
        assertEquals(10, l_player.getNbrOfReinforcementArmies(),
            "Reinforcement armies should remain unchanged if the deployment exceeds available armies.");

        // No order should be added when the deployment is invalid.
        List<Order> l_orders = l_player.getOrders();
        assertEquals(0, l_orders.size(),
            "No order should be added when trying to deploy more armies than available.");
    }
}
