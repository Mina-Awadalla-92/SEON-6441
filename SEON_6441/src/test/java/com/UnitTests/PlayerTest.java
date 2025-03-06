package com.UnitTests;
import com.Game.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class PlayerTest {

    // Save the original System.in so we can restore it after each test.
    private final InputStream originalSystemIn = System.in;

    @AfterEach
    public void restoreSystemInput() {
        System.setIn(originalSystemIn);
    }

    @Test
    public void testReinforcementCalculation() {
        // Create a player with 10 reinforcement armies.
        Player player = new Player("TestPlayer", 10);
        // Create a territory "Quebec" and add it to the player's owned territories.
        Territory territory = new Territory("Quebec", "Canada", 5);
        player.addTerritory(territory);

        // Simulate valid input: deploy 5 armies to "Quebec", then finish.
        String simulatedInput = "deploy Quebec 5\nFINISH\n";
        ByteArrayInputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        // Issue orders using the simulated input.
        player.issueOrder();

        // After the valid deploy order, reinforcement armies should be 5 (10 - 5).
        assertEquals(5, player.getNbrOfReinforcementArmies(),
            "Reinforcement armies should be reduced by the number of armies deployed.");

        // Verify that one order was added.
        List<Order> orders = player.getOrders();
        assertEquals(1, orders.size(), "One deploy order should have been added.");

        // Optionally, check that the order is a DeployOrder.
        Order order = orders.get(0);
        assertTrue(order instanceof DeployOrder, "The order should be an instance of DeployOrder.");
    }

    @Test
    public void testCannotDeployMoreThanAvailable() {
        // Create a player with 10 reinforcement armies.
        Player player = new Player("TestPlayer", 10);
        // Create a territory "Quebec" and add it to the player's owned territories.
        Territory territory = new Territory("Quebec", "Canada", 5);
        player.addTerritory(territory);

        // Simulate input: attempt to deploy 15 armies (more than available), then finish.
        String simulatedInput = "deploy Quebec 15\nFINISH\n";
        ByteArrayInputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        // Issue orders using the simulated input.
        player.issueOrder();

        // Since the deployment exceeds available armies, reinforcement armies should remain unchanged.
        assertEquals(10, player.getNbrOfReinforcementArmies(),
            "Reinforcement armies should remain unchanged if the deployment exceeds available armies.");

        // No order should be added when the deployment is invalid.
        List<Order> orders = player.getOrders();
        assertEquals(0, orders.size(),
            "No order should be added when trying to deploy more armies than available.");
    }
}
