package com.Game.command;

import static org.junit.Assert.*;

import com.Game.model.order.DeployOrder;
import com.Game.model.order.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.Game.model.Player;
import com.Game.model.Territory;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the Deploy Command f the game.
 */
@RunWith(MockitoJUnitRunner.class)
public class DeployCommandTest {

    @Mock
    private Player player;

    @Mock
    private Territory territory;

    private DeployCommand deployCommand;

    @Before
    public void setUp() {
        // Mock dependencies
        player = mock(Player.class);
        territory = mock(Territory.class);
        deployCommand = new DeployCommand(player, territory, 5);
    }

    @Test
    public void testValidate_whenPlayerOwnsTerritoryAndHasEnoughArmies() {
        // Arrange
        when(player.getOwnedTerritories()).thenReturn(List.of(territory));
        when(player.getNbrOfReinforcementArmies()).thenReturn(10);

        // Act
        boolean isValid = deployCommand.validate();

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void testValidate_whenPlayerDoesNotOwnTerritory() {
        // Arrange
        Territory otherTerritory = mock(Territory.class);
        when(player.getOwnedTerritories()).thenReturn(List.of(otherTerritory));

        // Act
        boolean isValid = deployCommand.validate();

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void testValidate_whenPlayerDoesNotHaveEnoughArmies() {
        // Arrange
        when(player.getOwnedTerritories()).thenReturn(List.of(territory));
        when(player.getNbrOfReinforcementArmies()).thenReturn(3);

        // Act
        boolean isValid = deployCommand.validate();

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void testExecute_whenValid() {
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
    public void testExecute_whenInvalid() {
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
    public void testGetCommandName() {
        // Act
        String commandName = deployCommand.getCommandName();

        // Assert
        assertEquals("deploy", commandName);
    }

    @Test
    public void testUndo() {
        // Act
        deployCommand.undo();

        // Assert
        // No assertion needed as we are just checking that the undo prints a message
        // In a real test, we could verify that a logging mechanism captured the output
    }
}
