package com.Game.command;

import com.Game.model.Player;
import com.Game.model.Territory;
import com.Game.model.order.DeployOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the DeployCommand class.
 * This test class verifies the functionality of the DeployCommand class,
 * including validation, execution, and command name retrieval.
 * Mock objects are used for Player and Territory to isolate the behavior of DeployCommand.
 */
public class DeployCommandTest {

    private Player mockPlayer;
    private Territory mockTerritory;
    private DeployCommand deployCommand;

    @BeforeEach
    void setUp() {
        // Set up mock objects for Player and Territory
        mockPlayer = mock(Player.class);
        mockTerritory = mock(Territory.class);

        // Mock behavior for player and territory
        when(mockPlayer.getName()).thenReturn("Player1");
        when(mockTerritory.getName()).thenReturn("Territory1");
        when(mockPlayer.getOwnedTerritories()).thenReturn(new ArrayList<>(List.of(mockTerritory)));
        when(mockPlayer.getNbrOfReinforcementArmies()).thenReturn(10);

        // Initialize DeployCommand with 5 armies
        deployCommand = new DeployCommand(mockPlayer, mockTerritory, 5);
    }

    @Test
    
    void testExecute_ValidCommand() {
    // Test the execute method when the command is valid
    List<DeployOrder> mockOrders = new ArrayList<>();
    when(mockPlayer.getOrders()).thenReturn((List) mockOrders); // Cast to List<Order>

    deployCommand.execute();

    // Verify that the deploy order is added and armies are deducted
    assertEquals(1, mockOrders.size(), "Deploy order should be added to the player's orders.");
    assertEquals(5, mockOrders.get(0).getNumberOfArmies(), "Deploy order should have 5 armies.");
    
}

    @Test
    void testExecute_InvalidCommand_NotOwnedTerritory() {
        // Test the execute method when the player does not own the territory
        when(mockPlayer.getOwnedTerritories()).thenReturn(new ArrayList<>());

        deployCommand.execute();

        // Verify that no orders are added and no armies are deducted
        verify(mockPlayer, never()).getOrders();
        verify(mockPlayer, never()).setNbrOfReinforcementArmies(anyInt());
    }

    @Test
    void testExecute_InvalidCommand_NotEnoughArmies() {
        // Test the execute method when the player does not have enough armies
        when(mockPlayer.getNbrOfReinforcementArmies()).thenReturn(3);

        deployCommand.execute();

        // Verify that no orders are added and no armies are deducted
        verify(mockPlayer, never()).getOrders();
        verify(mockPlayer, never()).setNbrOfReinforcementArmies(anyInt());
    }

    @Test
    void testValidate_ValidCommand() {
        // Test the validate method for a valid command
        boolean isValid = deployCommand.validate();
        assertTrue(isValid, "Validation should pass for a valid deploy command.");
    }

    @Test
    void testValidate_InvalidCommand_NotOwnedTerritory() {
        // Test the validate method when the player does not own the territory
        when(mockPlayer.getOwnedTerritories()).thenReturn(new ArrayList<>());

        boolean isValid = deployCommand.validate();
        assertFalse(isValid, "Validation should fail if the player doesn't own the territory.");
    }

    @Test
    void testValidate_InvalidCommand_NotEnoughArmies() {
        // Test the validate method when the player does not have enough armies
        when(mockPlayer.getNbrOfReinforcementArmies()).thenReturn(3);

        boolean isValid = deployCommand.validate();
        assertFalse(isValid, "Validation should fail if the player doesn't have enough armies.");
    }

    @Test
    void testGetCommandName() {
        // Test the getCommandName method
        String commandName = deployCommand.getCommandName();
        assertEquals("deploy", commandName, "Command name should be 'deploy'.");
    }

    @Test
    void testUndo_NotImplemented() {
        // Test the undo method (currently not implemented)
        deployCommand.undo();
        // No assertions since undo is a placeholder
    }
}