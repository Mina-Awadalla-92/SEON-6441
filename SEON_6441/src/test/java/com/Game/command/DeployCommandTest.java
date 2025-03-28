package com.Game.command;

import com.Game.model.Player;
import com.Game.model.Territory;
import com.Game.model.order.DeployOrder;
import com.Game.model.order.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DeployCommandTest {

    private Player player;
    private Territory territory;
    private DeployCommand deployCommand;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        player = mock(Player.class);
        territory = mock(Territory.class);
        deployCommand = new DeployCommand(player, territory, 5);
    }

    @Test
    void testValidate_whenPlayerOwnsTerritoryAndHasEnoughArmies() {
        // Arrange
        when(player.getOwnedTerritories()).thenReturn(List.of(territory));
        when(player.getNbrOfReinforcementArmies()).thenReturn(10);

        // Act
        boolean isValid = deployCommand.validate();

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testValidate_whenPlayerDoesNotOwnTerritory() {
        // Arrange
        Territory otherTerritory = mock(Territory.class);
        when(player.getOwnedTerritories()).thenReturn(List.of(otherTerritory));
        when(player.getNbrOfReinforcementArmies()).thenReturn(10);

        // Act
        boolean isValid = deployCommand.validate();

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testValidate_whenPlayerDoesNotHaveEnoughArmies() {
        // Arrange
        when(player.getOwnedTerritories()).thenReturn(List.of(territory));
        when(player.getNbrOfReinforcementArmies()).thenReturn(3);

        // Act
        boolean isValid = deployCommand.validate();

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testExecute_whenValid() {
        // Arrange
        List<Order> mockOrders = new ArrayList<>();  // Use List<Order> instead of List<DeployOrder>
        when(player.getOwnedTerritories()).thenReturn(List.of(territory));
        when(player.getNbrOfReinforcementArmies()).thenReturn(10);
        when(player.getOrders()).thenReturn(mockOrders);

        // Act
        deployCommand.execute();

        // Assert
        verify(player).getOrders(); // Check if the orders were accessed
        assertEquals(1, mockOrders.size()); // Check if the deploy order is added
        assertTrue(mockOrders.get(0) instanceof DeployOrder); // Check if the first order is a DeployOrder
        verify(player).setNbrOfReinforcementArmies(5); // Ensure the reinforcement armies are updated correctly
    }

    @Test
    void testExecute_whenInvalid() {
        // Arrange
        when(player.getOwnedTerritories()).thenReturn(List.of(territory));
        when(player.getNbrOfReinforcementArmies()).thenReturn(3);

        // Act
        deployCommand.execute();

        // Assert
        verify(player, times(0)).getOrders(); // Check if the orders were not accessed
        verify(player, times(0)).setNbrOfReinforcementArmies(anyInt()); // Armies should not be reduced
    }

    @Test
    void testGetCommandName() {
        // Act
        String commandName = deployCommand.getCommandName();

        // Assert
        assertEquals("deploy", commandName);
    }

    @Test
    void testUndo() {
        // Act
        deployCommand.undo();

        // Assert
        // No assertion needed as we are just checking that the undo prints a message
        // In a real test, we could verify that a logging mechanism captured the output
    }
}
